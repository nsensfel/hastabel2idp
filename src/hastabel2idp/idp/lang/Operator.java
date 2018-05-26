package hastabel2idp.idp.lang;

import java.util.List;
import java.util.ArrayList;

public enum Operator
{
   NOT,
   AND,
   OR,
   IFF,
   IMPLIES;

   public Formula as_formula (final List<Formula> params)
   {
      final OperatorFormula result;

      result = new OperatorFormula(this, params);

      return result;
   }

   public Formula as_formula_ (final Formula... e_params)
   {
      final ArrayList<Formula> params;

      params = new ArrayList<Formula>();

      for (final Formula f: e_params)
      {
         params.add(f);
      }

      return as_formula(params);
   }

   public static Operator convert (final hastabel.lang.Operator operator)
   {
      switch (operator)
      {
         case NOT: return NOT;
         case AND: return AND;
         case OR: return OR;
         case IFF: return IFF;
         case IMPLIES: return IMPLIES;

         default:
            System.err.println
            (
               "[F] HaStABeL to IDP can't handle the "
               + operator.toString()
               + " operator."
            );

            return null;
      }
   }
}
