package hastabel2idp.idp;

import hastabel2idp.Parameters;

import hastabel.World;

import hastabel.lang.Formula;
import hastabel.lang.Predicate;
import hastabel.lang.Type;
import hastabel.lang.Element;

import java.util.Collection;
import java.util.List;

public class Project
{
   private final Vocabulary vocabulary;
   private final Structure structure;
   private final Theory theory;

   public Project (final Parameters params)
   {
      vocabulary = new Vocabulary(params.get_vocabulary_filename());
      structure = new Structure(params.get_structure_filename());
      theory = new Theory(params.get_theory_filename());
   }

   public void generate (final World world, final Formula property)
   {
      final Collection<Type> types;
      final Collection<Predicate> predicates;

      types = world.get_types_manager().get_all();
      predicates = world.get_predicates_manager().get_all();

      vocabulary.write_header();
      structure.write_header();
      theory.write_header();

      for (final Type type: types)
      {
         if (type.is_used())
         {
            vocabulary.add_type(type);
            structure.add_type(type);
         }
      }

      for (final Predicate predicate: predicates)
      {
         if (predicate.is_used())
         {
            final Collection<List<Type>> relevant_signatures;
            final Collection<List<Type>> partial_signatures;

            relevant_signatures = predicate.get_relevant_signatures();
            partial_signatures = predicate.get_partial_signatures();

            vocabulary.add_predicate
            (
               predicate,
               relevant_signatures,
               partial_signatures
            );

            structure.add_predicate
            (
               predicate,
               relevant_signatures,
               partial_signatures
            );

         }
      }

      theory.add_formula(property);

      vocabulary.write_footer();
      structure.write_footer();
      theory.write_footer();
   }

   public static String signature_to_suffix (final List<Type> signature)
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
