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
	private double confidence = 0.5;
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
			String a1 = writeAnnotationTextToRdfXml ();
			String a2 = writeAnnotationProvenanceToRdfXml ();
			String a3 = writeAnnotationToRdfXml ();
			return a1+a2+a3;
		}
		else 
		{
			return toString();
		}
	}
	
	public String writeSpotlightAgentRDF () {
		
		StringWriter sw = new StringWriter();
		 <rdf:Description rdf:about="http://spotlight.dbpedia.org/rest/annotate">

	        <rdf:type rdf:resource="http://xmlns.com/foaf/0.1/Agent"/>
	        <foaf:name>Paolo Ciccarese</foaf:name>

	    </rdf:Description> 
	    
	}
	
	private String writeAnnotationTextToRdfXml () {
	
		<rdf:Description rdf:about="http://esipfed.org/essi-lod/instances/annotation_selection_001">
        <rdf:type rdf:resource="&essi;ESSIselector"/>
        <aos:exact>surface form text here</aos:exact>
	<essi:support/>
	<essi:dbPediaConcept />
	<essi:dbPediaSupport />
	<essi:dbPediaType />
	<essi:dbPediaSimilarityScore />
	<essi:dbPediaPercentOfSecondRank />
        <aof:onDocument rdf:resource="http://esipfed.org/essi-lod/instances/abstract_001"/>
        <ao:onSourceDocument rdf:resource="http://esipfed.org/essi-lod/instances/source_doc_001"/>
    </rdf:Description>
    	return sw;
	}
	
	private String writeAnnotationProvenanceToRdfXml () {
	
		    <rdf:Description rdf:about="http://esipfed.org/essi-lod/instances/source_doc_001">
		        <rdf:type rdf:resource="&pav;SourceDocument"/>
		        <pav:retrievedFrom rdf:resource="http://esipfed.org/essi-lod/instances/abstract_001"/>
		        <pav:sourceAccessedOn>2010-03-26</pav:sourceAccessedOn>
		    </rdf:Description> 
		    
	}
	
	private String writeAnnotationToRdfXml( String annotationID, Vector <String> anotationURIs,  )
	{
		StringWriter sw = new StringWriter();
		
		sw.append( Utils.writeXmlHeader() );
	    sw.append( Utils.writeDocumentEntities() );
		sw.append( Utils.writeRdfHeader() );

		sw.append( "<rdf:Description rdf:about=\"" + annotationID + "\">" );

		  	
		        <rdf:type rdf:resource="&aot;ExactQualifier"/>
		        <rdf:type rdf:resource="&aot;Qualifier"/>
		        <rdf:type rdf:resource="&ao;Annotation"/>
		        <rdf:type rdf:resource="&ann;Annotation"/>

		        // reference to the text that was annotated - an AO  Selector instance
		        <ao:context rdf:resource="http://esipfed.org/essi-lod/instances/annotation_selection_001"/>
		        <ao:context rdf:resource="http://esipfed.org/essi-lod/instances/annotation_selection_002"/>

			// a reference to the AGU abstract we are annotating
		        <aof:annotatesDocument rdf:resource="http://esipfed.org/essi-lod/instances/abstract_001"/>

			// abstract has the following DBpedia concepts
		        <ao:hasTopic rdf:resource="http://dbpedia.uri.../concept1"/>
		        <ao:hasTopic rdf:resource="http://dbpedia.uri.../concept2"/>

		        <pav:createdBy rdf:resource="http://spotlight.dbpedia.org/rest/annotate"/>
		        <pav:createdOn>2011-10-01</pav:createdOn>

		    </rdf:Description> 

		return sw.toString();
		
	}
}