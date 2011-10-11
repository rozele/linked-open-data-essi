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
	private StringBuilder date;
	
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
	
	/**
	 * Returns the DBpedia Spotlight service as an RDF FOAF Agent.
	 * This Agent RDF can be used as the source for annotation RDF.
	 * @return sw RDF/XML String
	 */
	public static String writeSpotlightAgentRDF () {
		
		StringWriter sw = new StringWriter();
		
		sw.append( Utils.writeXmlHeader() );
	    sw.append( Utils.writeDocumentEntities() );
		sw.append( Utils.writeRdfHeader() );
		
		sw.append( "<rdf:Description rdf:about=\"http://spotlight.dbpedia.org/rest/annotate\">" );
	    sw.append( "  <rdf:type rdf:resource=\"http://xmlns.com/foaf/0.1/Agent\"/>" );
	    sw.append( "  <foaf:name>DBpedia Spotlight Service</foaf:name>" );
	    sw.append( "</rdf:Description>" );
	    
		sw.append( Utils.writeRdfFooter() );
	    return sw.toString();
	    
	}
	
	public String writeAnnotationTextToRdfXml ( String surfaceForm, Vector <String> dbpediaTypes, 
		double similarityScore, double psr, String abstractURI ) {
	
		StringWriter sw = new StringWriter();
		String sdURI = abstractURI.replace("Abstract", "SourceDocument");
		String dataType = "rdf:datatype=\"http://www.w3.org/2001/XMLSchema#string\"";
		String sfURI = surfaceForm.replace(" ", "_");
		String annTextURI = abstractURI.replace("Abstract", "AnnotationTextSelector_" + sfURI);
		
		sw.append( Utils.writeXmlHeader() );
	    sw.append( Utils.writeDocumentEntities() );
		sw.append( Utils.writeRdfHeader() );
		
		sw.append( "<rdf:Description rdf:about=\"" + annTextURI + "\">" );
		sw.append( "  <rdf:type rdf:resource=\"&dbanno;DBpediaSpotlightSelector\"/>" );
		 
		sw.append( "  <aos:exact " + dataType + ">" + surfaceForm + "</aos:exact>" );
		sw.append( "  <dbanno:dbpediaConcept " + dataType + ">" + "<dbanno:dbpediaConcept/>" );
		sw.append( "  <dbanno:dbpediaSupport " + dataType + ">" + support + "<dbanno:dbpediaSupport/>" );
		for ( int i=0; i<dbpediaTypes.size(); i++ ) {
		  sw.append( "  <dbanno:dbpediaType " + dataType + ">" + dbpediaTypes.get(i) + "<dbanno:dbpediaType/>" );
		}
		sw.append( "  <dbanno:dbpediaSimilarityScore " + dataType + ">"+ similarityScore + "<dbanno:dbpediaSimilarityScore/>" );
		sw.append( "  <dbanno:dbpediaPercentOfSecondRank " + dataType + ">" + psr +  "<dbanno:dbpediaPercentOfSecondRank/>" );
		sw.append( "  <aof:onDocument rdf:resource=\"" + abstractURI + "\"/>" );
		sw.append( "  <ao:onSourceDocument rdf:resource=\"" + sdURI + "\"/>" );
		sw.append( "</rdf:Description>" );
    
		sw.append( Utils.writeRdfFooter() );
		return sw.toString();
    
	}
	
	public String writeAnnotationProvenanceToRdfXml ( String abstractURI ) {
	
		StringWriter sw = new StringWriter();
		String sdURI = abstractURI.replace("Abstract", "SourceDocument");
		sw.append( "<rdf:Description rdf:about=\"" + sdURI + "\">" );
		sw.append( "  <rdf:type rdf:resource=\"&pav;SourceDocument\"/>" );
		sw.append( "  <pav:retrievedFrom rdf:resource=\"" + abstractURI + "\"/>" );
		sw.append( "  <pav:sourceAccessedOn>" + date + "</pav:sourceAccessedOn>" );
		sw.append( "</rdf:Description>" ); 
		    
		sw.append( Utils.writeRdfFooter() );
	    return sw.toString();
		    
	}
	
	public String writeAnnotationToRdfXml( String abstractURI, Vector <String> dbpediaURIs, Vector <String> surfaceForms )
	{
		StringWriter sw = new StringWriter();
		String annotationURI = abstractURI.replace("Abstract","Annotation");
		
		sw.append( Utils.writeXmlHeader() );
	    sw.append( Utils.writeDocumentEntities() );
		sw.append( Utils.writeRdfHeader() );

		sw.append( "<rdf:Description rdf:about=\"" + annotationURI + "\">" );
		sw.append( "  <rdf:type rdf:resource=\"&aot;ExactQualifier\"/>" );
		sw.append( "  <rdf:type rdf:resource=\"&aot;Qualifier\"/>" );
		sw.append( "  <rdf:type rdf:resource=\"&ao;Annotation\"/>" );
		sw.append( "  <rdf:type rdf:resource=\"&aocore;Annotation\"/>" );

		// reference to the text that was annotated - an AO Selector instance
		for ( int i=0; i<surfaceForms.size(); i++ ) {
		  String sfURI = surfaceForms.get(i).replace(" ", "_");
		  String annTextURI = abstractURI.replace("Abstract", "AnnotationTextSelector_" + sfURI);
		  sw.append( "  <ao:context rdf:resource=\"" + annTextURI + "\"/>" );
		}

		// a reference to the AGU abstract we are annotating
		sw.append( "  <aof:annotatesDocument rdf:resource=\" + abstractURI + \"/>" );

		// abstract has the following DBpedia concepts
		for ( int i=0; i<dbpediaURIs.size(); i++ ) {
		  sw.append( "  <ao:hasTopic rdf:resource=\"" + dbpediaURIs.get(i) + "\"/>" );
		}

		// reference to dbpedia spotlight rdf and creation date
		sw.append( "  <pav:createdBy rdf:resource=\"http://spotlight.dbpedia.org/rest/annotate\"/>" );
		sw.append( "  <pav:createdOn>" + date + "</pav:createdOn>" );

		sw.append( "</rdf:Description>");

		return sw.toString();
		
	}

}