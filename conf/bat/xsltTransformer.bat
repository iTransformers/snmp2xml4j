IF "%SNMP2XML4J%"=="" (
    ECHO Please define SNMP2XML4J environment variable pointing to your snmp2xml4j folder!
    ) ELSE (
	SETLOCAL
	set BASE_DIR=%SNMP2XML4J%
	set LIB=%SNMP2XML4J%/lib
	set CLASSPATH=".;%LIB%\*"
    java -Dbase.dir=%BASE_DIR% %JAVA_OPTS% -classpath %CLASSPATH% net.itransformers.snmptoolkit.XsltExecutor %*
)

