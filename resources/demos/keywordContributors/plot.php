<?php

require_once("sparql.php");

$endpoint = "http://aquarius.tw.rpi.edu:8890/sparql";

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
select ?name (count(distinct ?abstract) as ?count) where {
 ?abstract swc:hasTopic ?keyword .
 ?keyword dc:identifier ?id .
 ?abstract tw:hasAgentWithRole ?role .
 ?person tw:hasRole ?role .
 ?person foaf:name ?name .
 filter ( ?id = "{%KEYWORD%}"^^xsd:string )
}
order by desc(?count)
limit 10
EOF;

if (@$_REQUEST['keyword'] != null && @$_REQUEST['keyword'] != '')
{
  $keyword = $_REQUEST['keyword'];
  $query = str_replace("{%KEYWORD%}",$keyword,$query);
  $results = sparqlToArray(sparqlSelect($query,$endpoint));
  $chart = true;
}

$script = <<<EOF
    <script type="text/javascript">
      google.load("visualization", "1", {packages:["corechart"]});
      google.setOnLoadCallback(drawChart);
      function drawChart() {
        var data = new google.visualization.DataTable();
        data.addColumn('string', 'Year');
        data.addColumn('number', 'Count');
        data.addRows([
          {%ROWS%}
        ]);

        var options = {
          width: 800, height: 480,
          title: 'Most Contributing Authors for Keyword {%KEYWORD_ID%}',
          hAxis: {title: 'Author', titleTextStyle: {color: 'red'}}
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
    $rows[] = '["' . $binding['name'] . '",' . $binding['count'] . ']';
  }
  $script = str_replace("{%ROWS%}",implode(',',$rows),$script);
  $script = str_replace("{%KEYWORD_ID%}",$keyword,$script);
}

?>

<html>
  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <? if (@$chart) echo $script; ?>
  </head>
  <body>
    <div id="chart_div"></div>
  </body>
</html>