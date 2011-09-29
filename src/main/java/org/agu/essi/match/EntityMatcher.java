package org.agu.essi.match;

import org.agu.essi.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;

/**
 * A Matcher interface which should be implemented for individual data sources
 * @author Eric Rozell
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
