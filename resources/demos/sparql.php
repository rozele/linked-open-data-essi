<?php

$prefixes = array(
	"essiInst" => "http://essi-lod.org/instances/",
	"essi" => "http://essi-lod.org/ontology#",
	"rdf" => "http://www.w3.org/1999/02/22-rdf-syntax-ns#",
	"swc" => "http://data.semanticweb.org/ns/swc/ontology#",
	"swrc" => "http://swrc.ontoware.org/ontology#",
	"owl" => "http://www.w3.org/2002/07/owl#",
	"dc" => "http://purl.org/dc/terms/",
	"xsd" => "http://www.w3.org/2001/XMLSchema#",
	"tw" => "http://tw.rpi.edu/schema/",
	"foaf" => "http://xmlns.com/foaf/0.1/",
	"geo" => "http://www.w3.org/2003/01/geo/wgs84_pos#",
	"skos" => "http://www.w3.org/2004/02/skos/core#",
	"dbanno" => "http://purl.org/annotations/DBpedia/Spotlight",
	"aos" => "http://purl.org/ao/selectors/",
	"aof" => "http://purl.org/ao/foaf/",
	"ao" => "http://purl.org/ao/",
	"pav" => "http://purl.org/pav/",
	"aot" => "http://purl.org/ao/types/",
	"aocore" => "http://purl.org/ao/core/"
);

/**
 * Builds an HTTP parameter string
 * @param array $data HTTP parameters for request
 * @return a URL payload
 */
function getRequestData($data) 
{
	 $arr = array();
	 foreach ($data as $param => $value)
	 {
		$arr[] = $param . "=" . $value;
	 }
	 return implode("&", $arr);
}

/**
 * Executes a SPARQL SELECT query (also works for ASK)
 * @param string $query SPARQL query string
 * @param string $endpoint SPARQL endpoint location
 * @return a PHP object generated from the SPARQL JSON response
 */
function sparqlSelect($query, $endpoint) 
{
	 $data = array( "query" => urlencode($query), "output" => "json" );
	 $curl = curl_init();
	 curl_setopt($curl, CURLOPT_URL, $endpoint);
	 curl_setopt($curl, CURLOPT_POST, true);
	 curl_setopt($curl, CURLOPT_POSTFIELDS, getRequestData($data));
	 curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
	 $content = curl_exec($curl);
	 curl_close($curl);
	 return json_decode($content, true);
}

/**
 * Gets prefixes used for ESSI data queries
 * @return a string contaning SPARQL prefixes
 */
function getPrefixes() 
{
	global $prefixes;
	$ret = "";
	foreach($prefixes as $curie => $uri)
	{
		$ret .= "PREFIX $curie: <$uri> ";
	}
	return $ret;
}

?>