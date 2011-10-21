package org.agu.essi.web.spotlight;

import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.match.MemoryMatcher;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

public class SpotlightAnnotationToRdfXml {
	
	private Vector <String> annotationRDF = new Vector <String> ();
	private EntityMatcher matcher;
	
	public void setEntityMatcher(EntityMatcher m) { matcher = m; }
	
	public void writeAnnotationToRDFXML( String dataDir ) {
	
		FileWrite fw = new FileWrite();
		fw.newFile(dataDir + "spotlight_annotations.rdf", Utils.writeXmlHeader());
		fw.append(dataDir + "spotlight_annotations.rdf", Utils.writeDocumentEntities());
		fw.append(dataDir + "spotlight_annotations.rdf", Utils.writeRdfHeader());
		for ( int i=0; i<annotationRDF.size(); i++ ) { fw.append(dataDir + "spotlight_annotations.rdf", annotationRDF.get(i)); }
		fw.append(dataDir + "spotlight_annotations.rdf", Utils.writeRdfFooter());
	
	}
	
	public void annotationsToRDF( Vector <org.agu.essi.annotation.Annotation> annotations, 
			SpotlightAnnotator annotator, Abstract a ) throws EntityMatcherRequiredException
		{
			Vector <String> dbpediaURIs = new Vector <String> ();
			Vector <String> allSurfaceForms = new Vector <String> ();
			if (matcher == null) matcher = new MemoryMatcher();
			
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
			
		}
}