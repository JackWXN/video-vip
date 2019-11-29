#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`
cd ..
DEPLOY_DIR=`pwd`

if [ "$1" != "online" ] && [ "$1" != "test" ] && [ "$1" != "dev" ]; then
    echo "ERROR:usage ./stop.sh online|test|dev [dump]"
    exit 1
fi

CONF_DIR=$DEPLOY_DIR/conf/$1

SERVER_NAME="${DEPLOY_DIR##*/}"

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`cat logs/server.pid`
if [ -z "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME does not started!"
    exit 1
fi

if [ "$2" = "dump" ]; then
    $BIN_DIR/dump.sh
fi

echo -e "Stopping the $SERVER_NAME ...\c"
kill $PIDS > /dev/null 2>&1

COUNT=0
RETRY=0
while [ $COUNT -lt 1 ] && [ $RETRY -lt 5 ]; do    
    echo -e ".\c"
    sleep 1
    COUNT=1

    PID_EXIST=`ps -ef -p $PIDS | grep java`
    if [ -n "$PID_EXIST" ]; then
        COUNT=0
        break
    fi
    
    RETRY=$(($RETRY+1))
    kill $PIDS > /dev/null 2>&1
done

if [ $COUNT != 0 ]; then
    kill -9 $PIDS > /dev/null 2>&1
fi

rm -rf logs/server.pid
echo "OK!"
echo "PID: $PIDS"
