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

public class ValidateXML {

	boolean isValid = false;
	
	public boolean validate( Schema schema, String xmlFile) {
		
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
	  
		// Inputs 
		// 0 = Directory of xml files to validate
		
		ValidateXML validate = new ValidateXML ();
		
		// load the agu schema
		String spaseSchema = "http://www.agu.org/focus_group/essi/schema/agu.xsd";
		Schema schema = validate.load( spaseSchema );
		File dir = new File ( args[0] );
		File[] files = dir.listFiles();
		for (int i=0; i<files.length; i++) { validate.validate( schema, files[i].toString() ); };
		
	}
	
}
