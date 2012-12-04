<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8890/sparql";

$sectionsQuery = <<<EOF
prefix :        <http://esipfed.org/essi-lod/ontology#>
prefix dc:      <http://purl.org/dc/terms/>
prefix geo:     <http://www.w3.org/2003/01/geo/wgs84_pos#>
prefix foaf:    <http://xmlns.com/foaf/0.1/>
prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>
prefix tw:      <http://tw.rpi.edu/schema/>
prefix owl:     <http://www.w3.org/2002/07/owl#>
prefix xsd:     <http://www.w3.org/2001/XMLSchema#>
prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
prefix swrc:    <http://swrc.ontoware.org/ontology#>
prefix skos:    <http://www.w3.org/2004/02/skos/core#>
prefix swc:     <http://data.semanticweb.org/ns/swc/ontology#>
select distinct ?id where {
 ?session swc:isSubEventOf ?section .
 ?section swc:isSubEventOf ?meeting .
 ?section dc:identifier ?id .
}
order by ?id
EOF;

function getSections() {
  global $sectionsQuery, $endpoint;
  $output = array();
  $results = sparqlToArray(sparqlSelect($sectionsQuery, $endpoint));
  foreach ($results as $i => $binding)
    $output[] = $binding['id'];
  return $output;
}

$query = <<<EOF
prefix :        <http://esipfed.org/essi-lod/ontology#>
prefix dc:      <http://purl.org/dc/terms/>
prefix geo:     <http://www.w3.org/2003/01/geo/wgs84_pos#>
prefix foaf:    <http://xmlns.com/foaf/0.1/>
prefix rdfs:    <http://www.w3.org/2000/01/rdf-schema#>
prefix tw:      <http://tw.rpi.edu/schema/>
prefix owl:     <http://www.w3.org/2002/07/owl#>
prefix xsd:     <http://www.w3.org/2001/XMLSchema#>
prefix rdf:     <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
prefix swrc:    <http://swrc.ontoware.org/ontology#>
prefix skos:    <http://www.w3.org/2004/02/skos/core#>
prefix swc:     <http://data.semanticweb.org/ns/swc/ontology#>
select ?s1 ?s2 (count(distinct ?person) / xsd:double(?numPeople)) as ?pct where {
 {
  select (count(distinct ?subPerson) as ?numPeople) where {
   ?subAbstract swc:relatedToEvent ?subSession .
   ?subAbstract tw:hasAgentWithRole ?subRole .
   ?subPerson tw:hasRole ?subRole .
   ?subSession swc:isSubEventOf ?subSection .
   ?subSection dc:identifier ?subId .
   filter ( ?subId = "{%SECTION%}"^^xsd:string )
  }
 }
 ?abstract1 swc:relatedToEvent ?session1 .
 ?abstract1 tw:hasAgentWithRole ?role1 .
 ?person tw:hasRole ?role1 .
 ?session1 swc:isSubEventOf ?section1 .
 ?section1 dc:identifier ?s1 .
 ?abstract2 swc:relatedToEvent ?session2 .
 ?abstract2 tw:hasAgentWithRole ?role2 .
 ?person tw:hasRole ?role2 .
 ?session2 swc:isSubEventOf ?section2 .
 ?section2 dc:identifier ?s2 .	            
 filter ( ?s1 = "{%SECTION%}"^^xsd:string && ?s2 != "{%SECTION%}"^^xsd:string )
}
order by desc(?pct)
EOF;

if (@$_REQUEST['section'] != null && @$_REQUEST['section'] != '')
{
  $sections = getSections();
  $section = $_REQUEST['section'];
  if ($section == "all") {
    $query = str_replace("{%FILTER%}","",$query);
    $results = sparqlToArray(sparqlSelect($query,$endpoint));
    $chart = true;
  } else if (in_array($section,$sections)) {
    $query = str_replace("{%SECTION%}",$section,$query);
    $results = sparqlToArray(sparqlSelect($query,$endpoint));
    $chart = true;
  }
}

$script = <<<EOF
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Year');
        data.addColumn('number', 'Percent');
        data.addRows([
          {%ROWS%}
        ]);

        var options = {
          width: 800, height: 480,
          title: 'Multidisciplinary Authors at AGU from {%SECTION%}',
          hAxis: {title: 'Section', titleTextStyle: {color: 'red'}}
        };

        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>
EOF;

if (@$chart) {
  $rows = array();
  foreach($results as $i => $binding)
  {
    $rows[] = '["' . $binding['s2'] . '",' . $binding['pct'] . ']';
  }
  $script = str_replace("{%ROWS%}",implode(',',$rows),$script);
  $script = str_replace("{%SECTION%}",$section,$script);
}

?>

<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <? if (@$chart) echo $script; ?>
  </head>
  <body>
  <? $options = "<option>" . implode("</option><option>", getSections()) . "</option>";
  echo "  Select a section ID: <form action=\"sectionCorrelation.php\" method=\"get\"><select name=\"section\" width=\"4\">$options</select><input type=\"submit\"/></form>\n"; ?>
    <div id="chart_div"></div>
  </body>
</html>