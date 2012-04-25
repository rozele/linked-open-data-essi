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
package essi.lod.abstracts;

import java.io.StringWriter;
import java.util.Vector;


import essi.lod.data.matcher.EntityMatcher;
import essi.lod.entity.agu.Author;
import essi.lod.entity.agu.Keyword;
import essi.lod.entity.agu.Meeting;
import essi.lod.entity.agu.Organization;
import essi.lod.entity.agu.Section;
import essi.lod.entity.agu.Session;
import essi.lod.enumeration.AbstractEnumeration;
import essi.lod.exception.EntityMatcherRequiredException;
import essi.lod.util.Namespaces;
import essi.lod.util.Utils;

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
	public abstract AbstractEnumeration getAbstractType();
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
			sw.write("    <rdf:type rdf:resource=\"&essi;Abstract\"/>\n");
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
			sw.write(writeAuthorRolesRDFXML());
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
	}
	
	private String writeAuthorRolesRDFXML()
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
