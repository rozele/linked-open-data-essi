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
package org.agu.essi.match;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.agu.essi.abstracts.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.MeetingType;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.KeywordParser;
import org.agu.essi.util.Namespaces;
import org.agu.essi.util.Utils;

public class MemoryMatcher implements EntityMatcher 
{
	//Stores unique identifier bases
	private static String meetingBaseId = Namespaces.esip + "Meeting_";
	private static String sectionBaseId = Namespaces.esip + "Section_";	
	private static String personBaseId = Namespaces.esip + "Person_";
	private static String organizationBaseId = Namespaces.esip + "Organization_";
	private static String sessionBaseId = Namespaces.esip + "Session_";
	private static String abstractBaseId = Namespaces.esip + "Abstract_";
	private static String keywordBaseId = Namespaces.esip + "Keyword_";
	
	//index registers for objects
	private Vector<Person> people;
	private Vector<Organization> organizations;
	private HashMap<String,Keyword> keywords;
	private HashMap<String,Session> sessions;
	private HashMap<String,Section> sections;
	private HashMap<String,Meeting> meetings;
	
	//start index for URIs
	private int peopleStartIdx;
	private int organizationsStartIdx;

	//location of keywords index
	private static String keywordsIndex = "http://www.agu.org/pubs/authors/manuscript_tools/journals/index_terms/AGU_index_terms.txt";
	
	public MemoryMatcher()
	{
		people = new Vector<Person>();
		organizations = new Vector<Organization>();
		keywords = new HashMap<String,Keyword>();
		sessions = new HashMap<String,Session>();
		sections = new HashMap<String,Section>();
		meetings = new HashMap<String,Meeting>();
		organizationsStartIdx = 0;
		peopleStartIdx = 0;
		addKeywordSource(keywordsIndex);
	}
	
	/**
	 * Gets an existing identifier for a meeting, if available, otherwise creates a new identifier
	 * @param meeting a Meeting instance
	 * @return a new or existing identifier for the input meeting
	 */
	public String getMeetingId(Meeting meeting)
	{	
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = meetingBaseId + mt + ((year > 0) ? "_" + year : "");
		if (!meetings.containsKey(id))
		{
			meetings.put(id, meeting);
		}
		return id;
	}

	/**
	 * Gets an existing identifier for a meeting section, if available, otherwise creates a new identifier
	 * @param section a Section instance
	 * @return a new or existing identifier for the input section
	 */
	public String getSectionId(Section section)
	{
		Meeting meeting = section.getMeeting();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = sectionBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + section.getId();
		if (!sections.containsKey(id))
		{
			sections.put(id, section);
		}
		return id;
	}	
	
	/**
	 * Gets an existing identifier for an abstract, if available, otherwise creates a new identifier
	 * @param abstr an Abstract instance
	 * @return a new or existing identifier for the input abstract
	 */
	public String getAbstractId(Abstract abstr)
	{
		Meeting meeting = abstr.getMeeting();
		String id = abstr.getId();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		return abstractBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + id; 
	}

	/**
	 * @deprecated Deprecated method for deprecated class, org.agu.essi.Abstract
	 * Gets an existing identifier for an abstract, if available, otherwise creates a new identifier
	 * @param abstr an Abstract instance
	 * @return a new or existing identifier for the input abstract
	 */
	public String getAbstractId(org.agu.essi.Abstract abstr)
	{
		Meeting meeting = abstr.getMeeting();
		String id = abstr.getId();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		return abstractBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + id; 
	}
	
	/**
	 * Gets an existing identifier for a person, if available, otherwise creates a new identifier
	 * @param person a Person instance
	 * @return a new or existing identifier for the input person
	 */
	public String getPersonId(Person person)
	{
		if (people.contains(person))
		{
			int idx = people.indexOf(person);
			return personBaseId + (peopleStartIdx + idx + 1);
		}
		else
		{
			people.add(person);
			return personBaseId + (peopleStartIdx + people.size());
		}
	}
	
	/**
	 * Gets an existing identifier for a meeting session, if available, otherwise creates a new identifier
	 * @param session a Session instance
	 * @return a new or existing identifier for the input session
	 */
	public String getSessionId(Session session)
	{
		Meeting meeting = session.getSection().getMeeting();
		MeetingType mt = Utils.getMeetingType(meeting);
		int year = Utils.getMeetingYear(meeting);
		String id = sessionBaseId + mt + ((year > 0) ? "_" + year : "") + "_" + session.getId();
		if (!sessions.containsKey(id))
		{
			sessions.put(id, session);
		}
		return id;
	}
	
	/**
	 * Gets an existing identifier for an organization, if available, otherwise creates a new identifier
	 * @param org a plain text description of an organization
	 * @return a new or existing identifier for the input organization
	 */
	public String getOrganizationId(Organization org)
	{
		if (organizations.contains(org))
		{
			int idx = organizations.indexOf(org);
			return organizationBaseId + (organizationsStartIdx + idx + 1);
		}
		else
		{
			organizations.add(org);
			return organizationBaseId + (organizationsStartIdx + organizations.size());
		}
	}
	
