package essi.lod.util;

import java.io.BufferedReader;
import java.io.FileReader;
import org.jsoup.Jsoup;

public class RemoveHtmlTags {
	
	public static void main (String[] args)
	{

	    String line;
	    String fixedLine;
	    BufferedReader br;
		FileWrite fw = new FileWrite ();
		
		try {
		      br = new BufferedReader(new FileReader(args[0]));
		      
	    	  // read the file line by line
	    	  while ((line = br.readLine()) != null) {
	    		fixedLine = Jsoup.parse(line).text();
	    	    fw.append(args[1], fixedLine);		         
		      }
	
 	  		  br.close();

		} catch (Exception e) { System.out.println(e); }
			
	}
	
}