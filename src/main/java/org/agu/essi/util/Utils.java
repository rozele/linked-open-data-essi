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
package org.agu.essi.util;

import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Keyword;
import org.agu.essi.Meeting;
import org.agu.essi.MeetingType;
import org.agu.essi.Organization;
import org.agu.essi.Person;
import org.agu.essi.Section;
import org.agu.essi.Session;
import org.agu.essi.util.exception.InvalidAbstractConstraintException;
import org.apache.commons.lang.StringEscapeUtils;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;

/**
 * Utility functions
 * @author Eric Rozell
 */
public class Utils {
	
	public static String cleanXml(String s)
	{
		return fixSpecialChars(iconv(StringEscapeUtils.escapeXml(s),"UTF-8"));
	}
	
	public static String unescapeHtml(String s)
	{
	    return fixSpecialChars(iconv(StringEscapeUtils.unescapeHtml(s),"UTF-8"));
	}
	
	public static String iconv(String s, String format)
	{
		try
		{
			return new String(s.getBytes(format));
		}
		catch (Exception e)
		{
			return s;
		}
	}
	
	//this is a hack
	public static String fixSpecialChars(String s)
	{
		s = s.replaceAll("\\p{Cntrl}", "");
		s = s.replaceAll(" "," ");
		return s;
	}
	
