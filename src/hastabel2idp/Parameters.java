package hastabel2idp;

import java.util.List;
import java.util.ArrayList;

import java.io.File;

public class Parameters
{
   private final List<String> level_files;
   private final List<String> model_files;
   private final String property_file;
   private final String output_dir;
   private final String idp_exec;
   private final boolean be_verbose;
   private final boolean are_valid;
   private final String property_name;

   public static void print_usage ()
   {
      System.out.println
      (
         "HaStABeL to IDP\n"
         + "USAGE:\n"
         + "\tjava -jar hastabel2idp.jar <PROP_NAME> <OUTPUT_DIR> <FILES|OPTIONS>+\n"
         + "PARAMETERS:\n"
         + "\t- <PROP_NAME>\tName of the property.\n"
         + "\t- <OUTPUT_DIR>\tDirectory in which to write the IDP model.\n"
         + "\t- <FILES>\tList of files to be loaded.\n"
         + "OPTIONS:\n"
         + "\t- -e|--exec <IDP_EXEC>\tIDP executable path.\n"
         + "NOTES:\n"
         + "\t- Exactly one property file must be in <FILES>.\n"
         + "\t- Property files have a \".pro\" extension.\n"
         + "\t- Model files have a \".mod\" extension.\n"
         + "\t- Level files have a \".lvl\" extension.\n"
         + "\t- The files may be given in any order."
         + "\t- \".mod\" and \".lvl\" files from <OUTPUT_DIR> are also loaded."
      );
   }

   public Parameters (final String... args)
   {
      final File output_folder;
      boolean has_pro_file, has_error, should_be_verbose;
      String prop_file, idp_file;

      level_files = new ArrayList<String>();
      model_files = new ArrayList<String>();

      should_be_verbose = false;

      if (args.length < 3)
      {
         print_usage();

         property_file = new String();
         output_dir = new String();

         are_valid = false;
         be_verbose = false;
         idp_exec = null;

         property_name = null;

         return;
      }

      has_pro_file = false;
      has_error = false;
      idp_file = null;

      property_name = args[0];
      output_dir = args[1];

      if
      (
         (property_name.equals("-e") || property_name.equals("--exec"))
         || (output_dir.equals("-e") || output_dir.equals("--exec"))
         /* || ... */
      )
      {
         print_usage();

         System.err.println
         (
            "[F] An option was found in lieu of the property name or of the"
            + " output directory."
         );

         System.exit(-1);
      }

      if
      (
         property_name.endsWith(".lvl")
         || property_name.endsWith(".mod")
         || property_name.endsWith(".pro")
         || output_dir.endsWith(".lvl")
         || output_dir.endsWith(".mod")
         || output_dir.endsWith(".pro")
      )
      {
         print_usage();

         System.err.println
         (
            "[F] The output file has an extension that could be used in an"
            + " input file. It is most likely that you did not indicate an"
            + " output file, meaning that one of the input files was about to"
            + " be written over. So likely, in fact, that we'll abort here. The"
            + " output file you indicated was \""
            + output_dir
            + "\"."
         );

         System.exit(-1);
      }

      prop_file = new String();

      for (int i = 2; i < args.length; ++i)
      {
         if (args[i].endsWith(".lvl"))
         {
            level_files.add(args[i]);
         }
         else if (args[i].endsWith(".mod"))
          {
            model_files.add(args[i]);
         }
         else if (args[i].endsWith(".pro"))
         {
            if (has_pro_file)
            {
               System.err.println
               (
                  "[E] Both files \""
                  + prop_file
                  + "\" and \"."
                  + args[i]
                  + "\" contain a property. Only one can be used at a time."
               );

               has_error = true;
            }
            else
            {
               has_pro_file = true;
               prop_file = args[i];
            }
         }
         else if (args[i].equals("-e") || args[i].equals("--exec"))
         {
            i++;

            if (i < args.length)
            {
               idp_file = args[i];
            }
         }
         else
         {
            System.err.println
            (
               "[E] Unknown file type \""
               + args[i]
               + "\"."
            );

            has_error = true;
         }
      }

      output_folder = new File(output_dir);

      for (final File file : output_folder.listFiles())
      {
         final String filename;

         filename = file.getName();

         if (filename.endsWith(".lvl"))
         {
            level_files.add(output_dir + "/" + filename);
         }
         else if (filename.endsWith(".mod"))
          {
            model_files.add(output_dir + "/" + filename);
         }
      }

      idp_exec = idp_file;
      property_file = prop_file;
      if (!has_pro_file)
      {
         System.err.println("[E] There was no property file.");

         has_error = true;
      }

      be_verbose = should_be_verbose;
      are_valid = !has_error;
   }

   public List<String> get_level_files ()
   {
      return level_files;
   }

   public String get_idp_executable ()
   {
      return idp_exec;
   }

   public List<String> get_model_files ()
   {
      return model_files;
   }

   public String get_property_file ()
   {
      return property_file;
   }

   public String get_output_dir ()
   {
      return output_dir;
   }

   public String get_theory_filename ()
   {
      return output_dir + "/03_theory.txt";
   }

   public String get_vocabulary_filename ()
   {
      return output_dir + "/00_vocabulary.txt";
   }

   public String get_vocabulary_out_filename ()
   {
      return output_dir + "/02_vocabulary.txt";
   }

   public String get_structure_filename ()
   {
      return output_dir + "/01_structure.txt";
   }

   public String get_idp_main_filename ()
   {
      return "./99_idp_main.txt";
   }

   public String get_property_name ()
   {
      return property_name;
   }

   public boolean be_verbose ()
   {
      return be_verbose;
   }

   public boolean are_valid ()
   {
      return are_valid;
   }
}
