package org.esipfed.xml;


import java.io.FileReader;
import java.util.Vector;
import org.esipfed.Person;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

/** An XML Parser for the AGU People RDF
 * 
 * @author Tom Narock
 * 
 */
public class PeopleParserAGU extends DefaultHandler {
 
    StringBuffer accumulator = new StringBuffer();
    Vector <Person> aguPeople = new Vector <Person> ();
    String personID;
    String name;
   
     /** Parse the file and return the information in a Person Vector
      * 
	  * @param  file  the file to parse
	  * @return Vector <Person>
	  */
    public Vector <Person> parse ( String file ) throws Exception {
    	XMLReader xr = XMLReaderFactory.createXMLReader();
        PeopleParserAGU handler = new PeopleParserAGU();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        FileReader r = new FileReader(file);
        xr.parse(new InputSource(r));
        return handler.aguPeople;
    }

    public PeopleParserAGU() { super(); }

    ////////////////////////////////////////////////////////////////////
    // Event handlers.
    ////////////////////////////////////////////////////////////////////

     /** Method which tells the XML parser what to do at the start of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  */
    public void startElement (String uri, String name,
		      String qName, Attributes atts) {
    	
    	accumulator.setLength(0); // Ready to accumulate new text
    	if ( name.equals("Description") ) { this.personID = atts.getValue("rdf:about"); }
    	    	
    }
    
     /** Method which tells the XML parser what to do at the end of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  * @param  qName  the fully qualified name of the element
	  */
    public void endElement (String uri, String name, String qName) {

      String d = accumulator.toString().trim();
  	  if ( name.equals("name") ) this.name = d;
  	  if ( name.equals("mbox") ) {
  		  
  		  // name may have *, which AGU uses to designate presenter
  		  this.name = this.name.replaceAll("\\*", " ");
  		  String[] parts = this.name.trim().split(",");
          if ( parts.length == 2 ) {
        	  Person p = new Person ( parts[1], parts[0], "", d, "" );
        	  p.setID( this.personID );
        	  this.aguPeople.add(p);
          }
          this.personID = null;
  		  this.name = null;
  		  
  	  }
     
	        
    }
    
     /** Method for parsing the character data of the XML file
	  *
	  */
    public void characters (char ch[], int start, int length) { accumulator.append(ch, start, length); }

}
