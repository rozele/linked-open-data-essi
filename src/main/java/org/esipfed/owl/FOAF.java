package org.esipfed.owl;

import java.util.Vector;

import org.esipfed.util.HashFunction;
import org.agu.essi.util.Namespaces;

public class FOAF {
		
	public String writePerson ( String firstName, String lastName, String emailAddress, String phoneNumber ) {
				  
      StringBuilder str = new StringBuilder();
	  str.append( "<rdf:RDF\n" );
	  str.append( "  xmlns:rdf=\"" + Namespaces.rdf + "\"\n" );
	  str.append( "  xmlns:owl=\"" + Namespaces.owl + "\"\n" );
	  str.append( "  xmlns:dc=\"" + Namespaces.dc + "\"\n" );
	  str.append( "  xmlns:foaf=\"" + Namespaces.foaf + "\"\n" );
	  str.append( ">\n");
	  
	  str.append( "  <rdf:Description rdf:about=\"" + firstName + lastName + "\"> \n" );
	  str.append( "    <dc:title>FOAF for " + firstName + " " + lastName + "</dc:title> \n" ); 
	  str.append( "    <dc:description>Friend-of-a-Friend description for " + 
		firstName + " " + lastName + "</dc:description> \n" );
	  str.append( "  </rdf:Description> \n" );
	  str.append( "  <foaf:Person rdf:ID=\"" + firstName + lastName + "> \n" );
	  str.append( "    <foaf:name>" + firstName + " " + lastName + "</foaf:name> \n" );
	  str.append( "    <foaf:firstName>" + firstName + "</foaf:firstName> \n" );
	  str.append( "    <foaf:surname>" + lastName + "</foaf:surname> \n" );		 
	  str.append( "    <foaf:mbox rdf:resource=\"mailto:" + emailAddress + "\"/> \n" ); 
	  str.append( "    <foaf:mbox_sha1sum>" + HashFunction.sha1( emailAddress ) + "</foaf:mbox_sha1sum> \n" );
	  str.append( "    <foaf:phone rdf:resource=\"tel:" + phoneNumber +  "\"/> \n" );
	  str.append( "  </foaf:Person>  \n" );
	  str.append( "</rdf:RDF> \n" );
		  		  
	  return str.toString();

	}
	
	public String writeOrganization ( String org, String orgID, Vector <String> orgMembers ) {
				  
	  StringBuilder str = new StringBuilder();
	  str.append( "<rdf:RDF\n" );
	  str.append( "  xmlns:rdf=\"" + Namespaces.rdf + "\"\n" );
	  str.append( "  xmlns:owl=\"" + Namespaces.owl + "\"\n" );
	  str.append( "  xmlns:dc=\"" + Namespaces.dc + "\"\n" );
	  str.append( "  xmlns:foaf=\"" + Namespaces.foaf + "\"\n" );
	  str.append( ">\n");

	  str.append( "  <rdf:Description rdf:about=\"" + orgID + "\"> \n" );
	  str.append( "    <dc:title>FOAF for " + org + "</dc:title> \n" ); 
	  str.append( "    <dc:description>Friend-of-a-Friend description for " + org + "</dc:description> \n" );
	  str.append( "  </rdf:Description> \n" );
	  str.append( "  <foaf:Organization rdf:ID=\"" + orgID + "> \n" );
	  str.append( "    <foaf:name>" + org + "</foaf:name> \n" );
	  str.append( "    <foaf:member>\n" );
	  for ( int i=0; i<orgMembers.size(); i++ ) { 
	    str.append( "      <foaf:Person rdf:about=\"" + orgMembers.get(i) + "\"></foaf:Person>\n" );
	  }
	  str.append( "    </foaf:member>\n" );
	  str.append( "  </foaf:Organization>  \n" );
	  str.append( "</rdf:RDF> \n" );
		  
      return str.toString();

	}

}