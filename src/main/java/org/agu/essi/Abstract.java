package org.agu.essi;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Vector;

import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.Namespaces;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.AbstractParserException;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

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
	private String _abstractId;
	private String _title;
	private String _abstract;
	private Vector<Author> _authors;
	private Vector<Keyword> _keywords;
	private String _sectionId;
	private String _meetingId;
	private Meeting _meeting;
	private Section _section;
	private Session _session;
	private AbstractType _type;
	
	// the EntityMatcher used for the abstract
	private EntityMatcher matcher;
	
	/**
	 * @constructor 
	 * @param text raw HTML for the abstract
	 * @throws AbstractParserException 
	 */
	public Abstract(String text) throws AbstractParserException
	{
		_rawHtml = text;
		_authors = new Vector<Author>();
		_keywords = new Vector<Keyword>();
		parseHtml();
		_meeting = new Meeting(_meetingId);
		_section = new Section(_sectionId, _meeting);
		_session = new Session(_sessionId.split("-")[0], _section);
	}
	
	/**
	 * @constructor
	 * @param title
	 * @param abstr
	 * @param id
	 * @param hour
	 * @param session
	 * @param authors
	 * @param keywords
	 */
	public Abstract(String title, String abstr, String id, String hour, Session session, Vector<Author> authors, Vector<Keyword> keywords)
	{
		_title = title;
		_abstract = abstr;
		_abstractId = id;
		_hour = hour;
		_session = session;
		_section = session.getSection();
		_meeting = _section.getMeeting();
		_authors = authors;
		_keywords = keywords;
	}
	
	public String toString(String format) throws EntityMatcherRequiredException
	{
		if (Utils.isXmlFormat(format))
		{
			return writeToXML();
		}
		else if (Utils.isRdfFormat(format))
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
	
	public String getAbstractText()
	{
		return _abstract;
	}
	
	public Meeting getMeeting()
	{
		return _meeting;
	}
	
	public String getId()
	{
		return _abstractId;
	}
	
	public AbstractType getAbstractType()
	{
		return _type;
	}
	
	public void setEntityMatcher(EntityMatcher m)
	{
		matcher = m;
	}
	
	private void parseHtml() throws AbstractParserException
	{
		int index, endIndex, nextIndex, emIndex, emEndIndex, afIndex, afEndIndex;
		
		// Hour (time of presentation)
		index = _rawHtml.indexOf("<span class=\"hr\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index < 0)
		{
			throw new AbstractParserException();
		}
		_hour = _rawHtml.substring(index+17, endIndex).trim();
		
		// Session
		index = _rawHtml.indexOf("<span class=\"an\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index < 0)
		{
			throw new AbstractParserException();
		}
		String[] parts = _rawHtml.substring(index+17, endIndex).trim().split(" ");
		_abstractId = parts[0];
		_sessionId = _abstractId.split("-")[0];
		_type = Utils.getAbstractType((parts.length > 1) ? parts[1] : null);
		
		// Title
		index = _rawHtml.indexOf("<span class=\"ti\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index < 0)
		{
			throw new AbstractParserException();
		}
		_title = _rawHtml.substring(index+17, endIndex).trim();


		// Authors
		index = _rawHtml.indexOf("<span class=\"au\">");
		if (index < 0)
		{
			throw new AbstractParserException();
		}
		HashMap<String,Author> identifier = new HashMap<String,Author>();
		while (index >= 0) 
		{
			Author a = null;
			endIndex = _rawHtml.indexOf("<br>", index);
			nextIndex = _rawHtml.indexOf("<span class=\"au\">", endIndex);
			
			String name = _rawHtml.substring(index+17, endIndex).trim();
			boolean add = false;
			
			if (identifier.containsKey(name))
			{
				a = identifier.get(name);
			}
			else
			{
				a = new Author(name);
				identifier.put(name,a);
				add = true;
			}
		  
			// Author email
			emIndex = _rawHtml.indexOf("<span class=\"em\">", endIndex);
			if ((emIndex < nextIndex || nextIndex == -1) && emIndex >= 0)
			{
				emEndIndex = _rawHtml.indexOf("<br>", emIndex);
				String email = _rawHtml.substring(emIndex+17, emEndIndex).trim();
				a.getPerson().addEmail(email);
			}
		  
			// Author affiliation
			afIndex = _rawHtml.indexOf("<span class=\"af\">", endIndex);
			if (afIndex < nextIndex && nextIndex >= 0 && afIndex >= 0)
			{
				afEndIndex = _rawHtml.indexOf("<br>", afIndex);
				String affiliation = _rawHtml.substring(afIndex+17, afEndIndex).trim();
				a.addAffiliation(affiliation);
			}

			if (add)
			{
				_authors.add(a);
			}

			index = nextIndex;
		}
		 
		// Abstract
		index = _rawHtml.indexOf("<span class=\"ab\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index < 0)
		{
			throw new AbstractParserException();
		}
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
		if (index < 0)
		{
			throw new AbstractParserException();
		}
		_sectionId = _rawHtml.substring(index+17, endIndex).trim();
		
		// AGU Meeting the abstract was submitted to
		index = _rawHtml.indexOf("<span class=\"mn\">");
		endIndex = _rawHtml.indexOf("<br>", index);
		if (index < 0)
		{
			throw new AbstractParserException();
		}
		_meetingId = _rawHtml.substring(index+17, endIndex).trim();
		
	}
	
	private String writeToXML()
	{
		StringWriter sw = new StringWriter();
		String xmlNS = "xmlns:xsi=\"" + Namespaces.xsi + "\" xmlns=\"" + Namespaces.essiSchema + "\"";
		String aguSchema = Namespaces.essiSchema + " " + Namespaces.essiXsd;
		sw.write(Utils.writeXmlHeader());
		sw.write("<AGUAbstract " + xmlNS + " xsi:schemaLocation=\"" + aguSchema + "\">\n");
		sw.write("  <Meeting>" + Utils.cleanXml(_meeting.getName()) + "</Meeting>\n");
		sw.write("  <Section>" + Utils.cleanXml(_section.getName()) + "</Section>\n");
		sw.write("  <Id>" + Utils.cleanXml(_abstractId) + "</Id>\n");
		if (_keywords.size() >= 0)
		{
			sw.write("  <Keywords>\n");
			for (int i=0; i < _keywords.size(); i++) 
			{ 
				sw.write("    <Keyword>" + Utils.cleanXml(_keywords.get(i).toString()) + "</Keyword>\n"); 
			}
			sw.write("  </Keywords>\n");
		}
		sw.write("  <Abstract>" + Utils.cleanXml(_abstract) + "</Abstract>\n");
		sw.write("  <Title>" + Utils.cleanXml(_title) + "</Title>\n");
		if (_session != null)
		{
			sw.write("  <Session>" + Utils.cleanXml(_session.getId()) + "</Session>\n");
		}
		if (_hour != null)
		{
			sw.write("  <Hour>" + Utils.cleanXml(_hour) + "</Hour>\n");
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
	
	private String writeToRDFXML() throws EntityMatcherRequiredException
	{
		if (matcher == null)
		{
			throw new EntityMatcherRequiredException();
		}
		else
		{
			StringWriter sw = new StringWriter();		
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			sw.write("  <rdf:Description rdf:about=\"" + matcher.getAbstractId(this) + "\">\n");
			sw.write("    <rdf:type rdf:resource=\"&esip;Abstract\"/>\n");
			sw.write("    <dc:title rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(_title) + "</dc:title>\n");
			sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(_abstractId) + "</dc:identifier>\n");
			sw.write("    <swc:relatedToEvent rdf:resource=\"" + matcher.getSessionId(_session) + "\" />\n");
			sw.write("    <swrc:abstract rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(_abstract) + "</swrc:abstract>\n");
			for (int i = 0; i < _keywords.size(); ++i)
			{
				sw.write("    <swc:hasTopic rdf:resource=\"" + matcher.getKeywordId(_keywords.get(i)) + "\" />\n");
			}
			for (int i = 0; i < _authors.size(); ++i)
			{
				sw.write("    <tw:hasAgentWithRole rdf:nodeID=\"A"+i+"\" />\n");
			}
			sw.write("  </rdf:Description>\n");
			sw.write(writeAuthorRoles());
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
	}
	
	private String writeAuthorRoles()
	{
		StringWriter sw = new StringWriter();
		for (int i = 0; i < _authors.size(); ++i)
		{
			Author a = _authors.get(i);
			sw.write("  <rdf:Description rdf:about=\"" + matcher.getPersonId(a.getPerson()) + "\">\n");
			sw.write("    <tw:hasRole>\n");
			sw.write("      <rdf:Description rdf:nodeID=\"A"+i+"\">\n");
			sw.write("        <rdf:type rdf:resource=\"&tw;Author\" />\n");

			for (int j = 0; j < a.getAffiliations().size(); ++j)
			{
				Organization org = a.getAffiliations().get(j);
				sw.write("        <tw:withAffiliation rdf:resource=\"" + matcher.getOrganizationId(org) + "\" />\n");
			}
			sw.write("        <tw:index rdf:datatype=\"&xsd;positiveInteger\">"+(i+1)+"</tw:index>\n");
			sw.write("      </rdf:Description>\n");
			sw.write("    </tw:hasRole>\n");
			sw.write("  </rdf:Description>\n");
		}
		return sw.toString();
	}

}
