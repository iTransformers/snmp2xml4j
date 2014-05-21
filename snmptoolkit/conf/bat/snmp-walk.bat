set LIB="..\lib"
set DIST="..\lib"
#set JAVA_HOME="%LIB%\jdk1.6.0_25"
#set CLASSPATH=".;%DIST%\snmp-discoverer.jar;%LIB%\snmp4j\snmp4j-1.11.2.jar;%LIB%\mibble\mibble-mibs-2.9.2.jar;%LIB%\mibble\grammatica-1.5.jar;%LIB%\mibble\mibble-parser-2.9.2.jar;%LIB%\log4j\log4j-1.2.14.jar;%LIB%\apache-commons\commons-io-2.0.1.jar;%LIB%\apache-commons\commons-beanutils-1.8.3.jar;%LIB%\apache-commons\commons-logging-1.1.1.jar;%LIB%\saxon\saxon9.jar"
set CLASSPATH=".;%LIB%\*"
java -Xms256m -Xmx512m -classpath %CLASSPATH% net.itransformers.snmptoolkit.Walk %*