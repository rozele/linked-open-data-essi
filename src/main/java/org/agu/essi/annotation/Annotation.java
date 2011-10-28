/**
 * Copyright (C) 2011 Tom Narock and Eric Rozell
 *
 *     This software is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */
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
