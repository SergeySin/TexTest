package test;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sergey on 12.02.17.
 */
public class DataRow {

    private List<String> colsName = new ArrayList<String>();
    private List<Integer> colsWidth = new ArrayList<Integer>();
    private int pageHeight;
    private int pageWidth;


    DataRow(String confPath){

        try {
            File inputFile = new File(confPath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            Node pageNode = doc.getElementsByTagName("page").item(0);

            Element eElement = (Element) pageNode;

            String pageWidth = eElement.getElementsByTagName("width").item(0).getChildNodes().item(0).getNodeValue();
            String pageHeight = eElement.getElementsByTagName("height").item(0).getChildNodes().item(0).getNodeValue();

            this.pageHeight = Integer.parseInt(pageHeight);
            this.pageWidth = Integer.parseInt(pageWidth);

            NodeList colsNode = doc.getElementsByTagName("column");

            for (int i=0; i < colsNode.getLength();i++){

                Element eCol =  (Element) colsNode.item(i);
                String colTitle = eCol.getElementsByTagName("title").item(0).getChildNodes().item(0).getNodeValue().toString();
                String cloWidth = eCol.getElementsByTagName("width").item(0).getChildNodes().item(0).getNodeValue().toString();

                this.colsName.add(colTitle);
                this.colsWidth.add(Integer.parseInt(cloWidth));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> parseRow( List<String> row){

        for ( int i=0; i< row.size();i++){

            int stringSize = colsWidth.get(i);

        }

        return new ArrayList<String>();

    }

    private static List<String> createPhraseCol(String phrase, int colWidth){

        String arrWords[] = phrase.split("\\s|(?<=\\G.{" + colWidth + "})");

        List<String> wordList = Arrays.asList(arrWords);

        Iterator<String> listIter = wordList.iterator();

        List<String> retList = new ArrayList<String>();

        int pos = 0;

        while( pos< wordList.size() ) {

            String curStr = wordList.get(pos);
            pos++;

            boolean nextWordFlag = false;

            while( pos < wordList.size() ){

                nextWordFlag = true;
                String nextStr = wordList.get(pos);

                String buildStr = curStr + " " + nextStr;

                if ( buildStr.length() > colWidth ){

                    retList.add( curStr );

                    break;

                }else{

                    curStr = buildStr;
                    pos++;

                    if ( pos >= wordList.size() ){

                        retList.add( curStr );
                    }

                }

            }

            if (!nextWordFlag){

                retList.add( curStr );

            }
        }

        return retList;

    }

    private List<String> getRowText( List<String> strData){

        List<String> retText = new ArrayList<String>();

        List<List<String>> phraseColArr = new ArrayList<List<String>>();

        for (int j=0; j<strData.size();j++){

            int colWidth = this.colsWidth.get(j);
            String curWord = strData.get(j);
            phraseColArr.add(createPhraseCol( curWord, colWidth ));

        }

        int maxColSize = getMaxColSize(phraseColArr);

        List<List<StringBuffer>> phraseColBufArr = getNormalizeRowData(phraseColArr, maxColSize);

        for (int k=0; k<maxColSize;k++){

            StringBuffer curStr = new StringBuffer();

            for (int m=0; m<phraseColBufArr.size();m++){

                StringBuffer curWord = phraseColBufArr.get(m).get(k);
                curStr.append("| ").append( curWord ).append(" ");

            }

            curStr.append("|");
            retText.add(curStr.toString());
        }

        return retText;
    }

    private List<String> getDataText( List<List<String>> allData ){

        List<String> retText = new ArrayList<String>();

            for (int i=0; i<allData.size();i++){
                retText.add(addDelimiter(getFullWidth()).toString());
                retText.addAll(getRowText(allData.get(i)));
            }

        return retText;

    }


    public List<String> getText(List<List<String>> allData){

        List<String> allText = getDataText( allData );
        allText = addTitles(allText);
        return allText;
    }

    private List<String> addTitles( List<String> textArr) {

        String nextPageSym = "~";

        List<String> retText = new ArrayList<String>();

        List<String> titleText = getRowText( this.colsName );
        retText.addAll(titleText);

        Iterator<String> it = textArr.iterator();

        while (it.hasNext()){

            retText.add(it.next());

            if((retText.size()%this.pageHeight) == 0){

                retText.add(nextPageSym);
                retText.addAll(titleText);

            }

        }
        Iterator<String> iter = retText.iterator();

        while (iter.hasNext()){

            System.out.println( iter.next() );
        }

        return retText;

    }

    private int getMaxColSize(List<List<String>> arr){

        int size = 0;

        for (int i=1;i<arr.size();i++){

            int colSize = arr.get(i).size();

            if ( colSize > size ){

                size = colSize;

            }

        }

      return size;

    }

    private List<List<StringBuffer>> getNormalizeRowData(List<List<String>> arr, int rowCount){

        List<List<StringBuffer>> retList = new ArrayList<List<StringBuffer>>();

        for (int i=0; i < arr.size(); i++){

            List<StringBuffer> colListBuf = new ArrayList<StringBuffer>();
            List<String> colListStr = arr.get(i);

            int colWidth = this.colsWidth.get(i);

            for (int j=0; j < colListStr.size(); j++){

                StringBuffer strBuf = new StringBuffer(colListStr.get(j));
                strBuf = addSpaces( strBuf, colWidth - strBuf.length() );
                colListBuf.add(strBuf);

            }

            for (int k=colListStr.size(); k < rowCount; k++){

                StringBuffer strBuf = new StringBuffer();
                strBuf = addSpaces(strBuf, colWidth);
                colListBuf.add(strBuf);

            }
            retList.add(colListBuf);
        }

        return retList;
    }

    private StringBuffer addSpaces( StringBuffer str,int count){

        for (int i=0;i<count;i++){

            str.append(" ");

        }

        return str;

    }

    private StringBuffer addDelimiter(int count){

        StringBuffer buf = new StringBuffer();

        for (int i=0;i<count;i++){

            buf.append("-");

        }

        return buf;

    }

    private int getFullWidth(){

        int frameByCol = 3;
        int endFrame = 1;

        int totalWidth = 0;

        for (int i=0;i<colsWidth.size();i++){

            totalWidth = totalWidth + frameByCol + colsWidth.get(i);

        }

        totalWidth = totalWidth + endFrame;

      return totalWidth;

    }

}
