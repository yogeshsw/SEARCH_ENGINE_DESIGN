package searchEngine.parsing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import main.Main;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ComputeInLinkCount  extends DefaultHandler {
	
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
    FileWriter fileWriter;
	BufferedWriter bw;
	Integer bogusPageTitleCnt = 0;
	StringBuilder sbPageText;
	StringBuilder sbPageToID;
	public static Integer[] pageInlinkCountArray = Collections.nCopies(10873965, 0).toArray(new Integer[0]);//10873965    177592
	int actualPageCount = 0;
	int index;
	
	
	public void processInLinkCount(){
    	
    	try {			
	    	long start = System.nanoTime();
	    	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
	    	SAXParser saxParser = saxParserFactory.newSAXParser();
	            //sample-enwiki-4.xml      enwiki-latest-pages-articles.xml_2
	        saxParser.parse(new File("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\enwiki-latest-pages-articles.xml_2"), this);
            long time = System.nanoTime() - start;
    		System.out.printf("Parsing Ends: Time Taken %.3f seconds%n", time/1e9);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
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
        }else if( qName.equalsIgnoreCase("id") && !idCount){
        	id = true;
        	idCount = true;
        }else if( qName.equalsIgnoreCase("title") ){
        	title = true;
        }else if ( qName.equalsIgnoreCase("text")) {
        	text = true;	        	
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
        		   || pageTitle.contains("Wikipedia:") || pageTitle.contains("Category:") || pageTitle.contains("Template:")
        		   ){
        	   //System.out.println("Contains -------------------------------------------");
        	   bogusPageTitleCnt++;
           }else{
        	   processPageText(sbPageText.toString());
        	   actualPageCount++;
        	  
        	   idCount = false;
        	   text = false;
           }
        }else if( qName.equalsIgnoreCase("title") ){
        	sbPageText = new StringBuilder();
        	title = false;
        }
    } 
 
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
    	
    	if(title){
    		pageTitle = new String(ch, start, length);
    		title = false;
    	} else if(id){
    		//pageID = new String(ch,start,length);
    		id = false;
    	} else if(text){
    		sbPageText.append(ch,start,length);	    		
    	}
    }	    
    
    public void processPageText(String text){
    	
    	
    	if(text.indexOf("==References==")>0){
    		text = text.substring(0,text.indexOf("==References=="));
    	}   	
    	try{
			StringTokenizer st = new StringTokenizer(text,"]]");
			while(st.hasMoreTokens()){
				String title = st.nextToken();
				title = title.substring(title.indexOf("[[")+2,title.length());
				
				if(title.contains(".jpg") || title.contains(".Jpg") || title.contains(".JPG") 
		        		   || title.contains(".jpeg") || pageTitle.contains(".Jpeg") || pageTitle.contains(".JPEG") 
		        		   || title.contains(".gif") || title.contains(".Gif") || title.contains(".GIF") 
		        		   || title.contains(".png") || title.contains(".Png") || title.contains(".PNG") 
		        		   || title.contains(".txt") || title.contains(".TXT") || title.contains(".xml")
		        		   || title.contains("Wikipedia:") || title.contains("Category:") || title.contains("Template:")){
					
				}else{
					if(title != null && title.length()<60){
						if(title.contains("|")){
							title = title.substring(0, title.indexOf("|")).trim();
						}
						index = Arrays.binarySearch(Main.pageTitleArray, title);
						if(index>=0){
							pageInlinkCountArray[index]++;
						}
					}
				}
			}
			
		}
    	catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Outlink of Page id:"+pageID+": pageTitle "+pageTitle+" is "+outlinkCount);
		
	}    
}
