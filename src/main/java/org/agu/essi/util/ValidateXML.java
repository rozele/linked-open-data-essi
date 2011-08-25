package org.agu.essi.util;

import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;
import javax.xml.XMLConstants;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

/**
 * Read a directory of XML files and validate against a schema
 * @author Tom Narock
 */
public class ValidateXML {

	public String aguSchema = "http://www.agu.org/focus_group/essi/schema/agu.xsd";
	private boolean isValid = false;
	
	/**
	 * Method to validate an XML file against a schema
	 * @param schema schema object of the schema to use in XML validation
	 * @param xmlFile full path and filename of the XML file to validate
	 * @return true/false (valid XML/invalid XML)
	 */
	public boolean validate( Schema schema, String xmlFile ) {
		
	  try {
		      // creating a Validator instance
		      Validator validator = schema.newValidator();
		      
		      // preparing the XML file as a SAX source
		      SAXSource source = null;
		      try {
		        source = new SAXSource(new InputSource(new java.io.FileInputStream(xmlFile)));
		      } catch (FileNotFoundException fnfe) { System.out.println(fnfe); }
		      
		      // validating the SAX source against the schema
		      try {
		        validator.validate(source);
		        isValid = true;
		      } catch (IOException ioe) { System.out.println(ioe); }
		      
		  } catch (SAXException SAXex) { System.out.println(xmlFile + " " + SAXex); }
		     
	  return isValid;
	  
	}
		
	/**
	 * Method to load a schema from a url
	 * @param url location of the schema to load
	 * @return schema a java Schema object
	 */
	public Schema load( String url ) {
		
		Schema schema = null;
		try {

			URL querySchema = new URL (url);
			
			// getting the default implementation of XML Schema factory
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);

			// parsing the schema file      
			schema = factory.newSchema( querySchema );

		} catch (Exception e) { System.out.println(e); }
      
		return schema;
  
	}
	
	static class MyErrorHandler implements ErrorHandler {
		
		String message = null;
		
		public void fatalError( SAXParseException e ) throws SAXException {
	      message = getMessage(e, "fatal");
	      throw new SAXException(message);
	    }
		
	    public void error( SAXParseException e ) throws SAXException { 
	      message = getMessage(e, "error");
		  throw new SAXException(message);
		}
	    
	    public void warning( SAXParseException e ) throws SAXException { 
	      message = getMessage(e, "warning");
	      throw new SAXException(message); 
	    }
	    
	    private String getMessage (SAXParseException e, String type) {
	      if ( type.equals("fatal") ) {
	        message = "Fatal Error, Line number: " + e.getLineNumber() + ", Message: " + e.getMessage();
	      }
	      if ( type.equals("error") ) {
	    	message = "Error, Line number: " + e.getLineNumber() + ", Message: " + e.getMessage();
	      }
	      if ( type.equals("warning") ) {
	    	message = "Warning, Line number: " + e.getLineNumber() + ", Message: " + e.getMessage();		 
	      }
	      return message;
	    }

	}
	
	public static void main ( String args[] ) {
	  
		ValidateXML validate = new ValidateXML ();
		
		// Object to deal with command line options (Apache CLI)
	  	Options options = new Options();
	  	options.addOption("xmlDirectory", true, "Directory of xml files to validate.");
	  	options.addOption("schema",true, "(Optional) The schema to use in validation. If not specified then default AGU " +
	  			"schema is used.");
	  	  
	  	// Parse the command line arguments
	  	CommandLine cmd = null;
	  	CommandLineParser parser = new PosixParser();
	  	String schemaString;
	  	try {
	  	  cmd = parser.parse( options, args);
	  	} catch ( Exception pe ) { System.out.println("Error parsing command line options: " + pe.toString()); }
	  	  
	  	// Check if the correct options were set
	  	boolean error = false;
	  	String errorMessage = null;
	    if ( !cmd.hasOption("xmlDirectory") ) {
	    	error = true;
	  		errorMessage = "--xmlDirectory Not Set. Directory of xml files to validate.";
	  	}
	  	if ( !cmd.hasOption("schema")) { schemaString = validate.aguSchema; } else { schemaString = cmd.getOptionValue("schema"); }
		
	  	if ( error ) { System.out.println( errorMessage ); } else {
	  		
	  	  // load the schema
		  Schema schema = validate.load( schemaString );
		  
		  // read and validate the xml files
		  File dir = new File ( cmd.getOptionValue("xmlDirectory") );
		  File[] files = dir.listFiles();
		  for (int i=0; i<files.length; i++) { validate.validate( schema, files[i].toString() ); };
	  	
	  	}
		
	}
	
}
