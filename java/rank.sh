#!/bin/sh

CP=.:classes

for i in lib/*jar; do
    CP=$CP:$i:/usr/share/java/postgresql.jar
done

java -Xmx1024M -Xms1024M -cp $CP walker.PrintRank "$@" 
