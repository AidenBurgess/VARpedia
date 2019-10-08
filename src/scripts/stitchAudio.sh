#!/bin/bash

mkdir -p temp

sox $(for arg in $1; do echo "audio/$arg.wav"; done) build/output.wav