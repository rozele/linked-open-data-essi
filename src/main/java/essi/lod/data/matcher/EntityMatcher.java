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
package essi.lod.data.matcher;


import essi.lod.abstracts.Abstract;
import essi.lod.entity.agu.Keyword;
import essi.lod.entity.agu.Meeting;
import essi.lod.entity.agu.Organization;
import essi.lod.entity.agu.Person;
import essi.lod.entity.agu.Section;
import essi.lod.entity.agu.Session;

/**
 * A Matcher interface which should be implemented for individual data sources
 * @author Eric Rozell and Tom Narock
 */
public interface EntityMatcher 
{
	
	/**
	 * Method to get unique ID of a meeting
	 * @param m a Meeting
	 * @return a unique identifier for the meeting
	 */
	public String getMeetingId(Meeting m);
	
	/**
	 * Method to get unique ID of a section
	 * @param s a Section
	 * @return a unique identifier for the section
	 */
	public String getSectionId(Section s);
	
	/**
	 * Method to get unique ID of a session
	 * @param s a Session
	 * @return a unique identifier for the session
	 */
	public String getSessionId(Session s);
	
	/**
	 * Method to get unique ID of a person
	 * @param p a Person
	 * @return a unique identifier for the person
	 */
	public String getPersonId(Person p);
	
	/**
	 * Method to get unique ID of an organization
	 * @param o an Organization
	 * @return a unique identifier for the organization
	 */
	public String getOrganizationId(Organization o);
	
	/**
	 * Method to get unique ID of a keyword
	 * @param k a keyword
	 * @return a unique identifier for the keyword
	 */
	public String getKeywordId(Keyword k);
	
	/**
	 * Method to get unique ID of an abstract
	 * @param a an abstract
	 * @return a unique identifier for the abstract
	 */
	public String getAbstractId(Abstract a);
	
	/**
	 * Method to output information about new Person instances
	 * @param format output format
	 * @return
	 */
	public String writeNewPeople(String format);

	/**
	 * Method to output information about new Session instances
	 * @param format output format
	 * @return
	 */
	public String writeNewSessions(String format);

	/**
	 * Method to output information about new Section instances
	 * @param format output format
	 * @return
	 */
	public String writeNewSections(String format);

	/**
	 * Method to output information about new Meeting instances
	 * @param format output format
	 * @return
	 */
	public String writeNewMeetings(String format);

	/**
	 * Method to output information about new Keyword instances
	 * @param format output format
	 * @return
	 */
	public String writeNewKeywords(String format);
	
	/**
	 * Method to output information about new Organization instances
	 * @param format output format
	 * @return
	 */
	public String writeNewOrganizations(String format);
	
	
}
