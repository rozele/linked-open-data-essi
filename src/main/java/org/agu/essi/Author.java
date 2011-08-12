package org.agu.essi;

import java.io.StringWriter;
import java.util.Vector;

/**
 * Container class for AGU abstract author roles
 * @author Eric Rozell
 */
public class Author {
	// container variables
	private Person _person;
	private Vector <String> _affiliations;
	
	/**
	 * Constructor based on author name
	 * @param name name of author
	 */
	public Author(String name)
	{
		_person = new Person(name);
		_affiliations = new Vector<String>();
	}
	
	/**
	 * Constructor based on author name and email
	 * @param name name of author
	 * @param email email of author
	 */
	public Author(String name, String email)
	{
		_person = new Person(name, email);
		_affiliations = new Vector<String>();
	}
	
	/**
	 * Constructor based on author name, email, and affiliation
	 * @param name name of author
	 * @param email email of author
	 * @param affiliation affiliation of author
	 */
	public Author(String name, String email, String affiliation)
	{
		_person = new Person(name, email);
		_affiliations = new Vector<String>();
		_affiliations.add(affiliation);
	}
	
	/**
	 * Method to add an affiliation for existing author
	 * @param affiliation affiliation of author
	 */
	public void addAffiliation(String affiliation)
	{
		_affiliations.add(affiliation);
	}
	
	/**
	 * 
	 * @return person with author role
	 */
	public Person getPerson()
	{
		return _person;
	}
	
	/**
	 * Method to get author affiliations
	 * @return affiliations of author
	 */
	public Vector<String> getAffiliations()
	{
		return _affiliations;
	}
	
	/**
	 * Override of equals method, compares by person
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Author && ((Author)o).getPerson().equals(this._person))
		{
			return true;
		}
		else 
		{
			return false;
		}
	}
	
	/**
	 * Override of the toString method
	 * @return stringified author
	 */
	public String toString()
	{
		return _person.toString();
	}
	
	/**
	 * A stringify method that enables multiple output formats
	 * @param format a specification for output format (e.g., "xml", "rdf/xml", etc.)
	 * @return stringified author in specified format
	 */
	public String toString(String format)
	{
		if (format.equals("xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write("<Author>");
			sw.write("<Name>" + _person.getName() + "</Name>");
			if (_person.getEmail() != null)
			{
				sw.write("<Email>" + _person.getEmail() + "</Email>");
			}
			if (_affiliations.size() > 0)
			{
				sw.write("<Affiliations>");
				for (int i = 0; i < _affiliations.size(); ++i)
				{
					sw.write("<Affiliation>" + _affiliations.get(i) + "</Affiliation>");
				}
				sw.write("</Affiliations>");
			}
			sw.write("</Author>");
			return sw.toString();
		}
		else
		{
			return this.toString();
		}
	}
}
