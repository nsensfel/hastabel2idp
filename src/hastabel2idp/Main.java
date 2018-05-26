package hastabel2idp;

import hastabel2idp.idp.Project;

import hastabel.World;
import hastabel.lang.Formula;

import java.io.IOException;

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

      OutputFile.close_all();

      System.out.println("# Done.");
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

      project.generate(world, property);
   }
}
