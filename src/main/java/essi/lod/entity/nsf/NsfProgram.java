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
 * Container class for NSF Programs
 * @author Tom Narock
 */
public class NsfProgram {
	
	private String _programName = "";
    private String _programCode = "";
    private String _id = "";
    
	/**
	 * Construct NSF Program
	 * @param programName NSF Program name
	 * @param programCode NSF Program code
	 * @param index an integer index to use in the id
	 */
	public void program (String programName, String programCode, int index) {
		_programName = programName;
		_programCode = programCode;
		this.createID(index);
	}
	
	/**
	 * Method to create an id for an existing NSF Program
	 * @param index an integer index to use in the id
	 */
	private void createID (int index) { _id = Namespaces.essi + "NSF_Program_" + index; }
	
	/**
	 * Method to set an id for an existing person
	 * @param id a string id to use
	 */
	public void setID(String id) { _id = id; }
	
	/**
	 * Method to get an id for the NSF Division
	 * @return id the id of the NSF Division
	 */
	public String getID() { return _id; }
	
	/**
	 * Method to get NSF Program name
	 * @return name the NSF Program name
	 */
	public String getProgramName() { return _programName; }
	
	/**
	 * Method to get NSF Program code
	 * @return code the NSF Program code
	 */
	public String getProgramCode() { return _programCode; }
	
	/**
	 * Equality method for NSF Project
	 * @return true if names are equivalent, false otherwise
	 */
	public boolean exists( Vector <NsfProgram> sources ) {

	  boolean result = false;
	  for ( int i=0; i<sources.size(); i++ ) {
		result = false;
	    NsfProgram s = sources.get(i);
	    if ( s.getProgramCode().equals(this._programCode) ) { 
		  result = true;
		  break;
	    } 
	  } // end for
	  return result;  
	
	}
}
