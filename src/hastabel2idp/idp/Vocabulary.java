package hastabel2idp.idp;

import hastabel2idp.OutputFile;

import hastabel.lang.Predicate;
import hastabel.lang.Type;
import hastabel.lang.Element;
import hastabel.lang.Variable;

import java.util.Collection;
import java.util.List;

public class Vocabulary
{
   private final OutputFile out;

   public Vocabulary (final String filename)
   {
      out = OutputFile.new_output_file(filename);
   }

   public void write_header ()
   {
      out.write("vocabulary V {");
      out.insert_newline();
   }

   public void write_footer ()
   {
      out.write("}");
      out.insert_newline();
   }

   public void add_type (final Type type)
   {
      out.write("   type ");
      out.write(type.get_name());
      out.insert_newline();
   }

   private void add_predicate_signature
   (
      final Predicate predicate,
      final List<Type> signature
   )
   {
      boolean is_first;

      is_first = true;

      out.write("   ");
      out.write(predicate.get_name());
      out.write(Project.signature_to_suffix(signature));
      out.write("(");

      for (final Type sig_type: signature)
      {
         if (sig_type == null)
         {
            continue;
         }

         if (is_first)
         {
            is_first = false;
         }
         else
         {
            out.write(", ");
         }

         out.write(sig_type.get_name());
      }

      out.write(")");
      out.insert_newline();
   }

   private void add_function_signature
   (
      final Predicate predicate,
      final List<Type> signature
   )
   {
      final int signature_size, signature_size_m1;
      boolean is_first;

      signature_size = signature.size();
      signature_size_m1 = (signature_size - 1);

      is_first = true;

      out.write("   ");
      out.write(predicate.get_name());
      out.write(Project.signature_to_suffix(signature));
      out.write("_f(");

      if (0 < signature_size_m1)
      {
         out.write(signature.get(0).get_name());
      }

      for (int i = 1; i < signature_size_m1; ++i)
      {
         out.write(", ");
         out.write(signature.get(i).get_name());
      }

      out.write("):");
      out.write(signature.get(signature_size_m1).get_name());
      out.insert_newline();
   }

   public void add_predicate
   (
      final Predicate predicate,
      final Collection<List<Type>> relevant_signatures,
      final Collection<List<Type>> partial_signatures
   )
   {
      for (final List<Type> signature: relevant_signatures)
      {
         add_predicate_signature(predicate, signature);
      }

      for (final List<Type> signature: partial_signatures)
      {
         add_predicate_signature(predicate, signature);
      }
   }

   public void add_function
   (
      final Predicate predicate,
      final Collection<List<Type>> relevant_signatures
   )
   {
      for (final List<Type> signature: relevant_signatures)
      {
         add_function_signature(predicate, signature);
      }
   }

   public void add_target_predicate
   (
      final String name,
      final List<Variable> arguments
   )
   {
      boolean is_first;

      is_first = true;

      out.write("   ");
      out.write(name);
      out.write("(");

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

         out.write(argument.get_type().get_name());
      }

      out.write(")");
      out.insert_newline();
   }
}
