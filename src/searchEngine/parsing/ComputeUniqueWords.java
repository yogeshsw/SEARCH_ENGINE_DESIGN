package searchEngine.parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ComputeUniqueWords extends DefaultHandler {   
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
	int actualPageCount = 0;
	HashSet<String> uniqueTokensSet = new HashSet<String>();
	public SortedSet<String> stopWordList;
	//char[] specialCh = {'!','@',']','#','$','%','^','&','*'};
	public ArrayList<String> specialChar = new ArrayList<String>(Arrays.asList("!","@","]","#","$","%","^","&","*"));

	public void computeUniqueWords(){		
    	try {   		
			FileReader file;
	
			
			file = new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\StopWordList.txt");
			BufferedReader br = new BufferedReader(file);
			stopWordList = new TreeSet<String>();
			String str;
			while ((str = br.readLine()) != null) {	
				StringTokenizer st = new StringTokenizer(str);
				while(st.hasMoreElements()){
					stopWordList.add(st.nextToken());
				}				
			}
			stopWordList.add("");
			br.close();			
			file.close();
			
	    	long start = System.nanoTime();
	    	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
            SAXParser saxParser = saxParserFactory.newSAXParser();           
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
    		pageID = new String(ch,start,length);
    		id = false;
    	} else if(text){
    		sbPageText.append(ch,start,length);	    		
    	}
    }	    
    
    public void processPageText(String text){    	   	
    	try{
    		StringTokenizer st = new StringTokenizer(text);
    		while(st.hasMoreTokens()){
    			String tempToken = st.nextToken();
    			
    			if(tempToken!=null && !stopWordList.contains(tempToken) && !specialChar.contains(tempToken) && tempToken.length()<20){
    				uniqueTokensSet.add(tempToken);
    			}
    		}    		
    	}
    	catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Outlink of Page id:"+pageID+": is "+outlinkCount);
	}    
}
