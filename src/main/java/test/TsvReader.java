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

    public static List<List<String>> tsvReadFile(String filePaths ) throws Exception {

        String filePath = "/home/sergey/Рабочий стол/Тестовое/source-data.tsv";

        StringTokenizer st;

        BufferedReader TSVFile = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), "utf-16"));

        List<List<String>> allData = new ArrayList<List<String>>();

        String dataRow = TSVFile.readLine();

        while (dataRow != null) {

            List<String> lineData = new ArrayList<String>();

            st = new StringTokenizer(dataRow, "\t");

            while( st.hasMoreElements() ){
                lineData.add( st.nextElement().toString() );
            }

            allData.add(lineData);

            dataRow = TSVFile.readLine();
        }

        TSVFile.close();

        return allData;
    }
}
