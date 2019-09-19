#!/bin/bash
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
# 
#    http://www.apache.org/licenses/LICENSE-2.0
# 
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

#if [ $# -lt 1 ];
#then
#	echo "USAGE: $0 [-daemon] server.properties"
#	exit 1
#fi
base_dir=$(dirname $0)/..

if [ "x$EDIP_SFTP_SERVER_HOME" = "x" ]; then
  export  EDIP_SFTP_SERVER_HOME=$(cd $base_dir; pwd)

  echo "EDIP_SFTP_SERVER_HOME:"$EDIP_SFTP_SERVER_HOME
fi

if [ "x$EDIP_SFTPD_LOG4J_OPTS" = "x" ]; then
    export EDIP_SFTPD_LOG4J_OPTS="-Dlog4j.configuration=file:$EDIP_SFTP_SERVER_HOME/conf/log4j.properties -Dedip.sftpd.home=$EDIP_SFTP_SERVER_HOME"
fi

if [ "x$EDIP_SFTPD_HEAP_OPTS" = "x" ]; then
    export EDIP_SFTPD_HEAP_OPTS="-Xmx1G -Xms1G"
fi

EXTRA_ARGS="-name EDIP_SFTPD_Server "

MAIN_CLASS="com.cib.edip.edipsftpserver.EdipSftpserverApplication"

CONF_FILE="application.yml"

CONF_FILE_OPT=" --spring.config.location=$EDIP_SFTP_SERVER_HOME/conf/"$CONF_FILE



while [ $# -gt 0 ]; do

COMMAND=$1
case $COMMAND in
  -daemon)
    EXTRA_ARGS_DAEMON="-daemon "
    shift
    ;;
  -n)
    SERVER_NAME=$2
    EXTRA_ARGS="-name $SERVER_NAME -Dsftpd.name=$SERVER_NAME"

    echo "SFTPD_SERVER start by name $SERVER_NAME"
    SERVER_NAME="-n $SERVER_NAME "
    shift 2
    ;;
  -f)
    if [ -f "$2" ];then
    echo "SFTPD_SERVER start using config file $2"
    CONF_FILE_OPT=" --spring.config.location=$2"
    else
    echo "config file $2 isn't exist"
    exit
    fi

    shift 2
    ;;
  *)
    ;;
esac
done


exec $EDIP_SFTP_SERVER_HOME/bin/edip-sftpserver-class.sh  $EXTRA_ARGS_DAEMON $EXTRA_ARGS  $MAIN_CLASS $SERVER_NAME $CONF_FILE_OPT "$@"
