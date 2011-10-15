package org.agu.essi;

import java.util.Vector;

/**
 * Container class for AGU Session information
 * @author Eric Rozell
 */
public class Session 
{
	private String _name;
	private String _id;
	private String _location;
	private String _conveners;
	private Vector<Convener> _convenerList;
	private Section _section;
	
	/**
	 * Constructor for Session from session ID and section
	 * @param id session ID
	 * @param section AGU section the session occurs in (e.g, ESSI)
	 */
	public Session(String id, Section section)
	{
		_id = id;
		_section = section;
	}	
	
	/**
	 * Constructor for Session from session name, ID, and section
	 * @param name name of the session
	 * @param id session ID
	 * @param section AGU section the session occurs in (e.g., ESSI)
	 */
	public Session(String name, String id, String location, String conveners, Section section)
	{
		_name = name;
		_id = id;
		_location = location;
		_conveners = conveners;
		_section = section;
		String[] list = _conveners.split(";");
		_convenerList = new Vector<Convener>();
		for (int i = 0; i < list.length; ++i)
		{
			_convenerList.add(new Convener(list[i].trim()));
		}
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
	 * Method to get session name
	 * @return session name
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method to get session location
	 * @return session location
	 */
	public String getLocation()
	{
		return _location;
	}	
	
	/**
	 * Method to get session conveners
	 * @return session conveners
	 */
	public Vector<Convener> getConveners()
	{
		return _convenerList;
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
