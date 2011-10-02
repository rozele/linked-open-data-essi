package org.agu.essi.abstracts;

import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Author;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Session;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

/**
 * Extension of Abstract for handling HTML for AGU abstracts
 * @author Eric Rozell
 */
public class HtmlAbstract extends Abstract 
{
	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Meeting getMeeting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Session getSession() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAbstract() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractType getAbstractType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Keyword> getKeywords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Vector<Author> getAuthors() {
		// TODO Auto-generated method stub
		return null;
	}

}