	/**
	 * Gets an existing identifier for a keyword, if available, otherwise creates a new identifier
	 * @param keyword a Keyword instance
	 * @return a new or existing identifier for the input keyword
	 */
	public String getKeywordId(Keyword keyword)
	{
		String id = keywordBaseId + keyword.getId();
		if (!keywords.containsKey(id))
		{
			keywords.put(id,keyword);
		}
		return id;
	}
	
	public void setPeopleStartIndex(int idx)
	{
		peopleStartIdx = idx;
	}
	
	public void setOrganizationsStartIndex(int idx)
	{
		organizationsStartIdx = idx;
	}
	
	/**
	 * Writes instance data for each person
	 * @param format
	 * @return
	 */
	public String writeNewPeople(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < people.size(); ++i)
			{
				Person p = people.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + personBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&foaf;Person\" />\n");
				sw.write("    <foaf:name rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(p.getName()) + "</foaf:name>\n");
				if (p.getEmail() != null)
				{
					sw.write("    <foaf:mbox>" + Utils.cleanXml(p.getEmail()) + "</foaf:mbox>\n");
				}
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public String writeNewKeywords(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			Set<String> keys = keywords.keySet();
			Iterator<String> iter = keys.iterator();
			while (iter.hasNext())
			{
				Keyword k = keywords.get(iter.next());
				sw.write(k.toString("rdf/xml"));
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public String writeNewOrganizations(String format)
	{
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			for(int i = 0; i < organizations.size(); ++i)
			{
				Organization o = organizations.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + organizationBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&foaf;Organization\" />\n");
				sw.write("    <dc:description rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(o.toString()) + "</dc:description>\n");
				/*if (o.getCoordinates() != null)
				{
					sw.write("    <foaf:based_near>\n");
					sw.write("      <rdf:Description>\n");
					sw.write("        <rdf:type rdf:resource=\"&geo;SpatialThing\" />\n");
					sw.write("        <geo:lat rdf:datatype=\"&xsd;float\">" + o.getCoordinates().getLat() + "</geo:lat>\n");
					sw.write("        <geo:long rdf:datatype=\"&xsd;float\">" + o.getCoordinates().getLng() + "</geo:long>\n");
					sw.write("      </rdf:Description>\n");
					sw.write("    </foaf:based_near>\n");
				}
				if (o.getGeoNamesId() != null)
				{
					sw.write("    <foaf:based_near rdf:resource=\"" + o.getGeoNamesId() + "\"/>\n");
				}*/
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}

	public String writeNewSessions(String format)
	{
		Set<String> keys = sessions.keySet();
		Iterator<String> iter = keys.iterator();
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			while(iter.hasNext())
			{
				String id = iter.next();
				Session s = sessions.get(id);
				sw.write("  <rdf:Description rdf:about=\"" + id + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swc;SessionEvent\" />\n");
				if (s.getName() != null)
				{
					sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(s.getName()) + "</swrc:eventTitle>\n");
				}
				if (s.getLocation() != null)
				{
					sw.write("    <swrc:hasLocation>\n");
					sw.write("      <swc:MeetingRoomPlace>\n");
					sw.write("        <dc:description rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(s.getLocation()) + "</dc:description>\n");
					sw.write("      </swc:MeetingRoomPlace>\n");
					sw.write("    </swrc:hasLocation>\n");
				}
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(s.getId()) + "</dc:identifier>\n");
				sw.write("    <swc:isSubEventOf rdf:resource=\"" + getSectionId(s.getSection()) + "\" />\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public String writeNewSections(String format)
	{
		Set<String> keys = sections.keySet();
		Iterator<String> iter = keys.iterator();
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			while(iter.hasNext())
			{
				String id = iter.next();
				Section s = sections.get(id);
				sw.write("  <rdf:Description rdf:about=\"" + id + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swrc;Meeting\" />\n");
				sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(s.getName()) + "</swrc:eventTitle>\n");
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(s.getId()) + "</dc:identifier>\n");
				sw.write("    <swc:isSubEventOf rdf:resource=\"" + getMeetingId(s.getMeeting()) + "\" />\n");
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public String writeNewMeetings(String format)
	{
		Set<String> keys = meetings.keySet();
		Iterator<String> iter = keys.iterator();
		if (format.equals("rdf/xml"))
		{
			StringWriter sw = new StringWriter();
			sw.write(Utils.writeXmlHeader());
			sw.write(Utils.writeDocumentEntities());
			sw.write(Utils.writeRdfHeader());
			while(iter.hasNext())
			{
				String id = iter.next();
				Meeting m = meetings.get(id);
				sw.write("  <rdf:Description rdf:about=\"" + id + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swrc;Meeting\" />\n");
				sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(m.getName()) + "</swrc:eventTitle>\n");
				if (m.getId() != null)
				{
					sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + Utils.cleanXml(m.getId()) + "</dc:identifier>\n");					
				}
				sw.write("  </rdf:Description>\n");
			}
			sw.write(Utils.writeRdfFooter());
			return sw.toString();
		}
		else 
		{
			return null;
		}
	}
	
	public void addKeywordSource(String src)
	{
		Vector<Keyword> k = KeywordParser.parseTerms(src);
		for (int i = 0; i < k.size(); ++i)
		{
			String id = keywordBaseId + k.get(i).getId();
			if (!keywords.containsKey(id))
			{
				keywords.put(id,k.get(i));
			}
		}		
	}
}
