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

import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.exception.SourceNotReadyException;

/**
 * Class for retrieving abstracts from a SPARQL endpoint
 * @author Eric Rozell
 */
public class SparqlDataSource implements DataSource 
{
	private EntityMatcher matcher;
	private String endpoint;
	
	/**
	 * Constructor for the SparqlDataSource
	 * @param ep URL of the SPARQL endpoint
	 */
	public SparqlDataSource(String ep)
	{
		endpoint = ep;
	}
	
	/**
	 * Retrieves all abstracts in the endpoint
	 */
	public Vector<Abstract> getAbstracts() throws SourceNotReadyException 
	{
		// TODO Auto-generated method stub
		return null;
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

}
