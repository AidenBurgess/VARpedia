#!/bin/bash

echo "(voice_$2) (SayText \"${1}\")" | festival -i

exit 0