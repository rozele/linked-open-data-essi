package essi.lod.data.agu;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Vector;

public class AguPre2000SessionParser {

	private Vector <Pre2000Session> data = new Vector <Pre2000Session> ();
	
	public Vector <Pre2000Session> getData () { return data; }
	
	public boolean allowedTag ( String tag )
	{
		boolean result = false;
		if ( tag.equals("SC") ) { result = true; }
		if ( tag.equals("LO") ) { result = true; }
		if ( tag.equals("DA") ) { result = true; }
		if ( tag.equals("HR") ) { result = true; }
		if ( tag.equals("SN") ) { result = true; }
		if ( tag.equals("PR") ) { result = true; }
		if ( tag.equals("MN") ) { result = true; }
		if ( tag.equals("--") ) { result = true; }
		return result;
	}
	
	public String read (String file) {
		
	  String exception = null;
	  
	  try {
	      BufferedReader br = new BufferedReader(new FileReader(file));
	      String line;
	      String currentTag;
	      String sessionID = "";
	      String location = "";
	      String day = "";
	      String hour = "";
	      String sessionName = "";
	      String convenors = "";
	      String meeting = "";
	      int counter = 0;
	      String previousTag = "";
	      
	      // Create a data Object
     	   Pre2000Session dataObject = new Pre2000Session ();
     	   
    	  // read the file line by line
    	  while ((line = br.readLine()) != null) {
	  	    		  
    	     if ( line.length() > 4 ) {
    	    	 
	    	    currentTag = line.substring(0,2);
	    	    if ( !allowedTag(currentTag) ) { currentTag = previousTag; }
	    	    
	    	    // Fields are: SC, LO, DA, HR, SN, PR, and MN
	    	    if ( currentTag.equals("SC") ) { 
	    	    	if ( sessionID.equals("") ) { sessionID = line.substring(3).trim(); } else { sessionID += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("LO") ) { 
	    	    	if ( location.equals("") ) { location = line.substring(3).trim(); } else { location += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("DA") ) { 
	    	    	if ( day.equals("") ) { day = line.substring(3).trim(); } else { day += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("HR") ) {
	    	    	if ( hour.equals("") ) { hour = line.substring(3).trim(); } else { hour += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("SN") ) { 
	    	    	if ( sessionName.equals("") ) { sessionName = line.substring(3).trim(); } else { sessionName += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("PR") ) { 
	    	    	if ( convenors.equals("") ) { convenors = line.substring(3).trim(); } else { convenors += " " + line.trim(); }
	    	    }
	    	    
	    	    if ( currentTag.equals("MN") ) { 
	    	    	if ( meeting.equals("") ) { meeting = line.substring(3).trim(); } else { meeting += " " + line.trim(); }
	    	    }
		    	
		    	// Determine if we've reached the end of a given abstract
		    	// Some files start with -- as well as uses -- to separate abstracts
		    	if ( currentTag.equals("--") && (counter > 0) ) {	
		    	   
		      	   // fill in the data
		      	   dataObject.setConvenors( convenors );
		      	   dataObject.setDay( day );
		      	   dataObject.setHour( hour );
		      	   dataObject.setLocation( location );
		      	   dataObject.setMeeting( meeting );
		      	   dataObject.setSessionID( sessionID );
		      	   dataObject.setSessionName( sessionName );
		      	   
		      	   // don't add unless we have a session title
		    	   if ( (dataObject.getSessionName() != null) && (!dataObject.getSessionID().equals("") ) ) { data.add( dataObject ); }

		    	   // reset the data
		    	   dataObject = new Pre2000Session ();
		    	   sessionID = "";
		 	       location = "";
		 	       day = "";
		 	       hour = "";
		 	       sessionName = "";
		 	       convenors = "";
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
		
		AguPre2000SessionParser p = new AguPre2000SessionParser ();
		String exception = null;
		try {
			exception = p.read(args[0]);
		} catch (Exception e) { System.out.println(e); }
		if ( exception != null ) { System.out.println(exception); }
		
		Vector <Pre2000Session> data = p.getData();
		System.out.println( "There are " + data.size() + " session" );
		Pre2000Session d = data.get(0);
		
		System.out.println( "Hour: " + d.getHour() );
		System.out.println( "Day: " + d.getDay() );
		System.out.println( "Location: " + d.getLocation() );
		System.out.println( "Session ID: " + d.getSessionID() );
		System.out.println( "Session Name: " + d.getSessionName() );
		System.out.println( "Convenors: " + d.getConvenors() );
		System.out.println( "Meeting: " + d.getMeeting() );
	
	}
	
}