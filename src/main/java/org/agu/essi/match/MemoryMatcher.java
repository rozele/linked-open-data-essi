package org.agu.essi.match;

import java.util.Vector;

import org.agu.essi.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.Namespaces;

public class MemoryMatcher implements Matcher 
{
	//Stores unique identifier bases
	private static String meetingBaseId = Namespaces.esip + "Meeting_";
	private static String sectionBaseId = Namespaces.esip + "Section_";	
	private static String personBaseId = Namespaces.esip + "Person_";
	private static String organizationBaseId = Namespaces.esip + "Organization_";
	private static String sessionBaseId = Namespaces.esip + "Session_";
	private static String abstractBaseId = Namespaces.esip + "Abstract_";
	private static String keywordBaseId = Namespaces.esip + "Keyword_";
	
	//index registers for objects
	private Vector<Person> people;
	private Vector<Organization> organizations;
	private Vector<Keyword> keywords;
	
	//start index for URIs
	private int peopleStartIdx;
	private int organizationsStartIdx;
	private int keywordsStartIdx;
	
	public MemoryMatcher()
	{
		people = new Vector<Person>();
		organizations = new Vector<Organization>();
		keywords = new Vector<Keyword>();
		organizationsStartIdx = 0;
		keywordsStartIdx = 0;
		peopleStartIdx = 0;
	}
	
	/**
	 * Gets an existing identifier for a meeting, if available, otherwise creates a new identifier
	 * @param meeting a Meeting instance
	 * @return a new or existing identifier for the input meeting
	 */
	public String getMeetingId(Meeting meeting)
	{	
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JM_" : "FM_";
		return meetingBaseId + mstr + year; 
	}

	/**
	 * Gets an existing identifier for a meeting section, if available, otherwise creates a new identifier
	 * @param section a Section instance
	 * @return a new or existing identifier for the input section
	 */
	public String getSectionId(Section section)
	{
		Meeting meeting = section.getMeeting();
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JM_" : "FM_";
		return sectionBaseId + mstr + year + "_" + section.getId(); 
	}	
	
	/**
	 * Gets an existing identifier for an abstract, if available, otherwise creates a new identifier
	 * @param abstr an Abstract instance
	 * @return a new or existing identifier for the input abstract
	 */
	//this is a hack
	public String getAbstractId(Abstract abstr)
	{
		Meeting meeting = abstr.getMeeting();
		String id = abstr.getId();
		
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JM_" : "FM_";
		return abstractBaseId + mstr + year + "_" + id; 
	}
	
	/**
	 * Gets an existing identifier for a person, if available, otherwise creates a new identifier
	 * @param person a Person instance
	 * @return a new or existing identifier for the input person
	 */
	public String getPersonId(Person person)
	{
		if (people.contains(person))
		{
			int idx = people.indexOf(person);
			return personBaseId + (peopleStartIdx + idx + 1);
		}
		else
		{
			people.add(person);
			return personBaseId + (peopleStartIdx + people.size());
		}
	}
	
	/**
	 * Gets an existing identifier for a meeting session, if available, otherwise creates a new identifier
	 * @param session a Session instance
	 * @return a new or existing identifier for the input session
	 */
	public String getSessionId(Session session)
	{
		Meeting meeting = session.getSection().getMeeting();
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JM_" : "FM_";
		return sessionBaseId + mstr + year + "_" + session.getId(); 
	}
	
	/**
	 * Gets an existing identifier for an organization, if available, otherwise creates a new identifier
	 * @param org a plain text description of an organization
	 * @return a new or existing identifier for the input organization
	 */
	public String getOrganizationId(Organization org)
	{
		if (organizations.contains(org))
		{
			int idx = organizations.indexOf(org);
			return organizationBaseId + (organizationsStartIdx + idx + 1);
		}
		else
		{
			organizations.add(org);
			return organizationBaseId + (organizationsStartIdx + organizations.size());
		}
	}
	
	/**
	 * Gets an existing identifier for a keyword, if available, otherwise creates a new identifier
	 * @param keyword a Keyword instance
	 * @return a new or existing identifier for the input keyword
	 */
	public String getKeywordId(Keyword keyword)
	{
		if (keywords.contains(keyword))
		{
			int idx = keywords.indexOf(keyword);
			return keywordBaseId + (keywordsStartIdx + idx + 1);
		}
		else
		{
			keywords.add(keyword);
			return keywordBaseId + (keywordsStartIdx + keywords.size());
		}
	}
	
	public void setPeopleStartIndex(int idx)
	{
		peopleStartIdx = idx;
	}
	
	public void setKeywordsStartIndex(int idx)
	{
		keywordsStartIdx = idx;
	}
	
	public void setOrganizationsStartIndex(int idx)
	{
		organizationsStartIdx = idx;
	}
}
