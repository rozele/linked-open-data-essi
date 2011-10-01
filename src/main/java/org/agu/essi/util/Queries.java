package org.agu.essi.util;

public class Queries {
	private static String prefixes = Utils.writeSparqlPrefixes();
	public static String countPeopleQuery = prefixes + "SELECT count(?person) AS ?count WHERE { ?person a foaf:Person . }";
	public static String countOrganizationsQuery = prefixes + "SELECT count(?organization) AS ?count WHERE { ?organization a foaf:Organization . }";
	public static String countKeywordsQuery = prefixes + "SELECT count(?keyword) AS ?count WHERE { ?keyword a swrc:ResearchTopic . }";
	public static String askMeetingQuery = prefixes + "ASK { <$uri> a swrc:Meeting . }";
	public static String askSectionQuery = prefixes + "ASK { <$uri> a swrc:Meeting . }";
	public static String askSessionQuery = prefixes + "ASK { <$uri> a swc:SessionEvent . }";
	public static String askKeywordQuery = prefixes + "ASK { <$uri> a swrc:ResearchTopic . }";
	public static String askAbstractQuery = prefixes + "ASK { <$uri> a esip:Abstract . }";	
	public static String selectPersonQuery = prefixes + "SELECT ?id WHERE { ?id a foaf:Person . ?id foaf:mbox ?email . filter regex(?email, \"$email\", \"i\") }";
	public static String selectOrganizationQuery = prefixes + "SELECT ?id WHERE { ?id a foaf:Organization . ?id dc:description ?description . filter (?description = \"$description\") }";
}
