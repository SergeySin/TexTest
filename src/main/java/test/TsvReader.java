package test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Created by sergey on 12.02.17.
 */
public class TsvReader {

    public static List<List<String>> tsvReadFile(String filePath ) throws Exception {
        StringTokenizer st;
        BufferedReader tsvFile = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-16"));
        List<List<String>> tsvData = new ArrayList<List<String>>();
        String line = tsvFile.readLine();
        while (line != null) {
            List<String> lineData = new ArrayList<String>();
            st = new StringTokenizer(line, "\t");
            while( st.hasMoreElements() ){
                lineData.add( st.nextElement().toString() );
            }
            tsvData.add(lineData);
            line = tsvFile.readLine();
        }
        tsvFile.close();
        return tsvData;
    }
}
