#!/bin/sh

LIB=../lib
BASE_DIR=..
#JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
CLASSPATH=".:${LIB}/*";
java -Dbase.dir=${BASE_DIR} ${JAVA_OPTS} -classpath ${CLASSPATH}  net.itransformers.snmptoolkit.SnmpWalkToSnmpsimConvertor2 $*