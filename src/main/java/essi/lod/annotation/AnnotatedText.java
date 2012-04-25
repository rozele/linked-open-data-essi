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
package essi.lod.annotation;

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
