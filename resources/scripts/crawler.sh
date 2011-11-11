#!/bin/bash

ENDPOINT=http://localhost:2020/sparql
DIRBASE=./resources/data/rdf
TDBLOADER=/opt/tdb/bin/tdbloader
TDBDIR=/work/data/esip
JOSEKIDIR=/opt/joseki/scripts/essi

#Data for fm10 IN
mkdir $DIRBASE/IN
./exec.sh $DIRBASE/IN/ "sm05,IN;fm05,IN;sm06,IN;fm06,IN;sm07,IN;fm07,IN;ja08,IN;fm08,IN;ja09,IN;fm09,IN;ja10,IN;fm10,IN" $ENDPOINT
#rm $DIRBASE/fm10_IN/keywords.rdf
$TDBLOADER -loc=$TDBDIR $DIRBASE/IN/*
$JOSEKIDIR/stop.sh
$JOSEKIDIR/start.sh