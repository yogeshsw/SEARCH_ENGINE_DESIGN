package searchEngine.parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Pattern;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.lang3.StringUtils;
import org.terrier.terms.PorterStemmer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ConstructDictionary extends DefaultHandler {
	
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
	//public static Integer[] pageInlinkCountArray = Collections.nCopies(10873965, 0).toArray(new Integer[0]);//10873965    177592
	int actualPageCount = 0;
	int index;
	Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);
	boolean isContainSpecialChar = false;
	String tokenToRefine;
	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	public SortedSet<String> stopWordList;
	PorterStemmer stemmer = new PorterStemmer();
	HashMap<String,Integer> hm = new HashMap<String,Integer>();
	
	public void constructDictionary(){
    	
    	try {			
	    	long start = System.nanoTime();
	    	
	    	FileReader file;			
			file = new FileReader("F:\\StopWordList.txt");
			BufferedReader br = new BufferedReader(file);
			stopWordList = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
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
			
			fileWriter = new FileWriter("F:\\Assig2_Stage1.txt");			
	    	bw = new BufferedWriter(fileWriter);
	    	
	    	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
	    	SAXParser saxParser = saxParserFactory.newSAXParser();
	            //sample-enwiki-4.xml      enwiki-latest-pages-articles.xml_2
	        saxParser.parse(new File("F:\\enwiki-latest-pages-articles.xml"), this);
            long time = System.nanoTime() - start;
    		System.out.printf("Parsing Ends: Time Taken %.3f seconds%n", time/1e9);
    		bw.close();
    		fileWriter.close();
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
//        	   if(pageID.equals("1355")){
//        		   System.out.println("PageText:"+sbPageText.toString()+":");
        	   processPageText(sbPageText.toString());
//        	   }
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
    		pageID = new String(ch,start,length);
    		id = false;
    	} else if(text){
    		sbPageText.append(ch,start,length);	    		
    	}
    }	    
    
    public void processPageText(String text){
    	
    	if(!text.startsWith("#REDIRECT") && !text.startsWith("#redirect")){
    	//System.out.println("Text is :"+text+":\n\n\n\n\n\n\n");
	    	if(text.indexOf("==References==")>0){
	    		text = text.substring(0,text.indexOf("==References=="));
	    	}else if(text.indexOf("== References ==")>0){
	    		text = text.substring(0,text.indexOf("== References =="));
	    	}else if(text.indexOf("==See also==")>0){
	    		text = text.substring(0,text.indexOf("==See also=="));
	    	}else if(text.indexOf("== See also ==")>0){
	    		text = text.substring(0,text.indexOf("== See also =="));
	    	}
	    	
	    	//System.out.println("Refined Tokens from the text is:"+text);
	    	try{
				StringTokenizer st = new StringTokenizer(text);
				while(st.hasMoreTokens()){
					tokenToRefine = st.nextToken();		
					
					if(tokenToRefine.contains(".jpg") || tokenToRefine.contains(".Jpg") || tokenToRefine.contains(".JPG") 
			        		   || tokenToRefine.contains(".jpeg") || pageTitle.contains(".Jpeg") || tokenToRefine.contains(".JPEG") 
			        		   || tokenToRefine.contains(".gif") || tokenToRefine.contains(".Gif") || tokenToRefine.contains(".GIF") 
			        		   || tokenToRefine.contains(".png") || tokenToRefine.contains(".Png") || tokenToRefine.contains(".PNG") 
			        		   || tokenToRefine.contains(".txt") || tokenToRefine.contains(".TXT") || tokenToRefine.contains(".xml")
			        		   || tokenToRefine.contains("Wikipedia:") || tokenToRefine.contains("Category:") || tokenToRefine.contains("Template:")
			        		   || tokenToRefine.contains("http") || tokenToRefine.contains("www") || tokenToRefine.contains("ftp") || tokenToRefine.endsWith("ref") 
			        		   || tokenToRefine.endsWith("REDIRECT")){
				    	
					}else{				
					
						isContainSpecialChar = p.matcher(tokenToRefine).find();
					    
					    if(isContainSpecialChar){
					    	tokenToRefine = tokenToRefine.replaceAll("[^a-zA-Z0-9]+", "").trim();
					    }
					    
					    if(!stopWordList.contains(tokenToRefine) && tokenToRefine.length()>0){				    	
					    	
					    	if(StringUtils.isNumeric(tokenToRefine) && tokenToRefine.length()<5){
								//int temp_int = Integer.parseInt(tokenToRefine);
								//tsDictionary.add(temp_int+"");
								//System.out.println("Integer");
							}
							else if(StringUtils.isAlpha(tokenToRefine)){
								tokenToRefine = stemmer.stem(tokenToRefine);
								if(tokenToRefine.length()>2 && tokenToRefine.length()<16){
									//tsDictionary.add(tokenToRefine);
									if(hm.containsKey(tokenToRefine)){
										hm.put(tokenToRefine, hm.get(tokenToRefine) + 1 );
									}else{
										hm.put(tokenToRefine, 1);
									}
								}
								//System.out.println("Alpha");
	
							}else{
								
							}				    	
					    }
					}
				}
				writePageListToFile();
				
			}catch(Exception e){
				e.printStackTrace();
			}
    	}
		//System.out.println("Outlink of Page id:"+pageID+": pageTitle "+pageTitle+" is "+outlinkCount);
		
	}
    
    public void writePageListToFile(){
		try {
			StringBuilder sb = new StringBuilder();
			sb.append(pageTitle+"-Title-"+pageID+"-Page-ID-");
			
			for(Map.Entry<String, Integer> entry:hm.entrySet()){
				sb.append(entry.getKey()+":"+entry.getValue()+",");
			}
			
			
			bw.write(sb.toString()+"-E-O-C-");
			bw.write("\n");
			bw.flush();
			//tsDictionary.clear();
			hm.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
}
