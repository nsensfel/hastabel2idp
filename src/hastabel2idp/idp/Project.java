package hastabel2idp.idp;

import hastabel2idp.Parameters;

import hastabel.World;

import hastabel2idp.idp.lang.Expression;

import hastabel.lang.Predicate;
import hastabel.lang.Type;
import hastabel.lang.Element;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;
import java.util.List;

public class Project
{
   private final Map<String, Expression> constants;
   private final VocabularyOut vocabulary_out;
   private final Vocabulary vocabulary;
   private final Structure structure;
   private final Theory theory;

   public Project (final Parameters params)
   {
      vocabulary_out = new VocabularyOut(params.get_vocabulary_out_filename());
      vocabulary = new Vocabulary(params.get_vocabulary_filename());
      structure = new Structure(params.get_structure_filename());
      theory = new Theory(params.get_theory_filename());
      constants = new HashMap<String, Expression>();
   }

   public void generate
   (
      final String property_name,
      final World world,
      final hastabel.lang.Formula property
   )
   {
      final hastabel2idp.idp.lang.Formula idp_property;
      final Collection<Type> types;
      final Collection<Predicate> predicates;

      idp_property = hastabel2idp.idp.lang.Formula.convert(constants, property);
      types = world.get_types_manager().get_all();
      predicates = world.get_predicates_manager().get_all();

      vocabulary.write_header();
      vocabulary_out.write_header();
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

            if (predicate.is_used_as_predicate())
            {
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

            if (predicate.is_used_as_function())
            {
               vocabulary.add_function(predicate, relevant_signatures);
               structure.add_function(predicate, relevant_signatures);
            }
         }
      }

      for (final Map.Entry<String, Expression> constant: constants.entrySet())
      {
         final String name, type;

         name = constant.getKey();
         type =
            Project.type_name_to_idp(constant.getValue().get_type().get_name());

         vocabulary.add_constant(type, name);
         structure.add_constant(type, name);
      }

      theory.add_predicate
      (
         property_name,
         world.get_variables_manager().get_all_seeked(),
         idp_property
      );

      vocabulary.add_target_predicate
      (
         property_name,
         world.get_variables_manager().get_all_seeked()
      );

      vocabulary_out.add_target_predicate
      (
         property_name,
         world.get_variables_manager().get_all_seeked()
      );

      vocabulary_out.write_footer();
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
            sb.append(Project.type_name_to_idp(t.get_name()));
         }
      }

      return sb.toString();
   }

   public static String parameters_to_suffix (final List<Expression> params)
   {
      final StringBuilder sb;

      sb = new StringBuilder();

      for (final Expression param: params)
      {
         if (param != null)
         {
            sb.append("_");
            sb.append(Project.type_name_to_idp(param.get_type().get_name()));
         }
      }

      return sb.toString();
   }

   public static String type_name_to_idp (final String type_name)
   {
      /** IDP doesn't like us using the 'string' type name. **/

      if (type_name.equals("string"))
      {
         return "hastastring";
      }

      return type_name;
   }
}
