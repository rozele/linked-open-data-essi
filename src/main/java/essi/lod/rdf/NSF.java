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
package essi.lod.rdf;

import java.util.Vector;

public class NSF {
	
	/**
	 * Method write NSF Project RDF 
	 * @param String projectID
	 * @param String principalInvestigator
	 * @param String grantNumber
	 * @param String startDate
	 * @param String endDate
	 * @param String abstractText
	 * @return String NSF Project RDF 
	 */
	
	private static String newLine = "\n";
	private String stringType = "rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"";
	private String dateType = "rdf:datatype=\"http://www.w3.org/2001/XMLSchema#date\"";

	public String writeProject ( String projectID, String principalInvestigator, String grantNumber, 
			String startDate, String endDate, String abstractText, String programID ) {
				
	  // http://vocab.ox.ac.uk/projectfunding#Project isA http://umbel.org/umbel/sc/ResearchProject
    
	  StringBuilder str = new StringBuilder();      
	  
	  str.append( "  <rdf:Description rdf:about=\"" + projectID + "\">" + newLine );
	  str.append( "	   <rdf:type rdf:resource=\"&projectFunding;Project\"/>" + newLine );
	  str.append( "	   <rdf:type rdf:resource=\"&umbel;ResearchProject\"/>" + newLine );
	  str.append( "    <projectFunding:hasPrincipalInvestigator rdf:resource=\"" + principalInvestigator + "\"/>" + newLine );

	  // the program ID may not be available - not all NSF xml files have this field populated
	  if ( programID != null ) {
		  str.append( "    <tw:hasProgram " + "rdf:resource=\"" + programID + "\"/>" + newLine ); 
	  }
	  
	  str.append( "    <projectFunding:grantNumber " + stringType + ">" + 
		grantNumber + "</projectFunding:grantNumber>" + newLine );
	  
	  // dates are in mm/dd/yyyy format - convert to xsd date
	  String[] parts = startDate.split("/");
	  startDate = parts[2] + "-" + parts[0] + "-" + parts[1];
	  parts = endDate.split("/");
	  endDate = parts[2] + "-" + parts[0] + "-" + parts[1];
	  str.append( "    <projectFunding:startDate " + dateType + ">" + 
				startDate + "</projectFunding:startDate>" + newLine );
	  str.append( "    <projectFunding:endDate " + dateType + ">" + 
				endDate + "</projectFunding:endDate>" + newLine );
	  
	  // the abstract also may not be available - not all NSF xml files have this field populated
	  if ( !abstractText.equals("") ) {
		  str.append( "    <projectFunding:statedPurpose " + stringType + ">" + 
				abstractText + "</projectFunding:statedPurpose>" + newLine );	  
	  }
	  
	  str.append( "  </rdf:Description>  \n" );
		  		  
	  return str.toString();

	}
	
	/**
	 * Method write NSF Funding RDF 
	 * @param String fundingID
	 * @param String projectID
	 * @param String nsfDivision
	 * @return String NSF Funding RDF 
	 */
	
	public String writeFundingBody ( String fundingID, Vector <String> projectIDs, String nsfDivision ) { 
		
	  StringBuilder str = new StringBuilder();      
	 
	  str.append( "  <rdf:Description rdf:about=\"" + fundingID + "\">" + newLine );
	  str.append( "	   <rdf:type rdf:resource=\"&projectFunding;FundingBody\"/>" + newLine );
	  
	  for ( int i=0; i<projectIDs.size(); i++ ) {
		str.append( "    <projectFunding:funds rdf:resource=\"" + projectIDs.get(i) + "\"/>" + newLine );  
	  }
	  
	  str.append( "    <dc:description " + stringType + ">" + nsfDivision + "</dc:description>" + newLine );
	  str.append( "  </rdf:Description>  \n" );
		  		  
	  return str.toString();

	}
	
	/**
	 * Method write NSF Funding Program RDF
	 * @param String fundingID
	 * @param String projectID
	 * @param String nsfDivision
	 * @return String NSF Funding RDF 
	 */
	
	public String writeProgram ( String programID, String programName ) { 
		
	  StringBuilder str = new StringBuilder();      
	 
	  str.append( "  <rdf:Description rdf:about=\"" + programID + "\">" + newLine );
	  str.append( "	   <rdf:type rdf:resource=\"&tw;Program\"/>" + newLine );	  
	  str.append( "    <dc:description " + stringType + ">" + programName + "</dc:description>" + newLine );
	  str.append( "  </rdf:Description>  \n" );
		  		  
	  return str.toString();

	}

}