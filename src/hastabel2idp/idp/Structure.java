package hastabel2idp.idp;

import hastabel2idp.OutputFile;

import hastabel.lang.Predicate;
import hastabel.lang.Type;
import hastabel.lang.Element;

import java.util.Collection;
import java.util.List;

public class Structure
{
   private final OutputFile out;

   public Structure (final String filename)
   {
      out = OutputFile.new_output_file(filename);
   }

   public void write_header ()
   {
      out.write("structure S:V {");
      out.insert_newline();
   }

   public void write_footer ()
   {
      out.write("}");
      out.insert_newline();
   }

   public void add_type (final Type type)
   {
      boolean is_first_member;

      out.write("   ");
      out.write(Project.type_name_to_idp(type.get_name()));
      out.write("={");
      out.insert_newline();

      is_first_member = true;

      for (final Element member: type.get_elements())
      {
         if (is_first_member)
         {
            is_first_member = false;
            out.write("      ");
         }
         else
         {
            out.write(";");
            out.insert_newline();
            out.write("      ");
         }

         out.write(member.get_name());
      }

      out.insert_newline();
      out.write("   }");
      out.insert_newline();
   }

   private void add_predicate_signature
   (
      final Predicate predicate,
      final List<Type> signature,
      final boolean is_partial
   )
   {
      boolean is_first_member;
      final Collection<List<Element>> relevant_members;

      out.write("   ");
      out.write(predicate.get_name());
      out.write(Project.signature_to_suffix(signature));
      out.write("={");
      out.insert_newline();

      is_first_member = true;

      if (is_partial)
      {
         relevant_members = predicate.get_relevant_partial_members(signature);
      }
      else
      {
         relevant_members = predicate.get_relevant_members(signature);
      }

      for (final List<Element> member: relevant_members)
      {
         boolean is_first_member_param;

         is_first_member_param = true;

         if (is_first_member)
         {
            is_first_member = false;
            out.write("      ");
         }
         else
         {
            out.write(";");
            out.insert_newline();
            out.write("      ");
         }

         for (final Element member_param: member)
         {
            if (is_first_member_param)
            {
               is_first_member_param = false;
            }
            else
            {
               out.write(", ");
            }

            out.write(member_param.get_name());
         }
      }

      out.insert_newline();
      out.write("   }");
      out.insert_newline();
   }

   public void add_predicate
   (
      final Predicate predicate,
      final Collection<List<Type>> relevant_signatures,
      final Collection<List<Type>> partial_signatures
   )
   {
      if (relevant_signatures.size() == 0)
      {
         System.err.println
         (
            "[E] Predicate "
            + predicate.get_name()
            + " has no signatures."
         );
      }

      for (final List<Type> signature: relevant_signatures)
      {
         add_predicate_signature(predicate, signature, false);
      }

      for (final List<Type> signature: partial_signatures)
      {
         add_predicate_signature(predicate, signature, true);
      }
   }

   private void add_function_signature
   (
      final Predicate predicate,
      final List<Type> signature
   )
   {
      boolean is_first_member;
      final Collection<List<Element>> relevant_members;

      out.write("   ");
      out.write(predicate.get_name());
      out.write(Project.signature_to_suffix(signature));
      out.write("_f={");
      out.insert_newline();

      is_first_member = true;

      relevant_members = predicate.get_relevant_members(signature);

      for (final List<Element> member: relevant_members)
      {
         final int signature_size, signature_size_m1;
         boolean is_first_member_param;

         signature_size = member.size();
         signature_size_m1 = (signature_size - 1);

         is_first_member_param = true;

         if (is_first_member)
         {
            is_first_member = false;
            out.write("      ");
         }
         else
         {
            out.write(";");
            out.insert_newline();
            out.write("      ");
         }

         for (int i = 0; i < signature_size_m1; ++i)
         {
            if (is_first_member_param)
            {
               is_first_member_param = false;
            }
            else
            {
               out.write(", ");
            }

            out.write(Project.type_name_to_idp(member.get(i).get_name()));
         }

         out.write("->");
         out.write
         (
            Project.type_name_to_idp(member.get(signature_size_m1).get_name())
         );
      }

      out.insert_newline();
      out.write("   }");
      out.insert_newline();
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

   public void add_constant
   (
      final String type,
      final String name
   )
   {
      out.write("   ");
      out.write(name);
      out.write("=");
      out.write(name);
      out.insert_newline();
   }
}
