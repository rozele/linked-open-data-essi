package org.agu.essi.data;

import java.util.Vector;

import org.agu.essi.Abstract;
import org.agu.essi.match.EntityMatcher;
import org.agu.essi.util.exception.SourceNotReadyException;

/**
 * Interface for specifying sources of AGU abstracts
 * @author Eric Rozell
 */
public interface DataSource {
	/**
	 * Method to retrieve the set of abstracts from the data source
	 * @return a Vector of Abstract instances
	 */
	/* TODO: add exception when source is not ready */
	public Vector<Abstract> getAbstracts() throws SourceNotReadyException;
	
	/**
	 * Method to determine if source is ready for use
	 * @return true if source is ready
	 */
	public boolean ready();
	
	/**
	 * Method to set the EntityMatcher
	 * @param m an instance of EntityMatcher
	 */
	public void setEntityMatcher(EntityMatcher m);
	
	/**
	 * Method to get the EntityMatcher
	 * @return an instance of EntityMatcher
	 */
	public EntityMatcher getEntityMatcher();
}
