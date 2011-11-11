#!/bin/bash

ENDPOINT=http://localhost:2027/sparql
DIRBASE=/projects/esip/linked-open-data-essi/trunk/resources/data/rdf
TDBLOADER=/projects/tseval/tdb-0.8.2/bin/tdbloader
TDBDIR=/projects/esip/test/tdb
SYSDIR=/projects/esip/test/system
EXEC=/projects/esip/linked-open-data-essi/trunk/resources/scripts/exec.sh

#Data for IN
#mkdir $DIRBASE/IN
#$EXEC $DIRBASE/IN/ "sm05,IN;fm05,IN;sm06,IN;fm06,IN;sm07,IN;fm07,IN;ja08,IN;fm08,IN;ja09,IN;fm09,IN;ja10,IN;fm10,IN" $ENDPOINT
#rm $DIRBASE/IN/keywords.rdf
#$TDBLOADER -loc=$TDBDIR $DIRBASE/IN/*
#cd $SYSDIR
#$SYSDIR/stop.sh
#$SYSDIR/start.sh
#$SYSDIR/wait.sh

#Data for ED
#mkdir $DIRBASE/ED
#$EXEC $DIRBASE/ED/ "sm05,ED;fm05,ED;sm06,ED;fm06,ED;sm07,ED;fm07,ED;ja08,ED;fm08,ED;ja09,ED;fm09,ED;ja10,ED;fm10,ED" $ENDPOINT
#rm $DIRBASE/ED/keywords.rdf
#$TDBLOADER -loc=$TDBDIR $DIRBASE/ED/*
#cd $SYSDIR
#$SYSDIR/stop.sh
#$SYSDIR/start.sh
#$SYSDIR/wait.sh

#Data for SH
mkdir $DIRBASE/SH
$EXEC $DIRBASE/SH/ "sm05,SH;fm05,SH;sm06,SH;fm06,SH;sm07,SH;fm07,SH;ja08,SH;fm08,SH;ja09,SH;fm09,SH;ja10,SH;fm10,SH" $ENDPOINT
rm $DIRBASE/SH/keywords.rdf
$TDBLOADER -loc=$TDBDIR $DIRBASE/SH/*
cd $SYSDIR
$SYSDIR/stop.sh
$SYSDIR/start.sh
$SYSDIR/wait.sh

#Data for OS
mkdir $DIRBASE/OS
$EXEC $DIRBASE/OS/ "sm05,OS;fm05,OS;sm06,OS;fm06,OS;sm07,OS;fm07,OS;ja08,OS;fm08,OS;ja09,OS;fm09,OS;ja10,OS;fm10,OS" $ENDPOINT
rm $DIRBASE/OS/keywords.rdf
$TDBLOADER -loc=$TDBDIR $DIRBASE/OS/*
cd $SYSDIR
$SYSDIR/stop.sh
$SYSDIR/start.sh
$SYSDIR/wait.sh
