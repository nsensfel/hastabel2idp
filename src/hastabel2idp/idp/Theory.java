package hastabel2idp.idp;

import hastabel2idp.OutputFile;

//import hastabel.lang.Predicate;
//import hastabel.lang.Type;
//import hastabel.lang.Element;

//import java.util.Collection;
//import java.util.List;

public class Theory
{
   private final OutputFile out;

   public Theory (final String filename)
   {
      out = OutputFile.new_output_file(filename);
   }

   public void write_header ()
   {
      out.write("theory T:V {");
      out.insert_newline();
   }

   public void write_footer ()
   {
      out.write("}");
      out.insert_newline();
   }
}
