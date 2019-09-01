#!/bin/bash
BASE=`pwd`
JAR_NAME="https-server.jar"
LOG_JAR=${BASE}/"jSSLKeyLog.jar"

# -javaagent:jSSLKeyLog.jar=${BASE}/strace.log 追踪session key 用于wireshark解析https报文
JAVA_ARGS="-cp ${LOG_JAR} -javaagent:jSSLKeyLog.jar=${BASE}/strace.log"

java ${JAVA_ARGS}  -jar ${BASE}/${JAR_NAME}

