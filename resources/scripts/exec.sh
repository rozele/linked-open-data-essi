#!/bin/bash

PROJECTDIR=/projects/esip/linked-open-data-essi/trunk
MAINCLASS=org.agu.essi.data.AguSessionCrawler
FORMAT=rdf/xml

if [ $# -ne 3 ]; then
    echo "Usage: crawler [output_dir] [meeting_ids] [optional_endpoint]"
else
    DIR=$1
    IDS=$2
    ENDPOINT=$3
    cd $PROJECTDIR
    mvn exec:java -Dexec.mainClass="$MAINCLASS" -Dexec.args="--outputDirectory $DIR --outputFormat $FORMAT --endpoint $ENDPOINT --meetings $IDS"
fi