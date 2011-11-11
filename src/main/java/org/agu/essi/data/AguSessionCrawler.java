/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.agu.essi.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.agu.essi.Meeting;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.abstracts.Abstract;
import org.agu.essi.abstracts.HtmlAbstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.match.MemoryMatcher;
import org.agu.essi.match.SparqlMatcher;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.AbstractParserException;
import org.agu.essi.util.exception.EntityMatcherRequiredException;
import org.agu.essi.util.exception.SourceNotReadyException;
import org.agu.essi.web.spotlight.SpotlightAnnotator;
import org.agu.essi.web.spotlight.SpotlightAnnotationToRdfXml;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.log4j.Logger;
import org.agu.essi.data.source.AbstractDataSource;

/**
 * Crawls AGU Session data
 * @author Eric Rozell
 */
public class AguSessionCrawler implements AbstractDataSource 
{
	static final Logger log = Logger.getLogger(org.agu.essi.data.AguSessionCrawler.class);  
	
	private EntityMatcher _matcher;
	private boolean _crawled;
	private String _dataDir;
	private Vector<String[]> _meetings;
	private Vector<Abstract> _abstracts;
	
	//AGU URLs
	private static String sessionsTemplate = "http://www.agu.org/cgi-bin/sessions5?meeting={meeting}&sec={sectionId}";
	private static String baseUrl = "http://www.agu.org";
	
	//regex variables
	private static String sessionUrlRegex = "(.{50,60})";
	private static String sessionNameRegex = "(.{0,200})";
	private static String sessionIdRegex = "([A-Z0-9]{4,6})";
	private static String sessionLocationRegex = "(.{0,80})";
	private static String sessionConvenersRegex = "(.{0,400}?)";

	// annotate with DBpedia Spotlight
	private boolean annotate = true;
	private SpotlightAnnotationToRdfXml sWriter = new SpotlightAnnotationToRdfXml ();
	
	// class constructor creates a HashMap of AGU meetings/data directory key/value pairs
	// AGU nomenclature - FM = Fall Meeting, JA = Joint Assembly, SM = Spring Meeting (changed to Joint Assembly in 2008)
	public AguSessionCrawler ( String dir, Vector<String[]> meetings, boolean a ) 
	{
		_dataDir = dir;
		_crawled = false;
		_meetings = meetings;
		_abstracts = new Vector<Abstract>();
		annotate = a;
		if ( annotate ) { 
			
			// configure the annotation writer
		  	sWriter.setOutputDirectory( _dataDir );
		  		
		  	// write the DBpedia Source annotation
		  	sWriter.writeSpotlightProvenance();
		  				
		}
		//crawl();
	}
	
	public AguSessionCrawler ( boolean a )
	{
		_crawled = false;
		_abstracts = new Vector<Abstract>();
		annotate = a;
		if ( annotate ) { 
			
			// configure the annotation writer
		  	sWriter.setOutputDirectory( _dataDir );
		  		
		  	// write the DBpedia Source annotation
		  	sWriter.writeSpotlightProvenance();
		  				
		}
		crawl();
	}

