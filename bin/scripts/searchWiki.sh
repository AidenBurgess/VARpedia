#!/usr/bin/env bash
mkdir videos
FULL_CONTENT=$(wikit "$1")
if [ "$FULL_CONTENT" == "$1 not found :^(" ]
then
    exit 1
fi
echo $FULL_CONTENT | sed 's/[.]  */&\n/g' | nl

exit 0