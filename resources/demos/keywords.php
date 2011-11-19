<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8890/sparql";
$graph = "http://essi-lod.org/instances/";

$keywordsQuery = getPrefixes() .
	       "select ?k ?n ?p ?i " .
	       "from <$graph> " .
	       "where { " .
	       "?k a swrc:ResearchTopic . " .
	       "?k dc:subject ?n . " .
	       "?k dc:identifier ?i . " .
	       "optional { ?k skos:broadMatch ?p } " .
	       "}";

$query = getPrefixes() .
       "select ?k1 ?k2 (count(?abstract) as ?count) " .
       "from <$graph> " .
       "where { " .
       "?abstract swc:hasTopic ?k1 . " .
       "?abstract swc:hasTopic ?k2 . " .
       "FILTER (?k1 < ?k2) " .
       "} " .
       "group by ?k1 ?k2 " .
       "order by desc(?count)";

function getKey($value, $array)
{
	$keyArray = array_keys($array, $value);
	if (count($keyArray) == 1) return $keyArray[0];
	else return -1;
}

$keywords = array();
$groups = array();
$nodeArray = array();

$results = sparqlSelect($keywordsQuery, $endpoint);

foreach($results["results"]["bindings"] as $i => $binding)
{
	$keyword = $binding["k"]["value"];
	$name = $binding["n"]["value"];
	$id = $binding["i"]["value"];
	$parent = null;
	if (array_key_exists("p",$binding)) $parent = $binding["p"]["value"];
	else $parent = $keyword;
	$keywords[] = $keyword;
	$group = -1;
	if (getKey($parent, $groups) < 0)
	{
		$groups[] = $parent;
		$group = count($groups) - 1;
	}
	else $group = getKey($parent, $groups);
	$nodeArray[] = array( "name" => $id . " - " . $name, "group" => $group );
}

$results = sparqlSelect($query, $endpoint);

$edgeArray = array();

foreach ($results["results"]["bindings"] as $i => $binding)
{
	$keyword1 = $binding["k1"]["value"];
	$keyword2 = $binding["k2"]["value"];
	$id1 = getKey($keyword1, $keywords);
	$id2 = getKey($keyword2, $keywords);
	$val = round(intval($binding["count"]["value"]) / 50) ;
	if ($val > 0)
	{
		$edgeArray[] = array( "source" => $id1, "target" => $id2, "value" => $val );
	}
}

$output = array( "nodes" => $nodeArray, "links" => $edgeArray );
echo json_encode($output);
