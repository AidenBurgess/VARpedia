#!/usr/bin/env bash

if [ -e "videos/$1".mp4 ]
then
    exit 1
else
    exit 0
fi