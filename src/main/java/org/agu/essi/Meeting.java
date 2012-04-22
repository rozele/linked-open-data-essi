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
package org.agu.essi;

import java.io.StringWriter;

import org.agu.essi.util.Namespaces;

/**
 * Container class for AGU Meeting information
 * @author Eric Rozell
 */
public class Meeting {
	private String _name;
	private String _id;
	
	/**
	 * Construct Meeting instance from name
	 * @param name name of meeting
	 */
	public Meeting(String name)
	{
		_name = name;
	}
	
	/**
	 * Construct Meeting instance from name
	 * @param name name of meeting
	 * @param id ID for meeting
	 */
	public Meeting(String name, String id)
	{
		_name = name;
		_id = id;
	}
	
	/**
	 * Method to get meeting name
	 * @return name of meeting
	 */
	public String getName()
	{
		return _name;
	}
	
	/**
	 * Method to get meeting ID
	 * @return ID of meeting
	 */	
	public String getId()
	{
		return _id != null ? _id.toUpperCase() : _id;
	}
	
	/**
	 * Equality method for Meeting class
	 * @param o an Object
	 * @return true if meeting has the same name, false otherwise
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof Meeting && ((Meeting)o).getName().equals(this._name))
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
		return _orig;
	}
	
	/**
	 * Method to stringify Keyword in specific format
	 * @param format the output format
	 * @return
	 */
	public String toString(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write("  <rdf:Description rdf:about=\"&meeting;Meeting_" + _id + "\">\n");
			sw.write("    <rdf:type rdf:resource=\"&swrc;ResearchTopic\" />\n");
			sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + _id + "</dc:identifier>\n");
			sw.write("    <dc:subject rdf:datatype=\"&xsd;string\">" + _name + "</dc:subject>\n");
			sw.write("    <dc:description rdf:datatype=\"&xsd;string\">" + _orig + "</dc:description>\n");
			if (_parent != null)
			{
				sw.write("    <skos:broadMatch rdf:resource=\"" + Namespaces.essi + "Keyword_" + _parent.getId() + "\" />\n");
			}
			for (int i = 0; i < _related.size(); ++i)
			{
				sw.write("    <skos:related rdf:resource=\"" + Namespaces.essi + "Keyword_" + _related.get(i) + "\" />\n");

			}
			sw.write("  </rdf:Description>\n");
			return sw.toString();
		}
		else
		{
			return toString();
		}
	}
}
