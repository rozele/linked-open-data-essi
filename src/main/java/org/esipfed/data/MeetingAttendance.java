package org.esipfed.data;

import org.apache.log4j.Logger;
import java.io.File;
import org.esipfed.owl.FOAF;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MeetingAttendance {
	
	static final Logger log = Logger.getLogger(org.esipfed.data.MeetingAttendance.class);  
	
	public static void main ( String[] args ) {
	 
	  FOAF foaf = new FOAF ();
	  
	  // directory containing ESIP CSV meeting attendee files
	  File dir = new File(args[0]);
	  File[] files = dir.listFiles();
	  
	  // loop over all the files
	  String[] parts;
	  String firstName = "";
	  String lastName = "";
	  String emailAddress = "";
	  String phoneNumber = "";
	  String affiliation = "";
	  for (int i=0; i<files.length; i++) {
		  
		// read the file line by line
		try {
		    FileInputStream fstream = new FileInputStream( files[i].toString() );
		    DataInputStream in = new DataInputStream( fstream );
		    BufferedReader br = new BufferedReader( new InputStreamReader(in) );
		    String strLine;
		    int counter = 0; 
		    while ( (strLine = br.readLine()) != null )   {
		      if ( counter != 0 ) { // ignore the first line (header)
		        parts = strLine.split(",");
		        firstName = parts[2];
		        lastName = parts[3];
		        affiliation = parts[5];
		        affiliation = affiliation.replace("\"", "");
		        phoneNumber = parts[12];
		        emailAddress = parts[14];
		      }
		      counter++;
		    }
		    in.close();
		} catch (Exception e) { 
			log.error("Error reading ESIP Attendance: " + files[i].toString());
			log.error("File: " + e.getMessage());
			log.error(" ");
		}
			 
		// write FOAF
		foaf.writePerson(firstName, lastName, emailAddress, phoneNumber);
		
		// affiliation - don't write twice
		foaf.writeOrganization(org, orgID, orgMembers)
		
	  }
	  
	}
	
}