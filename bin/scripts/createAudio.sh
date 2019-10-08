#!/usr/bin/env bash

mkdir -p audio

echo "$2" | text2wave -o audio/"$1".wav -eval "(voice_$3)"
exit 0