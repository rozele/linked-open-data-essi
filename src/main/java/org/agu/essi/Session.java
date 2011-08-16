package org.agu.essi;

/**
 * Container class for AGU Session information
 * @author Eric Rozell
 */
public class Session {
	private String _id;
	private Section _section;
	
	/**
	 * Constructor for Session from session ID and meeting
	 * @param id session ID
	 * @param meeting meeting the session occurs in
	 */
	public Session(String id, Section section)
	{
		_id = id;
		_section = section;
	}
	
	/**
	 * Method to get session ID
	 * @return session ID
	 */
	public String getId()
	{
		return _id;
	}
	
	/**
	 * Method to get meeting the session occurs in
	 * @return meeting the session occurs in
	 */
	public Section getSection()
	{
		return _section;
	}
	
	/**
	 * Equality method for Session
	 * @param o an Object
	 * @return true if session ID and meeting match, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Session && ((Session)o).getId().equals(this._id) 
			&& ((Session)o).getSection().equals(this._section))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
