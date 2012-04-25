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
package essi.lod.entity.esip;

import java.util.Vector;

/**
 * Container class for ESIP cluster information
 * @author Tom Narock
 */
public class Cluster {
	
	private String _name;
    private String _id;
    
	/**
	 * Construct Cluster instance from name and id
	 * @param name name of the Cluster
	 * @param id id of the Cluster
	 */
	public Cluster( String name, String id ) {
		_name = name;
		_id = id;
	}
	
	/**
	 * Method to get an id for an existing cluster
	 * @return id the id of the cluster
	 */
	public String getID() { return _id; }
	
	/**
	 * Method to add name of an existing cluster
	 * @return name the name of the cluster
	 */
	public String getName() { return _name; }
	
	/**
	 * Equality method for Cluster
	 * @return true if names are equivalent, false otherwise
	 */
	public boolean exists( Vector <Cluster> clusters ) {

	  boolean result = false;
	  for ( int i=0; i<clusters.size(); i++ ) {
		result = false;
	    Cluster c = clusters.get(i);
	    if ( c.getName().equals(this._name) ) { 
		  result = true;
		  break;
	    } 
	  } // end for
	  return result;  
	
	}
}
