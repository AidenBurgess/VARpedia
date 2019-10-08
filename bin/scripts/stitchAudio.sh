#!/bin/bash

mkdir -p build

sox $(for arg in $1; do echo "audio/$arg.wav"; done) build/output.wav