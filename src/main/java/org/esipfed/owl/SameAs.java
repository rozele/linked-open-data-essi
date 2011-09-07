package org.esipfed.owl;

public class SameAs {
	
	/**
	 * Method write OWL sameAs statement
	 * @param String person ID 1
	 * @param String person ID 2
	 * @return String OWL/RDF sameAs statement
	 */
	public String writeSameAs ( String id1, String id2 ) {
		        	 
      StringBuilder str = new StringBuilder();      
	  str.append( "  <rdf:Description rdf:about=\"" + id1 + "\"> \n" );
	  str.append( "	   <owl:sameAs rdf:resource=\"" + id2 + "\" /> \n");
	  str.append( "  </rdf:Description>  \n" );
		  		  
	  return str.toString();

	}
	
}