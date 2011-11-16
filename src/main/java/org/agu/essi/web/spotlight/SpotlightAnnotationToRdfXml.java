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

import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.FileWrite;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

/**
 * Write Annotations out as RDF/XML
 * @author Tom Narock
 */
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
	
	/**
	 * Method to set the entity matcher
	 * @param EntityMatcher matcher
	 */
	public void setEntityMatcher(EntityMatcher m) { matcher = m; }
		
	/**
	 * Method to set the output directory
	 * @param String directory
	 */
	public void setOutputDirectory( String dir ) {
		String test = dir.substring( dir.length()-1 );
		if ( !test.equals( java.io.File.separator ) ) { dir = dir + java.io.File.separator; }
		directory = dir; 
	}
	
	/**
	 * Method to write spotlight provenance file
	 */
	public void writeSpotlightProvenance () { 
		
		String source = SpotlightAnnotator.writeSpotlightAgentRDF();
	  	fw.newFile( directory + "spotlight_provenance.rdf", source );
	  	  
	}
	
	/**
	 * Method to write annotations to RDF/XML
	 * @param Vector <Annotation> annotations
	 * @param SpotlightAnnotator annotator
	 * @param String abstractID
	 */
	public void annotationsToRDF( Vector <org.agu.essi.annotation.Annotation> annotations, 
			SpotlightAnnotator annotator, String id ) {
		
			Vector <String> dbpediaURIs = new Vector <String> ();
			Vector <String> allSurfaceForms = new Vector <String> ();
			
			// provenance rdf about which abstract we are annotating and the date accessed
			annotationRDF.add( annotator.writeAnnotationProvenanceToRdfXml( id ) ); 
			  
			// loop over each annotation associated with this abstract
			for ( int i=0; i<annotations.size(); i++ ) {
				
			  SpotlightAnnotation sa = (SpotlightAnnotation) annotations.get(i);
			  
			  // check if the abstract already has a reference to this dbpedia uri
			  // we want to avoid repetitive references, i.e. the abstract mentions
			  // the same dbpedia concept multiple times
			  if ( !dbpediaURIs.contains(sa.getURI()) ) { dbpediaURIs.add(sa.getURI()); }
				  
			  allSurfaceForms.add(sa.getSurfaceForm());
			  
			  // rdf about the specific annotation - surface form, dbpedia type, etc.
			  annotationRDF.add( annotator.writeAnnotationTextToRdfXml( sa.getSurfaceForm(), sa.getDBpediaTypes(), 
			    sa.getSimilarityScore(), sa.getPercentSecondRank(), id, sa.getURI(), sa.getIndex() ) );
			  
			} // end for		

			// rdf about all dbpedia URIs and all text selector URIs related to this abstract
			annotationRDF.add( annotator.writeAnnotationToRdfXml( id, dbpediaURIs, allSurfaceForms ) );

			writeAnnotationToRDFXML( );

		}
	
	/**
	 * Method to write annotations to RDF/XML
	 * @param Vector <Annotation> annotations
	 * @param SpotlightAnnotator annotator
	 * @param Abstract abstract
	 */
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
			  
			  // check if the abstract already has a reference to this dbpedia uri
			  // we want to avoid repetitive references, i.e. the abstract mentions
			  // the same dbpedia concept multiple times
			  if ( !dbpediaURIs.contains(sa.getURI()) ) { dbpediaURIs.add(sa.getURI()); }
				  
			  allSurfaceForms.add(sa.getSurfaceForm());
			  
			  // rdf about the specific annotation - surface form, dbpedia type, etc.
			  annotationRDF.add( annotator.writeAnnotationTextToRdfXml( sa.getSurfaceForm(), sa.getDBpediaTypes(), 
			    sa.getSimilarityScore(), sa.getPercentSecondRank(), matcher.getAbstractId(a), sa.getURI(), sa.getIndex() ) );
			  
			} // end for		

			// rdf about all dbpedia URIs and all text selector URIs related to this abstract
			annotationRDF.add( annotator.writeAnnotationToRdfXml( matcher.getAbstractId(a), dbpediaURIs, allSurfaceForms ) );

			writeAnnotationToRDFXML( );

		}
	
}