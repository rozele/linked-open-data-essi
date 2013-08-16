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
package essi.lod.data.nsf;


import java.io.FileReader;
import org.xml.sax.Attributes;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xml.sax.helpers.DefaultHandler;

import essi.lod.entity.nsf.*;

/** An XML Parser for the NSF Data
 * 
 * @author Tom Narock
 * 
 */
public class NsfData extends DefaultHandler {
 
    StringBuffer accumulator = new StringBuffer();
    String _abstract;
    String _nsfDivision;
    String _startDate;
    String _endDate;
    String _awardID;
    String _title;
    
    // there are cases where the NSF data does not contain the PI name
    // let's equate these to an "Unknown" person
    String _piFirstName = "Unknown";
    String _piLastName = "Unknown";
    
    String _email;
    String _affiliation;
    String _programName;
    String _programCode;
    NsfFundingSource _fs = null;	
	NsfProgram _program = null;	
	NsfProject _project = null;
	boolean affiliation = false;
	boolean program = false;
	boolean division = false;
	
	public NsfFundingSource getFundingSource() { return this._fs; }
	public NsfProgram getProgram() { return this._program; }
	public NsfProject getProject() { return this._project; }
    
    public void parse ( String file, int sourceIndex, int programIndex, int projectIndex ) throws Exception {
    	
    	XMLReader xr = XMLReaderFactory.createXMLReader();
        NsfData handler = new NsfData();
        xr.setContentHandler(handler);
        xr.setErrorHandler(handler);
        FileReader r = new FileReader(file);
        xr.parse(new InputSource(r));
        
        // create the funding source
		NsfFundingSource fs = new NsfFundingSource();
		fs.fundingSource( handler._nsfDivision, sourceIndex );
		this._fs = fs;
		
		// create the funding program
		NsfProgram program = new NsfProgram();
		program.program( handler._programName, handler._programCode, programIndex);
		this._program = program;
		
		// create the project
		NsfProject project = new NsfProject();
		project.project( handler._abstract, handler._nsfDivision, handler._startDate, 
				 handler._endDate, handler._awardID, handler._piFirstName, handler._piLastName, handler._email, 
				 handler._affiliation, handler._programName, handler._programCode, handler._title, projectIndex);
		this._project = project;

    }

    public NsfData() { super(); }

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
    	    	
    	// which section of the xml document are we in?
    	if ( name.equals("Division") ) { division = true; affiliation = false; program = false; }
    	if ( name.equals("Institution") ) { division = false; affiliation = true; program = false; }
    	if ( name.equals("ProgramElement") ) { division = false; affiliation = false; program = true; }
    		
    }
    
     /** Method which tells the XML parser what to do at the end of each element
	  * 
	  * @param  uri  the uri of the element
	  * @param  name  the name of the element
	  * @param  qName  the fully qualified name of the element
	  */
    public void endElement (String uri, String name, String qName) {

      String d = accumulator.toString().trim();
      if ( name.equals("AbstractNarration") ) { _abstract = d; }
  	  if ( division ) {
  		  if ( name.equals("LongName") ) { _nsfDivision = d; }
  	  }
  	  if ( name.equals("AwardEffectiveDate") ) { _startDate = d; }
  	  if ( name.equals("AwardExpirationDate") ) { _endDate = d; }
  	  if ( name.equals("AwardID") ) { _awardID = d; }
  	  if ( name.equals("AwardTitle") ) { _title = d; }
  	  if ( name.equals("FirstName") ) { _piFirstName = d; }
  	  if ( name.equals("LastName") ) { _piLastName = d; }
  	  if ( name.equals("EmailAddress") ) { _email = d; }
  	  if ( affiliation ) {
  		  if ( name.equals("Name") ) { _affiliation = d; }
  	  }
  	  if ( program ) {
  		  if ( name.equals("Text") ) { _programName = d; }
  		  if ( name.equals("Code") ) { _programCode = d; }
  	  }
  	  
    }
    
     /** Method for parsing the character data of the XML file
	  *
	  */
    public void characters (char ch[], int start, int length) { accumulator.append(ch, start, length); }

}
