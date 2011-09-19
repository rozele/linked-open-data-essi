package org.agu.essi.util;

import java.io.StringWriter;
import java.util.HashMap;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * Utility functions
 * @author Eric Rozell
 */
public class Utils {
	
	public static String clean(String s)
	{
		return fixSpecialChars(iconv(StringEscapeUtils.escapeXml(s),"UTF-8"));
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
	
	//this is a hack dat
	public static String fixSpecialChars(String s)
	{
		//s = s.replaceAll("&quot;", ""/*"&#34;"*/);
		//s = s.replaceAll("&apos;", ""/*"&#39;"*/);
		s = s.replaceAll("\\p{Cntrl}", "");
		return s;
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
		sw.write("         xml:base=\"" + Namespaces.esip + "\">\n");
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
		sw.write("]>\n");
		return sw.toString();	
	}
	
	public static HashMap<String,String> getAguDatabases()
	{
		HashMap <String, String> aguDatabases = new HashMap <String, String> ();
		aguDatabases.put( "fm10", "/data/epubs/wais/indexes/fm10/fm10");	
		//aguDatabases.put( "ja10", "/data/epubs/wais/indexes/ja10/ja10");	
		//aguDatabases.put( "fm09", "/data/epubs/wais/indexes/fm09/fm09");	
		//aguDatabases.put( "ja09", "/data/epubs/wais/indexes/ja09/ja09");	
		//aguDatabases.put( "fm08", "/data/epubs/wais/indexes/fm08/fm08");	
		//aguDatabases.put( "ja08", "/data/epubs/wais/indexes/ja08/ja08");	
		//aguDatabases.put( "fm07", "/data/epubs/wais/indexes/fm07/fm07");	
		//aguDatabases.put( "sm07", "/data/epubs/wais/indexes/sm07/sm07");	
		//aguDatabases.put( "fm06", "/data/epubs/wais/indexes/fm06/fm06");	
		//aguDatabases.put( "sm06", "/data/epubs/wais/indexes/sm06/sm06");	
		//aguDatabases.put( "fm05", "/data/epubs/wais/indexes/fm05/fm05");	
		//aguDatabases.put( "sm05", "/data/epubs/wais/indexes/sm05/sm05");
		return aguDatabases;
	}	
}
