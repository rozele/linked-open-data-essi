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
package org.agu.essi.data;

import java.util.Collection;
import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.abstracts.SparqlAbstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.match.SparqlMatcher;
import org.agu.essi.util.Queries;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.InvalidAbstractConstraintException;
import org.agu.essi.util.exception.SourceNotReadyException;
import org.agu.essi.data.source.DataSource;
import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Class for retrieving abstracts from a SPARQL endpoint
 * @author Eric Rozell
 */
public class SparqlAbstractDataSource implements DataSource 
{
	
	private EntityMatcher matcher;
	private String endpoint;
	
	static final Logger log = Logger.getLogger(org.agu.essi.data.SparqlAbstractDataSource.class);  
	
	/**
	 * Constructor for the SparqlDataSource
	 * @param ep URL of the SPARQL endpoint
	 */
	public SparqlAbstractDataSource(String ep)
	{
		matcher = new SparqlMatcher(ep);
		endpoint = ep;
	}
	
	/**
	 * Retrieves all abstracts in the endpoint
	 * @throws InvalidAbstractConstraintException 
	 */
	public Vector<Abstract> getAbstracts() throws SourceNotReadyException, InvalidAbstractConstraintException 
	{
		return getAbstractsWithConstraints(null, false);	
	}
	
	/**
	 * Retrieve all abstracts meeting one of each type of constraint in the endpoint
	 */
	@SuppressWarnings("rawtypes")
	public Vector<Abstract> getAbstractsWithConstraints(Collection c, boolean conjunctive) throws SourceNotReadyException, InvalidAbstractConstraintException
	{
		String query = Queries.abstractsQuery(c, conjunctive);
		ResultSet results = Utils.sparqlSelect(query, endpoint);
		Vector<String> abstractIds = new Vector<String>();
		Vector<Abstract> abstracts = new Vector<Abstract>();
		while (results.hasNext())
		{
			QuerySolution qs = results.next();
			abstractIds.add(qs.get("abstract").toString());
		}
		for (int i = 0; i < abstractIds.size(); ++i)
		{
			Abstract abs = new SparqlAbstract(abstractIds.get(i), endpoint);
			abs.setEntityMatcher(matcher);
			abstracts.add(abs);
		}
		return abstracts;
	}
	
	public boolean ready() 
	{
		return true;
	}

	public void setEntityMatcher(EntityMatcher m) 
	{
		matcher = m;
	}

	public EntityMatcher getEntityMatcher() 
	{
		return matcher;
	}

	public static void main(String[] args)
	{
		SparqlAbstractDataSource data = new SparqlAbstractDataSource("http://aquarius.tw.rpi.edu:2025/sparql");
		try 
		{
			Vector<Abstract> abstracts = data.getAbstracts();
			for (int i = 0; i < abstracts.size(); ++i)
			{
				System.out.println(abstracts.get(i).toString("rdf/xml"));
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
}
