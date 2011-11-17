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
package org.esipfed.owl;

import java.util.HashMap;
import java.util.Vector;

import org.agu.essi.util.Namespaces;
import org.esipfed.Person;

public class TWC {
	
	/**
	 * Method to search through a set of Person objects and 
	 * group people by the meetings they attended 
	 * @param people Vector of Person objects
	 * @return HashMap <String, Vector <String>> where key/value is Meeting/attendees
	 */
	public HashMap <String, Vector <String>> createMeetingMap( Vector <Person> people ) {

		Vector <String> peopleAtMeeting;
		Vector <String> meetingsAttendedByThisPerson;
		HashMap <String, Vector <String>> meetings = new HashMap <String, Vector <String>> ();
		for ( int i=0; i<people.size(); i++ ) {
		  meetingsAttendedByThisPerson = people.get(i).getMeetings();
		  for ( int j=0; j<meetingsAttendedByThisPerson.size(); j++ ) { 
			  
			// if the meeting already exists in the hash map then just add the person 
			// else create a new key/value entry in the hash map
	 	    if ( meetings.containsKey(meetingsAttendedByThisPerson.get(j)) ) { 
	 	      peopleAtMeeting = meetings.get(meetingsAttendedByThisPerson.get(j));
	 	    } else { 
	 	      peopleAtMeeting = new Vector <String> ();
	 	    }
	 	    peopleAtMeeting.add( people.get(i).getID() );
	 	    meetings.put( meetingsAttendedByThisPerson.get(j), peopleAtMeeting );
	 	    
		  } // end for
		} // end for
		
		return meetings;
		
	}	
	
	/**
	 * Method to write TWC Meeting
	 * @param HashMap meetings/attendees hash map
	 * @return String TWC Meeting 
	 */
	public String writeMeetings ( HashMap <String, Vector <String>> meetings ) { 
		
		Vector <String> people;
		StringBuilder str = new StringBuilder();

		int index = 1;
		for (String key : meetings.keySet()) {
			  
			str.append( "  <rdf:Description rdf:about=\"" + Namespaces.essi + "ESIP_Meeting_" + index + "\"> \n" );	  
		    str.append( "    <rdf:type rdf:resource=\"&tw;Meeting\" /> \n" );
		    str.append( "    <tw:Meeting rdf:datatype=\"&xsd;string\">" + key + "</tw:Meeting> \n" );
		    people = meetings.get(key);
		    for ( int i=0; i<people.size(); i++ ) { 
			  str.append( "    <tw:hasAttendee rdf:resource=\"" + people.get(i) + "\" />\n" );
		    }
		    str.append( "  </rdf:Description> \n" );
			
		    index++;
					    
		} // end for
		  
	    return str.toString();
	
	}
	
}