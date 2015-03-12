package essi.lod.util;

import essi.lod.data.agu.Pre2000Data;
import essi.lod.data.agu.Pre2000Session;
import essi.lod.entity.agu.Author;
import java.util.Vector;
import essi.lod.util.FileWrite;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class OutputTurtle
{
	
	private String newLine = "\r\n";
	private Vector <String> sessionList = new Vector <String> ();
	
	// in 1997, 1998, and 1999 the sessions are not written out properly 
	// and are difficult to parse. For those years this method gets
	// called to generate the sessions from the abstract uris
	public void writeSessionData ( String sessionUri, String year, String outputFileName, FileWrite fw ) {
		
		if ( !sessionList.contains(sessionUri) ) {
			
		  sessionList.add( sessionUri );
		  
	      String[] parts = sessionUri.split(" ");
	      sessionUri = parts[0].trim();
		
	      parts = sessionUri.split("/");
		  String title = parts[parts.length-1];
		  title = title.substring(0,title.length()-1); // remove the > on the uri
				
		  if ( !title.equals("default") ) {
			  
		     String line = sessionUri + " <http://data.semanticweb.org/ns/swc/ontology#isSubEventOf> <http://abstractsearch.agu.org/meetings/" + 
		       year.trim() + "/FM> . ";
		     fw.append( outputFileName, line);
		     fw.append( outputFileName, newLine );

		     line = sessionUri + " <http://swrc.ontoware.org/ontology#eventTitle> \"" + title + "\"@en ."; 
		     fw.append( outputFileName, line);
		     fw.append( outputFileName, newLine );

		     String section = parts[6];
		     
		     if ( section.length() >= 2 ) {
			      section = section.substring(0,2);
			      String test = section.substring(1);
			      // if second character is a number then this is a one
			      // character section name. if second character is a 
			      // letter then it is also part of the section name.
			      // test both cases
			      if (test.matches(".*\\d.*")) { 
			        section = section.substring(0,1);
			      } else {
			        // not a number and thus part of the section
			      }
			 }
		     
		     line = sessionUri + " <http://abstractsearch.agu.org/ontology#section> <http://abstractsearch.agu.org/sections/" + section + "> . ";
		     //System.out.println(line);
		     fw.append( outputFileName, line);
		     fw.append( outputFileName, newLine );

             line = sessionUri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://data.semanticweb.org/ns/swc/ontology#SessionEvent> . ";
             fw.append( outputFileName, line);
		     fw.append( outputFileName, newLine );
		
		     line = sessionUri + " <http://purl.org/dc/terms/identifier> \"" + title + "\" .";
		     fw.append( outputFileName, line);
		     fw.append( outputFileName, newLine );
	
		  }
		}
	}
	
	public void write ( String outputFileName, String year, Vector <Pre2000Session> sessions,
			Vector <Pre2000Data> abstracts, boolean sessionsFromAbstracts)
	{
		
		String section = "default";
		
		int counter = 0;
		FileWrite fw = new FileWrite();
		
		String uri = "<http://abstractsearch.agu.org/meetings/" + year + "/FM>"; 
		fw.append( outputFileName, uri);
		fw.append( outputFileName, newLine );
		
		String line = "<http://abstractsearch.agu.org/ontology#meetingCode> \"FM\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
		fw.append( outputFileName, line);
		fw.append( outputFileName, newLine );
		
		line = uri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://swrc.ontoware.org/ontology#Meeting> . ";
		fw.append( outputFileName, line);
		fw.append( outputFileName, newLine );
		
		line = uri + " <http://swrc.ontoware.org/ontology#eventTitle> \"Fall Meeting\"@en . ";
		fw.append( outputFileName, line);
		fw.append( outputFileName, newLine );
		
		line = uri + " <http://swrc.ontoware.org/ontology#year> \"" + year + "\"^^<http://www.w3.org/2001/XMLSchema#gYear> . ";
		fw.append( outputFileName, line);
		fw.append( outputFileName, newLine );
		
		// sessions
		for ( int i=0; i<sessions.size(); i++ ) {

			Pre2000Session s = sessions.get(i);

			// determine the section from the session ID
			if ( s.getSessionName() != null ) {
			  if ( !s.getSessionName().equals("") ) {
			
				// Sections can be one or two characters long
				// Here we test both possibilities
				if ( s.getSessionID().length() >=2 ) { 
				  section = s.getSessionID().substring(0,2).trim();
				  String test = section.substring(1);
				  if (test.matches(".*\\d.*")) { 
					 section = s.getSessionID().substring(0,1);
				  } else {
						// not a number and thus part of the section
				  }
				} else { section = s.getSessionID().trim(); }
					
				String prefix = "<http://abstractsearch.agu.org/meetings/" + year + "/FM/";

				String sessionUri = prefix + s.getSessionID() + ">";
			
				line = sessionUri + " <http://swrc.ontoware.org/ontology#eventTitle> " + "\"" + s.getSessionName() + "\"@en . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://tw.rpi.edu/schema/hasAgentWithRole> " + 
						prefix + s.getSessionID() + "/convener1> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = prefix + s.getSessionID() + "/convener1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
					+ "<http://abstractsearch.agu.org/ontology#ConvenerRole> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = prefix + s.getSessionID() + "/convener1> <http://tw.rpi.edu/schema/index> \"1\"^^"
					+ "<http://www.w3.org/2001/XMLSchema#positiveInteger> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://abstractsearch.agu.org/ontology#section> <http://abstractsearch.agu.org/sections/" + section + "> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> " +
				   "<http://data.semanticweb.org/ns/swc/ontology#SessionEvent> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://purl.org/dc/terms/identifier> \"" + s.getSessionID() + "\" . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://data.semanticweb.org/ns/swc/ontology#isSubEventOf> " + uri + " . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
			  }
			} // end if
		}
		
		// abstracts
		for ( int i=0; i<abstracts.size(); i++ )
		{
			
			Pre2000Data d = abstracts.get(i);
			
			// see if the section if available
			//section = d.getSection();
			section = "default";
			String session = "default";
			//
			// section is too unpredictable given the quality of the data
			// let's always try to get the section from the abstract ID
			// parsing the abstract and the title separately has led
			// to abstracts being assigned to the wrong section
			
			// if section not available then try to 
			// determine the section from the abstract ID
			if ( d.getAbstractNumber() != null ) {	
				
				// replace spaces in title with underscore
				if ( !d.getTitle().equals("") ) { d.getTitle().replace(" ", "_"); }
				
				// if no abstract number then use title
				if ( d.getAbstractNumber().equals("") ) { d.setAbstractNumber(d.getTitle()); }
				
				// check the abstract number for POSTER and UNKNOWN
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher( d.getAbstractNumber() );
				if (matcher.find()) {
					String[] split = d.getAbstractNumber().split("\\s");
					d.setAbstractNumber( split[0] );
				}
				
				if ( d.getAbstractNumber().length() >= 2 ) {
			      section = d.getAbstractNumber().substring(0,2);
 			      String test = section.substring(1);
 			      // if second character is a number then this is a one
 			      // character section name. if second character is a 
 			      // letter then it is also part of the section name.
 			      // test both cases
			      if (test.matches(".*\\d.*")) { 
			        section = d.getAbstractNumber().substring(0,1);
			      } else {
			        // not a number and thus part of the section
			      }
				}
				
			    // there is no linkage between the abstracts and the sessions
			    // we can try to guess it when we have the abstract number
			    session = d.getAbstractNumber();
			    if ( session != null ) {
			  	  if ( !session.equals("") ) {
					String[] parts = session.split("-");
					session = parts[0];
				  } else { session = "default"; }				
			    } else { session = "default"; }
			
			} else { d.setAbstractNumber("AbstractID" + String.valueOf(counter)); counter++; }
			
			String sessionUri = "<http://abstractsearch.agu.org/meetings/" + year + "/FM/" + session + "> . ";
			String abstractUri = "<http://abstractsearch.agu.org/meetings/" + year + "/FM/" + d.getAbstractNumber();
		
			// special session creation method for some years
			if ( sessionsFromAbstracts ) { writeSessionData( sessionUri, year, outputFileName, fw ); }
			
			if ( abstractUri.contains("INVITED") ) { abstractUri = abstractUri.replace(" INVITED", ""); }
			
			// if abstract is null and title is null then ignore
			if ( ((d.getTitle() != null) && (d.getAbstractText() != null)) && ((d.getTitle() != "") && (d.getAbstractText() != "")) ) {
				
			  // if uri contains spaces then ignore it
			  if ( !abstractUri.matches("\\s+") ) {
			   
			   line = abstractUri + "> <http://data.semanticweb.org/ns/swc/ontology#relatedToEvent> " + sessionUri;
			   fw.append( outputFileName, line);
			   fw.append( outputFileName, newLine );
			
			   line = abstractUri + "> <http://purl.org/dc/terms/identifier> \"" + d.getAbstractNumber() + "\" . ";
			   fw.append( outputFileName, line);
			   fw.append( outputFileName, newLine );
				 
			   Vector <Author> authors = d.getAuthors();
			   for ( int j=0; j<authors.size(); j++) {

				 int authorIndex = j+1;
				 line = abstractUri + "> <http://tw.rpi.edu/schema/hasAgentWithRole> " + 
					abstractUri + "/author" + String.valueOf(authorIndex) + "> . ";
				 fw.append( outputFileName, line);
				 fw.append( outputFileName, newLine );
				
				 String authorUri = abstractUri + "/author" + String.valueOf(authorIndex) + "> ";
				
				 line = authorUri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://tw.rpi.edu/schema/Author> . ";
				 fw.append( outputFileName, line);
				 fw.append( outputFileName, newLine );
				
				 line = authorUri + " <http://abstractsearch.agu.org/ontology#isCorrespondingAuthor> \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> . ";
				 fw.append( outputFileName, line);
				 fw.append( outputFileName, newLine );
				
				 line = authorUri + " <http://tw.rpi.edu/schema/index> \"" + authorIndex + "\"^^<http://www.w3.org/2001/XMLSchema#positiveInteger> . ";
				 fw.append( outputFileName, line);
				 fw.append( outputFileName, newLine );
				
			   }
			
			   line = abstractUri + "> <http://purl.org/dc/terms/title> \"" + d.getTitle() + "\"@en .";
			   fw.append( outputFileName, line);
			   fw.append( outputFileName, newLine );
			
			   // keywords
			   Vector <String> keywords = d.getKeywords();
			   for ( int j=0; j<keywords.size(); j++ )
			   {
			      // is the keyword all numbers?
			      String regex = "[0-9]+";	
			      if (keywords.get(j).matches(regex)) { 
			        line = abstractUri + "> <http://data.semanticweb.org/ns/swc/ontology#hasTopic> " + 
			          "<http://abstractsearch.agu.org/keywords/" + keywords.get(j) + "> .";
			        fw.append( outputFileName, line);
			        fw.append( outputFileName, newLine );
			      }
			   }
			
			   // Abstract Text
			   line = abstractUri + "> <http://swrc.ontoware.org/ontology#abstract> \"" + 
			     d.getAbstractText() + "\"@en .";
			   fw.append( outputFileName, line);
			   fw.append( outputFileName, newLine );
			
			   // Abstract Text for Quick Search (text search feature)
			   line = abstractUri + "> <http://abstractsearch.agu.org/ontology#raw> \"" + d.getAbstractText() + "\"@en .";
			   fw.append( outputFileName, line);
			   fw.append( outputFileName, newLine );
			   
			   line = abstractUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://abstracts.agu.org/ontology#Abstract> .";
			   fw.append( outputFileName, line);
			   fw.append( outputFileName, newLine );
			
			} // end if abstract contains spaces
		  } // end if title and abstract are both null
			
		} // end for abstracts
		
	}
	
}