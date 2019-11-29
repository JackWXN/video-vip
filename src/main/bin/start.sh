#!/bin/bash
cd `dirname $0`
BIN_DIR=`pwd`


cd ..
DEPLOY_DIR=`pwd`
PRO_NAME="${DEPLOY_DIR##*/}"
echo "proname:"$PRO_NAME

PAGE_NAME="com."${PRO_NAME//-/.} 
echo "page src:"$PAGE_NAME

if [ $# -lt 1 ]; then
    echo "ERROR:usage ./start.sh online|test|dev [jmx|debug]"
    exit 1
fi

if [ "$1" != "online" ] && [ "$1" != "test" ] && [ "$1" != "dev" ]; then
    echo "ERROR:usage ./start.sh online|test|dev [jmx|debug]"
    exit 1
fi

HJ=$1
CONF_DIR=$DEPLOY_DIR/conf/$HJ

JAVA_HOME=""

JAVA_MEM_OPTS=" -server -Xmx1g -Xms1g -Xmn256m -XX:PermSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "

JAVA_J=java

#设置环境变量
if [ "$HJ" == "dev" ]; then
    JAVA_HOME=/usr/jdk/jdk1.8.0/bin
    JAVA_J=$JAVA_HOME
    JAVA_MEM_OPTS=" -server -Xmx256m -Xms256m -Xmn256m -XX:MetaspaceSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
fi
if [ "$HJ" == "test" ]; then
    JAVA_HOME=/usr/jdk/jdk1.8.0/bin
    JAVA_J=$JAVA_HOME
    JAVA_MEM_OPTS=" -server -Xmx256m -Xms256m -Xmn256m -XX:MetaspaceSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
fi
if [ "HJ" == "online" ]; then
    JAVA_HOME=/app/middleware/jdk1.8.0_111/bin
    JAVA_J=$JAVA_HOME
    JAVA_MEM_OPTS=" -server -Xmx1g -Xms1g -Xmn256m -XX:MetaspaceSize=128m -Xss256k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:+UseCMSCompactAtFullCollection -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 "
fi

#项目名称
SERVER_NAME=$PRO_NAME
#SERVER_PROTOCOL=`sed '/dubbo.protocol.name/!d;s/.*=//' $CONF_DIR/dubbo.properties | tr -d '\r'`
#项目端口
#SERVER_PORT=`sed '/dubbo.protocol.port/!d;s/.*=//' $CONF_DIR/dubbo.properties | tr -d '\r'`
#项目日志目录
LOGS_FILE=$DEPLOY_DIR/logs

if [ -z "$SERVER_NAME" ]; then
    SERVER_NAME=`hostname`
fi

PIDS=`ps -ef | grep java | grep "$CONF_DIR" | grep -v grep | awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

LOGS_DIR=""
if [ -n "$LOGS_FILE" ]; then
    LOGS_DIR=`dirname $LOGS_FILE`
else
    LOGS_DIR=$DEPLOY_DIR/logs
fi
if [ ! -d $LOGS_DIR ]; then
    mkdir $LOGS_DIR
fi
STDOUT_FILE=$LOGS_DIR/logs/stdout.log

LIB_DIR=$DEPLOY_DIR/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`
JAVA_OPTS=" -Ddubbo.shutdown.hook=true -Djava.awt.headless=true -Dfile.encoding=UTF-8 -Djava.net.preferIPv4Stack=true"

JAVA_DEBUG_OPTS=""

if [ "$HJ" == "dev" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8300,server=y,suspend=n "
fi
if [ "$HJ" == "test" ]; then
    JAVA_DEBUG_OPTS=" -Xdebug -Xnoagent -Djava.compiler=NONE -Xrunjdwp:transport=dt_socket,address=8100,server=y,suspend=n "
fi

JAVA_JMX_OPTS=""
if [ "$2" = "jmx" ]; then
    JAVA_JMX_OPTS=" -Dcom.sun.management.jmxremote.port=1099 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false "
fi
BITS=`java -version 2>&1 | grep -i 64-bit`

echo -e "Starting the $SERVER_NAME ...\c"
STDOUT_FILE=/dev/null
nohup $JAVA_J/java $JAVA_OPTS $JAVA_MEM_OPTS $JAVA_DEBUG_OPTS $JAVA_JMX_OPTS -classpath $CONF_DIR:$LIB_JARS $PAGE_NAME.MainApplication > $STDOUT_FILE 2>&1 &

COUNT=0
while [ $COUNT -lt 1 ]; do    
    echo -e ".\c"
    sleep 1 
    COUNT=`ps -ef | grep java | grep "$CONF_DIR" | grep -v grep | awk '{print $2}' | wc -l`
    if [ $COUNT -gt 0 ]; then
        break
    fi
done

echo "OK!"
PIDS=`ps -ef | grep java | grep "$CONF_DIR" | grep -v grep | awk '{print $2}'`
echo $PIDS > logs/server.pid
echo "PID: $PIDS"
echo "STDOUT: $STDOUT_FILE"