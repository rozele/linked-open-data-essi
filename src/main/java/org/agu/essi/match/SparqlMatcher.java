package org.agu.essi.match;

import org.agu.essi.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.MeetingType;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.Namespaces;
import org.agu.essi.util.Queries;
import org.agu.essi.util.Utils;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SparqlMatcher implements EntityMatcher 
{
	//Stores unique identifier bases
	private static String meetingBaseId = Namespaces.esip + "Meeting_";
	private static String sectionBaseId = Namespaces.esip + "Section_";	
	private static String sessionBaseId = Namespaces.esip + "Session_";
	private static String abstractBaseId = Namespaces.esip + "Abstract_";
	private static String keywordBaseId = Namespaces.esip + "Keyword_";
	
	private MemoryMatcher newMatches;
	private String endpoint;
	
	public SparqlMatcher(String ep)
	{
		newMatches = new MemoryMatcher();
		endpoint = ep;
		setStartIndices();
	}
	
	public String getMeetingId(Meeting meeting)
	{
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = meetingBaseId + mt + ((year > 0) ? "_" + year : "");
		if (!Utils.sparqlAsk(Queries.askMeetingQuery.replaceAll("$uri", id), endpoint))
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
		if (!Utils.sparqlAsk(Queries.askSectionQuery.replaceAll("$uri", id), endpoint))
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
		if (!Utils.sparqlAsk(Queries.askSessionQuery.replaceAll("$uri", id), endpoint))
		{
			newMatches.getSessionId(session);
		}
		return id;
	}

	public String getPersonId(Person person)
	{
		ResultSet personResults = Utils.sparqlSelect(Queries.selectPersonQuery.replaceAll("$email", person.getEmail()), endpoint);
		String id = null;
		if (personResults.hasNext())
		{
			QuerySolution solution = personResults.next();
			RDFNode node = solution.get("id");
			id = node.toString();
		}
		else
		{
			id = newMatches.getPersonId(person);
		}
		return id;
	}

	public String getOrganizationId(Organization organization)
	{
		ResultSet organizationResults = Utils.sparqlSelect(Queries.selectOrganizationQuery.replaceAll("$description",organization.toString()), endpoint);
		String id = null;
		if (organizationResults.hasNext())
		{
			QuerySolution solution = organizationResults.next();
			RDFNode node = solution.get("id");
			id = node.toString();
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
		if (!Utils.sparqlAsk(Queries.askKeywordQuery.replaceAll("$uri", id), endpoint))
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
	
	private void setStartIndices()
	{
		ResultSet peopleResults = Utils.sparqlSelect(Queries.countPeopleQuery, endpoint);
		if (peopleResults.hasNext())
		{
			QuerySolution solution = peopleResults.next();
			RDFNode node = solution.get("count");
			newMatches.setPeopleStartIndex(Integer.parseInt(node.toString()));
		}
		ResultSet organizationsResults = Utils.sparqlSelect(Queries.countOrganizationsQuery, endpoint);
		if (organizationsResults.hasNext())
		{
			QuerySolution solution = organizationsResults.next();
			RDFNode node = solution.get("count");
			newMatches.setOrganizationsStartIndex(Integer.parseInt(node.toString()));
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
