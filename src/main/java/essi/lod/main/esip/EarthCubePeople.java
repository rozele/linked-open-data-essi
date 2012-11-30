package essi.lod.main.esip;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import essi.lod.data.agu.rdf.AguPeopleParser;
import essi.lod.entity.esip.Person;
import java.util.Vector;
import essi.lod.rdf.FOAF;
import essi.lod.util.FileWrite;
import essi.lod.util.Utils;
import java.io.File;

public class EarthCubePeople {
	
	public static void main ( String[] args ) {
	
		 String esipCsvFile = args[0];
		 String outFile = args[1];
		 String group;
		 String email;
		 
		 FOAF foaf = new FOAF ();
		 FileWrite fw = new FileWrite ();
		 File f = new File (outFile);
		 if ( f.exists() ) { f.delete(); }
		 String aguPeopleRDF = "/Users/tnarock/Git/Work/ESIP/FundingFriday/rdf/people.rdf";
		 
		 // write RDF/XML header information
	     fw.append( outFile, Utils.writeXmlHeader() );
	     fw.append( outFile, Utils.writeDocumentEntities() );
		 fw.append( outFile, Utils.writeRdfHeader() );
		  
		 // parse the agu people RDF
		 Vector <Person> aguPeople = new Vector <Person> ();
		 AguPeopleParser parser = new AguPeopleParser ();
		 try {
			 aguPeople = parser.parse(aguPeopleRDF);
		 } catch (Exception e) { System.out.println(e); }
		 
		 // read the csv file with EarthCube data
		  try {
			    FileInputStream fstream = new FileInputStream( esipCsvFile );
			    DataInputStream in = new DataInputStream( fstream );
			    BufferedReader br = new BufferedReader( new InputStreamReader(in) );
			    String strLine;
			    int counter = 0; 
			    String[] parts;
			    String earthCubeID;
			    String personID;
			    while ( (strLine = br.readLine()) != null )   {
			    	
			      if ( counter != 0 ) { // ignore the first line (header)
			    	  
			    	// get the next EarthCube person
			        parts = strLine.split(",");
			        group = parts[0].trim();
			        group = group.replace(" ", "_"); 
			        email = parts[1].trim();
			        earthCubeID = "http://essi-lod.org/instances/Group_" + group;
			        
			        // output status
			        System.out.println("Looking for: " + email);
			        
			        // find this person in AGU people data
			        for (int i=0; i<aguPeople.size(); i++) {
			        	Person p = aguPeople.get(i);
			        	Vector <String> emails = p.getEmail();
			        	if ( emails.contains(email) ) {
			        		personID = p.getID().replace("http://aquarius.tw.rpi.edu/essi-lod/instances/", 
			        				"http://essi-lod.org/instances/");
			        		fw.append( outFile, foaf.writeGroupMembership(earthCubeID, personID) );
			        		break;
			        	}
			        }
			        
			      } // end if not header
			      counter++;
			    } // end while
			    in.close();
		  } catch (Exception e) { System.out.println("Error reading ESIP CSV file: " + e.getMessage()); }
				 		
		  fw.append( outFile, Utils.writeRdfFooter() );
		  
	}
	
}