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
import java.util.regex.*;

import essi.lod.util.Namespaces;

/**
 * Container class for AGU keywords
 * @author Eric Rozell
 */
public class Keyword 
{
	private String _orig;
	private String _name;
	private String _id;
	private Vector<String> _related;
	private Keyword _parent;
	
	/**
	 * Constructs keyword from raw abstract HTML content
	 * @param name keyword content of AGU abstract HTML
	 */
	public Keyword(String name)
	{
		_orig = name;
		_related = new Vector<String>();
		Pattern p1 = Pattern.compile("(\\[?(\\d+)\\]?)?\\s*(.*?)(\\s*\\((.*)\\))");
		Pattern p2 = Pattern.compile("(\\[?(\\d+)\\]?)?\\s*(.*)");
		
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
				_related.add(list[i].trim());
			}
			if (_id == null && _related.size() > 0)
			{
				_id = _related.get(0);
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
	 * Sets a broader Keyword
	 * @param p a parent Keyword
	 */
	public void setParent(Keyword p)
	{
		_parent = p;
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
			sw.write("  <rdf:Description rdf:about=\"&keyword;Keyword_" + _id + "\">\n");
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
