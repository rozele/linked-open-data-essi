package org.agu.essi.annotation;

import java.util.Vector;

/**
 * An interface for handling annotated bodies of text.
 * @author Eric Rozell
 */
public interface AnnotatedText 
{
	/**
	 * Method to get the raw text used
	 * @return raw text
	 */
	public String getText();
	
	/**
	 * Method to get annotations for the text
	 * @return a Vector of Annotations
	 */
	public Vector<Annotation> getAnnotations();
}
