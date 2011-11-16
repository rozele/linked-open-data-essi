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

import java.util.Collection;

import org.agu.essi.util.exception.InvalidAbstractConstraintException;

public class Queries {
	private static String prefixes = Utils.writeSparqlPrefixes();
	
	public static String countPeopleQuery = prefixes + "SELECT (count(?person) AS ?count) WHERE { ?person a foaf:Person . }";
	public static String countOrganizationsQuery = prefixes + "SELECT (count(?organization) AS ?count) WHERE { ?organization a foaf:Organization . }";
	//public static String countKeywordsQuery = prefixes + "SELECT (count(?keyword) AS ?count) WHERE { ?keyword a swrc:ResearchTopic . }";

	public static String sessionsQuery = prefixes + "SELECT ?session WHERE { ?session a swc:SessionEvent . }";
	public static String meetingsQuery = prefixes + "SELECT ?meeting WHERE { ?meeting a swrc:Meeting . OPTIONAL { ?meeting swc:isSubEventOf ?null } FILTER (!bound(?null)) }";
	public static String sectionsQuery = prefixes + "SELECT ?section WHERE { ?section a swrc:Meeting . OPTIONAL { ?section swc:isSubEventOf ?meeting } FILTER (bound(?meeting)) }";
	public static String keywordsQuery = prefixes + "SELECT ?keyword { ?keyword a swrc:ResearchTopic . }";
	public static String peopleQuery = prefixes + "SELECT ?id ?email WHERE { ?id a foaf:Person . ?id foaf:mbox ?email . }";
	public static String organizationsQuery = prefixes + "SELECT ?id ?description WHERE { ?id a foaf:Organization . ?id dc:description ?description . }";

	private static String askMeetingQuery = prefixes + "ASK { <{uri}> a swrc:Meeting . }";
	private static String askSectionQuery = prefixes + "ASK { <{uri}> a swrc:Meeting . }";
	private static String askSessionQuery = prefixes + "ASK { <{uri}> a swc:SessionEvent . }";
	private static String askKeywordQuery = prefixes + "ASK { <{uri}> a swrc:ResearchTopic . }";
	private static String askAbstractQuery = prefixes + "ASK { <{uri}> a esip:Abstract . }";	
	private static String selectPersonQuery = prefixes + "SELECT ?id WHERE { ?id a foaf:Person . ?id foaf:mbox ?email . filter regex(?email, \"{email}\", \"i\") }";
	private static String selectOrganizationQuery = prefixes + "SELECT ?id WHERE { ?id a foaf:Organization . ?id dc:description ?description . filter (?description = \"{description}\") }";
	private static String abstractInfoQuery = prefixes + "SELECT ?session ?section ?meeting ?id ?title ?abstract where { <{uri}> swrc:abstract ?abstract . <{uri}> swc:relatedToEvent ?sessionId . ?sessionId dc:identifier ?session . ?session swc:isSubEventOf ?sectionId . ?sectionId swrc:eventTitle ?section . ?sectionId swc:isSubEventOf ?meeting . ?meeting swrc:eventTitle . <{uri}> dc:identifier ?id . <{uri}> dc:title ?title . }";
	private static String abstractAuthorQuery = prefixes + "SELECT ?person ?email ?index ?affiliation WHERE { <{uri}> tw:hasAgentWithRole ?author . ?author a tw:Author . ?personId tw:hasRole ?author . ?personId foaf:name ?person . OPTIONAL { ?personId foaf:mbox ?email . } ?author tw:index ?index . ?author tw:withAffiliation ?affiliationId . ?affiliationId dc:description ?affiliation . } ORDER BY ?index";
	private static String abstractKeywordQuery = prefixes + "SELECT ?keyword WHERE { <{uri}> swc:hasTopic ?keywordId . ?keywordId dc:description ?keyword . }";
	private static String abstractTypeQuery = prefixes + "SELECT ?type WHERE { <{uri}> a ?type . }";
	private static String abstractsQuery = prefixes + "SELECT ?abstract WHERE { ?abstract a esip:Abstract . {constraints}}";
			
	public static String selectOrganizationQuery(String description)
	{
		return selectOrganizationQuery.replaceAll("\\{description\\}", description);
	}
	
	public static String selectPersonQuery(String email)
	{
		return selectPersonQuery.replaceAll("\\{email\\}", email);
	}
	
	public static String askMeetingQuery(String uri)
	{
		return askMeetingQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askSectionQuery(String uri)
	{
		return askSectionQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askSessionQuery(String uri)
	{
		return askSessionQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askKeywordQuery(String uri)
	{
		return askKeywordQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askAbstractQuery(String uri)
	{
		return askAbstractQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractInfoQuery(String uri)
	{
		return abstractInfoQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractAuthorQuery(String uri)
	{
		return abstractAuthorQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractKeywordQuery(String uri)
	{
		return abstractKeywordQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractTypeQuery(String uri)
	{
		return abstractTypeQuery.replaceAll("\\{uri\\}", uri);
	}
	
	@SuppressWarnings("rawtypes")
	public static String abstractsQuery(Collection constraints, boolean conjunctive) throws InvalidAbstractConstraintException
	{
		String constraintString = Utils.buildAbstractConstraintString(constraints, conjunctive);
		return abstractsQuery.replaceAll("\\{constraints\\}", constraintString);
	}
}
