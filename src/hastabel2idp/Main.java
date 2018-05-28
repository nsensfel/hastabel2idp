package hastabel2idp;

import hastabel2idp.idp.Project;

import hastabel.World;
import hastabel.lang.Formula;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main
{
   public static void main (String... args)
   {
      final Parameters params;
      final World world;
      final Formula property;

      System.out.println("#### HaStABeL to IDP ####");
      System.out.println("# Parsing parameters...");
      params = new Parameters(args);

      if (!params.are_valid())
      {
         return;
      }

      world = new World();

      System.out.println("# Loading model...");

      load_model(world, params);

      if (!world.is_valid())
      {
         System.out.println("# Failure.");

         return;
      }

      System.out.println("# Loading property...");

      property = load_property(world, params);

      if ((!world.is_valid()) || (property == null))
      {
         System.out.println("# Failure.");

         return;
      }

      System.out.println("# Generating IDP...");

      write_idp(world, property, params);

      System.out.println("# Flushing generated files...");
      OutputFile.flush_all();

      if (params.get_idp_executable() == null)
      {
         System.out.println("# No IDP executable set. Stopping here.");
         OutputFile.close_all();

         return;
      }

      System.out.println("# Running IDP...");

      System.out.println
      (
         "# Result: "
         + run_idp(world, params)
      );

      OutputFile.close_all();
   }

   private static void load_file (final World world, final String filename)
   {
      try
      {
         System.out.println("# Loading \"" + filename + "\"...");
         world.load(filename);
      }
      catch (final IOException ioe)
      {
         System.err.println
         (
            "[E] IOException when loading \""
            + filename
            + "\":\n"
            + ioe.getMessage()
         );

         world.invalidate();
      }
   }

   private static void load_model
   (
      final World world,
      final Parameters params
   )
   {
      for (final String level_file: params.get_level_files())
      {
         load_file(world, level_file);

         if (!world.is_valid())
         {
            return;
         }
      }

      for (final String model_file: params.get_model_files())
      {
         load_file(world, model_file);

         if (!world.is_valid())
         {
            return;
         }
      }

      System.out.println("# Modeling graphs in first-order...");
      world.ensure_first_order();
   }

   private static Formula load_property
   (
      final World world,
      final Parameters params
   )
   {
      try
      {
         return world.load_property(params.get_property_file());
      }
      catch (final IOException ioe)
      {
         System.err.println
         (
            "[E] IOException when loading \""
            + params.get_property_file()
            + "\":\n"
            + ioe.getMessage()
         );

         world.invalidate();
      }

      return null;
   }

   private static void write_idp
   (
      final World world,
      final Formula property,
      final Parameters params
   )
   {
      final Project project;

      project = new Project(params);

      project.generate("my_property", world, property);
   }

   private static String run_idp
   (
      final World world,
      final Parameters params
   )
   {
      final Process idp;
      final BufferedReader idp_stdout, idp_stderr;
      final String target;
      final String[] args;

      String line, result;

      result = null;

      args = new String[6];

      args[0] = params.get_idp_executable();
      args[1] = params.get_vocabulary_filename();
      args[2] = params.get_structure_filename();
      args[3] = params.get_theory_filename();
      args[4] = params.get_vocabulary_out_filename();
      args[5] = params.get_idp_main_filename();

      try
      {
         idp =
            (
               new ProcessBuilder
               (
                  args[0],
                  args[1],
                  args[2],
                  args[3],
                  args[4],
                  args[5]
               )
            ).start();

         target = "my_property = {";

         idp_stdout =
            new BufferedReader(new InputStreamReader(idp.getInputStream()));

         while ((line = idp_stdout.readLine()) != null)
         {
            System.out.println(line);

            if (line.contains(target))
            {
               result = line;
            }
         }

         idp_stderr =
            new BufferedReader(new InputStreamReader(idp.getErrorStream()));

         while ((line = idp_stderr.readLine()) != null)
         {
            System.err.println("[E] " + line);
         }

         idp.waitFor();
      }
      catch (final Exception e)
      {
         System.err.println(e.toString());
      }

      return result;
   }
}
