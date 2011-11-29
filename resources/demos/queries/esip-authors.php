<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8891/sparql";
$graph = "http://essi-lod.org/instances/";
$esipGraph = "http://esipfed.org/instances/";
$sameAsGraph = "http://essi-lod.org/sameas/";

$peopleQuery = getPrefixes() .
	       "select distinct ?e ?n " .
	       "where { " .
	       "graph <$esipGraph> { " .
	       "?e a essi:EsipMember . " .
	       "?e foaf:name ?n . " .
	       "} " .
	       "graph <$sameAsGraph> { " .
	       "?p owl:sameAs ?e . " .
	       "} " .
	       "graph <$graph> { " .
	       "?p a foaf:Person . " .
	       "?p tw:hasRole ?role . " .
	       "?abstract tw:hasAgentWithRole ?role . " .
	       "} " .
	       "}";

$query = getPrefixes() .
       "select ?e1 ?e2 (count(?abstract) as ?count) " .
       "where { " .
       "graph <$esipGraph> { " .
       "?e1 a essi:EsipMember . " .
       "?e2 a essi:EsipMember . " .
       "} " .
       "graph <$sameAsGraph> { " .
       "?p1 owl:sameAs ?e1 . " . 
       "?p2 owl:sameAs ?e2 . " .
       "} " .
       "graph <$graph> { " .
       "?abstract tw:hasAgentWithRole ?r1 . " .
       "?abstract tw:hasAgentWithRole ?r2 . " .
       "?p1 tw:hasRole ?r1 . " .
       "?p2 tw:hasRole ?r2 . " .
       "FILTER (?p1 < ?p2) " .
       "} " .
       "} " .
       "group by ?e1 ?e2 " .
       "order by desc(?count)";

function getKey($value, $array)
{
	$keyArray = array_keys($array, $value);
	if (count($keyArray) == 1) return $keyArray[0];
	else return -1;
}

$people = array();
//$groups = array();
$nodeArray = array();

$results = sparqlSelect($peopleQuery, $endpoint);
foreach($results["results"]["bindings"] as $i => $binding)
{
	$person = $binding["e"]["value"];
	$name = $binding["n"]["value"];
	$people[] = $person;
	$nodeArray[] = array( "name" => $name , "group" => $i );
}

$results = sparqlSelect($query, $endpoint);
$edgeArray = array();

foreach ($results["results"]["bindings"] as $i => $binding)
{
	$person1 = $binding["e1"]["value"];
	$person2 = $binding["e2"]["value"];
	$id1 = getKey($person1, $people);
	$id2 = getKey($person2, $people);
	$val = intval($binding["count"]["value"]) ;
	if ($val > 0 && $id1 >= 0 && $id2 >= 0)
	{
		$edgeArray[] = array( "source" => $id1, "target" => $id2, "value" => $val );
	}
}

$output = array( "nodes" => $nodeArray, "links" => $edgeArray );
echo json_encode($output);
