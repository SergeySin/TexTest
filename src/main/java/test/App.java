package test;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {

        DataRow er = new DataRow(args[0]);

        try{

            List<String> textArr = er.getText(TsvReader.tsvReadFile(args[1]));
            TextWriter.WriteText( args[2], textArr );

        }catch (Exception e){

            e.printStackTrace();
        };

    }


}
