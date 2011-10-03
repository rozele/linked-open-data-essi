package org.agu.essi.web.spotlight;

import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.annotation.AnnotatedText;
import org.agu.essi.annotation.Annotation;
import org.agu.essi.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Class for annotating text with DBPedia Spotlight and writing those annotations in RDF
 * @author Eric Rozell
 */
public class SpotlightAnnotator implements AnnotatedText
{
	private static String annotationService = "http://spotlight.dbpedia.org/rest/annotate";
	private String text;
	private Vector<Annotation> annotations;
	private double confidence = 0.0;
	private int support = 0;
	
	/**
	 * Constructor for annotator from free text
	 * @param t the free text to be annotated
	 */
	public SpotlightAnnotator(String t)
	{
		text = t;
		annotate();
	}
	
	public String getText() 
	{
		return text;
	}

	public Vector<Annotation> getAnnotations() 
	{
		return annotations;
	}

	private void annotate()
	{
		String loc = annotationService + "?text=" + text + "&confidence=" + confidence + "&support=" + support;
		try 
		{
			URL url = new URL(loc);
			URLConnection conn = url.openConnection();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(conn.getInputStream());
			NodeList nl = dom.getElementsByTagName("Resource");
			for (int i = 0; i < nl.getLength(); ++i)
			{
				Node n = nl.item(i);
				SpotlightAnnotation annotation = new SpotlightAnnotation(n);
				annotations.add(annotation);
			}
		} 
		catch (MalformedURLException e) 
		{
			System.err.println("Unable to instantiate URL from location: " + loc);
			e.printStackTrace();
		}
		catch (IOException e)
		{
			System.err.println("Unable to retrieve XML from the location: " + loc);
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			System.err.println("Retrieved malformed XML from the location: " + loc);
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}	
	}
	
	public String toString(String format)
	{
		if (Utils.isRdfXmlFormat(format))
		{
			return writeToRdfXml();
		}
		else 
		{
			return toString();
		}
	}
	
	private String writeToRdfXml()
	{
		StringWriter sw = new StringWriter();
		return sw.toString();
		
	}
}