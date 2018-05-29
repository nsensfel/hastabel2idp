package hastabel2idp.idp;

import hastabel2idp.OutputFile;

import hastabel.lang.Variable;
//import hastabel.lang.Type;
//import hastabel.lang.Element;

//import java.util.Collection;
import java.util.List;

public class Theory
{
   private final OutputFile out;

   public Theory (final String filename)
   {
      out = OutputFile.new_output_file(filename);
   }

   public void write_header ()
   {
      out.write("theory T:V {");
      out.insert_newline();
   }

   public void add_predicate
   (
      final String name,
      final List<Variable> arguments,
      final hastabel.lang.Formula formula
   )
   {
      boolean is_first;

      out.write("   ");

      for (final Variable argument: arguments)
      {
         out.write("!");
         out.write(argument.get_name());
         out.write(" [");
         out.write(Project.type_name_to_idp(argument.get_type().get_name()));
         out.write("]: ");
      }

      out.write(name);
      out.write("(");
      is_first = true;

      for (final Variable argument: arguments)
      {
         if (is_first)
         {
            is_first = false;
         }
         else
         {
            out.write(", ");
         }
         out.write(argument.get_name());
      }

      out.write(") <=> (");
      out.write(hastabel2idp.idp.lang.Formula.convert(formula).toString());
      out.write(").");
      out.insert_newline();
   }

   public void write_footer ()
   {
      out.write("}");
      out.insert_newline();
   }
}
