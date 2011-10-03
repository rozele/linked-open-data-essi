package org.agu.essi.abstracts;

import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Author;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.Utils;

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
	
	public void lazyLoader()
	{
		
	}
}
