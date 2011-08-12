package org.agu.essi.web;

import java.util.HashMap;
import java.util.Set;
import java.util.Map;
import java.util.Vector;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.swing.text.html.HTMLEditorKit.ParserCallback;
import javax.swing.text.html.parser.ParserDelegator;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML.Attribute;
import javax.swing.text.html.HTML.Tag;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import org.agu.essi.Abstract;
import org.agu.essi.util.FileWrite;

public class Crawler {
	
	// AGU Variables - location of, and access to, AGU Abstract Database
	private String aguBaseURL = "http://www.agu.org/";
	private String aguApplicationURL = "cgi-bin/SFgate/SFgate?application=";
	private String aguURLOptions = "listenv=table&multiple=1&range=0&fieldsel_1_name=&fieldsel_2_tie=and&" +
	  "fieldsel_2_name=sc&fieldsel_2_content=Informatics&maxhits=10000&desc=+&requestm=POST";
	private HashMap <String, String> aguDatabases;
	
	private String dataDir;
	private String dataFormat;
	private ParserDelegator parserDelegator = new ParserDelegator();
	private Abstract abstr;
	
	// class constructor creates a HashMap of AGU meetings/data directory key/value pairs
	// AGU nomenclature - FM = Fall Meeting, JA = Joint Assembly, SM = Spring Meeting (changed to Joint Assembly in 2008)
	public Crawler ( String dir, String format ) {
	  dataDir = dir;
	  dataFormat = format;
	  aguDatabases = new HashMap <String, String> ();
	  aguDatabases.put( "fm10", "/data/epubs/wais/indexes/fm10/fm10");	
	  aguDatabases.put( "ja10", "/data/epubs/wais/indexes/ja10/ja10");	
	  aguDatabases.put( "fm09", "/data/epubs/wais/indexes/fm09/fm09");	
	  aguDatabases.put( "ja09", "/data/epubs/wais/indexes/ja09/ja09");	
	  aguDatabases.put( "fm08", "/data/epubs/wais/indexes/fm08/fm08");	
	  aguDatabases.put( "ja08", "/data/epubs/wais/indexes/ja08/ja08");	
	  aguDatabases.put( "fm07", "/data/epubs/wais/indexes/fm07/fm07");	
	  aguDatabases.put( "sm07", "/data/epubs/wais/indexes/sm07/sm07");	
	  aguDatabases.put( "fm06", "/data/epubs/wais/indexes/fm06/fm06");	
	  aguDatabases.put( "sm06", "/data/epubs/wais/indexes/sm06/sm06");	
	  aguDatabases.put( "fm05", "/data/epubs/wais/indexes/fm05/fm05");	
	  aguDatabases.put( "sm05", "/data/epubs/wais/indexes/sm05/sm05");	
	}
	
	private void writeToXML ( String text, String dir ) {
		
		// parse the AGU web page and extract the abstract information
		abstr = new Abstract(text);
	
		// replace spaces with _ for file name
		String title = abstr.getTitle();
		String meeting = abstr.getMeeting().getName();
		title = title.replaceAll("\\s+", "_");
		title = title.replaceAll("\\s", "_");
		meeting = meeting.replaceAll("\\s", "_");
		String fileExt = null;
		if (dataFormat == "xml") fileExt = ".xml";
		else if (dataFormat == "rdf/xml") fileExt = ".rdf";
		String file = dataDir + meeting + "_" + title + fileExt;
		FileWrite fw = new FileWrite();
		fw.append(file, abstr.toString(dataFormat));
	}

	// HTML parser - reads an AGU HTML page and extracts links to abstracts
    ParserCallback parserCallback = new ParserCallback() {
      
        public void handleText(final char[] data, final int pos) {}

        public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) {
          if (tag == Tag.A) {
            String address = (String) attribute.getAttribute(Attribute.HREF);
            String line;
            if (address.equals("/") || address.equals("/meetings/waismulti_smfm.html")) return;
            try {
            	URL u = new URL( aguBaseURL + address ); 
    			HttpURLConnection http = (HttpURLConnection) u.openConnection();
    			StringBuilder builder = new StringBuilder();
    			BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream()));
    			while((line = reader.readLine()) != null) { builder.append(line + " "); }
    			writeToXML( builder.toString(), dataDir );
            } catch ( Exception e ) { e.printStackTrace(); }
          }
        }

        public void handleEndTag(Tag t, final int pos) {}

        public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) {}

        public void handleComment(final char[] data, final int pos) {}

        public void handleError(final java.lang.String errMsg, final int pos) {}
        
    };
    
    // call the AGU interface and extract abstracts from all available meetings
	public void queryAll ( ) {
	    
	  String url = null;
	  Set <Map.Entry<String, String>> databases = aguDatabases.entrySet();
	  for (Map.Entry<String, String> me : databases) {
		try {
	      url = aguBaseURL + aguApplicationURL + me.getKey() + "&database=" + me.getValue() + "&" + aguURLOptions;
	      URL u = new URL( url ); 
	      ReadableByteChannel rbc = Channels.newChannel( u.openStream() );
	      String localFilename = dataDir + me.getKey() + ".html";
	      FileOutputStream fos = new FileOutputStream( localFilename );
	      fos.getChannel().transferFrom(rbc, 0, 1 << 24);	      
	      parserDelegator.parse(new FileReader( localFilename ), parserCallback, false);		  
		} catch (Exception e) { e.printStackTrace(); } 
	  }
		
	}
	
	public static void main (String[] args) {
		
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("outputDirectory", true, "Directory in which to store the retrieved abstracts.");
	  	options.addOption("outputFormat",true,"Serialization format for the resulting data");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try {
	  	  cmd = parser.parse( options, args);
	  	} catch ( Exception pe ) { System.out.println("Error parsing command line options: " + pe.toString()); }
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String errorMessage = null;
	  	String format = null;
	    if ( !cmd.hasOption("outputDirectory") ) {
	    	error = true;
	  		errorMessage = "--outputDirectory Not Set. Directory in which to store the retrieved abstracts.";
	  	}
	  	if ( !cmd.hasOption("outputFormat")) {
	  		format = "xml";
	  	} else {
	  		format = cmd.getOptionValue("outputFormat");
	  	}
	    
	    if ( error ) { System.out.println(errorMessage); } else {
	    	
	      // query AGU
		  Crawler crawler = new Crawler ( cmd.getOptionValue("outputDirectory"), format);
		  crawler.queryAll();	  
	    }
	}
}