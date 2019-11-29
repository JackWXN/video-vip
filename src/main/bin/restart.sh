#!/bin/bash
cd `dirname $0`
./stop.sh $@
sleep 10
./start.sh $@
