package searchEngine.parsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseDocument extends DefaultHandler {
	 
    
    public HashSet<String> s = new HashSet<String>();
    boolean title = false;
    boolean text = false;
    boolean id = false;
    boolean idCount = false;
    boolean bGender = false;
    boolean bRole = false;
    String pageTitle = "";
    String pageID = "";
    Integer pageNumber = 1;
    //FileWriter fileWriter;
	BufferedWriter bw;
	Integer bogusPageTitleCnt = 0;
	int totalPageNumber = 10873965;
	public static String[] pageTitleArray = new String[10873965];//10873965//177592
	public static Integer[] pageIDArray = new Integer[10873965];
	int actualPageCount = 0;
    
    public void startParsing(){
    	
    	try {
			//fileWriter = new FileWriter("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\hmPageIDTitle_45GB_TEST1.txt");			
	    	//bw = new BufferedWriter(fileWriter);
    		
	    	long start = System.nanoTime();
	    	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //sample-enwiki-4.xml      enwiki-latest-pages-articles.xml_2
            saxParser.parse(new File("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\enwiki-latest-pages-articles.xml_2"), this);
            
            //writeMapToFile();
            
            long time = System.nanoTime() - start;
    		System.out.printf("Parsing Ends: Time Taken %.3f seconds%n", time/1e9);
    		    		
    		//bw.close();
    		//fileWriter.close();
    		
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	System.out.println("Total actual page count :"+actualPageCount);
        	System.out.println("Total bogus title page count :"+bogusPageTitleCnt);
        	System.out.println("Total Size of Page ID:"+pageNumber);
        }
    }
     
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
 
    	s.add(qName.toUpperCase().trim());
    	
        if (qName.equalsIgnoreCase("page")) {
        	pageNumber = pageNumber+1;        	
        }else if( qName.equalsIgnoreCase("title") ){
        	title = true;
        }else if( qName.equalsIgnoreCase("id") && !idCount){
        	id = true;
        	idCount = true;
        }
    } 
 
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
    	if (qName.equalsIgnoreCase("page")) {
           if(pageTitle.contains(".jpg") || pageTitle.contains(".Jpg") || pageTitle.contains(".JPG") 
        		   || pageTitle.contains(".jpeg") || pageTitle.contains(".Jpeg") || pageTitle.contains(".JPEG") 
        		   || pageTitle.contains(".gif") || pageTitle.contains(".Gif") || pageTitle.contains(".GIF") 
        		   || pageTitle.contains(".png") || pageTitle.contains(".Png") || pageTitle.contains(".PNG") 
        		   || pageTitle.contains(".txt") || pageTitle.contains(".TXT") || pageTitle.contains(".xml")
        		   || pageTitle.contains("Wikipedia:") || pageTitle.contains("Category:") || pageTitle.contains("Template:")){
        	   bogusPageTitleCnt++;
           }else{
        	   if(pageTitle != null && pageID != null){
	        	   pageTitleArray[actualPageCount] = pageTitle;
	        	   pageIDArray[actualPageCount] = Integer.parseInt(pageID);
	        	   actualPageCount++;
        	   }else{
        		   System.out.println("PageTitle Is NULL");
        	   }
        		   
	    	   idCount = false;
	    	  		
	    	   text = false;
           }
        }
    } 
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	
    	if(title){
    		pageTitle = new String(ch, start, length);
    		title = false;
    	} else if(id){
    		pageID = new String(ch,start,length);
    		id = false;
    	} 
    }
    
    
    public void writeMapToFile(){
		/*try {				
			StringBuffer sb = new StringBuffer();
			Set<String> keySet = hmPageTitleID.keySet();
			for(String pageTitle : keySet){
                sb.append(pageTitle+"<>"+hmPageTitleID.get(pageTitle));
                sb.append("\n");
                bw.write(sb.toString());
    			bw.flush();
    			sb = new StringBuffer();
			}				
			hmPageTitleID.clear();
			hmPageIDTitle.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		
    }
}
