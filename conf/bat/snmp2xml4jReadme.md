#SNMP walk example

##On Winodws
snmp-walk.bat Walk -md ../mibs -a 10.10.10.10/161 -c public -v 2c -t 1000 -r 1 -m 100 -f bgp.xml -o "bgp4PathAttrEntry"
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
snmp-walk.sh Walk -md ../mibs -a 10.10.10.10/161 -c public -v 2c -t 1000 -r 1 -m 100 -f bgp.xml -o "bgp4PathAttrEntry"
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
