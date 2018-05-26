package hastabel2idp.idp.lang;

import hastabel.lang.Variable;

import java.util.List;

public class Quantifier extends Formula
{
   private final boolean is_forall;
   private final Variable parent;
   private final Formula formula;

   public Quantifier
   (
      final Variable parent,
      final Formula formula,
      final boolean is_forall
   )
   {
      this.parent = parent;
      this.formula = formula;
      this.is_forall = is_forall;
   }

   public boolean is_forall ()
   {
      return is_forall;
   }

   public Variable get_variable ()
   {
      return parent;
   }

   public Formula get_formula ()
   {
      return formula;
   }
}
