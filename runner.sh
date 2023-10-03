#!/bin/bash

# http://mywiki.wooledge.org/BashFAQ/028
if [[ $BASH_SOURCE = */* ]]; then
    DIR=${BASH_SOURCE%/*}/
else
    DIR=./
fi

exec java -Xmx256M -jar "$DIR/build/libs/kotlinstrom-0.0.1-SNAPSHOT.jar"
