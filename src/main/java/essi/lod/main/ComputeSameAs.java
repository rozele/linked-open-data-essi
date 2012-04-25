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
package essi.lod.main;

import java.util.Vector;

import essi.lod.data.esip.EsipPeopleData;
import essi.lod.data.matcher.SparqlMatcher;
import essi.lod.entity.esip.Person;
import essi.lod.rdf.SameAs;
import essi.lod.util.FileWrite;
import essi.lod.util.Utils;

/**
 * Compute OWL sameAs relationships from ESIP and AGU People data
 * @author Tom Narock
 */
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
		SparqlMatcher matcher = new SparqlMatcher ( "http://aquarius.tw.rpi.edu:8890/sparql", "http://essi-lod.org/instances/" );
		EsipPeopleData esipPeopleParser = new EsipPeopleData ();
		esipPeopleParser.setEntityMatcher( matcher );
		Vector <Person> esipPeople = esipPeopleParser.getPeople( args[0] );
	
		/*/ compute sameAs
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
		}*/
		
		// clsoe RDF
		same.append( Utils.writeRdfFooter() );	  
		fw.newFile( args[2], same.toString() );
		
	}
}