<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8891/sparql";
$graph = "http://essi-lod.org/instances/";

$keywordsQuery = getPrefixes() .
  	       "select ?keyword ?name (count(?abstract) as ?count) " .
               "from <$graph> " .
  	       "where { ".
               " ?abstract swc:hasTopic ?t . " .
               " ?t dc:subject ?keyword . " .
               " ?abstract tw:hasAgentWithRole ?agent . " .
               " ?person tw:hasRole ?agent . " .
               " ?person foaf:name ?name . " .
               "} " .
               "group by ?keyword ?name " .
               "order by desc(?count) ";

function getKey($value, $array)
{
	$keyArray = array_keys($array, $value);
	if (count($keyArray) == 1) return $keyArray[0];
	else return -1;
}

$keywords = array();
$groups = array();
$nodeArray = array();
$edgeArray = array();

$results = sparqlSelect($keywordsQuery, $endpoint);
echo count($results["results"]);

foreach($results["results"]["bindings"] as $i => $binding)
{
	$keyword = $binding["keyword"]["value"];
	$name = $binding["name"]["value"];
	$count = $binding["count"]["value"];
	$keywords[] = $keyword;

	$group = -1;
	if (getKey($keyword, $groups) < 0)
	{
		$groups[] = $keyword;
		$group = count($groups) - 1;
	}
	else $group = getKey($keyword, $groups);

	$nodeArray[] = array( "name" => $name, "group" => $group );
	$edgeArray[] = array( "source" => $name, "target" => $keyword, "value" => $count );

}

$output = array( "nodes" => $nodeArray, "links" => $edgeArray );
echo json_encode($output);
