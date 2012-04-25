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
package essi.lod.main.annotations;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import essi.lod.data.SparqlUnannotatedAbstractData;

/**
 * Read AGU Abstracts from a SPARQL endpoint and call Spotlight Annotation service
 * @author Tom Narock
 */
public class SpotlightAnnotationsFromSparql {
	
	private static Log log = LogFactory.getLog(SpotlightAnnotationsFromSparql.class);
	
	public static void main ( String[] args ) {
		
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("limit", true, "Limit to use in SPARQL query.");
	  	options.addOption("offset", true, "Offset to use in SPARQL query.");
	  	options.addOption("port", true, "Port of the SPARQL proxy.");
	  	options.addOption("outputDirectory", true, "Directory in which to store the annotations.");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	try { cmd = parser.parse( options, args); } catch ( Exception pe ) { 
	  	  log.error( "Error parsing command line options: " + pe.toString() ); 
	  	}
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String output = null;
	  	String limit = null;
	  	String offset = null;
	  	String port = null;
	  	
	  	// output directory
	    if ( !cmd.hasOption("outputDirectory") ) {
	    	error = true;
	  		log.error("--outputDirectory Not Set. Directory in which to store the retrieved abstracts.");
	  	} else { output = cmd.getOptionValue("outputDirectory"); }
	    
	    // limit
	    if ( !cmd.hasOption("limit") ) {
	    	error = true;
	  		System.out.println("--limit Not Set. Limit to use in the SPARQL Query.");
	  	} else { limit = cmd.getOptionValue("limit"); }
	    
	    // offset
	    if ( !cmd.hasOption("offset") ) {
	    	error = true;
	  		System.out.println("--offset Not Set. Offset to use in the SPARQL Query.");
	  	} else { offset = cmd.getOptionValue("offset"); }
	    
	    // port
	    if ( !cmd.hasOption("port") ) {
	    	error = true;
	  		System.out.println("--port Not Set. Port of the SPARQL endpoint.");
	  	} else { port = cmd.getOptionValue("port"); }
	    
	    // if no errors then proceed
	    if (!error) {	
	    	
	      try {
	    	  
	    	    // POST the SPARQL query to the public endpoint and parse the results
	    	    SparqlUnannotatedAbstractData abstracts = new SparqlUnannotatedAbstractData ();
	    	    abstracts.annotate( limit, offset, port, output );
	    		
	      } catch ( Exception e ) { log.error(e); }

	    } // end if
	    
	}
	
}