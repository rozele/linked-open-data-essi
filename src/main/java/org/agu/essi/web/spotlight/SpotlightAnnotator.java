package org.agu.essi.web.spotlight;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.annotation.AnnotatedText;
import org.agu.essi.annotation.Annotation;
import org.agu.essi.util.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for annotating text with DBPedia Spotlight and writing those annotations in RDF
 * @author Eric Rozell
 */
public class SpotlightAnnotator implements AnnotatedText
{
	private static String annotationService = "spotlight.dbpedia.org";
	private static String annotationPath = "/rest/annotate?text=";
	private static String annotationEncoding = "text/xml; charset=\"utf-8\"";
	private String text;
	private Vector <Annotation> annotations = new Vector <Annotation> ();
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

	private void annotate ( ) {
		
		String result = "";
        int port = 80;
        try {
        	
          InetAddress addr = InetAddress.getByName( annotationService );
          Socket sock = new Socket(addr, port);
          BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(),"UTF-8"));
          text=text.replace(" ", "%20");
          wr.write("GET " + annotationPath + text + "&confidence=" + confidence + "&support=" + support + " HTTP/1.0\r\n");
          wr.write("Host: " + annotationService + "\r\n");
          wr.write("Content-Length: " + text.length() + "\r\n");
          wr.write("accept: " + annotationEncoding + "\r\n");
          wr.write("\r\n");
          wr.write(text);
          wr.flush();
          BufferedReader rd = new BufferedReader(new InputStreamReader(sock.getInputStream()));
          String line;
          String test = "";
          boolean httpHeader = true;
          while ((line = rd.readLine()) != null) {
   	        if ( httpHeader ) {
   	          if ( line.length() > 5 ) { test = line.substring(0,5); }
   	          if ( test.equals("<?xml") ) { httpHeader = false; result = result + line; }
   	        } else { result = result + line; }     
          }
        
	    } catch (UnknownHostException e) {
	    	System.err.println("Unknown host: " + annotationService);
    	    e.printStackTrace();
    	} catch (UnsupportedEncodingException e) {
    	    System.err.println("Unsupported Encoding: " + annotationEncoding);
    	    e.printStackTrace();
    	} catch (Exception e) { e.printStackTrace(); }	
        
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	try {
        
    	  DocumentBuilder db = dbf.newDocumentBuilder();
          Document dom = db.parse( new ByteArrayInputStream(result.getBytes("UTF-8")) );
          NodeList nl = dom.getElementsByTagName("Resource");
          for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);           
            SpotlightAnnotation annotation = new SpotlightAnnotation(n);
            annotations.add(annotation); 
          }
    	} catch ( Exception e ) { System.out.println(e); }
        
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