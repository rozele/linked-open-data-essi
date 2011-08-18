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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.BufferedWriter;

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
import org.agu.essi.data.DataSource;
import org.agu.essi.util.EntityIdentifier;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.Utils;

public class Crawler implements DataSource {
	
	// AGU Variables - location of, and access to, AGU Abstract Database
	private String aguBaseURL = "http://www.agu.org/";
	private String aguApplicationURL = "cgi-bin/SFgate/SFgate?application=";
	private String aguURLOptions = "listenv=table&multiple=1&range=0&fieldsel_1_name=&fieldsel_2_tie=and&" +
	  "fieldsel_2_name=sc&fieldsel_2_content=Informatics&maxhits=10000&desc=+&requestm=POST";
	private HashMap <String, String> aguDatabases;
	
	private String dataDir;
	private ParserDelegator parserDelegator = new ParserDelegator();
	private Vector<Abstract> _abstracts;
	boolean crawled;
	
	// class constructor creates a HashMap of AGU meetings/data directory key/value pairs
	// AGU nomenclature - FM = Fall Meeting, JA = Joint Assembly, SM = Spring Meeting (changed to Joint Assembly in 2008)
	public Crawler ( String dir ) {
	  dataDir = dir;
	  crawled = false;
	  _abstracts = new Vector<Abstract>();
	  aguDatabases = Utils.getAguDatabases();
	}
	
	public Crawler ()
	{
		_abstracts = new Vector<Abstract>();
		 aguDatabases = Utils.getAguDatabases();
		 crawled = false;
	}
	
	private void writeToRDFXML( )
	{
		FileWrite fw = new FileWrite();
		for (int i = 0; i < _abstracts.size(); ++i)
		{
			Abstract abstr = _abstracts.get(i);
			// replace spaces with _ for file name
			String title = abstr.getId();
			String meeting = abstr.getMeeting().getName();
			title = title.replaceAll("\\s+", "_");
			title = title.replaceAll("\\s", "_");
			meeting = meeting.replaceAll("\\s", "_");
			String file = dataDir + meeting + "_" + title + ".rdf";
			fw.newFile(file, abstr.toString("rdf/xml"));
		}
		fw.newFile(dataDir + "people.rdf", EntityIdentifier.writePeople("rdf/xml"));
		fw.newFile(dataDir + "organizations.rdf", EntityIdentifier.writeOrganizations("rdf/xml"));
		fw.newFile(dataDir + "sessions.rdf", EntityIdentifier.writeSessions("rdf/xml"));
		fw.newFile(dataDir + "sections.rdf", EntityIdentifier.writeSections("rdf/xml"));
		fw.newFile(dataDir + "meetings.rdf", EntityIdentifier.writeMeetings("rdf/xml"));
		fw.newFile(dataDir + "keywords.rdf", EntityIdentifier.writeKeywords("rdf/xml"));
	}
	
	private void writeToXML ( ) 
	{	
		for (int i = 0; i < _abstracts.size(); ++i)
		{
			Abstract abstr = _abstracts.get(i);
			// replace spaces with _ for file name
			String title = abstr.getId();
			String meeting = abstr.getMeeting().getName();
			title = title.replaceAll("\\s+", "_");
			title = title.replaceAll("\\s", "_");
			meeting = meeting.replaceAll("\\s", "_");
			String file = dataDir + meeting + "_" + title + ".xml";
			FileWrite fw = new FileWrite();
			fw.newFile(file, abstr.toString("xml"));
			//String data = abstr.toString("xml");
			//try {
			//  Writer out = new BufferedWriter(new OutputStreamWriter(
			//    new FileOutputStream( file ), "UTF-8"));
			//  out.write( data );			
			//  out.close();
			//} catch ( Exception e ) { System.out.println(e); }
			
		}
	}

	// HTML parser - reads an AGU HTML page and extracts links to abstracts
    ParserCallback parserCallback = new ParserCallback() 
    {
        public void handleStartTag(Tag tag, MutableAttributeSet attribute, int pos) 
        {
        	if (tag == Tag.A) 
        	{
        		String address = (String) attribute.getAttribute(Attribute.HREF);
        		String line;
        		try 
        		{
        			URL u = new URL( aguBaseURL + address ); 
        			HttpURLConnection http = (HttpURLConnection) u.openConnection();
        			StringBuilder builder = new StringBuilder();
        			BufferedReader reader = new BufferedReader(new InputStreamReader(http.getInputStream(),"UTF-8"));
        			while((line = reader.readLine()) != null) 
        			{ 
        				builder.append(line + " "); 
        			}
        			_abstracts.add(new Abstract(builder.toString()));
        		} 
        		catch ( Exception e ) 
        		{ 
        			System.err.println("File at " + address + " does not contain AGU abstract HTML.");
        		}
        	}
        }

        public void handleEndTag(Tag t, final int pos) {}

        public void handleSimpleTag(Tag t, MutableAttributeSet a, final int pos) {}
        
        public void handleText(final char[] data, final int pos) {}
        
        public void handleComment(final char[] data, final int pos) {}

        public void handleError(final java.lang.String errMsg, final int pos) {}
    };
    
    // call the AGU interface and extract abstracts from all available meetings
	public void crawl ( ) 
	{    
		String url = null;
		Set <Map.Entry<String, String>> databases = aguDatabases.entrySet();
		for (Map.Entry<String, String> me : databases) 
		{
			try 
			{
				url = aguBaseURL + aguApplicationURL + me.getKey() + "&database=" + me.getValue() + "&" + aguURLOptions;
				URL u = new URL( url ); 
				ReadableByteChannel rbc = Channels.newChannel( u.openStream() );
				String localFilename = dataDir + me.getKey() + ".html";
				FileOutputStream fos = new FileOutputStream( localFilename );
				fos.getChannel().transferFrom(rbc, 0, 1 << 24);	      
				parserDelegator.parse(new FileReader( localFilename ), parserCallback, false);		  
			} 
			catch (Exception e) 
			{ 
				e.printStackTrace(); 
			} 
		}
		crawled = true;
	}
	
	public static void main (String[] args)  {
		
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("outputDirectory", true, "Directory in which to store the retrieved abstracts.");
	  	options.addOption("outputFormat",true,"Serialization format for the resulting data");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try {
	      cmd = parser.parse( options, args);
	  	} catch ( Exception pe ) { 
	  	  System.err.println("Error parsing command line options: " + pe.toString()); 
	  	}
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String errorMessage = null;
	  	String format = null;
	    if ( !cmd.hasOption("outputDirectory") ) {
	    	error = true;
	  		errorMessage = "--outputDirectory Not Set. Directory in which to store the retrieved abstracts.";
	  	}
	  	if ( cmd.hasOption("outputFormat")) { format = cmd.getOptionValue("outputFormat"); } 
	    
	    if ( error ) { System.out.println(errorMessage); } else {	

	      // query AGU
		  Crawler crawler = new Crawler ( cmd.getOptionValue("outputDirectory"));
		  crawler.crawl();
		  if (format != null && format.equals("rdf/xml")) { crawler.writeToRDFXML(); } else { crawler.writeToXML(); }
	    
	    }
	    
	}

	public Vector<Abstract> getAbstracts() {
		if (!crawled) { this.crawl(); }
		return _abstracts;
	}

	public boolean hasUniqueIdentifiers() {
		return false;
	}
	
}