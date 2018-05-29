package hastabel2idp.idp.lang;

import java.util.List;

class OperatorFormula extends Formula
{
   private final Operator parent;
   private final List<Formula> params;

   public OperatorFormula
   (
      final Operator parent,
      final List<Formula> params
   )
   {

      this.parent = parent;
      this.params = params;
   }

   public Operator get_operator ()
   {
      return parent;
   }

   public List<Formula> get_operands ()
   {
      return params;
   }

   @Override
   public boolean equals (Object o)
   {
      final OperatorFormula e;

      if ((o == null) || !(o instanceof OperatorFormula))
      {
         return false;
      }

      e = (OperatorFormula) o;

      return (e.parent.equals(parent) && e.params.equals(params));
   }

   @Override
   public String toString ()
   {
      final StringBuilder sb;
      boolean is_first;

      sb = new StringBuilder();
      is_first = true;

      if (parent == Operator.NOT)
      {
         sb.append("~");
      }

      sb.append("(");

      for (final Formula param: params)
      {
         if (is_first)
         {
            is_first = false;
         }
         else
         {
            sb.append(" ");
            sb.append(parent.toString());
            sb.append(" ");
         }

         sb.append(param.toString());
      }

      sb.append(")");

      return sb.toString();
   }
}
