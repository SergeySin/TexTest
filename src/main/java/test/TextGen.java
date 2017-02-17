package test;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sergey on 12.02.17.
 */
public class TextGen {

    private List<String> colsName = new ArrayList<String>();
    private List<Integer> colsWidth = new ArrayList<Integer>();
    private int pageHeight;
    private int pageWidth;
    private final String TITLE_COL = "title";
    private final String WIDTH_PAGE = "width";
    private final String HEIGHT_PAGE = "height";
    private final String WIDTH_COL = "width";

    TextGen(String confPath){
        try {
            File inputFile = new File(confPath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();
            Node pageNode = doc.getElementsByTagName("page").item(0); // удалить
            Element eElement = (Element) pageNode;
            this.pageHeight = GenUtils.getIntElemParam(eElement,HEIGHT_PAGE);
            this.pageWidth = GenUtils.getIntElemParam(eElement, WIDTH_PAGE);
            NodeList colsNode = doc.getElementsByTagName("column");

            for (int i=0; i < colsNode.getLength();i++){// итератор
                Element eCol = (Element) colsNode.item(i);
                this.colsName.add(GenUtils.getElemParam(eCol,TITLE_COL));
                this.colsWidth.add(GenUtils.getIntElemParam(eCol,WIDTH_COL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<String> getText(List<List<String>> tsvData){ // getContent
        List<String> text = getContentText( tsvData );
        text = addTitles(text);
        return text;
    }
    private List<String> getContentText(List<List<String>> rows ){
        List<String> contentText = new ArrayList<String>();
        for (List<String> row : rows){
            String delimiter = GenUtils.addDelimiter(getFullWidth()).toString();
            contentText.add(delimiter);
            contentText.addAll(getEntryText(row));
        }
        return contentText;
    }
    private static List<String> getPhraseCol(String phrase, int colWidth){

        List<String> phraseSplit = GenUtils.getSplitList( phrase, colWidth );
        List<String> phraseCol = new ArrayList<String>();

        int pos = 0;
        while( pos< phraseSplit.size() ) {
            String curStr = phraseSplit.get(pos);
            pos++;
            boolean nextWordFlag = false;
            while( pos < phraseSplit.size() ){
                nextWordFlag = true;
                String nextStr = phraseSplit.get(pos);
                String buildStr = curStr + " " + nextStr;
                if ( buildStr.length() > colWidth ){
                    phraseCol.add( curStr );
                    break;
                }else{
                    curStr = buildStr;
                    pos++;
                    if ( pos >= phraseSplit.size() ){
                        phraseCol.add( curStr );
                    }
                }
            }
            if (!nextWordFlag){
                phraseCol.add( curStr );
            }
        }
        return phraseCol;
    }

    private List<String> getEntryText(List<String> tableEntry){
        List<String> entryText = new ArrayList<String>();
        List<List<String>> phraseCols = new ArrayList<List<String>>();
        for (int j=0; j<tableEntry.size();j++){
            int colWidth = this.colsWidth.get(j);
            String curWord = tableEntry.get(j);
            phraseCols.add(getPhraseCol( curWord, colWidth ));
        }
        int maxColSize = GenUtils.getMaxColSize(phraseCols);
        List<List<StringBuffer>> normText = getNormalizeColsText(phraseCols, maxColSize);
        for (int k=0; k<maxColSize;k++){
            StringBuffer curStr = new StringBuffer();
            for (int m=0; m<normText.size();m++){
                StringBuffer curWord = normText.get(m).get(k);
                curStr.append("| ").append( curWord ).append(" ");
            }
            curStr.append("|");
            entryText.add(curStr.toString());
        }
        return entryText;
    }

    private List<String> addTitles( List<String> text) {
        String nextPageSym = "~";
        List<String> completeText = new ArrayList<String>();
        List<String> title = getEntryText( this.colsName );
        completeText.addAll(title);
        Iterator<String> it = text.iterator();

        while (it.hasNext()){
            completeText.add(it.next());
            if((completeText.size()%this.pageHeight) == 0){
                completeText.add(nextPageSym);
                completeText.addAll(title);
            }
        }
        Iterator<String> iter = completeText.iterator();
//        while (iter.hasNext()){
//            System.out.println( iter.next() );
//        }
        return completeText;
    }

    private List<List<StringBuffer>> getNormalizeColsText(List<List<String>> entryText, int rowCount){
        List<List<StringBuffer>> normalizeData = new ArrayList<List<StringBuffer>>();
        for (int i=0; i < entryText.size(); i++){
            List<StringBuffer> cellContent = new ArrayList<StringBuffer>();
            List<String> colListStr = entryText.get(i);
            int colWidth = this.colsWidth.get(i);

            for (int j=0; j < colListStr.size(); j++){
                StringBuffer strBuf = new StringBuffer(colListStr.get(j));
                strBuf = GenUtils.addSpaces( strBuf, colWidth - strBuf.length() );
                cellContent.add(strBuf);
            }
            for (int k=colListStr.size(); k < rowCount; k++){
                StringBuffer strBuf = new StringBuffer();
                strBuf = GenUtils.addSpaces(strBuf, colWidth);
                cellContent.add(strBuf);
            }
            normalizeData.add(cellContent);
        }
        return normalizeData;
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
