package hastabel2idp.idp.lang;

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
}
