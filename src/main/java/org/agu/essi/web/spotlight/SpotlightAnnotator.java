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
import java.util.Date;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.annotation.AnnotatedText;
import org.agu.essi.annotation.Annotation;
import org.agu.essi.util.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for annotating text with DBPedia Spotlight and writing those annotations in RDF
 * @author Eric Rozell
 */
public class SpotlightAnnotator implements AnnotatedText
{
	static final Log log = LogFactory.getLog(SpotlightAnnotator.class);
	
	private static String annotationService = "spotlight.dbpedia.org";
	private static String annotationPath = "/rest/annotate?text=";
	private static String annotationEncoding = "text/xml; charset=\"utf-8\"";
	private String text;
	private Vector <Annotation> annotations = new Vector <Annotation> ();
	private double confidence = 0.5;
	private int support = 0;
	private StringBuilder date;
	private static String newLine = "\n";
	
	/**
	 * Constructor for annotator from free text
	 * @param t the free text to be annotated
	 */
	public SpotlightAnnotator(String t)
	{
		text = t;
		Date dateNow = new Date ();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); 
        date = new StringBuilder( dateFormat.format( dateNow ) );
		annotate();
	}
	
	/**
	 * Method for changing the confidence used in calls to DBpedia Spotlight (default is 0.5)
	 * @param c the new confidence level
	 */
	public void setConfidence ( double c ) { confidence = c; }
	
	/**
	 * Method for changing the support used in calls to DBpedia Spotlight (default is 0)
	 * @param s the new support level
	 */
	public void setSupport ( int s ) { support = s; }
	
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
	    	log.error("Unknown host: " + annotationService);
    	    e.printStackTrace();
    	} catch (UnsupportedEncodingException e) {
    	    log.error("Unsupported Encoding: " + annotationEncoding);
    	    e.printStackTrace();
    	} catch (Exception e) { e.printStackTrace(); }	
        
    	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    	
    	try {
        
    	  DocumentBuilder db = dbf.newDocumentBuilder();
          Document dom = db.parse( new ByteArrayInputStream(result.getBytes("UTF-8")) );
          NodeList nl = dom.getElementsByTagName("Resource");
          for (int i = 0; i < nl.getLength(); ++i) {
            Node n = nl.item(i);           
            SpotlightAnnotation annotation = new SpotlightAnnotation(n, confidence);
            annotations.add(annotation); 
          }
    	} catch ( Exception e ) { log.error(e); }
        
	}
	
	/**
	 * Returns the DBpedia Spotlight service as an RDF FOAF Agent.
	 * This Agent RDF can be used as the source for annotation RDF.
	 * @return sw RDF/XML String
	 */
	public static String writeSpotlightAgentRDF () {
		
		StringWriter sw = new StringWriter();
		
		sw.append(Utils.writeXmlHeader());
		sw.append(Utils.writeDocumentEntities());
		sw.append(Utils.writeRdfHeader());
		
		sw.append( "<rdf:Description rdf:about=\"http://spotlight.dbpedia.org/rest/annotate\">" + newLine );
	    sw.append( "  <rdf:type rdf:resource=\"http://xmlns.com/foaf/0.1/Agent\"/>" + newLine );
	    sw.append( "  <foaf:name>DBpedia Spotlight Service</foaf:name>" + newLine );
	    sw.append( "</rdf:Description>" + newLine );
	    
	    sw.append(Utils.writeRdfFooter());
	    
	    return sw.toString();
	    
	}
	
	public String writeAnnotationTextToRdfXml ( String surfaceForm, Vector <String> dbpediaTypes, 
		double similarityScore, double psr, String abstractURI, String dbpediaConcept, int offset ) {
	
		StringWriter sw = new StringWriter();
		String sdURI = abstractURI.replace("Abstract", "SourceDocument");
		String floatType = "rdf:datatype=\"http://www.w3.org/2001/XMLSchema#float\"";
		String stringType = "rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"";
		String sfURI = surfaceForm.replace(" ", "_");
		String annTextURI = abstractURI.replace("Abstract", "AnnotationTextSelector_" + sfURI);
		
		sw.append( "<rdf:Description rdf:about=\"" + Utils.cleanXml(annTextURI) + "\">" + newLine );
		sw.append( "  <rdf:type rdf:resource=\"&dbanno;DBpediaSpotlightSelector\"/>" + newLine );
		 
		sw.append( "  <aos:offset " + floatType + ">" + offset + "</aos:offset>" + newLine );
		sw.append( "  <aos:range " + floatType + ">" + surfaceForm.length() + "</aos:range>" + newLine );
		sw.append( "  <aos:exact " + stringType + ">" + Utils.cleanXml(surfaceForm) + "</aos:exact>" + newLine );
		
		sw.append( "  <dbanno:dbpediaConcept " + stringType + ">" + dbpediaConcept + "</dbanno:dbpediaConcept>" + newLine );
		sw.append( "  <dbanno:dbpediaSupport " + floatType + ">" + support + "</dbanno:dbpediaSupport>" + newLine );
		sw.append( "  <dbanno:dbpediaConfidence " + floatType + ">" + confidence + "</dbanno:dbpediaConfidence>" + newLine );
		for ( int i=0; i<dbpediaTypes.size(); i++ ) {
		  sw.append( "  <dbanno:dbpediaType " + stringType + ">" + Utils.cleanXml(dbpediaTypes.get(i)) + "</dbanno:dbpediaType>" + newLine );
		}
		sw.append( "  <dbanno:dbpediaSimilarityScore " + floatType + ">"+ similarityScore + 
				"</dbanno:dbpediaSimilarityScore>" + newLine );
		sw.append( "  <dbanno:dbpediaPercentOfSecondRank " + floatType + ">" + psr + 
				"</dbanno:dbpediaPercentOfSecondRank>" + newLine );
		sw.append( "  <aof:onDocument rdf:resource=\"" + Utils.cleanXml(abstractURI) + "\"/>" + newLine );
		sw.append( "  <ao:onSourceDocument rdf:resource=\"" + Utils.cleanXml(sdURI) + "\"/>" + newLine );
		sw.append( "</rdf:Description>" + newLine );
    
		return sw.toString();
    
	}
	
	public String writeAnnotationProvenanceToRdfXml ( String abstractURI ) {
	
		StringWriter sw = new StringWriter();
		String sdURI = abstractURI.replace("Abstract", "SourceDocument");
		sw.append( "<rdf:Description rdf:about=\"" + Utils.cleanXml(sdURI) + "\">" + newLine );
		sw.append( "  <rdf:type rdf:resource=\"&pav;SourceDocument\"/>" + newLine );
		sw.append( "  <pav:retrievedFrom rdf:resource=\"" + Utils.cleanXml(abstractURI) + "\"/>" + newLine );
		sw.append( "  <pav:sourceAccessedOn>" + date + "</pav:sourceAccessedOn>" + newLine );
		sw.append( "</rdf:Description>" + newLine ); 
		    
	    return sw.toString();
		    
	}
	
	public String writeAnnotationToRdfXml( String abstractURI, Vector <String> dbpediaURIs, Vector <String> surfaceForms )
	{
		StringWriter sw = new StringWriter();
		String annotationURI = abstractURI.replace("Abstract","Annotation");

		sw.append( "<rdf:Description rdf:about=\"" + Utils.cleanXml(annotationURI) + "\">" + newLine );
		sw.append( "  <rdf:type rdf:resource=\"&aot;ExactQualifier\"/>" + newLine );
		sw.append( "  <rdf:type rdf:resource=\"&aot;Qualifier\"/>" + newLine );
		sw.append( "  <rdf:type rdf:resource=\"&ao;Annotation\"/>" + newLine );
		sw.append( "  <rdf:type rdf:resource=\"&aocore;Annotation\"/>" + newLine );

		// reference to the text that was annotated - an AO Selector instance
		for ( int i=0; i<surfaceForms.size(); i++ ) {
		  String sfURI = surfaceForms.get(i).replace(" ", "_");
		  String annTextURI = abstractURI.replace("Abstract", "AnnotationTextSelector_" + sfURI);
		  sw.append( "  <ao:context rdf:resource=\"" + Utils.cleanXml(annTextURI) + "\"/>" + newLine );
		}

		// a reference to the AGU abstract we are annotating
		sw.append( "  <aof:annotatesDocument rdf:resource=\"" + Utils.cleanXml(abstractURI) + "\"/>" + newLine );

		// abstract has the following DBpedia concepts
		for ( int i=0; i<dbpediaURIs.size(); i++ ) {
		  sw.append( "  <ao:hasTopic rdf:resource=\"" + Utils.cleanXml(dbpediaURIs.get(i)) + "\"/>" + newLine );
		}

		// reference to dbpedia spotlight rdf and creation date
		sw.append( "  <pav:createdBy rdf:resource=\"http://spotlight.dbpedia.org/rest/annotate\"/>" + newLine );
		sw.append( "  <pav:createdOn>" + date + "</pav:createdOn>" + newLine );

		sw.append( "</rdf:Description>" + newLine );

		return sw.toString();
		
	}

}