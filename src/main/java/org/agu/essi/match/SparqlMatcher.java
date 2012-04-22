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
package org.agu.essi.match;

import java.util.HashMap;
import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.types.MeetingType;
import org.agu.essi.util.Namespaces;
import org.agu.essi.util.Queries;
import org.agu.essi.util.Utils;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SparqlMatcher implements EntityMatcher 
{
	//Stores unique identifier bases
	private static String meetingBaseId = Namespaces.essi + "Meeting_";
	private static String sectionBaseId = Namespaces.essi + "Section_";	
	private static String sessionBaseId = Namespaces.essi + "Session_";
	private static String abstractBaseId = Namespaces.essi + "Abstract_";
	private static String keywordBaseId = Namespaces.essi + "Keyword_";
	
	private Vector<String> meetingIds = new Vector<String>();
	private Vector<String> sectionIds = new Vector<String>();
	private Vector<String> sessionIds = new Vector<String>();
	private Vector<String> keywordIds = new Vector<String>();
	private HashMap<String,String> personIds = new HashMap<String,String>();
	private HashMap<String,String> organizationIds = new HashMap<String,String>();
	
	private MemoryMatcher newMatches;
	private String endpoint;
	private String graph;
	
	public SparqlMatcher(String ep, String g)
	{
		newMatches = new MemoryMatcher();
		endpoint = ep;
		graph = g;
		setStartIndices();
		getIds();
	}
	
	public String getMeetingId(Meeting meeting)
	{
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = meetingBaseId + mt + ((year > 0) ? "_" + year : "");
		if (!meetingIds.contains(id))
		{
			newMatches.getMeetingId(meeting);
		}
		return id;
	}

	public String getSectionId(Section section)
	{
		Meeting meeting = section.getMeeting();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = sectionBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + section.getId();
		if (!sectionIds.contains(id))
		{
			newMatches.getSectionId(section);
		}
		return id;
	}

	public String getSessionId(Session session)
	{
		Meeting meeting = session.getSection().getMeeting();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = sessionBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + session.getId();
		if (!sessionIds.contains(id))
		{
			newMatches.getSessionId(session);
		}
		return id;
	}

	public String getPersonId(Person person)
	{
		boolean newId = true;
		String id = null;
		if (person.getEmail() != null)
		{
			if (personIds.containsKey(person.getEmail().toLowerCase()))
			{
				id = personIds.get(person.getEmail().toLowerCase());
				newId = false;
			}
		}
		if (newId)
		{
			id = newMatches.getPersonId(person);
		}
		return id;
	}

	public String getOrganizationId(Organization organization)
	{
		String id = null;
		if (organizationIds.containsKey(organization.toString().toLowerCase()))
		{
			id = organizationIds.get(organization.toString().toLowerCase());
		}
		else
		{
			id = newMatches.getOrganizationId(organization);
		}		
		return id;
	}

	public String getKeywordId(Keyword keyword)
	{
		String id = keywordBaseId + keyword.getId();
		if (!keywordIds.contains(id))
		{
			newMatches.getKeywordId(keyword);
		}
		return id;
	}
	
	public String getAbstractId(Abstract abstr)
	{
		Meeting meeting = abstr.getMeeting();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = abstractBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + abstr.getId();
		return id;
	}

	/**
	 * @deprecated Deprecated method for deprecated class, org.agu.essi.Abstract
	 */
	public String getAbstractId(org.agu.essi.Abstract abstr)
	{
		Meeting meeting = abstr.getMeeting();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = abstractBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + abstr.getId();
		return id;
	}
	
	private void setStartIndices()
	{
		ResultSet peopleResults = Utils.sparqlSelect(Queries.countPeopleQuery(graph), endpoint);
		if (peopleResults.hasNext())
		{
			QuerySolution solution = peopleResults.next();
			RDFNode node = solution.get("count");
			String count = node.toString().split("\\^\\^")[0];
			newMatches.setPeopleStartIndex(Integer.parseInt(count));
		}
		ResultSet organizationsResults = Utils.sparqlSelect(Queries.countOrganizationsQuery(graph), endpoint);
		if (organizationsResults.hasNext())
		{
			QuerySolution solution = organizationsResults.next();
			RDFNode node = solution.get("count");
			String count = node.toString().split("\\^\\^")[0];
			newMatches.setOrganizationsStartIndex(Integer.parseInt(count));
		}
	}
	
	private void getIds()
	{
		getMeetingIds();
		getSectionIds();
		getSessionIds();
		getKeywordIds();
		getPersonIds();
		getOrganizationIds();
	}
	
	private void getMeetingIds()
	{
		ResultSet meetingResults = Utils.sparqlSelect(Queries.meetingsQuery(graph), endpoint);
		while (meetingResults.hasNext())
		{
			QuerySolution solution = meetingResults.next();
			RDFNode node = solution.get("meeting");
			meetingIds.add(node.toString());
		}
	}

	private void getSectionIds()
	{
		ResultSet sectionResults = Utils.sparqlSelect(Queries.sectionsQuery(graph), endpoint);
		while (sectionResults.hasNext())
		{
			QuerySolution solution = sectionResults.next();
			RDFNode node = solution.get("section");
			sectionIds.add(node.toString());
		}
	}
	
	private void getSessionIds()
	{
		ResultSet sessionResults = Utils.sparqlSelect(Queries.sessionsQuery(graph), endpoint);
		while (sessionResults.hasNext())
		{
			QuerySolution solution = sessionResults.next();
			RDFNode node = solution.get("session");
			sessionIds.add(node.toString());
		}
	}
	
	private void getKeywordIds()
	{
		ResultSet keywordResults = Utils.sparqlSelect(Queries.keywordsQuery(graph), endpoint);
		while (keywordResults.hasNext())
		{
			QuerySolution solution = keywordResults.next();
			RDFNode node = solution.get("keyword");
			keywordIds.add(node.toString());
		}
	}
	
	private void getPersonIds()
	{
		ResultSet personResults = Utils.sparqlSelect(Queries.peopleQuery(graph), endpoint);
		while (personResults.hasNext())
		{
			QuerySolution solution = personResults.next();
			RDFNode idNode = solution.get("id");
			RDFNode emailNode = solution.get("email");
			personIds.put(emailNode.toString().toLowerCase(), idNode.toString());
		}
	}

	private void getOrganizationIds()
	{
		ResultSet orgResults = Utils.sparqlSelect(Queries.organizationsQuery(graph), endpoint);
		while (orgResults.hasNext())
		{
			QuerySolution solution = orgResults.next();
			RDFNode idNode = solution.get("id");
			RDFNode descNode = solution.get("description");
			organizationIds.put(descNode.toString().toLowerCase(), idNode.toString());
		}
	}
	
	public String writeNewPeople(String format) 
	{
		return newMatches.writeNewPeople(format);
	}

	public String writeNewSessions(String format) 
	{
		return newMatches.writeNewSessions(format);
	}

	public String writeNewSections(String format) 
	{
		return newMatches.writeNewSections(format);
	}

	public String writeNewMeetings(String format) 
	{
		return newMatches.writeNewMeetings(format);
	}

	public String writeNewKeywords(String format) 
	{
		return newMatches.writeNewKeywords(format);
	}

	public String writeNewOrganizations(String format) 
	{
		return newMatches.writeNewOrganizations(format);
	}
}
