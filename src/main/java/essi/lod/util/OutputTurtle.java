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
	
	public void write ( String outputFileName, String year, Vector <Pre2000Session> sessions,
			Vector <Pre2000Data> abstracts)
	{
		
		String section = "default";
		
		int counter = 0;
		FileWrite fw = new FileWrite();
		String newLine = "\r\n";
		
		String uri = "<http://abstracts.agu.org/meetings/" + year + "/FM>"; 
		fw.append( outputFileName, uri);
		fw.append( outputFileName, newLine );
		
		String line = "<http://abstracts.agu.org/ontology#meetingCode> \"FM\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
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
					
				String prefix = "<http://abstracts.agu.org/meetings/" + year + "/FM/sections/" + section + "/sessions/";

				String sessionUri = prefix + s.getSessionID() + ">";
			
				line = sessionUri + " <http://swrc.ontoware.org/ontology#eventTitle> " + "\"" + s.getSessionName() + "\"@en . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://tw.rpi.edu/schema/hasAgentWithRole> " + 
						prefix + s.getSessionID() + "/conveners/1> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = prefix + s.getSessionID() + "/conveners/1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> "
					+ "<http://abstracts.agu.org/ontology#ConvenerRole> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = prefix + s.getSessionID() + "/conveners/1> <http://tw.rpi.edu/schema/index> \"1\"^^"
					+ "<http://www.w3.org/2001/XMLSchema#positiveInteger> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
			
				line = sessionUri + " <http://abstracts.agu.org/ontology#section> <http://abstracts.agu.org/sections/" + section + "> . ";
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
			section = d.getSection();
			
			// if section not available then try to 
			// determine the section from the abstract ID
			if ( section != null ) {
			 if ( section.equals("") ) {
				
			  if ( !d.getAbstractNumber().equals("") ) {
				  
				// check the abstract number for POSTER and UNKNOWN
				Pattern pattern = Pattern.compile("\\s");
				Matcher matcher = pattern.matcher( d.getAbstractNumber() );
				if (matcher.find()) {
					String[] split = d.getAbstractNumber().split("\\s");
					d.setAbstractNumber( split[0] );
				}
				  
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
			  } else { section = "default"; }
			 
			 } else { } // section is not null and not "" so use it
			 
			// the case when we don't have an abstract number or a section available
			} else { section = "default"; }
			
			// there is no linkage between the abstracts and the sessions
			// we can try to guess it when we have the abstract number
			String session = d.getAbstractNumber();
			if ( session != null ) {
				if ( !session.equals("") ) {
					String[] parts = session.split("-");
					session = parts[0];
				} else { session = "default"; }				
			} else { session = "default"; }
				
			// if no abstract number then use title 
			if ( d.getAbstractNumber() != null ) {
			  if ( d.getAbstractNumber().equals("") ) {	
				if ( !d.getTitle().equals("") ) {
					d.setAbstractNumber(d.getTitle().replace(" ", "_"));
				} else { d.setAbstractNumber("AbstractID" + String.valueOf(counter)); counter++; }
			  }
			} else { d.setAbstractNumber("AbstractID" + String.valueOf(counter)); counter++; }
			
			String sessionUri = "<http://abstracts.agu.org/meetings/" + year + "/FM/sections/" + section + "/sessions/" + session + "> . ";
			String abstractUri = "<http://abstracts.agu.org/meetings/" + year + "/FM/sections/" + section + "/sessions/" + session + 
					"/abstracts/" + d.getAbstractNumber();
		
			line = abstractUri + "> <http://data.semanticweb.org/ns/swc/ontology#relatedToEvent> " + sessionUri;
			fw.append( outputFileName, line);
			fw.append( outputFileName, newLine );
			
			Vector <Author> authors = d.getAuthors();
			for ( int j=0; j<authors.size(); j++) {

				int authorIndex = j+1;
				line = abstractUri + "> <http://tw.rpi.edu/schema/hasAgentWithRole> " + 
					abstractUri + "/authors/" + String.valueOf(authorIndex) + "> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
				
				String authorUri = abstractUri + "/authors/" + String.valueOf(authorIndex) + "> ";
				
				line = authorUri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://tw.rpi.edu/schema/Author> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
				
				line = authorUri + " <http://abstracts.agu.org/ontology#isCorrespondingAuthor> \"true\"^^<http://www.w3.org/2001/XMLSchema#boolean> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
				
				line = authorUri + " <http://tw.rpi.edu/schema/index> \"" + authorIndex + "\"^^<http://www.w3.org/2001/XMLSchema#positiveInteger> . ";
				fw.append( outputFileName, line);
				fw.append( outputFileName, newLine );
				
				line = authorUri + " <http://purl.org/dc/terms/identifier> \"" + d.getAbstractNumber() + "\" . ";
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
			       "<http://abstracts.agu.org/keywords/" + keywords.get(j) + "> .";
			    fw.append( outputFileName, line);
			    fw.append( outputFileName, newLine );
			  }
			}
			
			line = abstractUri + "> <http://swrc.ontoware.org/ontology#abstract> \"" + 
			   d.getAbstractText() + "\"@en .";
			fw.append( outputFileName, line);
			fw.append( outputFileName, newLine );
			
			line = abstractUri + "> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://abstracts.agu.org/ontology#Abstract> .";
			fw.append( outputFileName, line);
			fw.append( outputFileName, newLine );
			
		} // end for abstracts
		
	}
	
}