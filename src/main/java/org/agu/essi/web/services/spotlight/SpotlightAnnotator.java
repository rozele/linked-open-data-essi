package org.agu.essi.web.services.spotlight;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.util.AnnotatedText;
import org.agu.essi.util.Annotation;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SpotlightAnnotator implements AnnotatedText
{
	private static String annotationService = "http://spotlight.dbpedia.org/rest/annotate";
	private String text;
	private Vector<Annotation> annotations;
	private double confidence = 0.0;
	private int support = 0;
	
	public SpotlightAnnotator(String t)
	{
		text = t;
	}
	
	public String getText() 
	{
		return text;
	}

	public Vector<Annotation> getAnnotations() 
	{
		return annotations;
	}

	private void makeSpotlightRequest()
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
}