#!/bin/sh

# This is the script for starting up Failover Manager through systemd.
#
# description: Starts and stops the EnterpriseDB Failover Manager.
# Copyright EnterpriseDB Corporation, 2016-2017. All Rights Reserved.

# cluster name
CLUSTER=$2

# version name
EFM=efm-2.1

# Configuration file for Java
EFM_CONFIG=/etc/sysconfig/$EFM
RUN_JAVA=/usr/$EFM/bin/runJavaApplication.sh

source $EFM_CONFIG
source $RUN_JAVA

verifyJRE


# default properties file
PROPS=/etc/$EFM/$CLUSTER.properties

LOGDIR=/var/log/$EFM
LOG=$LOGDIR/startup-$CLUSTER.log
LIB=/usr/$EFM/lib/EFM-2.1.2.jar
CLASS=com.enterprisedb.efm.main.ServiceCommand

case "$1" in
    start)
        source $EFM_CONFIG; source $RUN_JAVA; runJREApplication -cp $LIB $CLASS $1 $PROPS >> $LOG 2>&1 < /dev/null
        ;;
    stop)
        source $EFM_CONFIG; source $RUN_JAVA; runJREApplication -cp $LIB $CLASS $1 $PROPS < /dev/null
        ;;
esac

