#!/bin/bash
if [ ! -e temp ]; then
	mkdir temp
fi

sox $(for arg in $1; do echo "audio/$arg.wav"; done) build/output.wav