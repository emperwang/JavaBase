#!/bin/sh

# Copyright EnterpriseDB Corporation, 2014-2017. All Rights Reserved.


# version name
EFM=efm-2.1

# Configuration file for Java
EFM_CONFIG=/etc/sysconfig/$EFM
RUN_JAVA=/usr/$EFM/bin/runJavaApplication.sh

source $EFM_CONFIG
source $RUN_JAVA

verifyJRE


LIBS=/usr/$EFM/lib
runJREApplication -cp $LIBS/EFM-2.1.2.jar com.enterprisedb.efm.main.EfmCommand "$@"
