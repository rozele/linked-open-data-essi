package org.agu.essi.util;

public class Queries {
	private static String prefixes = Utils.writeSparqlPrefixes();
	public static String countPeopleQuery = prefixes + "SELECT count(?person) AS ?count WHERE { ?person a foaf:Person . }";
	public static String countOrganizationsQuery = prefixes + "SELECT count(?organization) AS ?count WHERE { ?organization a foaf:Organization . }";
	public static String countKeywordsQuery = prefixes + "SELECT count(?keyword) AS ?count WHERE { ?keyword a swrc:ResearchTopic . }";
	private static String askMeetingQuery = prefixes + "ASK { <$uri> a swrc:Meeting . }";
	private static String askSectionQuery = prefixes + "ASK { <$uri> a swrc:Meeting . }";
	private static String askSessionQuery = prefixes + "ASK { <$uri> a swc:SessionEvent . }";
	private static String askKeywordQuery = prefixes + "ASK { <$uri> a swrc:ResearchTopic . }";
	private static String askAbstractQuery = prefixes + "ASK { <$uri> a esip:Abstract . }";	
	private static String selectPersonQuery = prefixes + "SELECT ?id WHERE { ?id a foaf:Person . ?id foaf:mbox ?email . filter regex(?email, \"$email\", \"i\") }";
	private static String selectOrganizationQuery = prefixes + "SELECT ?id WHERE { ?id a foaf:Organization . ?id dc:description ?description . filter (?description = \"$description\") }";
	private static String abstractInfoQuery = prefixes + "SELECT ?session ?section ?meeting ?id ?title ?abstract where { <$uri> swrc:abstract ?abstract . <$uri> swc:relatedToEvent ?sessionId . ?sessionId dc:identifier ?session . ?session swc:isSubEventOf ?sectionId . ?sectionId swrc:eventTitle ?section . ?sectionId swc:isSubEventOf ?meeting . ?meeting swrc:eventTitle . <$uri> dc:identifier ?id . <$uri> dc:title ?title . }";
	private static String abstractAuthorQuery = prefixes + "SELECT ?person ?index ?affiliation WHERE { <$uri> tw:hasAgentWithRole ?author . ?author a tw:Author . ?person tw:hasRole ?author . ?author tw:index ?index . ?author tw:withAffiliation ?affiliation . }";
	private static String abstractKeywordQuery = prefixes + "SELECT ?keyword WHERE { <$uri> swc:hasTopic ?keyword . }";
	private static String abstractTypeQuery = prefixes + "SELECT ?type WHERE { <$uri> a ?type . }";
	
	public static String selectOrganizationQuery(String description)
	{
		return selectOrganizationQuery.replaceAll("$description", description);
	}
	
	public static String selectPersonQuery(String email)
	{
		return selectPersonQuery.replaceAll("$email", email);
	}
	
	public static String askMeetingQuery(String uri)
	{
		return askMeetingQuery.replaceAll("$uri", uri);
	}
	
	public static String askSectionQuery(String uri)
	{
		return askSectionQuery.replaceAll("$uri", uri);
	}
	
	public static String askSessionQuery(String uri)
	{
		return askSessionQuery.replaceAll("$uri", uri);
	}
	
	public static String askKeywordQuery(String uri)
	{
		return askKeywordQuery.replaceAll("$uri", uri);
	}
	
	public static String askAbstractQuery(String uri)
	{
		return askAbstractQuery.replaceAll("$uri", uri);
	}
	
	public static String abstractInfoQuery(String uri)
	{
		return abstractInfoQuery.replaceAll("$uri", uri);
	}
	
	public static String abstractAuthorQuery(String uri)
	{
		return abstractAuthorQuery.replaceAll("$uri", uri);
	}
	
	public static String abstractKeywordQuery(String uri)
	{
		return abstractKeywordQuery.replaceAll("$uri", uri);
	}
	
	public static String abstractTypeQuery(String uri)
	{
		return abstractTypeQuery.replaceAll("$uri", uri);
	}	
}
