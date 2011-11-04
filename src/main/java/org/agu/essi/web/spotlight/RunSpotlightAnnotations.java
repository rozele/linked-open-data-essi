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

import org.agu.essi.data.XmlAbstractDataSource;
import org.agu.essi.match.MemoryMatcher;
import org.agu.essi.abstracts.Abstract;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

import java.util.Vector;

/**
 * Read AGU Abstracts from an XML data source and call Spotlight Annotation service
 * @author Tom Narock
 */
public class RunSpotlightAnnotations {

	public static void main ( String[] args ) {
		
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("outputDirectory", true, "Directory in which to store the annotations.");
	  	options.addOption("inputDirectory",true,"Directory from which to retrieve existing abstract XML.");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try { cmd = parser.parse( options, args); } catch ( Exception pe ) { 
	  	  System.out.println( "Error parsing command line options: " + pe.toString() ); 
	  	}
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String output = null;
	  	String input = null;
	  	
	  	// output directory
	    if ( !cmd.hasOption("outputDirectory") ) {
	    	error = true;
	  		System.out.println("--outputDirectory Not Set. Directory in which to store the retrieved abstracts.");
	  	} else { output = cmd.getOptionValue("outputDirectory"); }
	    
	    //input directory
	    if ( !cmd.hasOption("inputDirectory") ) {
	    	error = true;
	    	System.out.println("--inputDirectory Not Set. Directory from which to retrieve existing abstract XML.");
	    } else { input = cmd.getOptionValue("inputDirectory"); }
	    
	    // if no errors then proceed
	    if (!error) {	
	    	
	      try {
	    	    MemoryMatcher matcher = new MemoryMatcher();
	    		XmlAbstractDataSource data = new XmlAbstractDataSource(input);
	    		Vector<Abstract> abstracts = data.getAbstracts();
	    		SpotlightAnnotationToRdfXml sWriter = new SpotlightAnnotationToRdfXml ();

	    		sWriter.setEntityMatcher( matcher );
	    		sWriter.setOutputDirectory( output );
	    		sWriter.writeSpotlightProvenance();
	    		
	    		// loop over all the abstracts and annotate
	    		for (int i = 0; i < abstracts.size(); i++) {
	    		  SpotlightAnnotator annotator = new SpotlightAnnotator ( abstracts.get(i).getAbstract() );
	      		  Vector <org.agu.essi.annotation.Annotation> annotations = annotator.getAnnotations();
	      		  sWriter.annotationsToRDF( annotations, annotator, abstracts.get(i) );
	    		}
	    		
	      } catch ( Exception e ) { System.out.println(e); }

	    } // end if
	    
	}
	
}