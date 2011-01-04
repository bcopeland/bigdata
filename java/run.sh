#!/bin/sh

CP=.:classes

for i in lib/*jar; do
    CP=$CP:$i:/usr/share/java/postgresql.jar
done

java -Xmx512M -Xms512M -cp $CP walker.RandWalk "$@"
