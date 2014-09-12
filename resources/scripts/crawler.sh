#!/bin/bash

GRAPH=http://essi-lod.org/instances/
DIRBASE=/projects/esip/linked-open-data-essi/trunk/resources/data/rdf
VLOAD=/opt/virtuoso/scripts/vload
EXEC=/projects/esip/linked-open-data-essi/trunk/resources/scripts/exec.sh
ENDPOINT=http://aquarius.tw.rpi.edu:8890/sparql
SECTIONS="C ED EP G GA GC GP H IA MA MR NH NS NG OS PA S SA SH SM SP DI T V"
COMPLETE="IN U A PP P B"

#for s in $COMPLETE
#do
#    for f in $DIRBASE/$s/*.rdf
#    do
#	echo "Loading $f..."
#	$VLOAD rdf $f $GRAPH
#    done
#done

for s in $SECTIONS
do
    mkdir $DIRBASE/$s
    $EXEC $DIRBASE/$s/ "sm05,$s;fm05,$s;sm06,$s;fm06,$s;sm07,$s;fm07,$s;ja08,$s;fm08,$s;ja09,$s;fm09,$s;ja10,$s;fm10,$s" $ENDPOINT 1
    rm $DIRBASE/$s/keywords.rdf
    rm $DIRBASE/$s/meetings.rdf
    for f in $DIRBASE/$s/*.rdf
    do
	$VLOAD rdf $f $GRAPH
    done    
done
