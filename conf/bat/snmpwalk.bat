@echo off


IF "%SNMP2XML4J%"=="" (
    ECHO Please define SNMP2XML4J environment variable pointing to your snmp2xml4j folder!
    ) ELSE (
	SETLOCAL
	set BASE_DIR=%SNMP2XML4J%
	set LIB=%SNMP2XML4J%/lib
	set CLASSPATH=".;%LIB%\*"
    java -Xms256m -Xmx512m -Dbase.dir=%BASE_DIR%  -classpath %CLASSPATH% net.itransformers.snmp2xml4j.snmptoolkit.MainClass -O walk %*
)
