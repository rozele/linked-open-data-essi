package org.agu.essi.abstracts;

import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Author;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.match.EntityMatcher;

/**
 * Extension of Abstract for a basic constructor
 * @author Eric Rozell
 */
public class DefaultAbstract extends Abstract 
{
	// containers for abstract info
	private String _rawHtml;
	private String _hour;
	private String _sessionId;
	private String _abstractId;
	private String _title;
	private String _abstract;
	private Vector<Author> _authors;
	private Vector<Keyword> _keywords;
	private String _sectionId;
	private String _meetingId;
	private Meeting _meeting;
	private Section _section;
	private Session _session;
	private AbstractType _type;	
	
	/**
	 * @constructor
	 */
	public DefaultAbstract()
	{
		_authors = new Vector<Author>();
		_keywords = new Vector<Keyword>();
	}
	
	/**
	 * @constructor
	 * @param title
	 * @param abstr
	 * @param id
	 * @param hour
	 * @param session
	 * @param authors
	 * @param keywords
	 */
	public DefaultAbstract(String title, String abstr, String id, String hour, Session session, Vector<Author> authors, Vector<Keyword> keywords)
	{
		_title = title;
		_abstract = abstr;
		_abstractId = id;
		_hour = hour;
		_session = session;
		_section = session.getSection();
		_meeting = _section.getMeeting();
		_authors = authors;
		_keywords = keywords;
	}
	
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
