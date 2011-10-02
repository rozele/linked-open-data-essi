package org.agu.essi.abstracts;

import java.io.StringWriter;
import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Author;
import org.agu.essi.Meeting;
import org.agu.essi.Keyword;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

/**
 * Abstract class for AGU Abstract implementations (sorry)
 * @author Eric Rozell
 */
public abstract class Abstract 
{	
	private EntityMatcher matcher;
	
	/**
	 * Abstract Methods
	 */
	
	public abstract String getTitle();
	public abstract Meeting getMeeting();
	public abstract String getId();
	public abstract Session getSession();
	public abstract String getAbstract();
	public abstract AbstractType getAbstractType();
	public abstract Vector<Keyword> getKeywords();
	public abstract Vector<Author> getAuthors();
	
	public void setEntityMatcher(EntityMatcher m)
	{
		matcher = m;
	}
	
	public EntityMatcher getEntityMatcher()
	{
		return matcher;
	}
	
	public String toString(String format) throws EntityMatcherRequiredException
	{
		StringWriter sw = new StringWriter();
		
		return sw.toString();
	}
}
