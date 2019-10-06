#!/usr/bin/env bash
if [ ! -e audio ]; then
	mkdir audio
fi

echo "$2" | text2wave -o audio/"$1".wav -eval "(voice_$3)"
exit 0