	public void crawl()
	{
		if (_matcher == null)
		{
			_matcher = new MemoryMatcher();
			sWriter.setEntityMatcher( _matcher );
		}
		
		for (int i = 0; i < _meetings.size(); ++i)
		{
			String[] arr = _meetings.get(i);
			String meetingId = arr[0];
			String sectionId = arr[1];
			String loc = sessionsTemplate.replaceAll("\\{meeting\\}", meetingId);
			loc = loc.replaceAll("\\{sectionId\\}", sectionId);
			try 
			{
				URL url = new URL(loc);
				URLConnection conn = url.openConnection();
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String response = "";
				String line = null;
				while ((line = br.readLine()) != null)
				{
					response += Utils.unescapeHtml(line);
				}
				parseSectionResponse(response, meetingId, sectionId);
			} 
			catch (MalformedURLException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
		//crawl abstracts known to be skipped
		/*Vector<String> skipped = Utils.skippedAbstracts();
		for (int i = 0; i < skipped.size(); i++)
		{
			getAbstractResponse(skipped.get(i));
		}*/
		_crawled = true;
	}
	
	private void parseSectionResponse(String content, String meetingId, String sectionId)
	{
		//Get Meeting and Section Details
		Pattern p1 = Pattern.compile("<tr><td valign=top><h2>Session Information</h2><h3>(.+?)</h3></td><td align=right valign=top><h3 align=right>(.+?)</h3></td></tr>");
		Matcher m = p1.matcher(content);
		Meeting meeting = null;
		Section section = null;
		boolean matched = m.find();
		if (matched)
		{			
			meeting = new Meeting(m.group(1), meetingId);
			_matcher.getMeetingId(meeting);
			section = new Section(m.group(2), sectionId, meeting);
			_matcher.getSectionId(section);
		}
		
		//Get Session Links
		Pattern p2 = Pattern.compile("<tr><td align=\"left\" valign=\"top\" bgcolor=\"#D1EBDB\"><B>(\\d+?)</B></td><td align=\"left\" valign=\"top\" bgcolor=\"#D1EBDB\"><B><a href=\"" + sessionUrlRegex 
				+ "\">" + sessionIdRegex + "</a></B></td><td align=\"left\" valign=\"top\" bgcolor=\"#D1EBDB\"><B>" + sessionLocationRegex + "</B></td><td align=\"left\" valign=\"top\" bgcolor=\"#D1EBDB\"><B>" 
				+ sessionNameRegex + "<br( /)?>(((<I>|<i>)?(\\(.+?\\))(</i>)?)?\\s*(<br( /)?>)?)?<i>Presiding:</i>\\s*<em>" + sessionConvenersRegex + "(</em>|<br></span>)");
		m = p2.matcher(content);
		Vector<Session> sessions = new Vector<Session>();
		Vector<String> sessionLinks = new Vector<String>();
		while (m.find())
		{
			String title = m.group(5) + ((m.group(10) != null) ? " " + m.group(10) : "");
			Session s = new Session(title, m.group(3), m.group(4), m.group(14), section);
			sessions.add(s);
			_matcher.getSessionId(s);
			sessionLinks.add(m.group(2));
		}

		for (int i = 0; i < sessionLinks.size(); ++i)
		{
			getSessionResponse(sessionLinks.get(i));
		}
	}
	
	private void getSessionResponse(String link)
	{
		String loc = baseUrl + link;
		try 
		{
			URL url = new URL(loc);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = "";
			String line = null;
			while ((line = br.readLine()) != null)
			{
				response += Utils.unescapeHtml(line);
			}
			parseSessionResponse(response);
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void parseSessionResponse(String content)
	{
		Pattern p = Pattern.compile("<font size=-1>(  |  )<a href=\"(.{20,30}?)\">Abstract</a></font>");
		Matcher m = p.matcher(content);
		Vector<String> abstractLinks = new Vector<String>();
		while(m.find())
		{
			abstractLinks.add(m.group(2));
		}
		
		for (int i = 0; i < abstractLinks.size(); ++i)
		{
			getAbstractResponse(abstractLinks.get(i));
		}
	}
	
	private void getAbstractResponse(String link)
	{
		String loc = baseUrl + link;
		try 
		{
			URL url = new URL(loc);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String response = "";
			String line = null;
			while ((line = br.readLine()) != null)
			{
				response += Utils.unescapeHtml(line);
			}
			try 
			{
				Abstract a = new HtmlAbstract(response);
				_abstracts.add(a);
				if ( annotate ) {
      			  SpotlightAnnotator annotator = new SpotlightAnnotator ( a.getAbstract() );
      			  Vector <org.agu.essi.annotation.Annotation> annotations = annotator.getAnnotations();
      			  sWriter.annotationsToRDF( annotations, annotator, a );
      			} // end if annotate
			} 
			catch (AbstractParserException e) 
			{
				System.err.println("File at " + link + " does not contain AGU abstract HTML.");
				e.printStackTrace();
			}
			catch (EntityMatcherRequiredException e) {
				System.err.println("Entity Matcher Required Exception when writing annotations.");
				e.printStackTrace();
			}
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private void writeToRDFXML( ) throws EntityMatcherRequiredException
	{
		if (_matcher == null) _matcher = new MemoryMatcher();
		FileWrite fw = new FileWrite();
		for (int i = 0; i < _abstracts.size(); ++i)
		{
			Abstract abstr = _abstracts.get(i);
			abstr.setEntityMatcher(_matcher);
			// replace spaces with _ for file name
			String title = abstr.getId();
			String meeting = abstr.getMeeting().getName();
			title = title.replaceAll("\\s+", "_");
			title = title.replaceAll("\\s", "_");
			meeting = meeting.replaceAll("\\s", "_");
			String file = _dataDir + meeting + "_" + title + ".rdf";
			fw.newFile(file, abstr.toString("rdf/xml"));
		}
		fw.newFile(_dataDir + "people.rdf", _matcher.writeNewPeople("rdf/xml"));
		fw.newFile(_dataDir + "organizations.rdf", _matcher.writeNewOrganizations("rdf/xml"));
		fw.newFile(_dataDir + "sessions.rdf", _matcher.writeNewSessions("rdf/xml"));
		fw.newFile(_dataDir + "sections.rdf", _matcher.writeNewSections("rdf/xml"));
		fw.newFile(_dataDir + "meetings.rdf", _matcher.writeNewMeetings("rdf/xml"));
		fw.newFile(_dataDir + "keywords.rdf", _matcher.writeNewKeywords("rdf/xml"));
	}
	
	private void writeToXML ( ) throws EntityMatcherRequiredException 
	{	
		for (int i = 0; i < _abstracts.size(); ++i)
		{
			Abstract abstr = _abstracts.get(i);
			// replace spaces with _ for file name
			String title = abstr.getId();
			if (title.contains("Integrating"))
				System.out.println("here");
			String meeting = abstr.getMeeting().getName();
			title = title.replaceAll("\\s+", "_");
			title = title.replaceAll("\\s", "_");
			meeting = meeting.replaceAll("\\s", "_");
			String file = _dataDir + meeting + "_" + title + ".xml";
			FileWrite fw = new FileWrite();
			fw.newFile(file, abstr.toString("xml"));
		}
	}
	
	public Vector<Abstract> getAbstracts() throws SourceNotReadyException 
	{
		return _abstracts;
	}

	public boolean ready() 
	{
		return _crawled;
	}

	public void setEntityMatcher(EntityMatcher m) 
	{
		_matcher = m;
		sWriter.setEntityMatcher( m );
	}

	public EntityMatcher getEntityMatcher() 
	{
		return _matcher;
	}
	
	public static void main (String[] args)  
	{	
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("outputDirectory", true, "Directory in which to store the retrieved abstracts");
	  	options.addOption("outputFormat", true, "Serialization format for the resulting data");
	  	options.addOption("annotate", false, "Turns on calls to DBpedia Spotlight annotation service (default is to not annotate" +
			"while crawling)");
	  	options.addOption("endpoint", true, "SPARQL endpoint with existing data for matching");
	  	options.addOption("meetings", true, "List of semi-colon separated meetings, with meeting ID and section ID separated by comma (e.g., fm10,IN;fm09,IN");
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try 
	  	{
	  		cmd = parser.parse( options, args);
	  	} 
	  	catch ( Exception pe ) { 
	  		log.error("Error parsing command line options: " + pe.toString()); 
	  		pe.printStackTrace();
	  	}
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String format = null;
	  	String endpoint = null;
	  	Vector<String[]> meetings = null;
	  	
	  	// output directory
	    if ( !cmd.hasOption("outputDirectory") ) 
	    {
	    	error = true;
	  		log.error("--outputDirectory Not Set. Directory in which to store the retrieved abstracts.");
	  	}
	    
	    // output format
	  	if ( cmd.hasOption("outputFormat")) 
	  	{ 
	  		format = cmd.getOptionValue("outputFormat"); 
	  	} 

	    // endpoint location
	  	if ( cmd.hasOption("endpoint")) 
	  	{ 
	  		endpoint = cmd.getOptionValue("endpoint"); 
	  	}
	  	
	  	// meeting IDs
	  	if (cmd.hasOption("meetings"))
	  	{
	  		try 
	  		{
	  			meetings = Utils.getAguMeetingsFromInput(cmd.getOptionValue("meetings"));
	  		}
	  		catch (Exception e)
	  		{
	  			error = true;
	  			log.error("--meetings argument (" + cmd.getOptionValue("meetings") + ") is invalid, see example: fm10,IN;fm09,IN;...");
	  		}
	  	}
	  	else 
	  	{
	  		meetings = Utils.getAguMeetings();
	  	}
	  	
	  	// section strings
	  		  	
	  	// annotate or not
	  	boolean annotate = false;
	  	if ( cmd.hasOption("annotate")) { annotate = true; }
	  	
	  	if (!error)
	    {	
	    	
	    	// query AGU
	    	AguSessionCrawler crawler = new AguSessionCrawler ( cmd.getOptionValue("outputDirectory"), meetings, annotate );
		  	
	    	if (endpoint != null)
	    	{
	    		SparqlMatcher m = new SparqlMatcher(endpoint);
	    		crawler.setEntityMatcher(m);
	    	}
	    	
	    	crawler.crawl();
	    	
	    	//crawler.setEntityMatcher(new SparqlMatcher("http://aquarius.tw.rpi.edu:2025/sparql"));
	    	// output abstracts and annotations
    		try {
    			if (format != null && format.equals("rdf/xml")) 
    			{ 
	    			crawler.writeToRDFXML();
    			} 
    			else 
    			{ 
    				crawler.writeToXML(); 
    			}
    		}
    		catch (EntityMatcherRequiredException e) 
    		{
    			log.error("EntityMatcher not set. Required for RDF output formats.");
    			e.printStackTrace();
    		} 
	    }
	}
}
