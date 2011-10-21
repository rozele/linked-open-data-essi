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
