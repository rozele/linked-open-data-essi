package org.esipfed.data;

import java.util.Vector;

import org.agu.essi.util.Utils;
import org.esipfed.Person;
import org.esipfed.xml.PeopleParserESIP;
import org.esipfed.xml.PeopleParserAGU;
import org.esipfed.owl.SameAs;
import org.agu.essi.util.FileWrite;

public class ComputeSameAs {
	
	public static void main (String[] args) {
		
		// inputs 
		// 0 = ESIP People RDF file
		// 1 = AGU People RDF file
		// 2 = output filename
		SameAs sameAs = new SameAs ();
		FileWrite fw = new FileWrite ();
		StringBuilder same = new StringBuilder();  
	    same.append( Utils.writeXmlHeader() );
	    same.append( Utils.writeDocumentEntities() );
		same.append( Utils.writeRdfHeader() );
		  
		// parse the RDF files
		PeopleParserESIP esipParser = new PeopleParserESIP ();
		PeopleParserAGU aguParser = new PeopleParserAGU ();
		Vector <Person> esipPeople = null;
		Vector <Person> aguPeople = null;
		try {
		  System.out.println( "Parsing: " + args[0] );
		  esipPeople = esipParser.parse( args[0] );
	      System.out.println( "Parsing: " + args[1] );
		  aguPeople = aguParser.parse( args[1] );
		} catch (Exception e) { System.out.println("Exception: " + e); }
	
		// compute sameAs
		String aName;
		String eName;
		Vector <String> aEmail;
		Vector <String> eEmail;
		for (int i=0; i<esipPeople.size(); i++) {
			eName = esipPeople.get(i).getLastName();
			eEmail = esipPeople.get(i).getEmail();
			for (int j=0; j<aguPeople.size(); j++) {
				aName = aguPeople.get(j).getLastName();
				aEmail = aguPeople.get(j).getEmail();
				if ( (eName.equals(aName)) && (eEmail.contains(aEmail.get(0)))) { 
					same.append( sameAs.writeSameAs( esipPeople.get(i).getID(), aguPeople.get(j).getID() ) ); 
				}
			}
		}
		
		// clsoe RDF
		same.append( Utils.writeRdfFooter() );	  
		fw.newFile( args[2], same.toString() );
		
	}
}