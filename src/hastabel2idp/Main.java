package hastabel2idp;

import hastabel.World;

import java.io.IOException;

public class Main
{
   public static void main (String... args)
   {
      final Parameters params;
      final World world;

      params = new Parameters(args);

      if (!params.are_valid())
      {
         return;
      }

      world = new World();

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

      world.ensure_first_order();

      if (!world.is_valid())
      {
         return;
      }

      try
      {
         world.load_property(params.get_property_file());
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
   }

   private static void load_file (final World world, final String filename)
   {
      try
      {
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
}
