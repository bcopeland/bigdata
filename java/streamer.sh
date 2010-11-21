#!/bin/sh

CP=.:classes

for i in lib/*jar; do
    CP=$CP:$i:/usr/share/java/postgresql.jar
done

consumer_key="JvQ5hmBionA6qGBy3qQ1A"
consumer_secret="C9XI6Wo1Xg1b062M2btVTPIn6CzKCMmBhOC5U5jumQ"
access_token="189948750-q9Y8BaF35abcgnV4oyLt17zUxXhngWQNIDv91Tha"
access_token_secret="bREif8tE7ErmztLttV6gYZvkqPfIkRcd7iOAYqCGiI"


DEFS="-Dtwitter4j.oauth.consumerKey=${consumer_key} -Dtwitter4j.oauth.consumerSecret=${consumer_secret} -Dtwitter4j.oauth.accessToken=${access_token} -Dtwitter4j.oauth.accessTokenSecret=${access_token_secret}"

java -Xmx1024M -Xms1024M -cp $CP $DEFS walker.Streamer "$@"
