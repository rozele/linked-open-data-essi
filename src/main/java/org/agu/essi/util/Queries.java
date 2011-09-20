package org.agu.essi.util;

public class Queries {
	private static String prefixes = Utils.writeSparqlPrefixes();
	public static String countPeopleQuery = prefixes + "SELECT count(?person) AS ?count WHERE { ?person a foaf:Person . }";
	public static String countOrganizationsQuery = prefixes + "SELECT count(?organization) AS ?count WHERE { ?organization a foaf:Organization . }";
	public static String countKeywordsQuery = prefixes + "SELECT count(?keyword) AS ?count WHERE { ?keyword a swrc:ResearchTopic . }";
}
