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
package essi.lod.entity.nsf;

import java.util.Vector;

import essi.lod.util.Namespaces;

/**
 * Container class for NSF Project
 * @author Tom Narock
 */
public class NsfProject {
	
	private String _abstract = "";
	private String _title = "";
    private String _nsfDivision = "";
    private String _startDate = "";
    private String _endDate = "";
    private String _awardID = "";
    private String _piFirstName = "";
    private String _piLastName = "";
    private String _email = "";
    private String _affiliation = "";    
    private String _id = "";
    private String _programID = "";
    private String _piID = "";
    private String _programName = "";
    private String _programCode = "";
    
	/**
	 * Construct NSF Project
	 * @param abstract Abstract text for the project
	 * @param nsfDivision NSF Division that funded this project
	 * @param startDate Date the project started
	 * @param endDate Date the project ends
	 * @param awardID NSF award ID
	 * @param piFirstName First Name of the Principal Investigator
	 * @param piLastName Last Name of the Principal Investigator
	 * @param affiliation Affiliation of the Principal Investigator
	 * @param programName NSF Program that funded this project
	 * @param programCode Code for the NSF Program that funded this project
	 * @param title Title of the NSF project 
     * @param index index to use in the RDF ID
	 */
	public void project (String abstrct, String nsfDivision, String startDate, String endDate,
			String awardID, String piFirstName, String piLastName, String email, String affiliation, 
			String programName, String programCode, String title, int index) { 
		this.createID(index); 
		_title = title;
		_abstract = abstrct;
	    _nsfDivision = nsfDivision;
	    _startDate = startDate;
	    _endDate = endDate;
	    _awardID = awardID;
	    _piFirstName = piFirstName;
	    _piLastName = piLastName;
	    _email = email;
	    _affiliation = affiliation;
	    _programName = programName;
	    _programCode = programCode;
	}
	
	/**
	 * Method to create an id for an existing NSF Project
	 * @param index an integer index to use in the id
	 */
	private void createID (int index) { _id = Namespaces.essi + "NSF_Project_" + index; }
	
	/**
	 * Method to set an id for an existing project
	 * @param id a string id to use
	 */
	public void setID(String id) { _id = id; }
	
	/**
	 * Method to set an id for an program that funded this project
	 * @param id a string id to use
	 */
	public void setProgramID(String id) { _programID = id; }
	
	/**
	 * Method to get program id
	 * @return programID program id
	 */
	public String getProgramID() { return _programID; }
	
	/**
	 * Method to set an id for principal investigator
	 * @param id a string id to use
	 */
	public void setPIid(String id) { _piID = id; }
	
	/**
	 * Method to get pi id
	 * @return piID pi ID
	 */
	public String getPIid() { return _piID; }
	
    /**
	 * Method to get the project abstract
	 * @return abstract abstract text
	 */
	public String getAbstract() { return _abstract; }
	
	/**
	 * Method to get NSF Division
	 * @return division NSF Division
	 */
	public String getDivision() { return _nsfDivision; }
	
	/**
	 * Method to get start date of the project
	 * @return startDate the start date of the project
	 */
	public String getStartDate() { return _startDate; }
	
	/**
	 * Method to get end date of the project
	 * @return endDate the end date of the project
	 */
	public String getEndDate() { return _endDate; }
	
	/**
	 * Method to get NSF award ID
	 * @return id the NSF award ID
	 */
	public String getAwardID() { return _awardID; }
	    
    /**
	 * Method to get PI first name
	 * @return piFirstName first name of the PI
	 */
	public String getPIfirstName() { return _piFirstName; }
	
	/**
	 * Method to get PI last name
	 * @return piLastName last name of the PI
	 */
	public String getPIlastName() { return _piLastName; }
	
	/**
	 * Method to get affiliation of the PI
	 * @return affiliation affiliation of the PI
	 */
	public String getAffiliation() { return _affiliation; }
	
	/**
	 * Method to get NSF Program Code
	 * @return programCode NSF Program Code
	 */
	public String getProgramCode() { return _programCode; }
	
	/**
	 * Method to get NSF Program Name
	 * @return programName NSF Program Name
	 */
	public String getProgramName() { return _programName; }

	/**
	 * Method to get the id of this project
	 * @return id the id of the NSF Project
	 */
	public String getID() { return _id; }
	
	/**
	 * Method to get the email address of the PI
	 * @return email email address of the PI
	 */
	public String getEmail() { return _email; }
	
	/**
	 * Method to get title of NSF project
	 * @return title the title of the NSF project
	 */
	public String getTitle() { return _title; }
	
	/**
	 * Equality method for NSF project
	 * @return true if awardIDs are equivalent, false otherwise
	 */
	public boolean exists( Vector <NsfProject> sources ) {

	  boolean result = false;
	  for ( int i=0; i<sources.size(); i++ ) {
		result = false;
	    NsfProject p = sources.get(i);
	    if ( p.getAwardID().equals(this._awardID) ) { 
		  result = true;
		  break;
	    } 
	  } // end for
	  return result;  
	
	}
}
