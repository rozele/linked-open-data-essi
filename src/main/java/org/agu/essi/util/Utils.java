package org.agu.essi.util;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Vector;

import org.agu.essi.AbstractType;
import org.agu.essi.Meeting;
import org.agu.essi.MeetingType;
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
		sw.write("<rdf:RDF xmlns=\"" + Namespaces.esipOwl + "\"\n");
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
		sw.write("         xml:base=\"" + Namespaces.esip + "\">\n");
		return sw.toString();
	}
	
	public static String writeSparqlPrefixes()
	{
		StringWriter sw = new StringWriter();
		sw.write("PREFIX owl: <" + Namespaces.owl + ">\n");
		sw.write("PREFIX esip: <" + Namespaces.esipOwl + ">\n");
		sw.write("PREFIX xsd: <" + Namespaces.xsd + ">\n");
		sw.write("PREFIX tw: <" + Namespaces.tw + ">\n");
		sw.write("PREFIX foaf: <" + Namespaces.foaf + ">\n");
		sw.write("PREFIX swrc: <" + Namespaces.swrc + ">\n");
		sw.write("PREFIX swc: <" + Namespaces.swc + ">\n");
		sw.write("PREFIX geo: <" + Namespaces.geo + ">\n");
		sw.write("PREFIX skos: <" + Namespaces.skos + ">\n");
		sw.write("PREFIX dbanno: <" + Namespaces.dbanno + ">\n");
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
		sw.write("  <!ENTITY esip \"" + Namespaces.esipOwl + "\" >\n");
		sw.write("  <!ENTITY xsd \"" + Namespaces.xsd + "\" >\n");
		sw.write("  <!ENTITY tw \"" + Namespaces.tw + "\" >\n");
		sw.write("  <!ENTITY foaf \"" + Namespaces.foaf + "\" >\n");
		sw.write("  <!ENTITY swrc \"" + Namespaces.swrc + "\" >\n");
		sw.write("  <!ENTITY swc \"" + Namespaces.swc + "\" >\n");
		sw.write("  <!ENTITY geo \"" + Namespaces.geo + "\" >\n");
		sw.write("  <!ENTITY skos \"" + Namespaces.skos + "\" >\n");
		sw.write("  <!ENTITY dbanno \"" + Namespaces.dbanno + "\" >\n");
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
	
	public static Vector<String> getAguMeetings()
	{
		Vector<String> meetings = new Vector<String>();
		meetings.add( "fm10;IN");
		meetings.add( "ja10;IN");
		meetings.add( "fm09;IN");
		meetings.add( "ja09;IN");
		meetings.add( "fm08;IN");
		meetings.add( "ja08;IN");
		meetings.add( "fm07;IN");
		meetings.add( "sm07;IN");
		meetings.add( "fm06;IN");
		meetings.add( "sm06;IN");
		meetings.add( "fm05;IN");
		meetings.add( "sm05;IN");
		return meetings;
	}
	
	public static Vector<String> getMissedAbstracts()
	{
		Vector<String> abstracts = new Vector<String>();
		abstracts.add("http://www.agu.org/cgi-bin/wais?rr=IN13A-19");
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
}
