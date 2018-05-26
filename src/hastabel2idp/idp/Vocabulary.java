package hastabel2idp.idp;

import hastabel2idp.OutputFile;

import hastabel.lang.Predicate;
import hastabel.lang.Type;
import hastabel.lang.Element;

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
}