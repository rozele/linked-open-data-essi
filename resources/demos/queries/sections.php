<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8890/sparql";
$graph = "http://essi-lod.org/instances/";
$query = getPrefixes() .
       "select ?s1 ?s2 (count(?person) as ?count) " .
       "from <$graph> " .
       "where { " .
       "?abstract1 swc:relatedToEvent ?session1 . " .
       "?abstract1 tw:hasAgentWithRole ?role1 . " .
       "?person tw:hasRole ?role1 . " .
       "?session1 swc:isSubEventOf ?section1 . " .
       "?section1 dc:identifier ?s1 . " .
       "?abstract2 swc:relatedToEvent ?session2 . " .
       "?abstract2 tw:hasAgentWithRole ?role2 . " .
       "?person tw:hasRole ?role2 . " .
       "?session2 swc:isSubEventOf ?section2 . " .
       "?section2 dc:identifier ?s2 . " .
       "filter (?s1 < ?s2) " .
       "}" .
       "group by ?s1 ?s2 " .
       "order by desc(?count)";

function getKey($value, $array)
{
	$keyArray = array_keys($array, $value);
	if (count($keyArray) == 1) return $keyArray[0];
	else return -1;
}

$results = sparqlSelect($query, $endpoint);

$sections = array();
$nodeArray = array();
$edgeArray = array();

foreach ($results["results"]["bindings"] as $i => $binding)
{
	$section1 = $binding["s1"]["value"];
	$section2 = $binding["s2"]["value"];
	$id1 = -1;
	$id2 = -1;
	if (getKey($section1, $sections) < 0)
	{
		$sections[] = $section1;
		$id1 = count($sections) - 1;
		$nodeArray[] = array( "name" => $section1, "group" => count($sections) - 1 );
	}
	else $id1 = getKey($section1, $sections);
	if (getKey($section2, $sections) < 0)
	{
		$sections[] = $section2;
		$id2 = count($sections) - 1;
		$nodeArray[] = array( "name" => $section2, "group" => count($sections) - 1 );
	}
	else $id2 = getKey($section2, $sections);
	$val = round(intval($binding["count"]["value"]) / 2000) ;
	if ($val > 0)
	{
		$edgeArray[] = array( "source" => $id1, "target" => $id2, "value" => $val );
	}
}

$output = array( "nodes" => $nodeArray, "links" => $edgeArray );
echo json_encode($output);