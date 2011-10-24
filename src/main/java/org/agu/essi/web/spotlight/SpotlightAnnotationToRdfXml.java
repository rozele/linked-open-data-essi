package org.agu.essi.web.spotlight;

import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

public class SpotlightAnnotationToRdfXml {
	
	private Vector <String> annotationRDF = new Vector <String> ();
	private EntityMatcher matcher;
	private String directory;
	private int counter = 0;
	private FileWrite fw = new FileWrite();
	
	private void writeRdfFooter ( String fileName ) { fw.append(directory + fileName, Utils.writeRdfFooter()); }
	
	private void writeRdfHeader ( String fileName ) {
		
		fw.newFile(directory + fileName, Utils.writeXmlHeader());
		fw.append(directory + fileName, Utils.writeDocumentEntities());
		fw.append(directory + fileName, Utils.writeRdfHeader());
		
	}
	
	private void writeAnnotationToRDFXML () {
		
		String fileName = "spotlight_annotation_" + counter +".rdf";

		writeRdfHeader( fileName );
		for ( int i=0; i<annotationRDF.size(); i++ ) { fw.append(directory + fileName, annotationRDF.get(i)); }	
		writeRdfFooter( fileName );
		annotationRDF.clear();
		counter++;
	
	}
	
	public void setEntityMatcher(EntityMatcher m) { matcher = m; }
		
	public void setOutputDirectory( String dir ) {
		String test = dir.substring( dir.length()-1 );
		if ( !test.equals( java.io.File.separator ) ) { dir = dir + java.io.File.separator; }
		directory = dir; 
	}
	
	public void writeSpotlightProvenance () { 
		
		String source = SpotlightAnnotator.writeSpotlightAgentRDF();
	  	fw.newFile( directory + "spotlight_provenance.rdf", source );
	  	  
	}
	
	public void annotationsToRDF( Vector <org.agu.essi.annotation.Annotation> annotations, 
			SpotlightAnnotator annotator, Abstract a ) throws EntityMatcherRequiredException
		{
			Vector <String> dbpediaURIs = new Vector <String> ();
			Vector <String> allSurfaceForms = new Vector <String> ();
			if (matcher == null) { throw new EntityMatcherRequiredException (); }
			
			// provenance rdf about which abstract we are annotating and the date accessed
			annotationRDF.add( annotator.writeAnnotationProvenanceToRdfXml( matcher.getAbstractId(a) ) ); 
			  
			// loop over each annotation associated with this abstract
			for ( int i=0; i<annotations.size(); i++ ) {
				
			  SpotlightAnnotation sa = (SpotlightAnnotation) annotations.get(i);
			  dbpediaURIs.add(sa.getURI());
			  allSurfaceForms.add(sa.getSurfaceForm());
			  
			  // rdf about the specific annotation - surface form, dbpedia type, etc.
			  annotationRDF.add( annotator.writeAnnotationTextToRdfXml( sa.getSurfaceForm(), sa.getDBpediaTypes(), 
				sa.getSimilarityScore(), sa.getPercentSecondRank(), matcher.getAbstractId(a) ) );
			  
			}		

			// rdf about all dbpedia URIs and all text selector URIs related to this abstract
			annotationRDF.add( annotator.writeAnnotationToRdfXml( matcher.getAbstractId(a), dbpediaURIs, allSurfaceForms ) );
			
			writeAnnotationToRDFXML( );
			
	}
	
}