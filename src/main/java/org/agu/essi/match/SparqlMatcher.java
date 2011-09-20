package org.agu.essi.match;

import org.agu.essi.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.Queries;
import org.agu.essi.util.Utils;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class SparqlMatcher implements Matcher 
{
	private MemoryMatcher newMatches;
	private String endpoint;
	
	public SparqlMatcher(String ep)
	{
		newMatches = new MemoryMatcher();
		endpoint = ep;
		setStartIndices();
	}
	
	public String getMeetingId(Meeting m)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getSectionId(Section s)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getSessionId(Session s)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getPersonId(Person p)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getOrganizationId(Organization o)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getKeywordId(Keyword k)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public String getAbstractId(Abstract a)
	{
		// TODO Auto-generated method stub
		return null;
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
		ResultSet keywordsResults = Utils.sparqlSelect(Queries.countKeywordsQuery, endpoint);
		if (keywordsResults.hasNext())
		{
			QuerySolution solution = keywordsResults.next();
			RDFNode node = solution.get("count");
			newMatches.setKeywordsStartIndex(Integer.parseInt(node.toString()));
		}
	}

}
