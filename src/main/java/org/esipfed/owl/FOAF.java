package org.esipfed.owl;

import java.util.HashMap;
import java.util.Vector;

import org.esipfed.util.HashFunction;
import org.agu.essi.util.Namespaces;
import org.agu.essi.util.Utils;
import org.esipfed.Person;

/**
 * Class for the creation of FOAF profiles
 * @author Tom Narock
 */
public class FOAF {
	
	/**
	 * Method to search through a set of Person objects and 
	 * group people by the organization they work for
	 * @param people Vector of Person objects
	 * @return HashMap <String, Vector <String>> where key/value is Organization/People affiliated
	 */
	public HashMap <String, Vector <String>> createOrgMap( Vector <Person> people ) {

		Vector <String> pV;
		Vector <String> affiliations;
		HashMap <String, Vector <String>> orgs = new HashMap <String, Vector <String>> ();
		for ( int i=0; i<people.size(); i++ ) {
		  affiliations = people.get(i).getAffiliations(); // get all affiliations associated with this person
		  for ( int j=0; j<affiliations.size(); j++ ) { 
			  
			// if the organization already exists in the hash map then just add the person 
			// else create a new key/value entry in the hash map
	 	    if ( orgs.containsKey(affiliations.get(j)) ) { 
	 	      pV = orgs.get(affiliations.get(j));
	 	    } else { 
	 	      pV = new Vector <String> ();
	 	    }
	 	    pV.add( people.get(i).getID() );
	 	    orgs.put( affiliations.get(j), pV);
	 	    
		  } // end for
		} // end for
		
		return orgs;
		
	}	
			
	/**
	 * Method to take a Person object and 
	 * write it out as a FOAF string
	 * @param person Person object
	 * @return String FOAF Person profile
	 */
	public String writePerson ( Person person ) {
				  
      StringBuilder str = new StringBuilder();
      Vector <String> emailAddresses = person.getEmail();
      
	  str.append( "  <rdf:Description rdf:about=\"" + person.getID() + "\"> \n" );
	  str.append( "	   <rdf:type rdf:resource=\"&foaf;Person\" /> \n");
	  str.append( "    <foaf:name>" + person.getFirstName() + " " + person.getLastName() + "</foaf:name> \n" );
	  str.append( "    <foaf:firstName>" + person.getFirstName() + "</foaf:firstName> \n" );
	  str.append( "    <foaf:surname>" + person.getLastName()+ "</foaf:surname> \n" );	
	  for ( int i=0; i<emailAddresses.size(); i++ ) {
	    str.append( "    <foaf:mbox>" + emailAddresses.get(i) + "</foaf:mbox> \n" ); 
	    str.append( "    <foaf:mbox_sha1sum>" + HashFunction.sha1( emailAddresses.get(i) ) + "</foaf:mbox_sha1sum> \n" );
	  }
	  str.append( "  </rdf:Description>  \n" );
		  		  
	  return str.toString();

	}
	
	/**
	 * Method to write FOAF Organization profile 
	 * @param HashMap organization/affiliated people hash map
	 * @return String FOAF organization profile
	 */
	public String writeOrganization ( HashMap <String, Vector <String>> orgs ) {
				  
	  Vector <String> people;
	  StringBuilder str = new StringBuilder();

	  int index = 1;
	  for (String key : orgs.keySet()) {
		  
		// a key value of "" means we do not know the person's organization
		if ( !key.equals("") ) {

	      str.append( "  <rdf:Description rdf:about=\"" + Namespaces.esip + "ESIP_Organization_" + index + "\"> \n" );	  
	      str.append( "    <rdf:type rdf:resource=\"&foaf;Organization\" /> \n" );
	      str.append( "    <foaf:name>" + Utils.clean(key) + "</foaf:name> \n" );
	      people = orgs.get(key);
	      for ( int i=0; i<people.size(); i++ ) { 
		    str.append( "    <foaf:member>\n" );
	        str.append( "      <foaf:Person rdf:about=\"" + people.get(i) + "\"/>\n" );
		    str.append( "    </foaf:member>\n" );
	      }
	      str.append( "  </rdf:Description> \n" );
		
	      index++;
		
		} // end if org name != ""
	    
	  } // end for
	  
      return str.toString();

	}

}