package test;


import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * Created by sergey on 15.02.17.
 */
public class TextWriter {

    public static void WriteText(String path, List<String> text){

        try {

            FileOutputStream outputStream = new FileOutputStream(path);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-16");
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            for (int i=0;i<text.size();i++){

                bufferedWriter.write(text.get(i));
                bufferedWriter.newLine();
            }

            bufferedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
