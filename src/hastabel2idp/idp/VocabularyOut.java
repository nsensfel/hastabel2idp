package hastabel2idp.idp;

import hastabel2idp.OutputFile;

import hastabel.lang.Variable;

import java.util.List;

public class VocabularyOut
{
   private final OutputFile out;

   public VocabularyOut (final String filename)
   {
      out = OutputFile.new_output_file(filename);
   }

   public void write_header ()
   {
      out.write("vocabulary Vout {");
      out.insert_newline();
   }

   public void write_footer ()
   {
      out.write("}");
      out.insert_newline();
   }

   public void add_target_predicate
   (
      final String name,
      final List<Variable> arguments
   )
   {
      out.write("   extern V::");
      out.write(name);
      out.write("/");
      out.write(Integer.toString(arguments.size()));
      out.insert_newline();
   }
}
