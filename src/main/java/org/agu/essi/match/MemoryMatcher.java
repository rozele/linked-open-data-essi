package org.agu.essi.match;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.agu.essi.Abstract;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
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
	private Vector<Keyword> keywords;
	private HashMap<String,Session> sessions;
	private HashMap<String,Section> sections;
	private HashMap<String,Meeting> meetings;
	
	//start index for URIs
	private int peopleStartIdx;
	private int organizationsStartIdx;
	private int keywordsStartIdx;
	
	public MemoryMatcher()
	{
		people = new Vector<Person>();
		organizations = new Vector<Organization>();
		keywords = new Vector<Keyword>();
		sessions = new HashMap<String,Session>();
		sections = new HashMap<String,Section>();
		meetings = new HashMap<String,Meeting>();
		organizationsStartIdx = 0;
		keywordsStartIdx = 0;
		peopleStartIdx = 0;
	}
	
	/**
	 * Gets an existing identifier for a meeting, if available, otherwise creates a new identifier
	 * @param meeting a Meeting instance
	 * @return a new or existing identifier for the input meeting
	 */
	public String getMeetingId(Meeting meeting)
	{	
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JA_" : "FM_";
		String id = meetingBaseId + mstr + year;
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
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JA_" : "FM_";
		String id = sectionBaseId + mstr + year + "_" + section.getId();
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
	//this is a hack
	public String getAbstractId(Abstract abstr)
	{
		Meeting meeting = abstr.getMeeting();
		String id = abstr.getId();
		
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JM_" : "FM_";
		return abstractBaseId + mstr + year + "_" + id; 
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
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) 
			{
				if (tokens[i].equals("Fall"))
				{
					mid = 0;
				}
				else if (tokens[i].equals("Joint"))
				{
					mid = 1;
				}
			}
		}
		String mstr = (mid > 0) ? "JA_" : "FM_";
		String id = sessionBaseId + mstr + year + "_" + session.getId();
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
		if (keywords.contains(keyword))
		{
			int idx = keywords.indexOf(keyword);
			return keywordBaseId + (keywordsStartIdx + idx + 1);
		}
		else
		{
			keywords.add(keyword);
			return keywordBaseId + (keywordsStartIdx + keywords.size());
		}
	}
	
	public void setPeopleStartIndex(int idx)
	{
		peopleStartIdx = idx;
	}
	
	public void setKeywordsStartIndex(int idx)
	{
		keywordsStartIdx = idx;
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
				sw.write("    <foaf:name rdf:datatype=\"&xsd;string\">" + p.getName() + "</foaf:name>\n");
				sw.write("    <foaf:mbox>" + p.getEmail() + "</foaf:mbox>\n");
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
			for(int i = 0; i < keywords.size(); ++i)
			{
				Keyword k = keywords.get(i);
				sw.write("  <rdf:Description rdf:about=\"" + keywordBaseId + (i + 1) + "\">\n");
				sw.write("    <rdf:type rdf:resource=\"&swrc;ResearchTopic\" />\n");
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + k.getId() + "</dc:identifier>\n");
				sw.write("    <dc:subject rdf:datatype=\"&xsd;string\">" + k.getName() + "</dc:subject>\n");
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
				sw.write("    <dc:description rdf:datatype=\"&xsd;string\">" + o + "</dc:description>\n");
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
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + s.getId() + "</dc:identifier>\n");
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
				sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + s.getName() + "</swrc:eventTitle>\n");
				sw.write("    <dc:identifier rdf:datatype=\"&xsd;string\">" + s.getId() + "</dc:identifier>\n");
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
				sw.write("    <swrc:eventTitle rdf:datatype=\"&xsd;string\">" + m.getName() + "</swrc:eventTitle>\n");
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
}
