package hastabel2idp.idp.lang;

import java.util.stream.Collectors;

public abstract class Formula
{
   public static hastabel2idp.idp.lang.Formula convert
   (
      final hastabel.lang.Formula hasta_f
   )
   {
      if (hasta_f instanceof hastabel.lang.Quantifier)
      {
         return convert_quantifier((hastabel.lang.Quantifier) hasta_f);
      }
      else if (hasta_f instanceof hastabel.lang.PredicateFormula)
      {
         return
            convert_predicate_formula
            (
               (hastabel.lang.PredicateFormula) hasta_f
            );
      }
      else if (hasta_f instanceof hastabel.lang.OperatorFormula)
      {
         return
            convert_operator_formula
            (
               (hastabel.lang.OperatorFormula) hasta_f
            );
      }
      else if (hasta_f instanceof hastabel.lang.Equals)
      {
         return convert_equals((hastabel.lang.Equals) hasta_f);
      }
      else
      {
         System.err.println
         (
            "[F] HaStABeL to IDP does not recognize the following HaStABeL"
            + " formula: "
            + hasta_f.toString()
         );

         return null;
      }
   }

   private static hastabel2idp.idp.lang.Formula convert_quantifier
   (
      final hastabel.lang.Quantifier hasta_quantifier
   )
   {
      return
         new hastabel2idp.idp.lang.Quantifier
         (
            hasta_quantifier.get_variable(),
            convert(hasta_quantifier.get_formula()),
            hasta_quantifier.is_forall()
         );
   }

   private static hastabel2idp.idp.lang.Formula convert_equals
   (
      final hastabel.lang.Equals hasta_equals
   )
   {
      return
         new hastabel2idp.idp.lang.Equals
         (
            Expression.convert(hasta_equals.get_first_operand()),
            Expression.convert(hasta_equals.get_second_operand())
         );
   }

   private static hastabel2idp.idp.lang.Formula convert_predicate_formula
   (
      final hastabel.lang.PredicateFormula hasta_predicate_formula
   )
   {
      return
         new hastabel2idp.idp.lang.PredicateFormula
         (
            hasta_predicate_formula.get_predicate(),
            hasta_predicate_formula.get_signature(),
            hasta_predicate_formula.get_parameters().stream().map
            (
               hastabel2idp.idp.lang.Expression::convert
            ).collect(Collectors.toList())
         );
   }

   private static hastabel2idp.idp.lang.Formula convert_operator_formula
   (
      final hastabel.lang.OperatorFormula hasta_operator_formula
   )
   {
      return
         new hastabel2idp.idp.lang.OperatorFormula
         (
            hastabel2idp.idp.lang.Operator.convert
            (
               hasta_operator_formula.get_operator()
            ),
            hasta_operator_formula.get_operands().stream().map
            (
               hastabel2idp.idp.lang.Formula::convert
            ).collect(Collectors.toList())
         );
   }
}
