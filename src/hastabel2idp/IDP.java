package hastabel2idp;

import hastabel.World;

import hastabel.lang.Predicate;
import hastabel.lang.Type;
import hastabel.lang.Element;

import java.util.Collection;
import java.util.Set;
import java.util.List;

public class IDP
{
   public static void generate
   (
      final World world,
      final Parameters params
   )
   {
      generate
      (
         world,
         OutputFile.new_output_file("/tmp/hasta_vocabulary.txt"),
         OutputFile.new_output_file("/tmp/hasta_structure.txt"),
         OutputFile.new_output_file("/tmp/hasta_theory.txt")
      );
   }

   private static void generate
   (
      final World world,
      final OutputFile vocabulary,
      final OutputFile structure,
      final OutputFile theory
   )
   {
      final Collection<Type> types;
      final Collection<Predicate> predicates;

      types = world.get_types_manager().get_all();
      predicates = world.get_predicates_manager().get_all();

      vocabulary.write("vocabulary V {");
      vocabulary.insert_newline();

      theory.write("theory T:V {");
      theory.insert_newline();

      structure.write("structure S:V {");
      structure.insert_newline();

      for (final Type type: types)
      {
         if (type.is_used())
         {
            add_type_to_vocabulary(vocabulary, type);
            add_type_to_structure(structure, type);
         }
      }

      for (final Predicate predicate: predicates)
      {
         if (predicate.is_used())
         {
            final Set<List<Type>> relevant_signatures;

            relevant_signatures = predicate.get_relevant_signatures();

            add_predicate_to_vocabulary(vocabulary, predicate, relevant_signatures);
            add_predicate_to_structure(structure, predicate, relevant_signatures);
         }
      }

      vocabulary.write("}");
      vocabulary.insert_newline();

      theory.write("}");
      theory.insert_newline();

      structure.write("}");
      structure.insert_newline();
   }

   private static void add_type_to_vocabulary
   (
      final OutputFile vocabulary,
      final Type type
   )
   {
      vocabulary.write("   type ");
      vocabulary.write(type.get_name());
      vocabulary.insert_newline();
   }

   private static void add_type_to_structure
   (
      final OutputFile structure,
      final Type type
   )
   {
      boolean is_first_member;

      structure.write("   ");
      structure.write(type.get_name());
      structure.write("={");
      structure.insert_newline();

      is_first_member = true;

      for (final Element member: type.get_elements())
      {
         if (is_first_member)
         {
            is_first_member = false;
            structure.write("      ");
         }
         else
         {
            structure.write(";");
            structure.insert_newline();
            structure.write("      ");
         }

         structure.write(member.get_name());
      }

      structure.insert_newline();
      structure.write("   }");
      structure.insert_newline();
   }

   private static void add_predicate_to_vocabulary
   (
      final OutputFile vocabulary,
      final Predicate predicate,
      final Set<List<Type>> relevant_signatures
   )
   {
      for (final List<Type> signature: relevant_signatures)
      {
         boolean is_first;

         is_first = true;

         vocabulary.write("   ");
         vocabulary.write(predicate.get_name());
         vocabulary.write(signature_to_suffix(signature));
         vocabulary.write("(");

         for (final Type sig_type:signature)
         {
            if (is_first)
            {
               is_first = false;
            }
            else
            {
               vocabulary.write(", ");
            }

            vocabulary.write(sig_type.get_name());
         }

         vocabulary.write(")");
         vocabulary.insert_newline();
      }
   }

   private static void add_predicate_to_structure
   (
      final OutputFile structure,
      final Predicate predicate,
      final Set<List<Type>> relevant_signatures
   )
   {
      for (final List<Type> signature: relevant_signatures)
      {
         boolean is_first_member;

         structure.write("   ");
         structure.write(predicate.get_name());
         structure.write(signature_to_suffix(signature));
         structure.write("={");
         structure.insert_newline();

         is_first_member = true;

         for
         (
            final List<Element> member:
               predicate.get_relevant_members(signature)
         )
         {
            boolean is_first_member_param;

            is_first_member_param = true;

            if (is_first_member)
            {
               is_first_member = false;
               structure.write("      ");
            }
            else
            {
               structure.write(";");
               structure.insert_newline();
               structure.write("      ");
            }

            for (final Element member_param: member)
            {
               if (is_first_member_param)
               {
                  is_first_member_param = false;
               }
               else
               {
                  structure.write(", ");
               }

               structure.write(member_param.get_name());
            }
         }

         structure.insert_newline();
         structure.write("   }");
         structure.insert_newline();
      }
   }

   private static String signature_to_suffix (final List<Type> signature)
   {
      final StringBuilder sb;

      sb = new StringBuilder();

      for (final Type t: signature)
      {
         if (t != null)
         {
            sb.append("_");
            sb.append(t.get_name());
         }
      }

      return sb.toString();
   }
}
