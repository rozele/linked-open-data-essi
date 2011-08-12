package org.agu.essi;
import java.io.StringWriter;
import java.util.Vector;

import org.agu.essi.util.EntityIdentifier;
import org.agu.essi.util.Namespaces;

/**
 * Container class for AGU abstract information
 * @author Eric Rozell and Tom Narock
 */
public class Abstract 
{
	// containers for abstract info
	private String _rawHtml;
	private String _hour;
	private String _sessionId;
	private String _title;
	private String _abstract;
	private Vector<Author> _authors;
	private Vector<Keyword> _keywords;
	private String _sectionId;
	private String _meetingId;
	private Meeting _meeting;
	private Section _section;
	private Session _session;
	
	
	/**
	 * @constructor 
	 * @param text raw HTML for the abstract
	 */
	public Abstract(String text)
	{
		_rawHtml = text;
		_authors = new Vector<Author>();
		_keywords = new Vector<Keyword>();
		parseHtml();
		_meeting = new Meeting(_meetingId);
		_section = new Section(_sectionId, _meeting);
		_session = new Session(_sessionId, _section);
	}
	
	public Abstract(String title, String abstr, Session session, Vector<Author> authors, Vector<Keyword> keywords)
	{
		_title = title;
		_abstract = abstr;
		_session = session;
		_section = _session.getSection();
		_meeting = _section.getMeeting();
		_authors = authors;
		_keywords = keywords;
	}
	
	public String toString(String format)
	{
		if (format.equals("xml"))
		{
			return writeToXML();
		}
		else if (format.equals("rdf/xml"))
		{
			return writeToRDFXML();
		}
		else 
		{
			return this.toString();
		}
	}
	
	public String getTitle()
	{
		return _title;
	}
	
	public Meeting getMeeting()
	{
		return _meeting;
	}
		
