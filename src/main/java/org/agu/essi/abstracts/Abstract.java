package org.agu.essi.abstracts;

import java.io.StringWriter;
import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Author;
import org.agu.essi.Meeting;
import org.agu.essi.Keyword;
import org.agu.essi.Organization;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.Namespaces;
import org.agu.essi.util.Utils;
import org.agu.essi.util.exception.EntityMatcherRequiredException;

/**
 * Abstract class for AGU Abstract implementations (sorry)
 * @author Eric Rozell
 */
public abstract class Abstract 
{	
	private EntityMatcher matcher;
	
	/**
	 * Abstract Methods
	 */
	
	public abstract String getTitle();
	public abstract Meeting getMeeting();
	public abstract String getId();
	public abstract Session getSession();
	public abstract Section getSection();
	public abstract String getAbstract();
	public abstract AbstractType getAbstractType();
	public abstract Vector<Keyword> getKeywords();
	public abstract Vector<Author> getAuthors();
	public abstract String getHour();
	
	public void setEntityMatcher(EntityMatcher m)
	{
		matcher = m;
	}
	
	public EntityMatcher getEntityMatcher()
	{
		return matcher;
	}
	
	public String toString(String format) throws EntityMatcherRequiredException
	{
		if (Utils.isXmlFormat(format))
		{
			return writeToXML();
		}
		else if (Utils.isRdfXmlFormat(format))
		{
			return writeToRDFXML();
		}
		else 
		{
			return this.toString();
		}
	}
	
	private String writeToXML()
	{
		StringWriter sw = new StringWriter();
		String xmlNS = "xmlns:xsi=\"" + Namespaces.xsi + "\" xmlns=\"" + Namespaces.essiSchema + "\"";
		String aguSchema = Namespaces.essiSchema + " " + Namespaces.essiXsd;
		sw.write(Utils.writeXmlHeader());
		sw.write("<AGUAbstract " + xmlNS + " xsi:schemaLocation=\"" + aguSchema + "\">\n");
		sw.write("  <Meeting>" + Utils.cleanXml(getMeeting().getName()) + "</Meeting>\n");
		sw.write("  <Section>" + Utils.cleanXml(getSection().getName()) + "</Section>\n");
		sw.write("  <Id>" + Utils.cleanXml(getId()) + "</Id>\n");
		if (getKeywords().size() >= 0)
		{
			sw.write("  <Keywords>\n");
			for (int i=0; i < getKeywords().size(); i++) 
			{ 
				sw.write("    <Keyword>" + Utils.cleanXml(getKeywords().get(i).toString()) + "</Keyword>\n"); 
			}
			sw.write("  </Keywords>\n");
		}
		sw.write("  <Abstract>" + Utils.cleanXml(getAbstract()) + "</Abstract>\n");
		sw.write("  <Title>" + Utils.cleanXml(getTitle()) + "</Title>\n");
		if (getSession() != null)
		{
			sw.write("  <Session>" + Utils.cleanXml(getSession().getId()) + "</Session>\n");
		}
		if (getHour() != null)
		{
			sw.write("  <Hour>" + Utils.cleanXml(getHour()) + "</Hour>\n");
		}
		if (getAuthors().size() >= 0)
		{
			sw.write("  <Authors>\n");
			for (int i=0; i < getAuthors().size(); i++) 
			{  
				sw.write("    " + getAuthors().get(i).toString("xml") + "\n");
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
			sw.write("    <dc:title rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(getTitle()) + "</dc:title>\n");
			sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(getId()) + "</dc:identifier>\n");
			sw.write("    <swc:relatedToEvent rdf:resource=\"" + matcher.getSessionId(getSession()) + "\" />\n");
			sw.write("    <swrc:abstract rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(getAbstract()) + "</swrc:abstract>\n");
			for (int i = 0; i < getKeywords().size(); ++i)
			{
				sw.write("    <swc:hasTopic rdf:resource=\"" + matcher.getKeywordId(getKeywords().get(i)) + "\" />\n");
			}
			for (int i = 0; i < getAuthors().size(); ++i)
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
		for (int i = 0; i < getAuthors().size(); ++i)
		{
			Author a = getAuthors().get(i);
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
