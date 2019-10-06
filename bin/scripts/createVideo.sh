#!/usr/bin/env bash
SEARCH_TERM="$1"
VIDEO_NAME="$2"
NUM_IMAGES="$3"

cat images/* | ffmpeg -f image2pipe -framerate 0.3 -i - -vf "scale=trunc(iw/4)*2:trunc(ih/4)*2" -r 40 -pix_fmt yuv420p -y build/slideshow.mp4 &> /dev/null

ffmpeg -i build/slideshow.mp4 -vf "drawtext=fontfile=myFont.ttf:fontsize=60:fontcolor=white:x=(w-text_w)/2:y=(h-text_h)/2:text=$SEARCH_TERM" -y build/slide.mp4 &> /dev/null

ffmpeg -i build/slide.mp4 -i build/output.wav -strict -2 -y videos/$VIDEO_NAME.mp4 

rm -f build/output.wav
rm -f images/*

exit 0