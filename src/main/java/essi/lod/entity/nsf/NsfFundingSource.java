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
 * Container class for NSF Funding Source
 * @author Tom Narock
 */
public class NsfFundingSource {
	
	private String _fundingID;
    private String _nsfDivision;
    private Vector <String> _projectIDs = new Vector <String> ();
    
	/**
	 * Construct NSF Funding Source instance from NSF Division name
	 * @param nsfDivision a division within NSF 
	 * @param index an integer index to use in the id
	 */
	public void fundingSource (String nsfDivision, int index) {
		_nsfDivision = nsfDivision;
		this.createID(index);
	}
	
	/**
	 * Method to create an id for an existing NSF Funding Source
	 * @param index an integer index to use in the id
	 */
	private void createID (int index) { _fundingID = Namespaces.aws + "NSF/FundingSource/" + index; }
	
	/**
	 * Method to set an id for an existing person
	 * @param id a string id to use
	 */
	public void setID(String id) { _fundingID = id; }
	
	/**
	 * Method to get an id for the NSF Division
	 * @return id the id of the NSF Division
	 */
	public String getID() { return _fundingID; }
	
	/**
	 * Method to get the name of the NSF Division
	 * @return name the name of the NSF Division
	 */
	public String getDivisionName() { return _nsfDivision; }
	
	/**
	 * Method to add a project ID to an existing NSF Funding Source
	 * @param projectID rdf ID of a project supported by this NSF Division
	 */
	public void addProject(String projectID) { _projectIDs.add( projectID ); }
	
	/**
	 * Method to get projects affiliated with this NSF Division
	 * @return affiliations of this NSF Division
	 */
	public Vector <String> getProjects() { return _projectIDs; }
	
	/**
	 * Method to test for the existence of a project
	 * @return true/false
	 */
	public boolean hasProject( String value ) { 
	  if ( this._projectIDs.contains(value) ) { return true; } else { return false; }	
	}
	
	/**
	 * Equality method for NSF Funding Source
	 * @return true if names are equivalent, false otherwise
	 */
	public boolean exists ( Vector <NsfFundingSource> sources, String projectID ) {

	  boolean result = false;
	  for ( int i=0; i<sources.size(); i++ ) {
		result = false;
	    NsfFundingSource s = sources.get(i);
	    if ( s.getDivisionName().equals(this._nsfDivision) ) { 
		  result = true;
		  s.addProject(projectID);
		  sources.setElementAt(s, i);
		  break;
	    } 
	  } // end for
	  return result;  
	
	}
}
