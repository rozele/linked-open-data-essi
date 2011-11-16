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

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class SparqlUnannotatedAbstractData {

	private String serviceUrl = "http://logd.tw.rpi.edu/ws/sparqlproxy.php";
	
	// test query - known to return results
//	private String query = "?query-option=text&query=" + 
//	"PREFIX+essi%3A+%3Chttp%3A%2F%2Fessi-lod.org%2Finstances%2F%3E%0D%0APREFIX+swrc%3A+%3Chttp%3A%2F%2F" + 
//	"swrc.ontoware.org%2Fontology%23%3E%0D%0APREFIX+pav%3A+%3Chttp%3A%2F%2Fpurl.org%2Fpav%2F%3E%0D%0ASELECT+" + 
//	"%3Fid+%3Fabstract+WHERE+{+%0D%0A%0D%0A++%3Fid+swrc%3Aabstract+%3Fabstract+.%0D%0A++}+LIMIT+10&service-uri=" + 
//	"http%3A%2F%2Faquarius.tw.rpi.edu%3A2025%2Fsparql";
	
	private String query = "?query-option=text&query=" + 
		"PREFIX+essi%3A+%3Chttp%3A%2F%2Fessi-lod.org%2Finstances%2F%3E%0D%0APREFIX+swrc%3A+%3Chttp%3A%2F%2F" + 
		"swrc.ontoware.org%2Fontology%23%3E%0D%0APREFIX+pav%3A+%3Chttp%3A%2F%2Fpurl.org%2Fpav%2F%3E%0D%0ASELECT+" + 
		"%3Fid+%3Fabstract+WHERE+{+%0D%0A%0D%0A++%3Fid+swrc%3Aabstract+%3Fabstract+.%0D%0A++OPTIONAL+{+%3FsourceDoc+" + 
		"pav%3AretrievedFrom+%3Fid+.+}%0D%0A++FILTER+%28!bound%28%3FsourceDoc%29%29%0D%0A%0D%0A}&service-uri=" + 
		"http%3A%2F%2Faquarius.tw.rpi.edu%3A2025%2Fsparql";
  
	public Vector <String> ids = new Vector <String> ();
	public Vector <String> abstracts = new Vector <String> ();
	
	private boolean isElementNode ( Node node ) {
		
		short type = node.getNodeType();
        if ( type == Node.ELEMENT_NODE ) { return true; } else { return false; }
        	
	}

	public void getAbstracts () {
		
	  String result = "";	
	  try {
    	            
            // Send data
            URL url = new URL( serviceUrl );
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(query);
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
            	          if ( isElementNode(idNode) ) { ids.add( idNode.getTextContent() ); }
            	        }
            	      }
            	      if ( value.equals("abstract") ) {
            	    	NodeList abstrList = cn.getChildNodes();
              	        for (int k=0; k<abstrList.getLength(); k++) { 
              	          Node abNode = abstrList.item(k);
              	          if ( isElementNode(abNode) ) { abstracts.add( abNode.getTextContent() ); }
              	        }
              	      }
            	    }
            	  } // end if isElement Node
       //     	  
               }
            	  //   		   
  //  		  System.out.println(id + " " + abstr);
            }
            
      } catch (Exception e) { System.out.println(e); }
  	  
      
      
	}

}