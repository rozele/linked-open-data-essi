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
package essi.lod.data;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import essi.lod.abstracts.Abstract;
import essi.lod.abstracts.SparqlAbstract;
import essi.lod.data.matcher.EntityMatcher;
import essi.lod.data.matcher.SparqlMatcher;
import essi.lod.data.source.DataSource;
import essi.lod.exception.InvalidAbstractConstraintException;
import essi.lod.exception.SourceNotReadyException;
import essi.lod.util.Queries;
import essi.lod.util.Utils;

/**
 * Class for retrieving abstracts from a SPARQL endpoint
 * @author Eric Rozell
 */
public class SparqlAbstractData implements DataSource 
{
	
	private EntityMatcher matcher;
	private String endpoint;
	private String graph;
	
	static final Logger log = Logger.getLogger(essi.lod.data.SparqlAbstractData.class);  
	
	/**
	 * Constructor for the SparqlDataSource
	 * @param ep URL of the SPARQL endpoint
	 */
	public SparqlAbstractData(String ep, String g)
	{
		matcher = new SparqlMatcher(ep, g);
		endpoint = ep;
		graph = g;
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
		String query = Queries.abstractsQuery(c, conjunctive, graph);
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
			Abstract abs = new SparqlAbstract(abstractIds.get(i), endpoint, graph);
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
		SparqlAbstractData data = new SparqlAbstractData("http://aquarius.tw.rpi.edu:2025/sparql", "http://essi-lod.org/instances/");
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
