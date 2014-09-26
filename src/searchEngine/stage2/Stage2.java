package searchEngine.stage2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

public class Stage2 {
	

	public static TreeSet<String> tsDictionary = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
	FileWriter fileWriter;
	BufferedWriter bw;
	
	public void refineStage1Output(){
		long start = System.nanoTime();
		FileReader file;
		BufferedReader br;
		String str;
		try{
			
			file = new FileReader("F:\\enwiki-latest-pages-articles.xml");
		
			fileWriter =  new FileWriter("F:\\Assig2_Stage2.txt");
			bw = new BufferedWriter(fileWriter);
			br = new BufferedReader(file);
			int i =0;
			StringBuffer sb = new StringBuffer();
			StringTokenizer st;
			while ((str = br.readLine()) != null) {
				
				String tempStr = str.substring(str.indexOf("-Page-ID-[")+10,str.indexOf("]-E-O-C-"));
				st = new StringTokenizer(tempStr);
				String temp_token;
				while(st.hasMoreElements()){
					temp_token = st.nextToken(",").trim();
					if(StringUtils.isAlpha(temp_token)){

						tsDictionary.add(temp_token);	
					}
				}
				i++;
				if(i%50000==0){
					System.out.println("Writing to file :"+tsDictionary.size()+" words");
					writePageListToFile();
				}
			}
			
			System.out.println("Final value of i is "+i+" :"+tsDictionary);
			writePageListToFile();
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