	private void parseHtml()
	{
		int index, endIndex, nextIndex, emIndex, emEndIndex, afIndex, afEndIndex;
		
		// Hour (time of presentation)
		index = _rawHtml.indexOf("<span class=\"hr\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index >= 0)
			_hour = _rawHtml.substring(index+17, endIndex).trim();
		
		// Session
		index = _rawHtml.indexOf("<span class=\"an\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index >= 0)
			_sessionId = _rawHtml.substring(index+17, endIndex).trim();
		
		// Title
		index = _rawHtml.indexOf("<span class=\"ti\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		_title = _rawHtml.substring(index+17, endIndex).trim();


		// Authors
		index = _rawHtml.indexOf("<span class=\"au\">");
		while (index >= 0) 
		{
			Author a = null;
			endIndex = _rawHtml.indexOf("<br>", index);
			nextIndex = _rawHtml.indexOf("<span class=\"au\">", endIndex);

			String name = _rawHtml.substring(index+17, endIndex).trim();
			a = new Author(name);
		  
			// Author email
			emIndex = _rawHtml.indexOf("<span class=\"em\">", endIndex);
			if ((emIndex < nextIndex || nextIndex == -1) && emIndex >= 0)
			{
				emEndIndex = _rawHtml.indexOf("<br>", emIndex);
				String email = _rawHtml.substring(emIndex+17, emEndIndex).trim();
				a.getPerson().addEmail(email);
			}

			// if an author has two affiliations then their name is listed twice in 
			// the AGU abstract. here, we check for this and keep a name only once
			try 
			{
				if (!_authors.contains(a))
				{
					_authors.add(a);
				}
				else
				{
					int idx = _authors.indexOf(a);
					a = _authors.elementAt(idx);
				}
			}
			catch (NullPointerException n)
			{
				System.out.println("Error.");
			}
		  
			// Author affiliation
			afIndex = _rawHtml.indexOf("<span class=\"af\">", endIndex);
			if (afIndex < nextIndex && nextIndex >= 0 && afIndex >= 0)
			{
				afEndIndex = _rawHtml.indexOf("<br>", afIndex);
				String affiliation = _rawHtml.substring(afIndex+17, afEndIndex).trim();
				a.addAffiliation(affiliation);
			}

			index = nextIndex;
		}
		 
		// Abstract
		index = _rawHtml.indexOf("<span class=\"ab\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		_abstract = _rawHtml.substring(index+17, endIndex).trim();

		// Keywords
		index = _rawHtml.indexOf("<span class=\"de\">");
		while (index >= 0) 
		{
			endIndex = _rawHtml.indexOf("<br>", index);
			_keywords.add( new Keyword(_rawHtml.substring(index+17, endIndex).trim()) );
			index = _rawHtml.indexOf("<span class=\"de\">", endIndex);
		}
		
		// Section
		index = _rawHtml.indexOf("<span class=\"sc\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		_sectionId = _rawHtml.substring(index+17, endIndex).trim();
		
		// AGU Meeting the abstract was submitted to
		index = _rawHtml.indexOf("<span class=\"mn\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		_meetingId = _rawHtml.substring(index+17, endIndex).trim();
	}
	
	private String writeToXML()
	{
		StringWriter sw = new StringWriter();
		String xmlNS = "xmlns:xsi=\"" + Namespaces.xsi + "\" xmlns=\"" + Namespaces.essiSchema + "\"";
		String aguSchema = Namespaces.essiSchema + " " + Namespaces.essiXsd;
		sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sw.write("<AGUAbstract " + xmlNS + " xsi:schemaLocation=\"" + aguSchema + "\">\n");
		sw.write("  <Meeting>" + _meeting.getName() + "</Meeting>\n");
		sw.write("  <Section>" + _section.getName() + "</Section>\n");
		if (_keywords.size() >= 0)
		{
			sw.write("  <Keywords>\n");
			for (int i=0; i < _keywords.size(); i++) 
			{ 
				sw.write("    <Keyword>" + _keywords.get(i) + "</Keyword>\n"); 
			}
			sw.write("  </Keywords>\n");
		}
		sw.write("  <Abstract>" + _abstract + "</Abstract>\n");
		sw.write("  <Title>" + _title + "</Title>\n");
		if (_session != null)
		{
			sw.write("  <Session>" + _session.getId() + "</Session>\n");
		}
		if (_hour != null)
		{
			sw.write("  <Hour>" + _hour + "</Hour>\n");
		}
		if (_authors.size() >= 0)
		{
			sw.write("  <Authors>\n");
			for (int i=0; i < _authors.size(); i++) 
			{  
				sw.write("    " + _authors.get(i).toString("xml") + "\n");
			}
		}
		sw.write("  </Authors>\n");
		sw.write("</AGUAbstract>");
		return sw.toString();
	}
	
	private String writeToRDFXML()
	{
		StringWriter sw = new StringWriter();
		sw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		sw.write(writeEntities());
		sw.write("<rdf:RDF xmlns=\"" + Namespaces.esipOwl + "\" " +
				"         xmlns:rdf=\"" + Namespaces.rdf + "\"\n" +
				"         xmlns:swc=\"" + Namespaces.swc + "\"\n" + 
				"         xmlns:swrc=\"" + Namespaces.swrc + "\"\n" +
				"         xmlns:dc=\"" + Namespaces.dc + "\"\n" +
				"         xmlns:tw=\"" + Namespaces.tw + "\"\n" +
				"         xml:base=\"" + Namespaces.esip + "\">\n");
		sw.write("  <rdf:Description rdf:about=\"" + EntityIdentifier.getNextAbstractId() + "\">\n");
		sw.write("    <rdf:type rdf:resource=\"&esip;Abstract\"/>\n");
		sw.write("    <dc:title rdf:datatype=\"&xsd;string\">" + _title + "</dc:title>\n");
		sw.write("    <swc:relatedToEvent rdf:resource=\"" + EntityIdentifier.getSessionId(_session) + "\" />\n");
		sw.write("    <swrc:abstract rdf:datatype=\"&xsd;string\">" + _abstract + "</swrc:abstract>\n");
		for (int i = 0; i < _keywords.size(); ++i)
		{
			sw.write("    <swc:hasTopic rdf:resource=\"" + EntityIdentifier.getKeywordId(_keywords.get(i)) + "\" />\n");
		}
		for (int i = 0; i < _authors.size(); ++i)
		{
			sw.write("    <tw:hasAgentWithRole rdf:nodeID=\"A"+i+"\">\n");
		}
		sw.write("  </rdf:Description>\n");
		sw.write(writeAuthorRoles());
		sw.write("</rdf:RDF>\n");
		return sw.toString();
	}
	
	private String writeEntities()
	{
		StringWriter sw = new StringWriter();
		sw.write("<!DOCTYPE rdf:RDF [\n");
		sw.write("  <!ENTITY owl \"" + Namespaces.owl + "\" >\n");
		sw.write("  <!ENTITY esip \"" + Namespaces.esipOwl + "\" >\n");
		sw.write("  <!ENTITY xsd \"" + Namespaces.xsd + "\" >\n");
		sw.write("  <!ENTITY tw \"" + Namespaces.tw + "\" >\n");
		sw.write("]>\n");
		return sw.toString();
	}
	
	private String writeAuthorRoles()
	{
		StringWriter sw = new StringWriter();
		for (int i = 0; i < _authors.size(); ++i)
		{
			Author a = _authors.get(i);
			sw.write("  <rdf:Description rdf:about=\"" + EntityIdentifier.getPersonId(a.getPerson()) + "\">\n");
			sw.write("    <tw:hasRole>\n");
			sw.write("      <rdf:Description rdf:nodeID=\"A"+i+"\">\n");
			sw.write("        <rdf:type rdf:resource=\"&tw;Author\" />\n");
			for (int j = 0; j < a.getAffiliations().size(); ++j)
			{
				String org = a.getAffiliations().get(j);
				sw.write("        <swrc:affiliation rdf:resource=\"" + EntityIdentifier.getOrganizationId(org) + "\" />\n");
			}
			sw.write("        <tw:index rdf:datatype=\"&xsd;positiveInteger\">"+(i+1)+"</tw:index>\n");
			sw.write("      </rdf:Description>\n");
			sw.write("    </tw:hasRole>\n");
			sw.write("  </rdf:Description>\n");
		}
		return sw.toString();
	}
}
