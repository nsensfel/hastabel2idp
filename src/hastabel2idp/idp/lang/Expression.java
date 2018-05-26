package hastabel2idp.idp.lang;

import hastabel.lang.Type;

import java.util.stream.Collectors;

public abstract class Expression
{
   protected final Type type;

   public Expression (final Type type)
   {
      this.type = type;
   }

   public Type get_type()
   {
      return type;
   }

   public static hastabel2idp.idp.lang.Expression convert
   (
      final hastabel.lang.Expression hasta_f
   )
   {
      if (hasta_f == null)
      {
         return null;
      }
      else if (hasta_f instanceof hastabel.lang.NamedExpression)
      {
         return
            convert_named_expression
            (
               (hastabel.lang.NamedExpression) hasta_f
            );
      }
      else if (hasta_f instanceof hastabel.lang.FunctionCall)
      {
         return
            convert_function_call
            (
               (hastabel.lang.FunctionCall) hasta_f
            );
      }
      else
      {
         System.err.println
         (
            "[F] HaStABeL to IDP does not recognize the following HaStABeL"
            + " expression: "
            + hasta_f.toString()
         );

         return null;
      }
   }

   private static hastabel2idp.idp.lang.Expression convert_named_expression
   (
      final hastabel.lang.NamedExpression hasta_named_expression
   )
   {
      return
         new hastabel2idp.idp.lang.NamedExpression
         (
            hasta_named_expression.get_type(),
            hasta_named_expression.get_name()
         );
   }

   private static hastabel2idp.idp.lang.Expression convert_function_call
   (
      final hastabel.lang.FunctionCall hasta_function_call
   )
   {
      return
         new hastabel2idp.idp.lang.FunctionCall
         (
            hasta_function_call.get_function(),
            hasta_function_call.get_arguments().stream().map
            (
               hastabel2idp.idp.lang.Expression::convert
            ).collect(Collectors.toList())
         );
   }
}
