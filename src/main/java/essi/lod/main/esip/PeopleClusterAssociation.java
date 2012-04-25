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
package essi.lod.main.esip;

//import org.apache.log4j.Logger;
import java.util.Vector;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import essi.lod.data.esip.rdf.EsipClusterParser;
import essi.lod.data.esip.rdf.EsipPeopleParser;
import essi.lod.entity.esip.Cluster;
import essi.lod.entity.esip.Person;
import essi.lod.rdf.FOAF;
import essi.lod.util.FileWrite;
import essi.lod.util.Utils;

import java.io.File;

/**
 * Class to read ESIP Meeting Attendance 
 * data and write it out as FOAF RDF
 * @author Tom Narock
 */
public class PeopleClusterAssociation {
	
	//static final Logger log = Logger.getLogger(org.esipfed.data.MeetingAttendance.class);  
	Vector <String> clustersNotFound = new Vector <String> ();
	
	public String getPersonID ( Person p, Vector <Person> people ) {
		
		String id = null;
		for ( int i=0; i<people.size(); i++ ) {
			boolean a = false;
			boolean b = false;
			if ( people.get(i).getFirstName().equals( p.getFirstName()) ) { a = true; }
			if ( people.get(i).getLastName().equals( p.getLastName()) ) { b = true; }
			if ( a && b ) { 
				id = people.get(i).getID();
				break;
			}
		}
		return id;
		
	}
	
	public String getClusterID ( String clusterName, Vector <Cluster> clusters ) {
	
		String id = null;
		for ( int i=0; i<clusters.size(); i++ ) {
			if ( clusters.get(i).getName().equals(clusterName) ) {
				id = clusters.get(i).getID();
				break;
			}
		}
		if ( (id == null) && (!clustersNotFound.contains(clusterName)) ) { clustersNotFound.add(clusterName); }
		return id;
		
	}
	
	public int getLastIndex( Vector <Person> people ) {
	
		int lastIndex = -1;
		String id;
		String[] parts;
		int index;
		for ( int i=0; i<people.size(); i++ ) {
			id = people.get(i).getID();
			parts = id.split("/");
			id = parts[parts.length-1];
			parts = id.split("_");
			index = Integer.valueOf( parts[2] ); // ESIP_Person_#
			if ( index > lastIndex ) { lastIndex = index; }
		}
		return lastIndex;
		
	}
	
