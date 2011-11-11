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
package org.agu.essi.abstracts;

import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Author;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.Queries;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Extension of Abstract for handling AGU abstracts in a SPARQL endpoint
 * @author Eric Rozell
 *
 */
public class SparqlAbstract extends Abstract 
{
	private String _uri;
	private String _endpoint;
	private Vector<Keyword> _keywords;
	private Vector<Author> _authors;
	private String _abstract;
	private Section _section;
	private Session _session;
	private String _abstractId;
	private Meeting _meeting;
	private String _title;

	public SparqlAbstract(String uri, String ep)
	{
		_uri = uri;
		_endpoint = ep;
	}
	
	@Override
	public String getTitle() 
	{
		lazyLoader();
		return _title;
	}

	@Override
	public Meeting getMeeting() 
	{
		lazyLoader();
		return _meeting;
	}

	@Override
	public String getId() 
	{
		lazyLoader();
		return _abstractId;
	}

	@Override
	public Session getSession() 
	{
		lazyLoader();
		return _session;
	}

	@Override
	public Section getSection() 
	{
		lazyLoader();
		return _section;
	}

	@Override
	public String getAbstract() 
	{
		lazyLoader();
		return _abstract;
	}

	@Override
	public AbstractType getAbstractType() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Keyword> getKeywords() 
	{
		lazyLoader();
		return _keywords;
	}

	@Override
	public Vector<Author> getAuthors() 
	{
		lazyLoader();
		return _authors;
	}

	@Override
	public String getHour() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString()
	{
		lazyLoader();
		return super.toString();
	}
	
	@Override
	public String toString(String format) throws EntityMatcherRequiredException
	{
		lazyLoader();
		return super.toString(format);
	}
	
	public void lazyLoader()
	{
		String query = Queries.abstractInfoQuery(_uri);
		ResultSet rs = Utils.sparqlSelect(query, _endpoint);
		if (rs.hasNext())
		{
			QuerySolution qs = rs.next();
		}
		else
		{
			//TODO: error if no abstract info available
		}
		query = Queries.abstractKeywordQuery(_uri);
		rs = Utils.sparqlSelect(query, _endpoint);
		while (rs.hasNext())
		{
			//TODO: add keywords
		}
		query = Queries.abstractAuthorQuery(_uri);
		rs = Utils.sparqlSelect(query, _endpoint);
		while (rs.hasNext())
		{
			//TODO: add authors
		}
		
		
	}
}
