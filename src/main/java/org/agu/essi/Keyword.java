package org.agu.essi;

import java.util.Vector;
import java.util.regex.*;

/**
 * Container class for AGU keywords
 * @author Eric Rozell
 */
public class Keyword {
	private String _name;
	private String _id;
	private Vector<String> _altIds;
	
	/**
	 * Constructs keyword from raw abstract HTML content
	 * @param name keyword content of AGU abstract HTML
	 */
	public Keyword(String name)
	{
		_altIds = new Vector<String>();
		Pattern p1 = Pattern.compile("(\\[(\\d+)\\])?\\s*(.*?)(\\s*\\((.*)\\))");
		Pattern p2 = Pattern.compile("(\\[(\\d+)\\])?\\s*(.*)");
		
		//check first pattern
		Matcher m = p1.matcher(name);
		boolean matched = m.find();
		if (matched)
		{
			_id = m.group(2);
			_name = m.group(3);
			String[] list = m.group(5).split(",");
			for (int i = 0; i < list.length; ++i)
			{
				_altIds.add(list[i].trim());
			}
			if (_id == null && _altIds.size() > 0)
			{
				_id = _altIds.get(0);
			}
			else if (_id == null)
			{
				_id = _name;
			}
		}
		else
		{	
			//check second pattern
			m = p2.matcher(name);
			matched = m.find();
			if (matched)
			{
				_id = m.group(2);
				_name = m.group(3);
				if (_id == null)
				{
					_id = _name;
				}
			}
			else
			{
				_name = name;
				_id = name;
			}
		}
	}
	
	/**
	 * Method to get the keyword number
	 * TODO: make a good regex to take care of this...
	 * @return number of keyword
	 */
	public String getId()
	{
		return _id;
	}
	
	/**
	 * Method to get the keyword name
	 * @return name of keyword
	 */
	public String getName()
	{
		return _name;
		
	}
	
	/**
	 * Equality method for Keywords
	 * @return true if name and number match, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Keyword && ((Keyword)o).getName().equals(this.getName())
				&& ((Keyword)o).getId().equals(this.getId()))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Method to stringify Keyword
	 * @return original abstract HTML keyword content
	 */
	@Override
	public String toString()
	{
		return _name;
	}
	
	/**
	 * A stringify method that enables multiple output formats
	 * @param format a specification for output format (e.g., "xml", "rdf/xml", etc.)
	 * @return stringified Keyword in specified format
	 */
	public String toString(String format)
	{
		if (format.equals("xml"))
		{
			return "<Keyword>" + _name + "</Keyword>";
		}
		else
		{
			return this.toString();
		}
	}
}
