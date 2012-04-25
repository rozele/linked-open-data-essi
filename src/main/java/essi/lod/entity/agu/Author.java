/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
package essi.lod.entity.agu;

import java.io.StringWriter;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import essi.lod.util.Utils;

/**
 * Container class for AGU abstract author roles
 * @author Eric Rozell
 */
public class Author {
	// container variables
	private Person _person;
	private Vector <Organization> _affiliations;
	private boolean _corresponding;
	
	/**
	 * Constructor based on author name
	 * @param name name of author
	 */
	public Author(String name)
	{
		_person = new Person(name);
		_affiliations = new Vector<Organization>();
		checkCorresponding(name);
	}
	
	/**
	 * Constructor based on author name and email
	 * @param name name of author
	 * @param email email of author
	 */
	public Author(String name, String email)
	{
		_person = new Person(name, email);
		_affiliations = new Vector<Organization>();
		checkCorresponding(name);
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
		_affiliations = new Vector<Organization>();
		_affiliations.add(new Organization(affiliation));
		checkCorresponding(name);
	}
	
	/**
	 * Method to add an affiliation for existing author
	 * @param affiliation affiliation of author
	 */
	public void addAffiliation(String affiliation)
	{
		_affiliations.add(new Organization(affiliation));
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
	public Vector<Organization> getAffiliations()
	{
		return _affiliations;
	}
	
	public boolean isCorresponding()
	{
		return _corresponding;
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
	@Override
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
			sw.write("<Name>" + Utils.cleanXml(_person.getName()) + "</Name>");
			if (_person.getEmail() != null)
			{
				sw.write("<Email>" + Utils.cleanXml(_person.getEmail()) + "</Email>");
			}
			if (_affiliations.size() > 0)
			{
				sw.write("<Affiliations>");
				for (int i = 0; i < _affiliations.size(); ++i)
				{
					sw.write("<Affiliation>" + Utils.cleanXml(_affiliations.get(i).toString()) + "</Affiliation>");
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
	
	private void checkCorresponding(String name)
	{
		Pattern p = Pattern.compile("(\\*\\s*)?(.*)");
		Matcher m = p.matcher(name);
		boolean matched = m.find();
		if (matched)
		{
			if (m.group(1) != null)
			{
				_corresponding = true;
			}
			else
			{
				_corresponding = false;
			}
		}
		else
		{
			_corresponding = false;
		}
	}
}
