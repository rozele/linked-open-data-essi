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
package essi.lod.data.agu.rdf;


import java.io.FileReader;
import java.util.Vector;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

import essi.lod.entity.esip.Person;

/** An XML Parser for the AGU People RDF
 * 
 * @author Tom Narock
 * 
 */
public class AguPeopleParser extends DefaultHandler {
 
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
        AguPeopleParser handler = new AguPeopleParser();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        FileReader r = new FileReader(file);
        xr.parse(new InputSource(r));
        return handler.aguPeople;
    }

    public AguPeopleParser() { super(); }

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