	public static ResultSet sparqlSelect(String q, String ep)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ep, query);
		return qexec.execSelect();
	}
	
	public static boolean sparqlAsk(String q, String ep)
	{
		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(ep, query);
		return qexec.execAsk();
	}
	
	public static boolean isRdfXmlFormat(String format)
	{
		return format.equals("rdf/xml") || format.equals("application/rdf+xml");
	}
	
	public static boolean isXmlFormat(String format)
	{
		return format.equals("xml") || format.equals("application/xml") || format.equals("text/xml");
	}
	
	public static String writeRdfHeader()
	{
		StringWriter sw = new StringWriter();
		sw.write("<rdf:RDF xmlns=\"" + Namespaces.essi + "\"\n");
		sw.write("         xmlns:owl=\"" + Namespaces.owl + "\"\n");
		sw.write("         xmlns:rdf=\"" + Namespaces.rdf + "\"\n");
		sw.write("         xmlns:swc=\"" + Namespaces.swc + "\"\n");
		sw.write("         xmlns:swrc=\"" + Namespaces.swrc + "\"\n");
		sw.write("         xmlns:dc=\"" + Namespaces.dc + "\"\n");
		sw.write("         xmlns:tw=\"" + Namespaces.tw + "\"\n");
		sw.write("         xmlns:foaf=\"" + Namespaces.foaf + "\"\n");
		sw.write("         xmlns:geo=\"" + Namespaces.geo + "\"\n");
		sw.write("         xmlns:skos=\"" + Namespaces.skos + "\"\n");
		sw.write("         xmlns:dbanno=\"" + Namespaces.dbanno + "\"\n");
		sw.write("		   xmlns:pav=\"" + Namespaces.pav + "\"\n");
		sw.write("		   xmlns:ao=\"" + Namespaces.ao + "\"\n");
		sw.write("		   xmlns:aocore=\"" + Namespaces.aocore + "\"\n");
		sw.write("		   xmlns:aof=\"" + Namespaces.aof + "\"\n");
		sw.write("		   xmlns:aos=\"" + Namespaces.aos + "\"\n");
		sw.write("		   xmlns:aot=\"" + Namespaces.aot + "\"\n");
		sw.write("         xml:base=\"" + Namespaces.essi + "\">\n");
		return sw.toString();
	}
	
	public static String writeSparqlPrefixes()
	{
		StringWriter sw = new StringWriter();
		sw.write("PREFIX owl: <" + Namespaces.owl + ">\n");
		sw.write("PREFIX essi: <" + Namespaces.essiOwl + ">\n");
		sw.write("PREFIX xsd: <" + Namespaces.xsd + ">\n");
		sw.write("PREFIX tw: <" + Namespaces.tw + ">\n");
		sw.write("PREFIX foaf: <" + Namespaces.foaf + ">\n");
		sw.write("PREFIX swrc: <" + Namespaces.swrc + ">\n");
		sw.write("PREFIX swc: <" + Namespaces.swc + ">\n");
		sw.write("PREFIX geo: <" + Namespaces.geo + ">\n");
		sw.write("PREFIX skos: <" + Namespaces.skos + ">\n");
		sw.write("PREFIX dbanno: <" + Namespaces.dbanno + ">\n");
		sw.write("PREFIX pav: <" + Namespaces.pav + ">\n");
		sw.write("PREFIX ao: <" + Namespaces.ao + ">\n");
		sw.write("PREFIX aocore: <" + Namespaces.aocore + ">\n");
		sw.write("PREFIX aof: <" + Namespaces.aof + ">\n");
		sw.write("PREFIX aos: <" + Namespaces.aos + ">\n");
		sw.write("PREFIX aot: <" + Namespaces.aot + ">\n");
		sw.write("PREFIX dc: <" + Namespaces.dc + ">\n");
		return sw.toString();
	}
	
	public static String writeRdfFooter()
	{
		return "</rdf:RDF>\n";
	}
	
	public static String writeXmlHeader()
	{
		return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	}
	
	public static String writeDocumentEntities()
	{
		StringWriter sw = new StringWriter();
		sw.write("<!DOCTYPE rdf:RDF [\n");
		sw.write("  <!ENTITY owl \"" + Namespaces.owl + "\" >\n");
		sw.write("  <!ENTITY essi \"" + Namespaces.essiOwl + "\" >\n");
		sw.write("  <!ENTITY xsd \"" + Namespaces.xsd + "\" >\n");
		sw.write("  <!ENTITY tw \"" + Namespaces.tw + "\" >\n");
		sw.write("  <!ENTITY foaf \"" + Namespaces.foaf + "\" >\n");
		sw.write("  <!ENTITY swrc \"" + Namespaces.swrc + "\" >\n");
		sw.write("  <!ENTITY swc \"" + Namespaces.swc + "\" >\n");
		sw.write("  <!ENTITY geo \"" + Namespaces.geo + "\" >\n");
		sw.write("  <!ENTITY skos \"" + Namespaces.skos + "\" >\n");
		sw.write("  <!ENTITY dbanno \"" + Namespaces.dbanno + "\" >\n");
		sw.write("  <!ENTITY pav \"" + Namespaces.pav + "\" >\n");
		sw.write("  <!ENTITY ao \"" + Namespaces.ao + "\" >\n");
		sw.write("  <!ENTITY aocore \"" + Namespaces.aocore + "\" >\n");
		sw.write("  <!ENTITY aof \"" + Namespaces.aof + "\" >\n");
		sw.write("  <!ENTITY aos \"" + Namespaces.aos + "\" >\n");
		sw.write("  <!ENTITY aot \"" + Namespaces.aot + "\" >\n");
		sw.write("]>\n");
		return sw.toString();	
	}
	
	public static HashMap<String,String> getAguDatabases()
	{
		HashMap <String, String> aguDatabases = new HashMap <String, String> ();
		aguDatabases.put( "fm10", "/data/epubs/wais/indexes/fm10/fm10");	
		aguDatabases.put( "ja10", "/data/epubs/wais/indexes/ja10/ja10");	
		aguDatabases.put( "fm09", "/data/epubs/wais/indexes/fm09/fm09");	
		aguDatabases.put( "ja09", "/data/epubs/wais/indexes/ja09/ja09");
		aguDatabases.put( "fm08", "/data/epubs/wais/indexes/fm08/fm08");	
		aguDatabases.put( "ja08", "/data/epubs/wais/indexes/ja08/ja08");	
		aguDatabases.put( "fm07", "/data/epubs/wais/indexes/fm07/fm07");	
		aguDatabases.put( "sm07", "/data/epubs/wais/indexes/sm07/sm07");	
		aguDatabases.put( "fm06", "/data/epubs/wais/indexes/fm06/fm06");	
		aguDatabases.put( "sm06", "/data/epubs/wais/indexes/sm06/sm06");	
		aguDatabases.put( "fm05", "/data/epubs/wais/indexes/fm05/fm05");	
		aguDatabases.put( "sm05", "/data/epubs/wais/indexes/sm05/sm05");
		return aguDatabases;
	}
	
	public static Vector<String[]> getAguMeetingsFromInput(String input)
	{
		Vector<String[]> meetings = new Vector<String[]>();
		String[] arr1 = input.split(";");
		for (int i = 0; i < arr1.length; ++i)
		{
			meetings.add(arr1[i].split(","));
		}
		return meetings;
	}
	
	public static Vector<String[]> getAguMeetings()
	{
		Vector<String[]> meetings = new Vector<String[]>();
		
		//IN sessions
		//meetings.add( new String[] { "fm10","IN" } );
		meetings.add( new String[] { "ja10","IN" } );
		meetings.add( new String[] { "fm09","IN" } );
		meetings.add( new String[] { "ja09","IN" } );
		meetings.add( new String[] { "fm08","IN" } );
		meetings.add( new String[] { "ja08","IN" } );
		meetings.add( new String[] { "fm07","IN" } );
		meetings.add( new String[] { "sm07","IN" } );
		meetings.add( new String[] { "fm06","IN" } );
		meetings.add( new String[] { "sm06","IN" } );
		meetings.add( new String[] { "fm05","IN" } );
		meetings.add( new String[] { "sm05","IN" } );
		
		/*/OS sessions
		meetings.add( new String[] { "fm10","OS" } );
		meetings.add( new String[] { "ja10","OS" } );
		meetings.add( new String[] { "fm09","OS" } );
		meetings.add( new String[] { "ja09","OS" } );
		meetings.add( new String[] { "fm08","OS" } );
		meetings.add( new String[] { "ja08","OS" } );
		meetings.add( new String[] { "fm07","OS" } );
		meetings.add( new String[] { "sm07","OS" } );
		meetings.add( new String[] { "fm06","OS" } );
		meetings.add( new String[] { "sm06","OS" } );
		meetings.add( new String[] { "fm05","OS" } );
		meetings.add( new String[] { "sm05","OS" } );
		
		//ED sessions
		meetings.add( new String[] { "fm10","ED" } );
		meetings.add( new String[] { "ja10","ED" } );
		meetings.add( new String[] { "fm09","ED" } );
		meetings.add( new String[] { "ja09","ED" } );
		meetings.add( new String[] { "fm08","ED" } );
		meetings.add( new String[] { "ja08","ED" } );
		meetings.add( new String[] { "fm07","ED" } );
		meetings.add( new String[] { "sm07","ED" } );
		meetings.add( new String[] { "fm06","ED" } );
		meetings.add( new String[] { "sm06","ED" } );
		meetings.add( new String[] { "fm05","ED" } );
		meetings.add( new String[] { "sm05","ED" } );
		
		//SH sessions
		meetings.add( new String[] { "fm10","SH" } );
		meetings.add( new String[] { "ja10","SH" } );
		meetings.add( new String[] { "fm09","SH" } );
		meetings.add( new String[] { "ja09","SH" } );
		meetings.add( new String[] { "fm08","SH" } );
		meetings.add( new String[] { "ja08","SH" } );
		meetings.add( new String[] { "fm07","SH" } );
		meetings.add( new String[] { "sm07","SH" } );
		meetings.add( new String[] { "fm06","SH" } );
		meetings.add( new String[] { "sm06","SH" } );
		meetings.add( new String[] { "fm05","SH" } );
		meetings.add( new String[] { "sm05","SH" } );*/
		
		return meetings;
	}
	
	public static Vector<String> duplicatedAbstracts()
	{
		Vector<String> abstracts = new Vector<String>();
		//2006_Fall_Meeting_SH31A-0393A.rdf
		//2010_Meeting_of_the_Americas_SH33E-02.rdf
		//2010_Meeting_of_the_Americas_SH42A-01.rdf
		//2008_Fall_Meeting_OS12A-08.rdf
		//2008_Fall_Meeting_OS33A-1318.rdf
		//2008_Fall_Meeting_OS43D-1327.rdf
		//2010_Meeting_of_the_Americas_OS23C-02.rdf
		return abstracts;
	}
	
	public static Vector<String> malformedAbstracts()
	{
		Vector<String> abstracts = new Vector<String>();
		//Fall_Meeting_2005_SH13A-0304.rdf
		//2009_Fall_Meeting_SH33B-1495.rdf
		//2008_Fall_Meeting_SH41A-1600.rdf
		//2008_Fall_Meeting_SH31A-1664.rdf
		//2006_Fall_Meeting_OS41A-0562.rdf
		//2006_Joint_Assembly_OS31A-14.rdf
		//2008_Fall_Meeting_OS31B-1262.rdf
		return abstracts;
	}
	
	public static Vector<String> unparsedAbstracts()
	{
		Vector<String> abstracts = new Vector<String>();
		//abstracts.add("/cgi-bin/wais?cc=ED13C-03");
		//abstracts.add("/cgi-bin/wais?mm=SH31B-1678");
		//abstracts.add("/cgi-bin/wais?mm=SH43A-1643");
		//abstracts.add("/cgi-bin/wais?cc=OS34A-04");
		//abstracts.add("/cgi-bin/wais?ii=OS34B-04");
		//abstracts.add("/cgi-bin/wais?mm=OS11A-1108");
		//abstracts.add("/cgi-bin/wais?cc=A34A-04");
		//abstracts.add("/cgi-bin/wais?cc=A34A-05");
		//abstracts.add("/cgi-bin/wais?cc=A41F-05");
		//abstracts.add("/cgi-bin/wais?mm=A21D-0219");
		//abstracts.add("/cgi-bin/wais?mm=A23B-0297");
		//abstracts.add("/cgi-bin/wais?mm=A23B-0297");
		return abstracts;
	}
	
	public static Vector<String> skippedAbstracts()
	{
		Vector<String> abstracts = new Vector<String>();
		abstracts.add("/cgi-bin/wais?rr=IN13A-19");
		abstracts.add("/cgi-bin/SFgate/SFgate?language=English&verbose=0&listenv=table&application=multismfm&convert=&converthl=&refinequery=&formintern=&formextern=&transquery=sc%3dinformatics%20and%20in42a-06&_lines=&multiple=0&descriptor=%2fdata%2fepubs%2fwais%2findexes%2ffm06%2ffm06%7c216%7c3905%7cNASA%27s%20Earth%20Science%20Gateway:%20A%20Platform%20for%20Interoperable%20Services%20in%20Support%20of%20the%20GEOSS%20Architecture%7cHTML%7clocalhost:0%7c%2fdata%2fepubs%2fwais%2findexes%2ffm06%2ffm06%7c21243172%2021247077%20%2fdata2%2fepubs%2fwais%2fdata%2ffm06%2ffm06.txt");
		return abstracts;
	}
	
	public static AbstractType getAbstractType(String s)
	{
		if (s != null)
		{
			if (s.toLowerCase().contains("poster"))
			{
				return AbstractType.POSTER;
			}
			else if (s.toLowerCase().contains("invited"))
			{
				return AbstractType.PRESENTATION;
			}	
			else if (s.toLowerCase().contains("withdrawn"))
			{
				return AbstractType.WITHDRAWN;
			}
			else
			{
				return AbstractType.DEFAULT;
			}
		}
		else
		{
			return AbstractType.DEFAULT;
		}
	}
	
	public static MeetingType getMeetingType(Meeting meeting)
	{
		String[] tokens = meeting.getName().split(" ");
		int mid = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			if (tokens[i].equals("Fall"))
			{
				mid = 0;
			}
			else if (tokens[i].equals("Joint") || tokens[i].equals("Assembly"))
			{
				mid = 1;
			}
			else if (tokens[i].equals("Americas"))
			{
				mid = 2;
			}
		}
		MeetingType mt = MeetingType.FALL;
		if (mid == 1) mt = MeetingType.SPRING;
		if (mid == 2) mt = MeetingType.AMERICAS;
		return mt;
	}
	
	public static int getMeetingYear(Meeting meeting)
	{
		String[] tokens = meeting.getName().split(" ");
		int year = -1;
		for (int i = 0; i < tokens.length; ++i)
		{
			try
			{
				year = Integer.parseInt(tokens[i]);
			}
			catch (NumberFormatException e) {}
		}
		return year;
	}
	
	@SuppressWarnings("rawtypes")
	public static String buildAbstractConstraintString(Collection constraints, boolean conjunctive) throws InvalidAbstractConstraintException
	{
		if (constraints == null) { return ""; }
		Vector<Person> personConstraints = new Vector<Person>();
		Vector<Organization> orgConstraints = new Vector<Organization>();
		Vector<Session> sessionConstraints = new Vector<Session>();
		Vector<Section> sectionConstraints = new Vector<Section>();
		Vector<Meeting> meetingConstraints = new Vector<Meeting>();
		Vector<Keyword> keywordConstraints = new Vector<Keyword>();
		Vector<AbstractType> typeConstraints = new Vector<AbstractType>();
		
		Iterator iter = constraints.iterator();
		while (iter.hasNext())
		{
			Object o = iter.next();
			if (o.getClass().equals(Person.class))
			{
				personConstraints.add((Person)o);
			}
			else if (o.getClass().equals(Organization.class))
			{
				orgConstraints.add((Organization)o);
			}
			else if (o.getClass().equals(Session.class) && !conjunctive)
			{
				sessionConstraints.add((Session)o);
			}	
			else if (o.getClass().equals(Session.class))
			{
				if (sessionConstraints.size() == 0)
				{
					sessionConstraints.add((Session)o);
				}
				else
				{
					throw new InvalidAbstractConstraintException();
				}
			}
			else if (o.getClass().equals(Section.class) && !conjunctive)
			{
				sectionConstraints.add((Section)o);
			}	
			else if (o.getClass().equals(Section.class))
			{
				if (sectionConstraints.size() == 0)
				{
					sectionConstraints.add((Section)o);
				}
				else
				{
					throw new InvalidAbstractConstraintException();
				}
			}
			else if (o.getClass().equals(Meeting.class) && !conjunctive)
			{
				meetingConstraints.add((Meeting)o);
			}	
			else if (o.getClass().equals(Meeting.class))
			{
				if (meetingConstraints.size() == 0)
				{
					meetingConstraints.add((Meeting)o);
				}
				else
				{
					throw new InvalidAbstractConstraintException();
				}
			}
			else if (o.getClass().equals(Keyword.class))
			{
				keywordConstraints.add((Keyword)o);
			}
			else if (o.getClass().equals(AbstractType.class) && !conjunctive)
			{
				typeConstraints.add((AbstractType)o);
			}	
			else if (o.getClass().equals(AbstractType.class))
			{
				if (typeConstraints.size() == 0)
				{
					typeConstraints.add((AbstractType)o);
				}
				else
				{
					throw new InvalidAbstractConstraintException();
				}
			}
			else
			{
				throw new InvalidAbstractConstraintException();
			}
		}
		
		StringWriter sw = new StringWriter();		
		if (conjunctive)
		{
			
		}
		else
		{
/*			for (int i = 0; i < personConstraints.size(); ++i)
			{
				StringWriter local = new StringWriter();
				Person p = personConstraints.get(i);
				sw.write("{ ?abstract tw:hasPersonWithRole ?role . ");
				sw.write("?person tw:hasRole ?role . ");
				sw.write("?person foaf:name ?name . ");
				sw.write(" FILTER (?name = \"" + p.getName() + "\") }");
				if (i < personConstraints.size() - 1) sw.write(" UNION ");
			}
			for (int i = 0; i < orgConstraints.size(); ++i)
			{
				StringWriter local = new StringWriter();
				Organization o = orgConstraints.get(i);
				sw.write("{ ?abstract tw:hasPersonWithRole ?role . ");
				sw.write("?role tw:withAffiliation ?org . ");
				sw.write("?org dc:description ?desc . ");
				sw.write(" FILTER (?desc = \"" + o.toString() + "\") }");
				if (i < orgConstraints.size() - 1) sw.write(" UNION ");
			}
			for (int i = 0; i < sessionConstraints.size(); ++i)
			{
				StringWriter local = new StringWriter();
				Organization o = orgConstraints.get(i);
				sw.write("{ ?abstract tw: . ");
				sw.write("?role tw:withAffiliation ?org . ");
				sw.write("?org dc:description ?desc . ");
				sw.write(" FILTER (?desc = \"" + o.toString() + "\") }");
				if (i < orgConstraints.size() - 1) sw.write(" UNION ");
			}*/
		}
		
		return sw.toString();
	}
}
