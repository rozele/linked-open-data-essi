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
	private static String from = "FROM <{graph}>";
	
	private static String countPeopleQuery = prefixes + "SELECT (count(?person) AS ?count) {from} WHERE { ?person a foaf:Person . }";
	private static String countOrganizationsQuery = prefixes + "SELECT (count(?organization) AS ?count) {from} WHERE { ?organization a foaf:Organization . }";
	//public static String countKeywordsQuery = prefixes + "SELECT (count(?keyword) AS ?count) {from} WHERE { ?keyword a swrc:ResearchTopic . }";

	private static String sessionsQuery = prefixes + "SELECT ?session {from} WHERE { ?session a swc:SessionEvent . }";
	private static String meetingsQuery = prefixes + "SELECT ?meeting {from} WHERE { ?meeting a swrc:Meeting . OPTIONAL { ?meeting swc:isSubEventOf ?null } FILTER (!bound(?null)) }";
	private static String sectionsQuery = prefixes + "SELECT ?section {from} WHERE { ?section a swrc:Meeting . OPTIONAL { ?section swc:isSubEventOf ?meeting } FILTER (bound(?meeting)) }";
	private static String keywordsQuery = prefixes + "SELECT ?keyword {from} WHERE { ?keyword a swrc:ResearchTopic . }";
	private static String peopleQuery = prefixes + "SELECT ?id ?email {from} WHERE { ?id a foaf:Person . ?id foaf:mbox ?email . }";
	private static String organizationsQuery = prefixes + "SELECT ?id ?description {from} WHERE { ?id a foaf:Organization . ?id dc:description ?description . }";

	private static String askMeetingQuery = prefixes + "ASK {from} { <{uri}> a swrc:Meeting . }";
	private static String askSectionQuery = prefixes + "ASK {from} { <{uri}> a swrc:Meeting . }";
	private static String askSessionQuery = prefixes + "ASK {from} { <{uri}> a swc:SessionEvent . }";
	private static String askKeywordQuery = prefixes + "ASK {from} { <{uri}> a swrc:ResearchTopic . }";
	private static String askAbstractQuery = prefixes + "ASK {from} { <{uri}> a essi:Abstract . }";	
	private static String selectPersonQuery = prefixes + "SELECT ?id {from} WHERE { ?id a foaf:Person . ?id foaf:mbox ?email . filter regex(?email, \"{email}\", \"i\") }";
	private static String selectOrganizationQuery = prefixes + "SELECT ?id {from} WHERE { ?id a foaf:Organization . ?id dc:description ?description . filter (?description = \"{description}\") }";
	private static String abstractInfoQuery = prefixes + "SELECT ?session ?section ?meeting ?id ?title ?abstract {from} WHERE { <{uri}> swrc:abstract ?abstract . <{uri}> swc:relatedToEvent ?sessionId . ?sessionId dc:identifier ?session . ?session swc:isSubEventOf ?sectionId . ?sectionId swrc:eventTitle ?section . ?sectionId swc:isSubEventOf ?meeting . ?meeting swrc:eventTitle . <{uri}> dc:identifier ?id . <{uri}> dc:title ?title . }";
	private static String abstractAuthorQuery = prefixes + "SELECT ?person ?email ?index ?affiliation {from} WHERE { <{uri}> tw:hasAgentWithRole ?author . ?author a tw:Author . ?personId tw:hasRole ?author . ?personId foaf:name ?person . OPTIONAL { ?personId foaf:mbox ?email . } ?author tw:index ?index . ?author tw:withAffiliation ?affiliationId . ?affiliationId dc:description ?affiliation . } ORDER BY ?index";
	private static String abstractKeywordQuery = prefixes + "SELECT ?keyword {from} WHERE { <{uri}> swc:hasTopic ?keywordId . ?keywordId dc:description ?keyword . }";
	private static String abstractTypeQuery = prefixes + "SELECT ?type {from} WHERE { <{uri}> a ?type . }";
	private static String abstractsQuery = prefixes + "SELECT ?abstract {from} WHERE { ?abstract a essi:Abstract . {constraints}}";
			
	public static String countPeopleQuery(String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return countPeopleQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return countPeopleQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String countOrganizationsQuery(String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return countOrganizationsQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return countOrganizationsQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String sessionsQuery(String graph) 
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return sessionsQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return sessionsQuery.replaceAll("\\{from\\}", "");
	}

	public static String sectionsQuery(String graph) 
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return sectionsQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return sectionsQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String meetingsQuery(String graph) 
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return meetingsQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return meetingsQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String keywordsQuery(String graph) 
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return keywordsQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return keywordsQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String peopleQuery(String graph) 
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return peopleQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return peopleQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String organizationsQuery(String graph) 
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			return organizationsQuery.replaceAll("\\{from\\}", graphClause);
		}
		else
			return organizationsQuery.replaceAll("\\{from\\}", "");
	}
	
	public static String selectOrganizationQuery(String description, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = selectOrganizationQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{description\\}", description);
		}
		else
			return selectOrganizationQuery.replaceAll("\\{description\\}", description);
	}
	
	public static String selectPersonQuery(String email, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = selectPersonQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{email\\}", email);
		}
		else
			return selectPersonQuery.replaceAll("\\{email\\}", email);
	}
	
	public static String askMeetingQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = askMeetingQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return askMeetingQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askSectionQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = askSectionQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return askSectionQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askSessionQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = askSessionQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return askSessionQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askKeywordQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = askKeywordQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return askKeywordQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String askAbstractQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = askAbstractQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return askAbstractQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractInfoQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = abstractInfoQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return abstractInfoQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractAuthorQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = abstractAuthorQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return abstractAuthorQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractKeywordQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = abstractKeywordQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return abstractKeywordQuery.replaceAll("\\{uri\\}", uri);
	}
	
	public static String abstractTypeQuery(String uri, String graph)
	{
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = abstractTypeQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{uri\\}", uri);
		}
		else
			return abstractTypeQuery.replaceAll("\\{uri\\}", uri);
	}
	
	@SuppressWarnings("rawtypes")
	public static String abstractsQuery(Collection constraints, boolean conjunctive, String graph) throws InvalidAbstractConstraintException
	{
		String constraintString = Utils.buildAbstractConstraintString(constraints, conjunctive);
		if (graph != null) {
			String graphClause = from.replaceAll("\\{graph\\}", graph);
			String query = abstractsQuery.replaceAll("\\{from\\}", graphClause);
			return query.replaceAll("\\{constraints\\}", constraintString);
		}
		else
			return abstractsQuery.replaceAll("\\{constraints\\}", constraintString);
	}
}
