snmp2xml4j
========

###Overview
Snmp2xml4j contains several Java Written tools easing the process of performing and transforming an snmp queries into a structured xml.

Currently the it is able to do snmpget, snmptset, snmpwalk and to transform the results through xslt. 
It provides a bridge between MIBs and raw data received from the SNMP enabled devices.

###Building the tool 
Checkout the code, install maven and run 'mvn package' in the main project folder.

The final built will be in ./distribution/target/snmp2xml-bin/snmp2xml 

The libraries will be in ./distribution/target/snmp2xml-bin/snmp2xml/lib

The MIB files will be in ./distribution/target/snmp2xml-bin/snmp2xml/mibs

The executables will be in ./distribution/target/snmp2xml-bin/snmp2xml/bin

The XSLT files will be in ./distribution/target/snmp2xml-bin/snmp2xml/conf/xslt




###snmp2xml4j console tools
[Running the tool](./conf/bat/snmp2xml4jReadme.md)

[Transforming the results through xslt] (./conf/bat/XsltTransformerReadme.md)

[Fix NET-SNMP snmpwalks to work with snmpsim (snmp simulator)] (./conf/bat/snmpwalk2snmpsimReadme.md)


###Junit test case
```java
      @Test
       public void openWrtTestWalk() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String oids = "system,ifEntry";
        String mibDir = "./mibs";

        HashMap<CmdOptions, String> cmdOptions = new HashMap<CmdOptions, String>();
        cmdOptions.put(CmdOptions.MIBS_DIR,mibDir);
        cmdOptions.put(CmdOptions.ADDRESS,"193.19.172.133/161");
        cmdOptions.put(CmdOptions.COMMUNITY,"netTransformer-r");
        cmdOptions.put(CmdOptions.VERSION,"2c");
        cmdOptions.put(CmdOptions.TIMEOUT,"1000");
        cmdOptions.put(CmdOptions.RETRIES,"100");
        cmdOptions.put(CmdOptions.MAX_REPETITIONS,"100");
        cmdOptions.put(CmdOptions.OIDS,oids);
        cmdOptions.put(CmdOptions.OUTPUT_FILE,"./snmptoolkit/src/test/java/resources/openwrt.xml");

        Properties parameters = new Properties();
        Walk.fillParams(cmdOptions, parameters);
        Walk walker = new Walk(new File(mibDir), false, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        Node root = walker.walk(oids.split(","), parameters);
        String xml = Walk.printTreeAsXML(root, true);
        Walk.outputXml(cmdOptions,xml);
        String expectedXML = FileUtils.readFileToString(new File("./snmptoolkit/src/test/java/resources/openwrt.xml"));
        Assert.assertEquals(expectedXML, xml);
    }
```
###SNMP2XML output
```xml
<?xml version="1.0" ?>
<root>
<iso oid="1" >
	<org oid="1.3" >
		<dod oid="1.3.6" >
			<internet oid="1.3.6.1" >
				<mgmt oid="1.3.6.1.2" >
					<mib-2 oid="1.3.6.1.2.1" >
						<system oid="1.3.6.1.2.1.1" >
							<sysDescr oid="1.3.6.1.2.1.1.1">adult playground</sysDescr>
							<sysObjectID oid="1.3.6.1.2.1.1.2">1.2.3.4</sysObjectID>
							<sysUpTime oid="1.3.6.1.2.1.1.3" >
							</sysUpTime>
							<sysContact oid="1.3.6.1.2.1.1.4">nmilovanov@nbu.bg</sysContact>
							<sysName oid="1.3.6.1.2.1.1.5">HeartOfGold</sysName>
							<sysLocation oid="1.3.6.1.2.1.1.6">nbu</sysLocation>
							<sysServices oid="1.3.6.1.2.1.1.7">0:00:00.05</sysServices>
							<sysORLastChange oid="1.3.6.1.2.1.1.8">0:00:00.05</sysORLastChange>
						</system>
						<interfaces oid="1.3.6.1.2.1.2" >
							<ifTable oid="1.3.6.1.2.1.2.2" >
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">1</index>
									<instance instanceIndex="1" instanceName="ifIndex">1</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.1" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.1" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">lo</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.1" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">24</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.1" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">16436</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.1" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.1" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only"></ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.1" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.1" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.1" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7714</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">70</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7714</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">70</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.1" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.1" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.1" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">2</index>
									<instance instanceIndex="2" instanceName="ifIndex">2</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.2" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">2</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.2" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">eth0</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.2" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.2" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.2" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.2" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.2" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.2" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.2" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">749553655</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">31922687</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1853504692</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">30422155</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.2" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.2" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.2" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">4</index>
									<instance instanceIndex="3" instanceName="ifIndex">4</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.4" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">4</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.4" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">sit0</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.4" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">131</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.4" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1480</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.4" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.4" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only"></ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.4" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">2</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.4" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">2</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.4" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.4" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.4" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.4" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">5</index>
									<instance instanceIndex="4" instanceName="ifIndex">5</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.5" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">5</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.5" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">br-wifi</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.5" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.5" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.5" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.5" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.5" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.5" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.5" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">2120688494</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7607706</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">2</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">996305470</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7335058</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.5" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.5" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.5" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">6</index>
									<instance instanceIndex="5" instanceName="ifIndex">6</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.6" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.6" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">eth0.19</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.6" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.6" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.6" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.6" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.6" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.6" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.6" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">10161228</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">119014</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.6" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.6" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.6" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">7</index>
									<instance instanceIndex="6" instanceName="ifIndex">7</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.7" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">7</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.7" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">eth0.20</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.7" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.7" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.7" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.7" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.7" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.7" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.7" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">688</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">8</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.7" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.7" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.7" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">8</index>
									<instance instanceIndex="7" instanceName="ifIndex">8</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.8" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">8</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.8" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">eth0.18</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.8" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.8" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.8" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.8" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.8" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.8" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.8" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">305070</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7235</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">13</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">806</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">9</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.8" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.8" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.8" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">9</index>
									<instance instanceIndex="8" instanceName="ifIndex">9</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.9" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">9</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.9" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">eth0.17</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.9" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.9" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.9" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.9" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.9" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.9" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.9" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">3272084438</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">9756264</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">335440</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">177560</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">232786715</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">11777520</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.9" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.9" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.9" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">10</index>
									<instance instanceIndex="9" instanceName="ifIndex">10</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.10" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">10</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.10" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">eth0.2</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.10" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.10" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.10" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.10" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.10" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.10" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.10" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1197520187</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">20373707</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1450009</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1488864940</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">18525595</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.10" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.10" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.10" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">11</index>
									<instance instanceIndex="10" instanceName="ifIndex">11</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.11" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">11</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.11" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">wlan0</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.11" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">6</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.11" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.11" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">10000000</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.11" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">64:70:02:cd:74:cc</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.11" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.11" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.11" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">2230436137</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7623825</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1126353073</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">7333393</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.11" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.11" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.11" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">12</index>
									<instance instanceIndex="11" instanceName="ifIndex">12</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.12" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">12</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.12" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">6in4-henet</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.12" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">131</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.12" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1480</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.12" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.12" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">c1:13:ac:85:00:00</ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.12" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.12" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.12" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">706</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">5</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">1044</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">8</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.12" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.12" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.12" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
								<ifEntry oid="1.3.6.1.2.1.2.2.1" primitiveSyntax="SEQUENCE" snmpSyntax ="SEQUENCE" access="not-accessible">
									<index name="ifIndex" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" oid="1.3.6.1.2.1.2.2.1.1" access="read-only">13</index>
									<instance instanceIndex="12" instanceName="ifIndex">13</instance>
									<ifIndex oid="1.3.6.1.2.1.2.2.1.1.13" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">13</ifIndex>
									<ifDescr oid="1.3.6.1.2.1.2.2.1.2.13" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only">tun0</ifDescr>
									<ifType oid="1.3.6.1.2.1.2.2.1.3.13" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifType>
									<ifMtu oid="1.3.6.1.2.1.2.2.1.4.13" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1500</ifMtu>
									<ifSpeed oid="1.3.6.1.2.1.2.2.1.5.13" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifSpeed>
									<ifPhysAddress oid="1.3.6.1.2.1.2.2.1.6.13" primitiveSyntax="OCTET STRING" snmpSyntax ="OCTET STRING" access="read-only"></ifPhysAddress>
									<ifAdminStatus oid="1.3.6.1.2.1.2.2.1.7.13" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-write">1</ifAdminStatus>
									<ifOperStatus oid="1.3.6.1.2.1.2.2.1.8.13" primitiveSyntax="INTEGER" snmpSyntax ="INTEGER" access="read-only">1</ifOperStatus>
									<ifLastChange oid="1.3.6.1.2.1.2.2.1.9.13" primitiveSyntax="INTEGER" snmpSyntax ="TimeTicks" access="read-only">0:00:00.00</ifLastChange>
									<ifInOctets oid="1.3.6.1.2.1.2.2.1.10.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">174244329</ifInOctets>
									<ifInUcastPkts oid="1.3.6.1.2.1.2.2.1.11.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">288154</ifInUcastPkts>
									<ifInNUcastPkts oid="1.3.6.1.2.1.2.2.1.12.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInNUcastPkts>
									<ifInDiscards oid="1.3.6.1.2.1.2.2.1.13.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInDiscards>
									<ifInErrors oid="1.3.6.1.2.1.2.2.1.14.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInErrors>
									<ifInUnknownProtos oid="1.3.6.1.2.1.2.2.1.15.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifInUnknownProtos>
									<ifOutOctets oid="1.3.6.1.2.1.2.2.1.16.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">81458571</ifOutOctets>
									<ifOutUcastPkts oid="1.3.6.1.2.1.2.2.1.17.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">234250</ifOutUcastPkts>
									<ifOutNUcastPkts oid="1.3.6.1.2.1.2.2.1.18.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutNUcastPkts>
									<ifOutDiscards oid="1.3.6.1.2.1.2.2.1.19.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">348</ifOutDiscards>
									<ifOutErrors oid="1.3.6.1.2.1.2.2.1.20.13" primitiveSyntax="INTEGER" snmpSyntax ="Counter32" access="read-only">0</ifOutErrors>
									<ifOutQLen oid="1.3.6.1.2.1.2.2.1.21.13" primitiveSyntax="INTEGER" snmpSyntax ="Gauge32" access="read-only">0</ifOutQLen>
									<ifSpecific oid="1.3.6.1.2.1.2.2.1.22.13" primitiveSyntax="OBJECT IDENTIFIER" snmpSyntax ="OBJECT IDENTIFIER" access="read-only">0.0</ifSpecific>
								</ifEntry>
							</ifTable>
						</interfaces>
					</mib-2>
				</mgmt>
			</internet>
		</dod>
	</org>
</iso>
</root>
```
