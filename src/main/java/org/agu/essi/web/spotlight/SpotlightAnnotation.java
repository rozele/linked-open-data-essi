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
	
	public double getSimilarityScore() {
		return similarity;
	}
	
	public double getPercentSecondRank() {
		return secondRankPct;
	}
	
	public String getAnnotation() {
		return this.toString();
	}
	
	public String getSurfaceForm() {
		return surfaceForm;
	}
	
	public Vector <String> getDBpediaTypes() {
		return types;
	}

	public int getIndex() {
		return index;
	}

	public double getConfidence() {
		return confidence;
	}
	
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

	public String getURI() {
		return uri;
	}

}
