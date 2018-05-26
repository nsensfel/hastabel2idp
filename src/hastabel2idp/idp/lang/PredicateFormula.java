package hastabel2idp.idp.lang;

import hastabel2idp.idp.Project;

import hastabel.lang.Predicate;
import hastabel.lang.Type;

import java.util.List;

class PredicateFormula extends Formula
{
   private final Predicate parent;
   private final List<Expression> params;
   private final List<Type> signature;

   public PredicateFormula
   (
      final Predicate parent,
      final List<Type> signature,
      final List<Expression> params
   )
   {
      this.parent = parent;
      this.signature = signature;
      this.params = params;
   }

   public Predicate get_predicate ()
   {
      return parent;
   }

   public List<Expression> get_parameters ()
   {
      return params;
   }

   public List<Type> get_signature ()
   {
      return signature;
   }

   @Override
   public boolean equals (Object o)
   {
      final PredicateFormula e;

      if ((o == null) || !(o instanceof PredicateFormula))
      {
         return false;
      }

      e = (PredicateFormula) o;

      return
         (
            e.parent.equals(parent)
            && e.params.equals(params)
            && e.signature.equals(signature)
         );
   }

   @Override
   public String toString ()
   {
      final StringBuilder sb;
      boolean is_first;

      sb = new StringBuilder();
      is_first = true;

      sb.append(parent.get_name());
      sb.append(Project.signature_to_suffix(signature));

      sb.append("(");
      for (final Expression param: params)
      {
         if (param == null)
         {
            continue;
         }

         if (is_first)
         {
            is_first = false;
         }
         else
         {
            sb.append(",");
         }

         sb.append(param.toString());
      }

      sb.append(")");

      return sb.toString();
   }
}
