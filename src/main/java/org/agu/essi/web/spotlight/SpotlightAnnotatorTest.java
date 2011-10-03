package org.agu.essi.web.spotlight;

import org.agu.essi.annotation.Annotation;
import org.agu.essi.data.XmlDataSource;
import org.agu.essi.Abstract;
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
		  spotlight = new SpotlightAnnotator ( abstracts.get(i).getAbstractText() );
		  annotations = spotlight.getAnnotations();
		  // for each annotation, write to rdf/xml
		}
		
	}
	
}