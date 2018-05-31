package hastabel2idp;

import hastabel2idp.OutputFile;

import hastabel.World;
import hastabel.Strings;
import hastabel.lang.Type;

import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HastabelResult
{
   private final OutputFile model, level;

   public HastabelResult (final Parameters params)
   {
      model =
         OutputFile.new_output_file
         (
            params.get_output_dir()
            + "99_"
            + params.get_property_name()
            + ".sol.mod"
         );

      level =
         OutputFile.new_output_file
         (
            params.get_output_dir()
            + "99_"
            + params.get_property_name()
            + ".sol.lvl"
         );
   }

   public void add_solution
   (
      final World world,
      final String predicate,
      final List<Type> signature,
      final String idp_solution
   )
   {
      add_solution_to_level(predicate, signature);
      add_solution_to_model
      (
         world.get_strings_manager(),
         predicate,
         idp_solution
      );
   }

   private void add_solution_to_level
   (
      final String predicate,
      final List<Type> signature
   )
   {
      boolean is_first;

      is_first = true;

      level.write("add_predicate ");
      level.write(predicate);
      level.write("(");

      for (final Type type: signature)
      {
         if (is_first)
         {
            is_first = false;
         }
         else
         {
            level.write(", ");
         }

         level.write(type.get_name());
      }

      level.write(")");
      level.insert_newline();
   }

   private void add_solution_to_model
   (
      final Strings strings_manager,
      final String predicate,
      final String idp_solution
   )
   {
      final Pattern pattern;
      final Matcher matcher;

      pattern = Pattern.compile(predicate + " = \\{(.*)\\}");
      matcher = pattern.matcher(idp_solution);

      if (matcher.find())
      {
         final String[] solutions;

         solutions = matcher.group(1).split(";");

         System.out.println("# " + solutions.length + " solutions found.");

         for (final String solution: solutions)
         {
            boolean is_first;

            is_first = true;

            model.write(predicate);
            model.write("(");

            for (String param: solution.split(","))
            {
               param = param.trim();

               if (param.startsWith("_string_"))
               {
                  param = strings_manager.get_string_from_element_name(param);
               }

               if (is_first)
               {
                  is_first = false;
               }
               else
               {
                  model.write(", ");
               }
               model.write(param.trim());
            }

            model.write(")");
            model.insert_newline();
         }
      }
      else
      {
         System.out.println("# No solutions found.");
      }
   }
}
