#SNMP walk example

## Environment settings
Setup SNMP2XML4J environment variable pointing to the folder you got after you have unpacked the snmp2xmlj tool.  For example:

SNMP2XML4J=/home/user/snmp2xml4j

Then if the MIB files in the $SNMP2XML4J/mibs folder does not contain your oids you have to find the mib files that will have them and copy them to the $SNMP2XML4J/mibs folder.

## Snmp walk example usage
###SNMP version 3 walk with auth and without privileges (AUTH_NOPRIV)

snmpwalk.sh -md $SNMP2XML4J/mibs -v 3 -a 195.218.195.228 -P 161 -pr udp -aa AUTH_NOPRIV -ap MD5 -u usr-md5-none -A authkey1 -t 1000 -r 1 -m 100 -o sysName,sysDescr

###SNMP version 3 walk with auth and with privileges (AUTH_PRIV)

snmpwalk.sh -md $SNMP2XML4J/mibs -v 3 -a 195.218.195.228 -P 161 -pr udp -aa AUTH_PRIV -ap SHA -u usr-sha-aes -A authkey1 -Y privkey1 -pp AES  -t 1000 -r 1 -m 100 -o sysDescr,sysName

###SNMP version 3 walk without auth and without privileges (NOAUTH_NOPRIV)

snmpwalk.sh -md $SNMP2XML4J/mibs -v 3 -a 195.218.195.228 -P 161 -pr udp -aa NOAUTH_NOPRIV -u usr-none-none -t 1000 -r 1 -m 100 -o sysDescr,sysName

###SNMP version 2 walk with community string

snmpwalk.sh -md $SNMP2XML4J/mibs -v 2 -a 195.218.195.228 -P 161 -pr udp -c public -t 1000 -r 1 -o sysDescr,sysName

###SNMP version 1 walk with community string

snmpwalk -md $SNMP2XML4J/mibs -v 2 -a 195.218.195.228 -P 161 -pr udp -c public -t 1000 -r 1 -m 100 -o sysDescr,sysName



##SNMP Get example usage

###SNMP version 1 get with community string

snmpget.sh -md $SNMP2XML4J/mibs -v 1 -a 195.218.195.228 -P 161 -pr udp -c public -t 1000 -r 1 -m 100 -o sysDescr,sysName

###SNMP version 2 get with community string

snmpget.sh -md $SNMP2XML4J/mibs -v 2 -a 195.218.195.228 -P 161 -pr udp -c public -t 1000 -r 1 -m 100 -o sysDescr,sysName

###SNMP version 3 get with auth and without privileges (AUTH_NOPRIV)

snmpget.sh -md $SNMP2XML4J/mibs -v 3 -a 195.218.195.228 -P 161 -pr udp -aa AUTH_NOPRIV -ap MD5 -u usr-md5-none -A authkey1 -t 1000 -r 1 -m 100 -o sysName,sysDescr

###SNMP version 3 get with auth and with privileges (AUTH_PRIV)

snmpget.sh -md $SNMP2XML4J/mibs -v 3 -a 195.218.195.228 -P 161 -pr udp -aa AUTH_PRIV -ap SHA -u usr-sha-aes -A authkey1 -Y privkey1 -pp AES  -t 1000 -r 1 -m 100 -o sysDescr,sysName

###SNMP version 3 get without auth and without privileges (NOAUTH_NOPRIV)

snmpget.sh -md $SNMP2XML4J/mibs -v 3 -a 195.218.195.228 -P 161 -pr udp -aa NOAUTH_NOPRIV -u usr-none-none -t 1000 -r 1 -m 100 -o sysDescr,sysName
