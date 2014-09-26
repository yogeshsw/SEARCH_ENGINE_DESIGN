package searchEngine.stage4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.terrier.terms.PorterStemmer;

public class Stage4 {
	

	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	FileWriter fileWriter;
	BufferedWriter bw;
	
	public void refineStage3Output(){
		long start = System.nanoTime();
		FileReader file;
		BufferedReader br;
		String str;
		try{
			PorterStemmer stemmer = new PorterStemmer();
			file = new FileReader("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\WikiDatabase\\finalRefinedDictionary.txt");
		
			fileWriter =  new FileWriter("C:\\Users\\Niravkumar\\Documents\\SanJose\\CS286_AkashNanavati\\WikiDatabase\\finalDictionary_SingleRow.txt");
			bw = new BufferedWriter(fileWriter);
			br = new BufferedReader(file);
			int i =0;
			StringBuffer sb = new StringBuffer();
			StringTokenizer st;
			while ((str = br.readLine()) != null) {
				
				String tempStr = str.substring(1,str.length()-1);
				st = new StringTokenizer(tempStr);
				String temp_token;
				while(st.hasMoreElements()){
					temp_token = st.nextToken(",").trim();
					//System.out.println(temp_token);
					
					if(StringUtils.isNumeric(temp_token) && temp_token.length()<5){
						int temp_int = Integer.parseInt(temp_token.trim());
						tsDictionary.add(temp_int+"");
						//System.out.println("Integer");
					}
					else if(StringUtils.isAlpha(temp_token)){
						tsDictionary.add(temp_token);
						//System.out.println("Alpha");

					}else{
						
					}
					
				}
				i++;
			}
			
			System.out.println("Final value of i is "+i+" :"+tsDictionary.size());
			//writePageListToFile();
			//bw.write(sb.toString());
			bw.flush();
			bw.close();
			br.close();
			fileWriter.close();
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
	}
	
	public void writePageListToFile(){
		try {				
			bw.write(tsDictionary.toString());
			bw.write("\n");
			bw.flush();
			tsDictionary.clear();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
    }
}
