package essi.lod.util;

import essi.lod.data.agu.Pre2000Data;
import essi.lod.data.agu.Pre2000Session;
import essi.lod.entity.agu.Author;
import essi.lod.entity.agu.Person;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import essi.lod.util.FileWrite;

public class OutputTurtlePeople
{
	
	public void write ( String outputFileName, String year, Vector <Pre2000Data> abstracts, Vector <Pre2000Session> sessions )
	{
		
		int counter = 0;
		String line;
		String personID;
		String section;
		String newLine = "\r\n";
		FileWrite fw = new FileWrite();
		
		// loop over all the sessions
		for ( int i=0; i<sessions.size(); i ++ ) {
			
			Pre2000Session s = sessions.get(i);
			String convenor = s.getConvenors();
			if ( s.getSessionName() != null ) {
			   if ( !s.getSessionName().equals("") ) {
				
				  // create the person ID 
				  personID = "FM" + year.substring(2).trim() + "_" + s.getSessionID().trim() + "_convener_" +  
					convenor.replace(" ", "_");  
				  String uri = "<http://abstracts.agu.org/people/" + personID + ">";
			  
				  line = "";
				
				  // determine the section from the session ID				
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
				
				  String sessionStmt = "<http://abstracts.agu.org/meetings/" + year + "/FM/sections/" + section + 
						  "/sessions/" + s.getSessionID() + "/conveners/1> . ";
				
				  line = uri + " <http://tw.rpi.edu/schema/hasRole> " + sessionStmt;
				  fw.append( outputFileName, line );
				  fw.append( outputFileName, newLine );

				  line = uri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . ";
				  fw.append( outputFileName, line );
				  fw.append( outputFileName, newLine );
				  
				  line = uri + " <http://xmlns.com/foaf/0.1/name> \"" + convenor + "\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
				  fw.append( outputFileName, line );
				  fw.append( outputFileName, newLine );

			   }
			}
		}
		
		// loop over all the abstracts
		for ( int i=0; i<abstracts.size(); i++ ) {
			
		   Pre2000Data d = abstracts.get(i);
		   Vector <Author> authors = d.getAuthors();
		   
		   if ( authors.size() > 0 ) {
			   
			   // loop over all the authors
			   for ( int authorIndex=0; authorIndex<authors.size(); authorIndex++ ) {
				   Author author = authors.get(authorIndex);
				   Person person = author.getPerson();
		   
				   line = "";
		   
				   // check the abstract number for POSTER and UNKNOWN
				   Pattern pattern = Pattern.compile("\\s");
				   Matcher matcher = pattern.matcher( d.getAbstractNumber() );
				   if (matcher.find()) {
					   String[] split = d.getAbstractNumber().split("\\s");
					   d.setAbstractNumber( split[0] );
				   }
		    
				   // create the person ID 
				   if ( d.getAbstractNumber().equals("") ) { 
					   personID = "FM" + year.substring(2).trim() + "_AbstractID" + String.valueOf(counter) + "_author_" + 
							   person.getName().replace(" ", "_"); 
					   counter++; 
				   } else { 
					   personID = "FM" + year.substring(2).trim() + "_" + d.getAbstractNumber().trim() + "_author_" + 
							   person.getName().replace(" ", "_"); 
				   }	 
		   
				   // if we have the email then use that in the URI otherwise use the person name
				   String uri = "<http://abstracts.agu.org/people/";
				   if ( person.getEmail().equals("") ) { 
					   uri += personID + ">";
				   } else { uri += person.getEmail() + ">"; }
		  
				   // see if the section if available
				   section = d.getSection();
					
				   // if section not available then try to 
				   // determine the section from the abstract ID
				   if ( section != null ) {
					   if ( section.equals("") ) {
						
						   if ( !d.getAbstractNumber().equals("") ) {
						  
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
			
				   // create the abstract ID
				   // if no abstract number then use title 
				   if ( d.getAbstractNumber() != null ) {
					   if ( d.getAbstractNumber().equals("") ) {	
						   if ( !d.getTitle().equals("") ) {
							   d.setAbstractNumber(d.getTitle().replace(" ", "_"));
						   } else { d.setAbstractNumber("AbstractID" + String.valueOf(counter)); counter++; }
					   }
				   } else { d.setAbstractNumber("AbstractID" + String.valueOf(counter)); counter++; }
						
				   String abstractUri = "<http://abstracts.agu.org/meetings/" + year + "/FM/sections/" + section + "/sessions/" + 
						   session + "/abstracts/" + d.getAbstractNumber();
			
				   line = uri + " <http://tw.rpi.edu/schema/hasRole> " + abstractUri + "/authors/" + 
						   String.valueOf(authorIndex+1).trim() + "> . ";

				   fw.append( outputFileName, line );
				   fw.append( outputFileName, newLine );
		   
				   line = uri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . ";
				   fw.append( outputFileName, line );
				   fw.append( outputFileName, newLine );
		   
				   line = uri + " <http://xmlns.com/foaf/0.1/name> \"" + person.getName().trim() + 
						   "\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
				   fw.append( outputFileName, line );
				   fw.append( outputFileName, newLine );
		   
				   //line = uri + " <http://xmlns.com/foaf/0.1/mbox> \"" + person.getEmail().trim() + 
				   //   "\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
				   
			   } // end loop over all authors
			
		   } // end if non-zero authors 
		   
		} // end loop over all abstract
		
	}
	
}