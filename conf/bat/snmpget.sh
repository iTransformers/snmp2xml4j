#!/bin/sh
if [ -z ${SNMP2XML4J+x} ]; then echo "SNMP2XML4J project home variable is unset"; exit; else echo "SNMP2XML4J is set to '$SNMP2XML4J'";  fi

LIB=${SNMP2XML4J}/lib
BASE_DIR=..${SNMP2XML4J}
#JAVA_OPTS=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
CLASSPATH=".:${LIB}/*";
echo "Fire up snmp get process"
java ${JAVA_OPTS} -classpath ${CLASSPATH} net.itransformers.snmp2xml4j.snmptoolkit.MainClass -O get $*
