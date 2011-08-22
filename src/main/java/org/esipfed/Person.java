package org.esipfed;

import java.util.Vector;
import org.agu.essi.util.Namespaces;

/**
 * Container class for ESIP person information
 * @author Tom Narock
 */
public class Person {
	
	private String _firstName;
    private String _lastName;
    private String _id;
    private Vector <String> _affiliations = new Vector <String> ();
    private Vector <String> _emailAddresses = new Vector <String> ();
    
	/**
	 * Construct Person instance from name
	 * @param firstName first name of person
	 * @param lastName last name of person
	 * @param phoneNumber phone number of person
	 * @param affiliation affiliation of person
	 * @param emailAddress email address of person
	 */
	public Person(String firstName, String lastName, String affiliation, String emailAddress) {
		_firstName = firstName;
		_lastName = lastName;
		_affiliations.add( affiliation );
		_emailAddresses.add( emailAddress );
	}
	
	/**
	 * Method to create an id for an existing person
	 * @param index an integer index to use in the id
	 */
	public void createID(int index) { _id = Namespaces.esip + "ESIP_Person_" + index; }
	
	/**
	 * Method to get an id for an existing person
	 * @return id the id of the person
	 */
	public String getID() { return _id; }
	
	/**
	 * Method to add an email for an existing person
	 * @param email email of person
	 */
	public void addEmail(String email) { _emailAddresses.add( email ); }
	
	/**
	 * Method to add an affiliation for an existing person
	 * @param affiliation affiliation of person
	 */
	public void addAffiliation(String affiliation) { _affiliations.add( affiliation ); }
	
	/**
	 * Method to get person first name
	 * @return first name of person
	 */
	public String getFirstName() { return _firstName; }
	
	/**
	 * Method to get person last name 
	 * @return last name of person
	 */
	public String getLastName() { return _lastName; }
	
	/**
	 * Method to get person affiliations
	 * @return affiliations of person
	 */
	public Vector <String> getAffiliations() { return _affiliations; }
	
	/**
	 * Method to get person email
	 * @return email of person
	 */
	public Vector <String> getEmail() { return _emailAddresses; }
	
	/**
	 * Method to test for the existence of an affiliation
	 * @return true/false
	 */
	public boolean hasAffiliation( String value ) { 
	  if ( this._affiliations.contains(value) ) { return true; } else { return false; }	
	}
	
	/**
	 * Method to test for the existence of an email address
	 * @return true/false
	 */
	public boolean hasEmail( String value ) { 
	  if ( this._emailAddresses.contains(value) ) { return true; } else { return false; }	
	}
	
	/**
	 * Equality method for Person
	 * @return true if names are equivalent, false otherwise
	 */
	public boolean exists( Vector <Person> people, String affiliation, String email ) {

	  boolean result = false;
	  for ( int i=0; i<people.size(); i++ ) {
		result = false;
	    Person p = people.get(i);
	    if ( p.getFirstName().equals(this._firstName) && (p.getLastName().equals(this._lastName)) ) { 
		  result = true;
		  if ( !p.hasAffiliation(affiliation) ) { p.addAffiliation( affiliation ); }
		  if ( !p.hasEmail(email) ) { p.addEmail( email ); }
		  break;
	    } 
	  } // end for
	  return result;  
	
	}
}
