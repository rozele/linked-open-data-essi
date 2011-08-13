package org.agu.essi;

/**
 * Container class for AGU keywords
 * @author Eric Rozell
 */
public class Keyword {
	private String _name;
	
	/**
	 * Constructs keyword from raw abstract HTML content
	 * @param name keyword content of AGU abstract HTML
	 */
	public Keyword(String name)
	{
		_name = name;
	}
	
	/**
	 * Method to get the keyword number
	 * @return number of keyword
	 */
	public String getId()
	{
		return _name.split("  ")[0];
	}
	
	/**
	 * Method to get the keyword name
	 * @return name of keyword
	 */
	public String getName()
	{
		if (_name.split("  ").length > 1)
		{
			return _name.split("  ")[1];
		}
		else if (_name.split("[.*]").length > 1)
		{
			return _name.split("[.*]")[1].trim();
		}
		else return _name;
		
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
