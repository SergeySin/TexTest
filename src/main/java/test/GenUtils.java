package test;

import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

/**
 * Created by sergey on 17.02.17.
 */
public class GenUtils {

    public static StringBuffer addDelimiter(int count){
        StringBuffer buf = new StringBuffer();
        for (int i=0;i<count;i++){
            buf.append("-");
        }
        return buf;
    }

    public static int getMaxColSize(List<List<String>> arr){
        int size = 0;
        for (int i=1;i<arr.size();i++){
            int colSize = arr.get(i).size();
            if ( colSize > size ){
                size = colSize;
            }
        }
        return size;
    }

    public static StringBuffer addSpaces( StringBuffer str,int count){
        for (int i=0;i<count;i++){
            str.append(" ");
        }
        return str;
    }

    public static String getElemParam(Element el, String paramName){
        String param = el.getElementsByTagName(paramName).item(0).getChildNodes().item(0).getNodeValue();
        return param;
    }

    public static int getIntElemParam(Element el, String paramName){
        return Integer.parseInt(getElemParam(el, paramName));
    }

    public static List<String> getSplitList(String phrase, int width){
        String split[] = phrase.split("\\s|(?<=\\G.{" + width + "})");
        List<String> splitList = Arrays.asList(split);
        return splitList;
    }

}