	 /** Method to create RDF/XML linking people to clusters
	  * 
	  * @param  esipCsvFile  the CSV file containing people names and cluster information
	  * @param  esipRdfFile  the RDF/XML file containing all the known ESIP People
	  * @param  outputFile   the file into which RDF will be written linking people to clusters
	  */
	public void createRDF ( String esipCsvFile, String esipPeopleFile, String esipClusterFile, String outputFile ) {
	 
	  // parse the ESIP People RDF into java vectors
	  EsipPeopleParser parser = new EsipPeopleParser ();
	  Vector <Person> people = new Vector <Person> ();
	  try { 
		 people = parser.parse( esipPeopleFile );
	  } catch (Exception e) { System.out.println("RDF/XML Parse Exception: " + e); }
	  int nextIndex = getLastIndex( people );
	  
	  // parse the ESIP Cluster RDF
	  EsipClusterParser clusterParser = new EsipClusterParser ();
	  Vector <Cluster> clusters = new Vector <Cluster> ();
	  try {
		  clusters = clusterParser.parse( esipClusterFile );
	  } catch (Exception e) { System.out.println("RDF/XML Parse Exception: " + e); }
	  
	  // write RDF/XML header information
	  StringBuilder sb = new StringBuilder();  
      sb.append( Utils.writeXmlHeader() );
      sb.append( Utils.writeDocumentEntities() );
	  sb.append( Utils.writeRdfHeader() );
	  
	  // objects we will need
	  String[] parts;
	  String firstName = "";
	  String lastName = "";
	  String cluster = "";
	  String personID = "";
	  String email = "";
	  boolean exists;
	  FOAF foaf = new FOAF ();
	  FileWrite fw = new FileWrite();
	  Vector <String> unique = new Vector <String> ();
	  
	  // read the file line by line
	  try {
		    FileInputStream fstream = new FileInputStream( esipCsvFile );
		    DataInputStream in = new DataInputStream( fstream );
		    BufferedReader br = new BufferedReader( new InputStreamReader(in) );
		    String strLine;
		    int counter = 0; 
		    while ( (strLine = br.readLine()) != null )   {
		      if ( counter != 0 ) { // ignore the first line (header)
		        parts = strLine.split(",");
		        cluster = parts[2];
		        email = parts[1];
		        if ( !parts[0].equals("") ) { // ignore lines that don't have names or only first names
		        	
		          parts = parts[0].trim().split(" ");
		          if ( parts.length == 2 ) {
		            firstName = parts[0];
		            lastName = parts[1];
		        
		            // create a new person object
		            Person p = new Person(firstName, lastName, null, email, null);
		            
		            // get/create an id for this person
		            exists = p.exists(people, null, null, null);
		            if ( exists ) { personID = getPersonID(p, people); } else { 
		        	  p.createID(nextIndex); 
		        	  nextIndex++; 
		        	  personID = p.getID();
		            }
		        
		            // ignore generic esip membership and just get the specific clusters
		            if ( !cluster.equals("ESIP-All") ) { 
		        
		              // Climate and Energy appears in membership data as Energy and Climate
		              if ( cluster.equals("Energy and Climate") ) { cluster = "Climate and Energy"; }
		              
		              // Executive Committee appears as ExCom
		              if ( cluster.equals("Executive Committee") ) { cluster = "ExCom"; }
		              
		              // Can't have & in RDF/XML
		              if ( cluster.equals("IT & I") ) { cluster = "IT &amp; I"; }
		              
		              // Preservation is also called Data Stewardship
		              if ( cluster.equals("Preservation") ) { cluster = "Data Stewardship"; }
		              
		              // Can't use & in Products and Services
		              if ( cluster.equals("Products & Services") ) { cluster = "Products/Services"; }
		              
			          // get the cluster URI
		        	  String clusterID = getClusterID( cluster, clusters );

		          	  // if person does not exist then write People RDF
		              if ( !exists ) { sb.append( foaf.writePerson(p) ); }
		        
		              // write cluster membership RDF
		              // check for duplicates - we might get the same person with a different 
		              // email address for the same cluster
		              // uniqueness is determined solely by first name, last name
		              String test = clusterID + personID;
		              if ( !unique.contains(test) && (clusterID != null) ) {
		                sb.append( foaf.writeGroupMembership( clusterID, personID ) );
		                unique.add(test);
		              }

		            } // end if not esip-all
		          } // end if no last name
		        } // end if no name   
		      } // end if not header
		      counter++;
		    } // end while
		    in.close();
	  } catch (Exception e) { System.out.println("Error reading ESIP CSV file: " + e.getMessage()); }
			 		
	  // write RDF footers
	  sb.append( Utils.writeRdfFooter() );	  
	  fw.newFile( outputFile, sb.toString() );

	}
	
	/**
	 * Main Method 
	 * @param args (ESIP csv file, ESIP RDF People file, ESIP RDF Cluster file, output RDF filename)
	 */
	public static void main ( String[] args ) {
	 
	  // input arguments 
	  String esipCsvFile = args[0];
	  String esipPeopleFile = args[1];
	  String esipClusterFile = args[2];
	  File outputFile = new File(args[3]);	  
	  if ( outputFile.exists() ) { outputFile.delete(); }

	  PeopleClusterAssociation pc = new PeopleClusterAssociation ();
	  pc.createRDF( esipCsvFile, esipPeopleFile, esipClusterFile, outputFile.toString() );
	  for ( int i=0; i<pc.clustersNotFound.size(); i++ ) { 
		  System.out.println("Could not find: " + pc.clustersNotFound.get(i));
	  }

	}

}