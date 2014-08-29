#SNMP walk example

## Environment settings
Setup SNMP2XML4J environment variable pointing to the folder you got after you have unpacked the snmp2xmlj tool.  For example:

SNMP2XML4J=/home/niau/snmp2xml

Then if the MIB files in the $SNMP2XML4J/mibs folder does not contain your oids you have to find the mib files that will have them and copy them to the $SNMP2XML4J/mibs folder.
Finally you can run snmpwalks like that.

##On Winodws
snmp-walk.bat Walk -md $SNMP2XML4J/mibs -a 10.10.10.10/161 -c public -v 2c -t 1000 -r 1 -m 100 -f bgp.xml -o "bgp4PathAttrEntry"
-md path to your mib files
-a  SNMP host/port
-c community
-v SNMP version
-t timeout
-r retries
-m (I forgot what it is ...)
-f output xml file
-o "snmp OID request"

##On Unix/Linux
snmp-walk.sh Walk -md $SNMP2XML4J/mibs -a 10.10.10.10/161 -c public -v 2c -t 1000 -r 1 -m 100 -f bgp.xml -o "bgp4PathAttrEntry"
-md path to your mib files
-a  SNMP host/port
-c community
-v SNMP version
-t timeout
-r retries
-m (I forgot what it is ...)
-f output xml file
-o "snmp OID request"

##An example SNMP OID request might be
"ifIndex ifDescr ifOperStatus ifAdminStatus ifNumber ifAlias ifPhysAddress ifType dot1dTpFdb dot1dTpFdbAddress dot1dTpFdbStatus dot1dTpFdbPort dot1dBasePort dot1dBasePortIfIndex system dot1dBaseBridgeAddress dot1dStpPort ipNetToMediaTable ipAddrTable lldpRemoteSystemsData cdpCacheDevicePort cdpCacheDevicePlatform cdpCacheDeviceId cdpCacheIfIndex"

#SNMP Get example

##On windows

snmp-get.bat  Get -a 10.10.10.10/161 -c public -t 1000 -r 1 -o 1.3.6.1.2.1.1.5

##On Linux

snmp-get.sh  Get -a 10.10.10.10/161 -c public -t 1000 -r 1 -o 1.3.6.1.2.1.1.5
