#!/bin/bash

ENDPOINT=http://localhost:2020/sparql
DIRBASE=/projects/esip/linked-open-data-essi/trunk/resources/data/rdf
TDBLOADER=/projects/tseval/tdb-0.8.2/bin/tdbloader
TDBDIR=/projects/esip/tdb-test
SYSDIR=/projects/esip/system
EXEC=/projects/esip/linked-open-data-essi/trunk/resources/scripts/exec.sh

#Data for fm10 IN
#mkdir $DIRBASE/IN
#./exec.sh $DIRBASE/IN/ "sm05,IN;fm05,IN;sm06,IN;fm06,IN;sm07,IN;fm07,IN;ja08,IN;fm08,IN;ja09,IN;fm09,IN;ja10,IN;fm10,IN" $ENDPOINT
#rm $DIRBASE/fm10_IN/keywords.rdf
#$TDBLOADER -loc=$TDBDIR $DIRBASE/IN/*
#$SYSDIR/stop.sh
#$SYSDIR/start.sh

#Data for fm09 IN
mkdir $DIRBASE/fm09_IN
$EXEC $DIRBASE/fm09_IN/ "fm09,IN" $ENDPOINT
#rm $DIRBASE/fm10_IN/keywords.rdf
$TDBLOADER -loc=$TDBDIR $DIRBASE/fm09_IN/*
$SYSDIR/stop.sh
$SYSDIR/start.sh

#Data for fm10 IN
mkdir $DIRBASE/fm10_IN
$EXEC $DIRBASE/fm10_IN/ "fm10,IN" $ENDPOINT
rm $DIRBASE/fm10_IN/keywords.rdf
$TDBLOADER -loc=$TDBDIR $DIRBASE/fm10_IN/*
$SYSDIR/stop.sh
$SYSDIR/start.sh