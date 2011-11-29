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

$query = getPrefixes() .
       "select ?p1 ?p2 (count(?abstract) as ?count) " .
       "from <$graph> " .
       "where { " .
       "?abstract tw:hasAgentWithRole ?r1 . " .
       "?abstract tw:hasAgentWithRole ?r2 . " .
       "?abstract swc:relatedToEvent ?session . " .
       "?session swc:isSubEventOf ?section . " .
       "?section dc:identifier \"$section\"^^xsd:string . " .
       "?p1 tw:hasRole ?r1 . " .
       "?p2 tw:hasRole ?r2 . " .
       "FILTER (?p1 < ?p2) " .
       "} " .
       "group by ?p1 ?p2 " .
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
	$person = $binding["p"]["value"];
	$name = $binding["n"]["value"];
	$people[] = $person;
	$nodeArray[] = array( "name" => $name , "group" => $i );
}

$results = sparqlSelect($query, $endpoint);
$edgeArray = array();

foreach ($results["results"]["bindings"] as $i => $binding)
{
	$person1 = $binding["p1"]["value"];
	$person2 = $binding["p2"]["value"];
	$id1 = getKey($person1, $people);
	$id2 = getKey($person2, $people);
	$val = intval($binding["count"]["value"]) ;
	if ($val > 0)
	{
		$edgeArray[] = array( "source" => $id1, "target" => $id2, "value" => $val );
	}
}

$output = array( "nodes" => $nodeArray, "links" => $edgeArray );
echo json_encode($output);
