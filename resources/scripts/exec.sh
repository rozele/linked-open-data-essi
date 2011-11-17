#!/bin/bash

PROJECTDIR=/projects/esip/linked-open-data-essi/trunk
MAINCLASS=org.agu.essi.data.AguSessionCrawler
FORMAT=rdf/xml

if [ $# -ne 4 ]; then
    echo "Usage: crawler [output_dir] [meeting_ids] [optional_endpoint] [boolean_use_graph]"
else
    DIR=$1
    IDS=$2
    ENDPOINT=$3
    GRAPH=$4
    if [ $GRAPH -ne 1 ]; then
	ARGS="--outputDirectory $DIR --outputFormat $FORMAT --endpoint $ENDPOINT --meetings $IDS"
    else
	ARGS="--outputDirectory $DIR --outputFormat $FORMAT --endpoint $ENDPOINT --meetings $IDS --graph"
    fi
    cd $PROJECTDIR
    mvn exec:java -Dexec.mainClass="$MAINCLASS" -Dexec.args="$ARGS"
fi