package searchEngine.parsing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.StringTokenizer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseOutGoingLink extends DefaultHandler {
	
    public HashSet<String> s = new HashSet<String>();
    private HashMap<Integer,ArrayList<Integer>> hmPageIDFromTO = new HashMap<Integer,ArrayList<Integer>>();
    private HashMap<Integer,Double> hmPageIDUpdateRank = new HashMap<Integer,Double>();
    private HashMap<Integer,Double> hmPageIDRank = new HashMap<Integer,Double>();
    private HashMap<Integer,Double> hmPageIDInLinkRank = new HashMap<Integer,Double>();
    private HashMap<String,String> hmPageTitleID = new HashMap<String,String>();
    private Integer N = 226028;
    private Double e = 0.1;
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
	
	public void processOutGoingLink(){
    	
    	try {
			fileWriter = new FileWriter("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\webGraph_45GB.txt");			
	    	bw = new BufferedWriter(fileWriter);
	    	long start = System.nanoTime();
	    	SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        
            SAXParser saxParser = saxParserFactory.newSAXParser();
            //---------------------------
            FileInputStream fstream;
    		StringTokenizer st;
    		StringBuilder sb = new StringBuilder();
    		try {
	    		fstream = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\hmPageIDTitle.txt");
	
	    		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	
	    		String strLine;
	    		//Read File Line By Line
	    		while ((strLine = br.readLine()) != null){
	    		// Print the content on the console
	    		st = new StringTokenizer(strLine,"<>");
	    		hmPageTitleID.put(st.nextToken(), st.nextToken());
	
	    		}
	    		
	    		System.out.println("Title ID Mapping Done:"+hmPageTitleID.size());
	    		//Close the input stream
	    		br.close();
    		}catch(Exception e){
    			e.printStackTrace();
    		}
            
            
            
            
            //----------------------------
            saxParser.parse(new File("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\enwiki-latest-pages-articles.xml_2"), this);
            
            writeMapToFile();
            System.out.println("WebGraphCreated");
            
            //------------------------------------------------------
            
            
            // hmPageIDFromTO contains pageID to PageId link Integer -> ArrayList<Integer> 
            // hmPageIDUpdateRank
            computePageRank();
            while(computeRankDifference()){
            	hmPageIDRank.putAll(hmPageIDUpdateRank);
            	hmPageIDUpdateRank = new HashMap<Integer,Double>();
            	computePageRank();
            }
            
            System.out.println("Printing Final Page Rank:"+hmPageIDUpdateRank);
            
            //-------------------------------------------------------
            
            
            long time = System.nanoTime() - start;
    		System.out.printf("Parsing Ends: Time Taken %.3f seconds%n", time/1e9);
    		//bw.close();
    		//fileWriter.close();
    		
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
	        	
	        	
	        	if(hmPageIDFromTO.size()>100){
	        		writeMapToFile();
	        	}
	        	
	        	
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
	        	   
		    		
		    		hmPageIDFromTO.put(Integer.parseInt(pageID),processPageText(sbPageText.toString()));
		    		idCount = false;
		    		text = false;
	           }
	        }else if( qName.equalsIgnoreCase("title") ){
	        	sbPageText = new StringBuilder();
	        	title = true;
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
	    
	    public void writeMapToFile(){
			try {				
				StringBuffer sb = new StringBuffer();
				for(Map.Entry<Integer,ArrayList<Integer>> entry : hmPageIDFromTO.entrySet()){
					Integer main = entry.getKey();
					sb.append(main+"==");
	                ArrayList<Integer> tempAL = entry.getValue();
	                for(Integer i:tempAL){
	                	sb.append(i+",");
	                }
	                sb.append("\n");
	                
				}
				bw.write(sb.toString());
    			bw.flush();
				hmPageIDFromTO.clear();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
	    }
	    
	    
	public ArrayList<Integer> processPageText(String text){
		ArrayList<Integer> redirectedPageID = new ArrayList<Integer>();
		StringTokenizer st = new StringTokenizer(text,"]]");
		while(st.hasMoreTokens()){
			String title = st.nextToken();
			title = title.substring(title.indexOf("[[")+2,title.length());
			if(pageTitle.contains(".jpg") || pageTitle.contains(".Jpg") || pageTitle.contains(".JPG") 
	        		   || pageTitle.contains(".jpeg") || pageTitle.contains(".Jpeg") || pageTitle.contains(".JPEG") 
	        		   || pageTitle.contains(".gif") || pageTitle.contains(".Gif") || pageTitle.contains(".GIF") 
	        		   || pageTitle.contains(".png") || pageTitle.contains(".Png") || pageTitle.contains(".PNG") 
	        		   || pageTitle.contains(".txt") || pageTitle.contains(".TXT") || pageTitle.contains(".xml")
	        		   || pageTitle.contains("Wikipedia:") || pageTitle.contains("Category:") || pageTitle.contains("Template:")){
				
			}else{
					if(title.contains("|")){
						title = title.substring(0, title.indexOf("|"));
					}
					//System.out.println("Title:"+title);
					String tempPageID = hmPageTitleID.get(title);//searchPageID(title);
					
					if(tempPageID!=null){
						try{
							redirectedPageID.add(Integer.parseInt(tempPageID));
						}catch(NumberFormatException nfe){
							System.out.println("Number Formate Exception for:"+tempPageID);
						}
					}
					
			}			
		}
		return redirectedPageID;
	}
	    
	public String searchPageID(String pageTitle){

		long start = System.nanoTime();
		FileReader file;
		try {
			file = new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\hmPageIDTitle.txt");
			BufferedReader br = new BufferedReader(file);
			
			String str ="";
			while ((str = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(str,"<>");	
				while (st.hasMoreElements()) {
					if(pageTitle.equalsIgnoreCase(st.nextToken())){
						return st.nextToken();
					}
				}
			}		
			
			br.close();
			file.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		long time = System.nanoTime() - start;
		System.out.printf("Time taken by Stage2 is %.3f seconds%n", time/1e9);
		return "0";
	}
	
	public void computePageRank(){
		
		try {
			
			//FileWriter fileWriter1 = new FileWriter("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\StoringTempResult.txt");
			//BufferedWriter bw1 = new BufferedWriter(fileWriter1);
			//StringBuffer sb1 = new StringBuffer();
		
			Double initValue = 1.0/N;
			
			// Initializing initial Page Rank
			//sb1.append("\n----------------Initializing initial Page Rank------------------------------");
			for(Map.Entry<Integer,ArrayList<Integer>> entry : hmPageIDFromTO.entrySet()){
				Integer pageID = entry.getKey();
				if(pageID!=null){
					hmPageIDRank.put(pageID, initValue);
				}
			}
			//sb1.append("\n Size of hmPageIDRank "+hmPageIDRank.size());
			//bw1.write(sb1.toString());
			//bw1.flush();
			//sb1 = null;
			//sb1 = new StringBuffer();
			
			
			
			//sb1.append("\n-----------Calculating InLinks---------------------------\n");
			for(Map.Entry<Integer,Double> entry : hmPageIDRank.entrySet()){
				Integer pageID = entry.getKey();
				//sb1.append("\nComputing InLinks of PageID:"+pageID+"\n");
				// Computing Inlinks with their corresponding outlink number
				for(Map.Entry<Integer,ArrayList<Integer>> entry1 : hmPageIDFromTO.entrySet()){
					Integer pageID1 = entry1.getKey();
					ArrayList<Integer> pageIDList = entry1.getValue();
					if(pageIDList.contains(pageID)){
						//sb1.append("\nInLink From PageID:"+pageID1);
						hmPageIDInLinkRank.put(pageID1, hmPageIDRank.get(pageID1)/pageIDList.size());
					}
				}
				//sb1.append("\nTotal Inlinks for pageID:"+pageID+":"+hmPageIDInLink);
				//bw1.write(sb1.toString());
				//bw1.flush();
				//sb1 = null;
				//sb1 = new StringBuffer();
				
				
				//sb1.append("\n--------------Fetching PageRank Of Inlinks-------------------");
				Double pageRankSum=0.0;
				for(Map.Entry<Integer,Double> entry2 : hmPageIDInLinkRank.entrySet()){
					Integer pageID2 = entry2.getKey();
					Double inLinkRankCount = entry2.getValue();
					
					pageRankSum = pageRankSum + inLinkRankCount;
					//sb1.append("\n   PageRank of pageID:"+pageID+": pageRankSum:"+pageRankSum);
				}
				Double pageRank = (0.85/N) + (0.15 * pageRankSum);
				hmPageIDUpdateRank.put(pageID, pageRank);
				
			}
			//bw1.close();
			//fileWriter1.close();
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public boolean computeRankDifference(){
		
		Double pageRankOld = 0.0;
		for(Map.Entry<Integer,Double> entry : hmPageIDRank.entrySet()){
			Double pageRank = entry.getValue();
			
			pageRankOld = pageRankOld + pageRank;			
		}
		System.out.println("Total Of OldPageRank:"+pageRankOld);
		
		Double pageRankNew = 0.0;
		for(Map.Entry<Integer,Double> entry : hmPageIDUpdateRank.entrySet()){
			Double pageRank = entry.getValue();
			
			pageRankNew = pageRankNew + pageRank;			
		}
		System.out.println("Total Of NewPageRank:"+pageRankNew);
		
		Double diff = pageRankOld - pageRankNew;
		System.out.println("Difference :"+diff);
		if(Math.abs(diff)>e){
			return true;
		}else{
			return false;
			
		}
	}
	
}
