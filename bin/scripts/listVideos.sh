#!/usr/bin/env bash
#NUM_VIDEOS=`ls videos | nl | cut -f 1 -d '.' | wc -l`
#if [ "$NUM_VIDEOS" == 0 ]
#    then
#        echo "There are currently no videos."
#        return 1;
#    fi
#echo "There are currently $NUM_VIDEOS videos."
ls videos | sort | cut -f 1 -d '.' 
exit 0