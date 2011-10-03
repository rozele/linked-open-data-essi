package org.agu.essi;

import java.io.StringWriter;
import java.util.Vector;
import java.util.regex.*;

import org.agu.essi.util.Namespaces;

/**
 * Container class for AGU keywords
 * @author Eric Rozell
 */
public class Keyword {
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
			sw.write("  <rdf:Description rdf:about=\"" + Namespaces.esip + "Keyword_" + _id + "\">\n");
			sw.write("    <rdf:type rdf:resource=\"&swrc;ResearchTopic\" />\n");
			sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + _id + "</dc:identifier>\n");
			sw.write("    <dc:subject rdf:datatype=\"&xsd;string\">" + _name + "</dc:subject>\n");
			if (_parent != null)
			{
				sw.write("    <skos:broadMatch rdf:resource=\"&esip;Keyword_" + _parent.getId() + "\" />\n");
			}
			for (int i = 0; i < _related.size(); ++i)
			{
				sw.write("    <skos:related rdf:resource=\"&esip;Keyword_" + _related.get(i) + "\" />\n");

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
