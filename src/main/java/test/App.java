package test;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        TextGen textGen = new TextGen(args[0]);
        try{
            List<String> textArr = textGen.getText(TsvReader.tsvReadFile(args[1]));
            TextWriter.writeText( args[2], textArr );
        }catch (Exception e){
            e.printStackTrace();
        };
    }
}
