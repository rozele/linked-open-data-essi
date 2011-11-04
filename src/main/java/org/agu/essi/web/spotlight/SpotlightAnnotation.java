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
package org.agu.essi.web.spotlight;

import java.util.Arrays;
import java.util.Vector;

import org.agu.essi.annotation.Annotation;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Parser for XML results returned from Spotlight service
 * @author Eric Rozell
 */
public class SpotlightAnnotation implements Annotation
{
	private String surfaceForm;
	private String uri;
	private int index;
	private double similarity;
	private double secondRankPct;
	private double confidence;
	private int support;
	private Vector <String> types;
	
	public SpotlightAnnotation(Node xml, double confidence)
	{
		this.confidence = confidence;
		parseDomNode(xml);
	}
	
	public SpotlightAnnotation(String sf, int idx, double sim, double pct, int sup, Vector<String> t, 
			double c)
	{
		surfaceForm = sf; index = idx; similarity = sim; secondRankPct = pct; support = sup; types = t; confidence = c;
	}
	
	/**
	 * Method to get the similarity value
	 * @return double similarity
	 */
	public double getSimilarityScore() {
		return similarity;
	}
	
	/**
	 * Method to get the percent of second rank
	 * @return double percent
	 */
	public double getPercentSecondRank() {
		return secondRankPct;
	}
	
	/**
	 * Method to get the annotation text
	 * @return String annotation
	 */
	public String getAnnotation() {
		return this.toString();
	}
	
	/**
	 * Method to get the surface form
	 * @return String surfaceForm
	 */
	public String getSurfaceForm() {
		return surfaceForm;
	}
	
	/**
	 * Method to get the dbpedia ontology types for this annotation
	 * @return Vector <String> types
	 */
	public Vector <String> getDBpediaTypes() {
		return types;
	}

	/**
	 * Method to get the index (location within the annotation where the surface form begins)
	 * @return int index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Method to get the confidence value sent to spotlight service
	 * @return double confidence
	 */
	public double getConfidence() {
		return confidence;
	}
	
	/**
	 * Method to get the support value (wikipedia inlinks)
	 * @return int support
	 */
	public int getSupport() { return support; }
	
	private void parseDomNode(Node xml)
	{
		NamedNodeMap nm = xml.getAttributes();
	
		uri = (nm.getNamedItem("URI") != null) ? 
			nm.getNamedItem("URI").getTextContent() : null;
			
		surfaceForm = (nm.getNamedItem("surfaceForm") != null) ? 
			nm.getNamedItem("surfaceForm").getTextContent() : null;
		
		similarity = (nm.getNamedItem("similarityScore") != null) ?
			Double.parseDouble(nm.getNamedItem("similarityScore").getTextContent()) : null;
	
		support = (nm.getNamedItem("support") != null) ?
			Integer.parseInt(nm.getNamedItem("support").getTextContent()) : null;
		
		index = (nm.getNamedItem("offset") != null) ?
			Integer.parseInt(nm.getNamedItem("offset").getTextContent()) : null;
		
		secondRankPct = (nm.getNamedItem("percentageOfSecondRank") != null) ?
			Double.parseDouble(nm.getNamedItem("percentageOfSecondRank").getTextContent()) : null;
		
		types = (nm.getNamedItem("types") != null) ?
			new Vector<String>(Arrays.asList(nm.getNamedItem("types").getTextContent().split(","))) : null;
	}

	/**
	 * Method to get the dbpedia uri
	 * @return String uri
	 */
	public String getURI() {
		return uri;
	}

}
