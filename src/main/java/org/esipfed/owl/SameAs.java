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
package org.esipfed.owl;

public class SameAs {
	
	/**
	 * Method write OWL sameAs statement
	 * @param String person ID 1
	 * @param String person ID 2
	 * @return String OWL/RDF sameAs statement
	 */
	public String writeSameAs ( String id1, String id2 ) {
		        	 
      StringBuilder str = new StringBuilder();      
	  str.append( "  <rdf:Description rdf:about=\"" + id1 + "\"> \n" );
	  str.append( "	   <owl:sameAs rdf:resource=\"" + id2 + "\" /> \n");
	  str.append( "  </rdf:Description>  \n" );
		  		  
	  return str.toString();

	}
	
}