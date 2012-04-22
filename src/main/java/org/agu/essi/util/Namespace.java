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
package org.agu.essi.util;

import java.io.StringWriter;

/**
 * Container class for all namespaces used throughout project
 * @author Eric Rozell
 */
public enum Namespace {
	
	essi ("http://essi-lod.org/ontology#"),
	xsi ("http://www.w3.org/2001/XMLSchema-instance"),
	essiXsd ("http://www.agu.org/focus_group/essi/schema/agu.xsd"),
	essiSchema ("http://www.agu.org/focus_group/essi/schema"),
	rdf ("http://www.w3.org/1999/02/22-rdf-syntax-ns#"),
	swc ("http://data.semanticweb.org/ns/swc/ontology#"),
	swrc ("http://swrc.ontoware.org/ontology#"),
	owl ("http://www.w3.org/2002/07/owl#"),
	dc ("http://purl.org/dc/terms/"),
	xsd ("http://www.w3.org/2001/XMLSchema#"),
	tw ("http://tw.rpi.edu/schema/"),
	foaf ("http://xmlns.com/foaf/0.1/"),
	geo ("http://www.w3.org/2003/01/geo/wgs84_pos#"),
	skos ("http://www.w3.org/2004/02/skos/core#"),
	dbanno ("http://purl.org/annotations/DBpedia/Spotlight"),
	aos ("http://purl.org/ao/selectors/"),
	aof ("http://purl.org/ao/foaf/"),
	ao ("http://purl.org/ao/"),
	pav ("http://purl.org/pav/"),
	aot ("http://purl.org/ao/types/"),
	aocore ("http://purl.org/ao/core/"),
	rdfs ("http://www.w3.org/2000/01/rdf-schema#"),
	abstracts ("http://data.agu.org/abstracts/"),
	meetings ("http://data.agu.org/meetings/"),
	sessions ("http://data.agu.org/sessions/"),
	sections ("http://data.agu.org/sections/"),
	people ("http://data.agu.org/people/"),
	organizations ("http://data.agu.org/organization/"),
	keywords ("http://data.agu.org/keywords/");

	private String _uri;
	
	private Namespace(String uri) {
		_uri = uri;
	}
	
	public String getURI() {
		return _uri;
	}
	
	public static String getEntityHeader() {
		Namespace[] curies = Namespace.values();
		StringWriter sw = new StringWriter();
		sw.write("<!DOCTYPE rdf:RDF [\n");
		for (int i = 0; i < curies.length; ++i)
		{
			sw.write("  <!ENTITY " + curies[i] + " \"" + curies[i].getURI() + "\" >\n");
		}
		sw.write("]>\n");
		return sw.toString();
	}
	
	public static String getSparqlPrefixes() {
		Namespace[] curies = Namespace.values();
		StringWriter sw = new StringWriter();
		for (int i = 0; i < curies.length; ++i)
		{
			sw.write("PREFIX " + curies[i] + ": <" + curies[i].getURI() + ">\n");
		}
		return sw.toString();
	}
	
	public static String getRdfHeader() {
		Namespace[] curies = Namespace.values();
		StringWriter sw = new StringWriter();
		sw.write("<rdf:RDF xmlns=\"" + Namespace.essi + "\"\n");
		for (int i = 0; i < curies.length; ++i)
		{
			sw.write("         xmlns:" + curies[i] + "=\"" + curies[i].getURI() + "\"\n");
		}
		sw.write("         xmlns:base=\"" + Namespace.essi + "\" >\n");
		return sw.toString();
	}
	
}
