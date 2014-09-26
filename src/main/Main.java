package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.print.attribute.standard.PagesPerMinuteColor;

import org.apache.commons.lang3.StringUtils;

import searchEngine.parsing.ComputeInLinkCount;
import searchEngine.parsing.ComputeOutLinkCount;
import searchEngine.parsing.ComputeUniqueWords;
import searchEngine.parsing.ConstructDictionary;
import searchEngine.parsing.ParseDocument;
import searchEngine.parsing.ParseOutGoingLink;
import searchEngine.stage2.Stage2;
import searchEngine.stage3.Stage3;
import searchEngine.stage4.Stage4;

public class Main {

	public static String[] pageTitleArray = new String[10873965];//10873965   177592
	public static Integer[] pageIDArray = new Integer[10873965];//177592
	public static Integer[] pageOutlinkCountArray = new Integer[10873965];//10873965    177592
	public static Integer[] pageInlinkCountArray = new Integer[10873965];//10873965    177592
	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	
	public static void main(String[] args) {
		
		try{
		long start = System.nanoTime();
//       Pattern pattern = Pattern.compile(".([a-z])\\1{2,}.", Pattern.CASE_INSENSITIVE);
      //   (?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])
      Pattern pattern = Pattern.compile("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])", Pattern.CASE_INSENSITIVE);
      
      FileInputStream fileIn = new FileInputStream("F:\\wiki\\Dictionary_45G_YOGESH.ser"); 
     FileWriter fileWriter = new FileWriter("F:\\wiki\\hmPageIDTitle_45GB_TEST2.txt");			
  		BufferedWriter bw = new BufferedWriter(fileWriter);
      
      ObjectInputStream in = new ObjectInputStream(fileIn); 
      tsDictionary = (TreeSet<String>)in.readObject(); 
      in.close(); fileIn.close(); 
      long time = System.nanoTime() - start; 
      System.out.printf(" Time Taken to desearialized tsDictionary %.3f seconds%n", time/1e9);
      int count = 0;
      System.out.println("Dictionary Size before:"+tsDictionary.size());
      Iterator<String> itr=tsDictionary.iterator();
      int temp_count = 0;
      while(itr.hasNext()){
              String temp = itr.next();
             
              bw.write(temp);
              bw.write("\n");
  				bw.flush();

              
              
              /*if(pattern.matcher(temp).find()){
                      itr.remove();temp_count++;
              }
             */ 
      }
      System.out.println("Removed word count:"+temp_count);
      System.out.println("Dictionary Size After:"+tsDictionary.size());
		fileWriter.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		/*try{
			 long start = System.nanoTime();
			FileReader file = new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Assig2_Stage2.txt");
			BufferedReader br = new BufferedReader(file);
			int i =0;
			StringBuffer sb = new StringBuffer();
			StringTokenizer st;
			String str;
			while ((str = br.readLine()) != null) {
				
				String tempStr = str.substring(str.indexOf("[")+1,str.indexOf("]"));
				st = new StringTokenizer(tempStr);
				String temp_token;
				while(st.hasMoreElements()){
					temp_token = st.nextToken(",").trim();
					if(temp_token.length()>2 && temp_token.length()<15){

						tsDictionary.add(temp_token);	
					}
				}
				i++;
				
			}
			  long time = System.nanoTime() - start;
				 System.out.printf(" Time Taken to construct Dictionary %.3f seconds%n", time/1e9);
			System.out.println(" Done Size of Dictionary :"+tsDictionary.size());
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("In finally Size of Dictionary :"+tsDictionary.size());
		}
		
		try
	      {			
			 long start = System.nanoTime();
	         FileOutputStream fileOut =
	         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\Dictionary_45G_1.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(tsDictionary);
	         out.close();
	         fileOut.close();
	         System.out.printf("Dictionary Serialized");
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to searized Dictionary %.3f seconds%n", time/1e9);
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }*/
		
		try{
		ConstructDictionary cd = new ConstructDictionary();
		cd.constructDictionary();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			System.out.println("Final SIze of Dictionary:"+ConstructDictionary.tsDictionary.size());
		}
		
		Stage2 s2 = new Stage2();
		s2.refineStage1Output();
		
		Stage3 s3 = new Stage3();
		//s3.refineStage2Output();
		
		Stage4 s4 = new Stage4();
//		s4.refineStage3Output();
		
	// TODO Auto-generated method stub
		
		/*ComputeUniqueWords cuw = new ComputeUniqueWords();
		cuw.computeUniqueWords();*/
		
		
		/*try
	      {
			 long start = System.nanoTime();
	         FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\TitleArray_15G.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         pageTitleArray = (String[])in.readObject();
	         in.close();
	         fileIn.close();
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to desearialized TitleArray %.3f seconds%n", time/1e9);
	      }catch(IOException i)
	      {
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c)
	      {
	         c.printStackTrace();
	         return;
	      }
		
		System.out.println("TitleArray DeSer");*/
		
		
		
		/*ParseDocument pd = new ParseDocument();
		pd.startParsing();*/
		

		/*System.out.println("Checkpoint");
		try
	      {
			System.out.println("Array Sorting Starts");
			Arrays.sort(ParseDocument.pageTitleArray);
			System.out.println("Array Sorting Ends");
			
			 long start = System.nanoTime();
	         FileOutputStream fileOut =
	         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\TitleArray_45G_1.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(ParseDocument.pageTitleArray);
	         out.close();
	         fileOut.close();
	         System.out.printf("TitleArraySaved");
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to searized TitleArray %.3f seconds%n", time/1e9);
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }*/
		
	/*	try
	      {
			 long start = System.nanoTime();
	         FileOutputStream fileOut =
	         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageIDArray_45G_1.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(ParseDocument.pageIDArray);
	         out.close();
	         fileOut.close();
	         System.out.printf("PageIDArraySaved");
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to searized PageIDArray %.3f seconds%n", time/1e9);
	      }catch(IOException i)
	      {
	          i.printStackTrace();
	      }*/

		/*String[] pageTitleArray = new String[177592];//177592    10873965
		Integer[] pageIDArray = new Integer[177592];//177592   
*/		
		/*try{
			 long start = System.nanoTime();
			 //TitleArray_45G_1.ser      TitleArray_15G_1.ser
	         FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\TitleArray_45G_1.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         pageTitleArray = (String[])in.readObject();
	         in.close();
	         fileIn.close();
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to desearialized TitleArray %.3f seconds%n", time/1e9);
		  }catch(IOException i){
		     i.printStackTrace();
		     return;
		  }catch(ClassNotFoundException c){
		     c.printStackTrace();
		     return;
		  }
		
		
		try
	      {
			 long start = System.nanoTime();
			
	         FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageIDArray_45G_1.ser");
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         pageIDArray = (Integer[])in.readObject();
	         in.close();
	         fileIn.close();
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to desearialized TitleArray %.3f seconds%n", time/1e9);
	      }catch(IOException i){
	         i.printStackTrace();
	         return;
	      }catch(ClassNotFoundException c){
	         c.printStackTrace();
	         return;
	      }
		
		try{
			long start = System.nanoTime();
			//TitleArray_45G_1.ser      TitleArray_15G_1.ser
			FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageOutCount_45G_1.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			pageOutlinkCountArray = (Integer[])in.readObject();
			in.close();
			fileIn.close();
			long time = System.nanoTime() - start;
			System.out.printf(" Time Taken to desearialized OutCount %.3f seconds%n", time/1e9);
		}catch(IOException i){
			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c){
			c.printStackTrace();
			return;
		}
		
		
		try{
			long start = System.nanoTime();
			//TitleArray_45G_1.ser      TitleArray_15G_1.ser
			FileInputStream fileIn = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageInCount_45G_1.ser");
			ObjectInputStream in = new ObjectInputStream(fileIn);
			pageInlinkCountArray = (Integer[])in.readObject();
			in.close();
			fileIn.close();
			long time = System.nanoTime() - start;
			System.out.printf(" Time Taken to desearialized OutCount %.3f seconds%n", time/1e9);
		}catch(IOException i){
			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c){
			c.printStackTrace();
			return;
		}*/
	
		
		/*for(int i=5000;i<10000;i++){
			System.out.println(pageTitleArray[i]+":"+pageIDArray[i]+":"+pageOutlinkCountArray[i]+":"+pageInlinkCountArray[i]);
		}*/
		/*while(true){
			
			try{

				long start = System.nanoTime();
				Scanner s = new Scanner(System.in);
				System.out.println("enter title");
				String title = s.nextLine();
				if(title.equalsIgnoreCase("Pooja")){
					s.close();
					break;
				}
				System.out.println("Title:"+title+": PageId:"+pageIDArray[Arrays.binarySearch(pageTitleArray, title)]);
				
			
				long time = System.nanoTime() - start;
				System.out.printf(" Time Taken to search %.3f seconds%n", time/1e9);
			}catch(Exception e){
				e.printStackTrace();
			}
		}*/
		
		/*ComputeOutLinkCount colc = new ComputeOutLinkCount();
		colc.processOutLinkCount();
		System.out.println("========================================");
		try
	      {
			 long start = System.nanoTime();
	         FileOutputStream fileOut =
	         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageOutCount_45G_1.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(ComputeOutLinkCount.pageOutlinkCountArray);
	         out.close();
	         fileOut.close();
	         System.out.printf("PageOutCountArraySaved");
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to searized PageIDArray %.3f seconds%n", time/1e9);
	      }catch(Exception i)
	      {
	          i.printStackTrace();
	      }*/
		
		/*ComputeInLinkCount cilc = new ComputeInLinkCount();
		cilc.processInLinkCount();
		System.out.println("========================================");
		try{
			 long start = System.nanoTime();
	         FileOutputStream fileOut =
	         new FileOutputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\PageInCount_45G_1.ser");
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(ComputeInLinkCount.pageInlinkCountArray);
	         out.close();
	         fileOut.close();
	         System.out.printf("PageInCountArray Saved");
	         long time = System.nanoTime() - start;
			 System.out.printf(" Time Taken to searized PageInCountArray %.3f seconds%n", time/1e9);
	      }catch(Exception i)
	      {
	          i.printStackTrace();
	      }*/
		
		
		/*ParseOutGoingLink pogl = new ParseOutGoingLink();
		pogl.processOutGoingLink(); Do not Use this method 
		
		Double d = 1.0;
		int i = 3;
		
		d = d/i;
		System.out.println(d);*/
		
		/*long start = System.nanoTime();
		FileInputStream fstream;
		StringTokenizer st;
		StringBuilder sb = new StringBuilder();
		HashMap hm = new HashMap();
		try {
		fstream = new FileInputStream("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\Assignment2\\hmPageIDTitle.txt");

		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

		String strLine;
		//Read File Line By Line
		while ((strLine = br.readLine()) != null){
		// Print the content on the console
		st = new StringTokenizer(strLine,"<>");
		sb.append(st.nextToken());
		sb.append(st.nextToken()+"<>");

		}
		
		//Close the input stream
		br.close();
		} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}

		long time = System.nanoTime() - start;
		System.out.printf("Time taken by Stage2 is %.3f seconds%n", time/1e9);*/
	}

}
