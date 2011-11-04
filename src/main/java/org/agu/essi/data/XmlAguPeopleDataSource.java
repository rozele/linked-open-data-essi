package org.agu.essi.data;

import org.agu.essi.data.source.EsipPersonDataSource;
import org.agu.essi.match.EntityMatcher;
import org.esipfed.Person;
import org.esipfed.data.PeopleParserAGU;
import java.util.Vector;

/**
 * Parser for AGU People data encoded in XML
 * @author Tom Narock
 */
public class XmlAguPeopleDataSource implements EsipPersonDataSource {
	
	private boolean parsed = false;
	private EntityMatcher matcher;
	
	/**
	 * Method to test if the data source is ready (it has been parsed)
	 * @return boolean ready
	 */
	public boolean ready() { return parsed; }

	/**
	 * Method to set the Entity Matcher
	 * @param EntityMatcher matcher
	 */
	public void setEntityMatcher ( EntityMatcher m ) { matcher = m; }
	
	/**
	 * Method to get the EntityMatcher 
	 * @return EntityMatcher matcher
	 */
	public EntityMatcher getEntityMatcher() { return matcher; }
	
	/**
	 * Method to parse the xml and return People
	 * @return Vector <Person> people
	 */
	public Vector <Person> getPeople( String xmlFile ) {
		
		PeopleParserAGU parser = new PeopleParserAGU();
		Vector <Person> people = null;
		try {
		  people = parser.parse( xmlFile );
		} catch (Exception e) { System.out.println(e); }
		parsed = true;
		return people;
		
	}
	
}