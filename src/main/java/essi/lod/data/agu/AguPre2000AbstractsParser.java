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
	
	public boolean allowedTag ( String tag )
	{
		boolean result = false;
		if ( tag.equals("HR") ) { result = true; }
		if ( tag.equals("AN") ) { result = true; }
		if ( tag.equals("TI") ) { result = true; }
		if ( tag.equals("AU") ) { result = true; }
		if ( tag.equals("EM") ) { result = true; }
		if ( tag.equals("AF") ) { result = true; }
		if ( tag.equals("AB") ) { result = true; }
		if ( tag.equals("DE") ) { result = true; }
		if ( tag.equals("SC") ) { result = true; }
		if ( tag.equals("MN") ) { result = true; }
		if ( tag.equals("PH") ) { result = true; }
		if ( tag.equals("--") ) { result = true; }
		return result;
	}
	
	public String read (String file) {
		
	  String exception = null;
	  
	  try {
	      BufferedReader br = new BufferedReader(new FileReader(file));
	      String line;
	      String currentTag;
	      Author author;
	      String hour = "";
	      String abNumber = "";
	      String title = "";
	      //String aDetails = "";
	      //Vector <String> authorDetails = new Vector <String> ();
	      Vector <String> authorNames = new Vector <String> ();
	      String abstractText = "";
	      Vector <String> keyword = new Vector <String> ();
	      String section = "";
	      String meeting = "";
	      int counter = 0;
	      String previousTag = "";
	      
	      // Create a data Object
     	   Pre2000Data dataObject = new Pre2000Data ();
     	   
    	  // read the file line by line
    	  while ((line = br.readLine()) != null) {
    		  
    	     if ( line.length() > 4 ) {
    	    	 
	    	    currentTag = line.substring(0,2);
	    	    if ( !allowedTag(currentTag) ) { currentTag = previousTag; }
	    	    
	    	    // Fields are: HR, AN, TI, AU, EM, AF, AB, DE, SC, MN, ignoring phone (PH) for now
	    	    if ( currentTag.equals("HR") ) { 
	    	    	if ( hour.equals("") ) { hour = line.substring(3).trim(); } else { hour += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("AN") ) { 
	    	    	if ( abNumber.equals("") ) { abNumber = line.substring(3).trim(); } else { abNumber += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("TI") ) { 
	    	    	if ( title.equals("") ) { title = line.substring(3).trim(); } else { title += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("AU") ) { authorNames.add( line.substring(3).trim() ); }
	    	    	
	    	    	// submit the previous author if there is one
	    	    	//if ( !aDetails.equals("") ) { authorDetails.add( aDetails ); }
	    	    	
	    	    	//aDetails = "AU:" + line.substring(3).trim();
	    	    
	    	    //}	    	    
	    	    
	    	    //if ( currentTag.equals("EM") ) { aDetails += ";EM:" + line.substring(3).trim(); }
	    	  
	    	    //if ( currentTag.equals("AF") ) { aDetails += ";AF:" + line.substring(3).trim(); }
	    	    
	    	    if ( currentTag.equals("AB") ) { 
	    	    	if ( abstractText.equals("") ) { 
	    	    		abstractText = line.substring(3).trim(); 
	    	    	} else { abstractText += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("DE") ) { keyword.add( line.substring(3).trim() ); }
	    	    
	    	    if ( currentTag.equals("SC") ) { 
	    	    	if ( section.equals("") ) { section = line.substring(3).trim(); } else { section += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("MN") ) { 
	    	    	if ( meeting.equals("") ) { meeting = line.substring(3).trim(); } else { meeting += " " + line.trim(); }
	    	    }
		    	
		    	// Determine if we've reached the end of a given abstract
		    	// Some files start with -- as well as uses -- to separate abstracts
		    	if ( currentTag.equals("--") && (counter > 0) ) {	
		    	   
		      	   // fill in the data
		      	   for ( int i=0; i<authorNames.size(); i++ ) {
		      		  // String details = authorDetails.get(i);
		      		  // String[] parts = details.split(";");
		      		  // String an = "";
	      			  // String em = "";
	      			  // String af = "";
		      		  // for ( int j=0; j<parts.length; j++ ) {   
		      		  //     String tag = parts[j].substring(0,2);
		      		  //     if ( tag.equals("AU") ) an = parts[j].substring(3).trim();
		      		  //     if ( tag.equals("EM") ) em = parts[j].substring(3).trim();
		      		  //     if ( tag.equals("AF") ) af = parts[j].substring(3).trim();
		      		  // }
		      		  author = new Author ( authorNames.get(i), "", "");
		      		  dataObject.addAuthor(author);
		      	   }
		      	   for ( int i=0; i<keyword.size(); i++ ) { dataObject.addKeyword( keyword.get(i) ); }
		      	   dataObject.setAbstractNumber( abNumber );
		      	   dataObject.setAbstractText( abstractText );
		      	   dataObject.setMeeting( meeting );
		      	   dataObject.setSection( section );
		      	   dataObject.setTime( hour );
		      	   dataObject.setTitle( title );
		      	   
		      	   // don't add unless we have a title
		    	   if ( dataObject.getTitle() != null ) { data.add( dataObject ); }

		    	   // reset the data
		    	   dataObject = new Pre2000Data ();
		    	   hour = "";
		 	       abNumber = "";
		 	       title = "";
		 	       authorNames.clear();
		 	       abstractText = "";
		 	       keyword.clear();
		 	       section = "";
		 	       meeting = "";
		    	  
		    	} 
		    	
		    	previousTag = currentTag;
		    	  
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
		String exception = null;
		try {
			exception = p.read(args[0]);
		} catch (Exception e) { System.out.println(e); }
		if ( exception != null ) { System.out.println(exception); }
		
		Vector <Pre2000Data> data = p.getData();
		System.out.println( "There are " + data.size() + " abstracts" );
		Pre2000Data d = data.get(0);
		
		Vector <Author> authors = d.getAuthors();
		Vector <String> keywords = d.getKeywords();
		System.out.println();   	
		
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