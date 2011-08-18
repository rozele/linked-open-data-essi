package org.agu.essi.data;

import java.util.Vector;

import org.agu.essi.Abstract;

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
	public Vector<Abstract> getAbstracts();
	
	/**
	 * Method to specify if URIs have already been generated for the data source
	 * @return true if the source already has unique identifiers, false otherwise
	 */
	public boolean hasUniqueIdentifiers();
}
