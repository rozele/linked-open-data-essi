package essi.lod.util;

import essi.lod.data.agu.Pre2000Data;
import essi.lod.entity.agu.Author;
import essi.lod.entity.agu.Person;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import essi.lod.util.FileWrite;

public class OutputTurtlePeople
{
	
	public void write ( String outputFileName, String year, Vector <Pre2000Data> abstracts )
	{
		
		int counter = 0;
		String line;
		String uri;
		String abstractID;
		String newLine = "\r\n";
		FileWrite fw = new FileWrite();
		
		for ( int i=0; i<abstracts.size(); i++ ) {
			
		   Pre2000Data d = abstracts.get(i);
		   Vector <Author> authors = d.getAuthors();
		   
		   if ( authors.size() > 0 ) {
 		   Author author = authors.get(0);
		   Person person = author.getPerson();
		   
		   line = "";
		   uri = "<http://abstracts.agu.org/people/";
		   
		   // check the abstract number for POSTER and UNKNOWN
		   Pattern pattern = Pattern.compile("\\s");
		   Matcher matcher = pattern.matcher( d.getAbstractNumber() );
		   if (matcher.find()) {
			   String[] split = d.getAbstractNumber().split("\\s");
			   d.setAbstractNumber( split[0] );
		   }
		    
		   // create the abstract ID 
		   if ( d.getAbstractNumber().equals("") ) { 
			   abstractID = "FM" + year.substring(2).trim() + "_AbstractID" + String.valueOf(counter) + "_author_" + 
		          person.getName().replace(" ", "_"); 
			   counter++; 
		   } else { 
			   abstractID = "FM" + year.substring(2).trim() + "_" + d.getAbstractNumber().trim() + "_author_" + 
			          person.getName().replace(" ", "_"); 
		   } 
		   
		   if ( person.getEmail().equals("") ) { 
			   uri += abstractID + ">";
		   } else { uri += person.getEmail() + ">"; }
		  
		   // see if the section if available
		   String section = d.getSection();
					
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
			
		   line = uri + " <http://tw.rpi.edu/schema/hasRole> " + 
		     "<http://abstracts.agu.org/meetings/" + year + "/FM/sections/" + section + 
		     "/sessions/" + session + "/abstracts/" + abstractID + "/authors/1> . ";

		   fw.append( outputFileName, line );
		   fw.append( outputFileName, newLine );
		   
		   line = uri + " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://xmlns.com/foaf/0.1/Person> . ";
		   fw.append( outputFileName, line );
		   fw.append( outputFileName, newLine );
		   
		   line = uri + " <http://xmlns.com/foaf/0.1/name> \"" + person.getName().trim() + 
		      "\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
		   fw.append( outputFileName, line );
		   fw.append( outputFileName, newLine );
		   
		   line = uri + " <http://xmlns.com/foaf/0.1/mbox> \"" + person.getEmail().trim() + 
		      "\"^^<http://www.w3.org/2001/XMLSchema#string> . ";
			
		}
		   
		}
	}
	
}