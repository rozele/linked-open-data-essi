<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8891/sparql";
$graph = "http://essi-lod.org/instances/";
$section = "IN";

$peopleQuery = getPrefixes() .
	      "select distinct ?p ?n " .
	      "from <$graph> " .
	      "where { " .
	      "?p a foaf:Person . " .
	      "?p foaf:name ?n . " .
	      "?p tw:hasRole ?role . " .
	      "?abstract tw:hasAgentWithRole ?role . " .
	      "?abstract swc:relatedToEvent ?session . " .
	      "?session swc:isSubEventOf ?section . " .
	      "?section dc:identifier \"$section\"^^xsd:string . " .
	      "}"; 

$keywordsQuery = getPrefixes() .
	       "select distinct ?k ?n ?i " .
	       "from <$graph> " .
	       "where { " .
	       "?abstract swc:hasTopic ?k . " .
	       "?abstract swc:relatedToEvent ?session . " .
	       "?session swc:isSubEventOf ?section . " .
	       "?section dc:identifier \"$section\"^^xsd:string . " .
	       "?k a swrc:ResearchTopic . " .
	       "?k dc:subject ?n . " .
	       "?k dc:identifier ?i . " .
	       "}";

$query = getPrefixes() .
  	       "select ?k ?p (count(?abstract) as ?count) " .
               "from <$graph> " .
  	       "where { ".
               "?abstract swc:hasTopic ?k . " .
               "?abstract swc:relatedToEvent ?session . " .
	       "?session swc:isSubEventOf ?section . " .
	       "?section dc:identifier \"$section\"^^xsd:string . " .
               "?abstract tw:hasAgentWithRole ?agent . " .
	       "?p tw:hasRole ?agent . " .
	       "} " .
               "group by ?k ?p " .
               "order by desc(?count)";

function getKey($value, $array)
{
	$keyArray = array_keys($array, $value);
	if (count($keyArray) == 1) return $keyArray[0];
	else return -1;
}

$nodes = array();
$nodeArray = array();
$edgeArray = array();

$results = sparqlSelect($peopleQuery, $endpoint);

foreach($results["results"]["bindings"] as $i => $binding)
{
	$person = $binding["p"]["value"];
	$name = $binding["n"]["value"];
	$nodes[] = $person;
	$nodeArray[] = array( "name" => $name , "group" => 1 );
}

$results = sparqlSelect($keywordsQuery, $endpoint);

foreach($results["results"]["bindings"] as $i => $binding)
{
	$keyword = $binding["k"]["value"];
	$name = $binding["n"]["value"];
	$id = $binding["i"]["value"];
	$nodes[] = $keyword;
	$nodeArray[] = array( "name" => $id . " - " . $name, "group" => 2 );
}

$results = sparqlSelect($query, $endpoint);

foreach($results["results"]["bindings"] as $i => $binding)
{
	$keyword = $binding["k"]["value"];
	$person = $binding["p"]["value"];
	$count = $binding["count"]["value"];
	$keywords[] = $keyword;
	$id1 = getKey($keyword, $nodes);
	$id2 = getKey($person, $nodes);
	$edgeArray[] = array( "source" => $id1, "target" => $id2, "value" => $count );
}

$output = array( "nodes" => $nodeArray, "links" => $edgeArray );
echo json_encode($output);
