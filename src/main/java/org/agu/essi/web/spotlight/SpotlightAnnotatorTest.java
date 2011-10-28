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
package org.agu.essi.web.spotlight;

import org.agu.essi.annotation.Annotation;
import org.agu.essi.data.XmlDataSource;
import org.agu.essi.abstracts.Abstract;
import java.util.Vector;

public class SpotlightAnnotatorTest {

	public static void main ( String[] args ) {
		
		// Inputs
		String xmlDir = args[0];
		
		Vector <Abstract> abstracts = null;
		try {
			XmlDataSource xmlData = new XmlDataSource ( xmlDir );
			abstracts = xmlData.getAbstracts();
		} catch ( Exception e ) { System.out.println(e); }

		SpotlightAnnotator spotlight = null;
		Vector <Annotation> annotations = null;
		for ( int i=0; i<abstracts.size(); i++ ) {
		  spotlight = new SpotlightAnnotator ( abstracts.get(i).getAbstract() );
		  annotations = spotlight.getAnnotations();
		  for ( int j=0; j<annotations.size(); j++ ) {
		    Annotation a = annotations.get(j);
		    System.out.println( a.getAnnotation() + " " + a.getSurfaceForm() );
		  }
		  // for each annotation, write to rdf/xml
		}
		
	}
	
}