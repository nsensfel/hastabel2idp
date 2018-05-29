package hastabel2idp.idp.lang;

import hastabel2idp.idp.Project;

import hastabel.lang.Predicate;

import java.util.List;

class FunctionCall extends Expression
{
   private final Predicate parent;
   private final List<Expression> params;

   public FunctionCall
   (
      final Predicate parent,
      final List<Expression> params
   )
   {
      super(parent.get_function_type());
      this.parent = parent;
      this.params = params;
   }

   public Predicate get_function ()
   {
      return parent;
   }

   public List<Expression> get_arguments ()
   {
      return params;
   }

   @Override
   public boolean equals (Object o)
   {
      final FunctionCall e;

      if ((o == null) || !(o instanceof FunctionCall))
      {
         return false;
      }

      e = (FunctionCall) o;

      return (e.parent.equals(parent) && e.params.equals(params));
   }

   @Override
   public String toString ()
   {
      final StringBuilder sb;
      boolean is_first;

      is_first = true;
      sb = new StringBuilder();

      sb.append(parent.get_name());
      Project.parameters_to_suffix(params);
      sb.append("_f(");

      is_first = true;

      for (final Expression param: params)
      {
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
