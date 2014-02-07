package essi.lod.data.agu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;
import essi.lod.entity.agu.Author;
import essi.lod.entity.agu.Person;
import essi.lod.entity.agu.Organization;

public class AguPre2000AbstractsParser {

	private Vector <Pre2000Data> data = new Vector <Pre2000Data> ();
	
	public Vector <Pre2000Data> getData () { return data; }
	
	public String read (String file) {
		
	  String exception = null;
	  
	  try {
	      BufferedReader br = new BufferedReader(new FileReader(file));
	      String line;
	      String type;
	      String value = "";
	      Author author;
	      String personName = null;
	      String email = null;
	      boolean abstct = false;
	      boolean title = false;
	      boolean affiliation = false;
	      int counter = 0;
	      
	      // Create a data Object
    	  Pre2000Data dataObject = new Pre2000Data ();
    	  
	      while ((line = br.readLine()) != null) {
	  	
              // Order of fields is: HR, AN, TI, AU, EM, AF, AB, DE, SC, MN 	
  	  
	    	  // First two characters tell us what the line is
	    	  if ( line.length() > 2 ) {
	    		  
	    	    type = line.substring(0,2);
	    	    
	    	    // Hour (time)
	    	    if ( type.equals("HR") ) { dataObject.setTime( line.substring(3).trim() ); }; 
	    	    
	    	    // Abstract number
	    	    if ( type.equals("AN") ) { dataObject.setAbstractNumber( line.substring(3).trim() ); }
	    	    
	    	    // Abstract title
	    	    if ( title && !type.equals("AU") ) { value += line; }
		    	if ( type.equals("TI") ) { value = line.substring(3).trim(); title = true; }
		    	
		    	// Abstract author name
	    	    if ( type.equals("AU") ) {
	    	    	
	    	    	  // title has finished and author has started
	    	    	  // write the title
		    		  dataObject.setTitle( value ); 
		    		  title = false;
		    		  value = "";
		    		  
		    		  // get author name
		    		  personName = line.substring(3).trim();
		    		  
		    		  // remove * for signifying first author
		    		  String test = personName.substring(0,1);
		    		  if ( test.equals("*") ) { personName = personName.substring(1); }
		    			  
	    	    } 
		    	
	    	    // author email
		    	if ( type.equals("EM") ) { email = line.substring(3).trim(); }
		    	
		    	// author affiliation
		    	if ( affiliation && !type.equals("AB") ) { value += line; }
		    	if ( type.equals("AF") ) { value = line.substring(3).trim(); affiliation = true; }
		    	
		    	// abstract text
		    	if ( type.equals("AB") ) { 
		    		
		    		 // write affiliation
		    		 author = new Author ( personName, email, value ); 
		    	     dataObject.addAuthor( author );
		    	     affiliation = false;
		    	     
		    	     // get abstract text
		    	     value = line.substring(3).trim();
		    	     abstct = true;
		    	     
		    	} 
		    	if ( abstct && !type.equals("DE") ) { value += line; }
		    	
		    	if ( type.equals("DE") ) { 
		    	
		    	   // write abstract text
  		    	   dataObject.setAbstractText( value );
  		    	   abstct = false;
  		    		  
		    	   // keep just the 4 digit keyword, ignore the text
		    	   value = value.substring(0, 4);
		    	   dataObject.addKeyword(value); 
		    	
		    	} 
		    		  
		    	if ( type.equals("SC") ) { dataObject.setSection( line.substring(3).trim() ); }
		    	
		    	if ( type.equals("MN") ) { dataObject.setMeeting( line.substring(3).trim() ); }
		    	
		    	// Determine if we've reached the end of a given abstract
		    	// The files start with -- as well as uses -- to separate abstracts
		    	if ( type.equals("--") && (counter > 0) ) {
		    				    
		    	   // don't add unless we have a title
		    	   if ( dataObject.getTitle() != null ) { data.add( dataObject ); }
		    	   dataObject = new Pre2000Data (); 
		    	   value = "";
		    	  
		    	} 
		    	
	    	  } // end if line has text
	    	  
	    	  counter++;
	          
	      }
	      
	      // add the last abstract to the vector
		  data.add( dataObject );
		  
	      br.close();
		  
	  } catch (Exception e) { exception = e.toString(); }
	  
	  return exception;
	  
	}
	
	public static void main (String[] args) {
		
		AguPre2000AbstractsParser p = new AguPre2000AbstractsParser ();
		try {
			p.read(args[0]);
		} catch (Exception e) { System.out.println(e); }
	
		Vector <Pre2000Data> data = p.getData();
		System.out.println( "There are " + data.size() + " abstracts" );
		Pre2000Data d = data.get(0);
		
		Vector <Author> authors = d.getAuthors();
		Vector <String> keywords = d.getKeywords();
		System.out.println();   // Order of fields is: HR, AN, TI, AU, EM, AF, AB, DE, SC, MN 	
		
		System.out.println( "Time: " + d.getTime() );
		System.out.println( "Abstract Number: " + d.getAbstractNumber() );
		System.out.println( "Abstract Title: " + d.getTitle() );
		for ( int i=0; i<authors.size(); i++ )
		{
			Author a = authors.get(i);
			Person pe = a.getPerson();
			Vector <Organization> affiliations = a.getAffiliations();
			System.out.println( "Author Name: " + pe.getName() );
			System.out.println( "Author Email: " + pe.getEmail() );
			System.out.println( );
			for ( int j=0; j<affiliations.size(); j++ )
			{
			  Organization o = affiliations.get(j);
			  System.out.println( "Author Affiliation: " + o.toString() );
		    }
		}
		System.out.println( "Abstract Text: " + d.getAbstractText() );
		for ( int i=0; i<keywords.size(); i++ ) { System.out.println( keywords.get(i) ); }
		System.out.println( "Section: " + d.getSection() );
		System.out.println( "Meeting: " + d.getMeeting() );
	
	}
	
}