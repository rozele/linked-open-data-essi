package org.agu.essi.annotation;

public interface Annotation 
{
	/**
	 * Method to get the surface form that was annotated
	 * @return a string
	 */
	public String getSurfaceForm();
	
	/**
	 * Method to get the start index of the surface form
	 * @return an integer
	 */
	public int getIndex();
	
	/**
	 * Method to get the confidence in the annotation
	 * @return an informative score for the annotation
	 */
	public double getConfidence();
	
	/**
	 * Method to get a string representation of the assigned annotation
	 * @return a string representative of the annotation
	 */
	public String getAnnotation();
}
