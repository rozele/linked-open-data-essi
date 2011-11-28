package org.agu.essi.data;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.agu.essi.web.spotlight.SpotlightAnnotationToRdfXml;
import org.agu.essi.web.spotlight.SpotlightAnnotator;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class SparqlUnannotatedAbstractData {

	private String serviceUrl = "http://logd.tw.rpi.edu/ws/sparqlproxy.php";
	
 	//  PREFIX essi: <http://essi-lod.org/ontology#>
	//	PREFIX swrc: <http://swrc.ontoware.org/ontology#>
	//	PREFIX pav:  <http://purl.org/pav/>
	//	SELECT ?id ?abstract
	//	FROM <http://essi-lod.org/instances/> 
	//	WHERE {
	//	  ?id swrc:abstract ?abstract . 
	//	  OPTIONAL { ?sourceDoc pav:retrievedFrom ?id . }
	//	  FILTER (!bound(?sourceDoc))
	//	} 
	//	ORDER BY ?id
	//	LIMIT 10
	//	OFFSET 10
	
	private String getQueryString( String limit, String offset, String port ) {
	
	  return "?query-option=text&query=" + 
		"PREFIX+essi%3A+%3Chttp%3A%2F%2Fessi-lod.org" +
		"%2Fontology%23%3E%0D%0APREFIX+swrc%3A+%3Chttp%3A%2F%2Fswrc.ontoware.org%2Fontology%23%3E%0D%0APREFIX+pav%3A++" +
		"%3Chttp%3A%2F%2Fpurl.org%2Fpav%2F%3E%0D%0ASELECT+%3Fid+%3Fabstract%0D%0AFROM+%3Chttp%3A%2F%2Fessi-lod.org%2F" +
		"instances%2F%3E+%0D%0AWHERE+{%0D%0A++%3Fid+swrc%3Aabstract+%3Fabstract+.+%0D%0A++OPTIONAL+{+%3FsourceDoc+pav%3A" +
		"retrievedFrom+%3Fid+.+}%0D%0A++FILTER+%28!bound%28%3FsourceDoc%29%29%0D%0A}+%0D%0AORDER+BY+%3Fid%0D%0ALIMIT" +
		"+" + limit + "%0D%0AOFFSET+" + offset + "&service-uri=http%3A%2F%2Faquarius.tw.rpi.edu%3A" + port + 
		"%2Fsparql";
	
	}
	
	private boolean isElementNode ( Node node ) {
		
		short type = node.getNodeType();
        if ( type == Node.ELEMENT_NODE ) { return true; } else { return false; }
        	
	}

	public void annotate ( String limit, String offset, String port, String outputDir ) {
		
	  // Configure the annotation writer
      SpotlightAnnotationToRdfXml sWriter = new SpotlightAnnotationToRdfXml ();
      sWriter.setOutputDirectory( outputDir );
      sWriter.writeSpotlightProvenance();
    	
	  String id = "";
	  String abs = "";
	  String result = "";
	  try {
    	            
            // Send data
            URL url = new URL( serviceUrl );
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( getQueryString(limit, offset, port) );
            wr.flush();

            // Get the response
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) { result = result + line; }
            wr.close();
            rd.close();
            
            // Parse the response
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();    	
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document dom = db.parse( new ByteArrayInputStream(result.getBytes("UTF-8")) );
            NodeList nl = dom.getElementsByTagName("result");
            System.out.println("Number of Results: " + nl.getLength() );
            for (int i=0; i<nl.getLength(); i++) {
               Node n = nl.item(i);
               NodeList children = n.getChildNodes();
               for (int j=0; j<children.getLength(); j++) {
            	  Node cn = children.item(j);
            	  if ( isElementNode(cn) ) { 
            	    if ( cn.getNodeName().equals("binding") ) {
            	    
            	      NamedNodeMap nm = cn.getAttributes();
            	      String value = nm.getNamedItem("name").getTextContent();
            	      if ( value.equals("id") ) {
            	        NodeList idList = cn.getChildNodes();
            	        for (int k=0; k<idList.getLength(); k++) { 
            	          Node idNode = idList.item(k);
            	          if ( isElementNode(idNode) ) { id = idNode.getTextContent(); }
            	        }
            	      }
            	      
            	      if ( value.equals("abstract") ) {
            	    	NodeList abstrList = cn.getChildNodes();
              	        for (int k=0; k<abstrList.getLength(); k++) { 
              	          Node abNode = abstrList.item(k);
              	          if ( isElementNode(abNode) ) { abs = abNode.getTextContent(); }
              	        }
              	      }
            	      
            	    } // end if binding
            	    
            	    // create annotations for this binding    	    	    	
    	    	    SpotlightAnnotator annotator = new SpotlightAnnotator ( abs );
    	      		Vector <org.agu.essi.annotation.Annotation> annotations = annotator.getAnnotations();
    	      		sWriter.annotationsToRDF( annotations, annotator, id );
    	      		  
    	    	  } // end if element node
    	    	    
               } // end loop over all result bindings
                      
            } // end loop over result sub-elements
            
      } catch (Exception e) { System.out.println(e); }
  	  
	}

}