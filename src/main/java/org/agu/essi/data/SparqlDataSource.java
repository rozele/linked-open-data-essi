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
