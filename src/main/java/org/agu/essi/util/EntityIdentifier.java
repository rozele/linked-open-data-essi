package org.agu.essi.util;

import java.util.Vector;

import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Person;
import org.agu.essi.Session;

public class EntityIdentifier {
	
	private static String meetingBaseId = Namespaces.esip + "Meeting_";
	private static String personBaseId = Namespaces.esip + "Person_";
	private static String organizationBaseId = Namespaces.esip + "Organization_";
	private static String sessionBaseId = Namespaces.esip + "Session_";
	private static String abstractBaseId = Namespaces.esip + "Abstract_";
	private static String keywordBaseId = Namespaces.esip + "Keyword_";
	private static Vector<Person> people = new Vector<Person>();
	private static Vector<Meeting> meetings = new Vector<Meeting>();
	private static Vector<String> organizations = new Vector<String>();
	private static Vector<Session> sessions = new Vector<Session>();
	private static Vector<Keyword> keywords = new Vector<Keyword>();
	private static Integer abstractCount = 0;
	
	public static String getMeetingId(Meeting meeting)
	{
		if (meetings.contains(meeting))
		{
			int idx = meetings.indexOf(meeting);
			return meetingBaseId + (idx+1);
		}
		else
		{
			meetings.add(meeting);
			return meetingBaseId + meetings.size();
		}
	}
	
	public static String getNextAbstractId()
	{
		return abstractBaseId + (++abstractCount);
	}
	
	public static String getPersonId(Person person)
	{
		if (people.contains(person))
		{
			int idx = people.indexOf(person);
			return personBaseId + (idx+1);
		}
		else
		{
			people.add(person);
			return personBaseId + people.size();
		}
	}
	
	public static String getSessionId(Session session)
	{
		if (sessions.contains(session))
		{
			int idx = sessions.indexOf(session);
			return sessionBaseId + (idx+1);
		}
		else
		{
			sessions.add(session);
			return sessionBaseId + sessions.size();
		}
	}
	
	public static String getOrganizationId(String org)
	{
		if (organizations.contains(org))
		{
			int idx = organizations.indexOf(org);
			return organizationBaseId + (idx+1);
		}
		else
		{
			organizations.add(org);
			return organizationBaseId + organizations.size();
		}
	}
	
	public static String getKeywordId(Keyword keyword)
	{
		if (keywords.contains(keyword))
		{
			int idx = keywords.indexOf(keyword);
			return keywordBaseId + (idx+1);
		}
		else
		{
			keywords.add(keyword);
			return keywordBaseId + keywords.size();
		}
	}
}
