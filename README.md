snmp2xml
========

A number of Java Written tools performing snmp queries into a structured xml.

Currently the tool is able to do snmpget, snmptset and snmpwalk.
It provides a bridge between MIBs and raw data received from the SNMP enabled devices.

Junit test case
```java
    public void ciscoTestWalk() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String oids = "ipv6Forwarding, ipv6IfIndex,ipv6AddrEntry,ipv6NetToMediaEntry,ipv6RouteEntry,cIpAddressEntry";
        String mibDir = "mibs";

        HashMap<CmdOptions, String> cmdOptions = new HashMap<CmdOptions, String>();
        cmdOptions.put(CmdOptions.MIBS_DIR,mibDir);
        cmdOptions.put(CmdOptions.ADDRESS,"localhost/11161");
        cmdOptions.put(CmdOptions.COMMUNITY,"ciscoIp");
        cmdOptions.put(CmdOptions.VERSION,"2c");
        cmdOptions.put(CmdOptions.TIMEOUT,"1000");
        cmdOptions.put(CmdOptions.RETRIES,"100");
        cmdOptions.put(CmdOptions.MAX_REPETITIONS,"100");
        cmdOptions.put(CmdOptions.OIDS,oids);

        Properties parameters = new Properties();
        Walk.fillParams(cmdOptions, parameters);
        Walk walker = new Walk(new File(mibDir), false, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        Node root = walker.walk(oids.split(","), parameters);
        String xml = Walk.printTreeAsXML(root, true);
        String expectedXML = FileUtils.readFileToString(new File("snmptoolkit/src/test/java/resources/cisco_ipv6.xml"));
        Assert.assertEquals(expectedXML,xml);
    }
```
SNMP2XML output
```
<?xml version="1.0" ?>
<root>
    <iso oid="1">
        <org oid="1.3">
            <dod oid="1.3.6">
                <internet oid="1.3.6.1">
                    <mgmt oid="1.3.6.1.2">
                        <mib-2 oid="1.3.6.1.2.1">
                            <ipv6MIB oid="1.3.6.1.2.1.55">
                                <ipv6MIBObjects oid="1.3.6.1.2.1.55.1">
                                    <ipv6Forwarding oid="1.3.6.1.2.1.55.1.1">1</ipv6Forwarding>
                                    <ipv6IfTable oid="1.3.6.1.2.1.55.1.5">
                                    </ipv6IfTable>
                                    <ipv6AddrTable oid="1.3.6.1.2.1.55.1.8">
                                    </ipv6AddrTable>
                                    <ipv6RouteTable oid="1.3.6.1.2.1.55.1.11">
                                    </ipv6RouteTable>
                                    <ipv6NetToMediaTable oid="1.3.6.1.2.1.55.1.12">
                                    </ipv6NetToMediaTable>
                                </ipv6MIBObjects>
                            </ipv6MIB>
                        </mib-2>
                    </mgmt>
                    <private oid="1.3.6.1.4">
                        <enterprises oid="1.3.6.1.4.1">
                            <cisco oid="1.3.6.1.4.1.9">
                                <ciscoExperiment oid="1.3.6.1.4.1.9.10">
                                    <ciscoIetfIpMIB oid="1.3.6.1.4.1.9.10.86">
                                        <ciscoIetfIpMIBObjects oid="1.3.6.1.4.1.9.10.86.1">
                                            <cIp oid="1.3.6.1.4.1.9.10.86.1.1">
                                                <cIpAddressTable oid="1.3.6.1.4.1.9.10.86.1.1.2">
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:20:01:04:70:1f:0b:0a:bd:00:00:00:00:00:00:00:10</index>
                                                        <instance instanceIndex="1" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:20:01:04:70:1f:0b:0a:bd:00:00:00:00:00:00:00:10</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.16">1</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.16">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.16">1.3.6.1.4.1.9.10.86.1.1.1.1.5.1.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.0.96</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.16">2</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.16">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:20:01:04:70:1f:0b:0a:bd:00:00:00:05:00:12:00:00</index>
                                                        <instance instanceIndex="2" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:20:01:04:70:1f:0b:0a:bd:00:00:00:05:00:12:00:00</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.18.0.0">7</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.18.0.0">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.18.0.0">1.3.6.1.4.1.9.10.86.1.1.1.1.5.7.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.18.0.0.127</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.18.0.0">2</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.18.0.0">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:20:01:04:70:1f:0b:0a:bd:00:00:00:05:00:13:00:00</index>
                                                        <instance instanceIndex="3" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:20:01:04:70:1f:0b:0a:bd:00:00:00:05:00:13:00:00</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.19.0.0">6</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.19.0.0">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.19.0.0">1.3.6.1.4.1.9.10.86.1.1.1.1.5.6.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.19.0.0.127</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.19.0.0">2</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.19.0.0">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:20:01:04:70:1f:0b:0a:bd:00:00:00:05:00:15:00:00</index>
                                                        <instance instanceIndex="4" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:20:01:04:70:1f:0b:0a:bd:00:00:00:05:00:15:00:00</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.21.0.0">8</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.21.0.0">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.21.0.0">1.3.6.1.4.1.9.10.86.1.1.1.1.5.8.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.21.0.0.127</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.21.0.0">2</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.32.1.4.112.31.11.10.189.0.0.0.5.0.21.0.0">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:20:01:04:70:1f:0b:0a:bd:00:00:00:06:00:00:00:01</index>
                                                        <instance instanceIndex="5" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:20:01:04:70:1f:0b:0a:bd:00:00:00:06:00:00:00:01</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.32.1.4.112.31.11.10.189.0.0.0.6.0.0.0.1">12</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.32.1.4.112.31.11.10.189.0.0.0.6.0.0.0.1">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.32.1.4.112.31.11.10.189.0.0.0.6.0.0.0.1">1.3.6.1.4.1.9.10.86.1.1.1.1.5.12.2.16.32.1.4.112.31.11.10.189.0.0.0.6.0.0.0.1.128</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.32.1.4.112.31.11.10.189.0.0.0.6.0.0.0.1">2</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.32.1.4.112.31.11.10.189.0.0.0.6.0.0.0.1">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:fe:80:00:00:00:00:00:00:00:00:00:00:00:00:00:69</index>
                                                        <instance instanceIndex="6" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:fe:80:00:00:00:00:00:00:00:00:00:00:00:00:00:69</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.254.128.0.0.0.0.0.0.0.0.0.0.0.0.0.105">1</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.254.128.0.0.0.0.0.0.0.0.0.0.0.0.0.105">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.254.128.0.0.0.0.0.0.0.0.0.0.0.0.0.105">0.0</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.254.128.0.0.0.0.0.0.0.0.0.0.0.0.0.105">3</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.254.128.0.0.0.0.0.0.0.0.0.0.0.0.0.105">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                    <cIpAddressEntry oid="1.3.6.1.4.1.9.10.86.1.1.2.1">
                                                        <index name="cIpAddressAddrType" syntax="INTEGER" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.1">2</index>
                                                        <index name="cIpAddressAddr" syntax="OCTET STRING" oid="1.3.6.1.4.1.9.10.86.1.1.2.1.2">10:fe:80:00:00:00:00:00:00:0c:00:00:ff:fe:00:65:00</index>
                                                        <instance instanceIndex="7" instanceName="cIpAddressAddrType.cIpAddressAddr">2.10:fe:80:00:00:00:00:00:00:0c:00:00:ff:fe:00:65:00</instance>
                                                        <cIpAddressIfIndex oid="1.3.6.1.4.1.9.10.86.1.1.2.1.3.2.16.254.128.0.0.0.0.0.0.12.0.0.255.254.0.101.0">6</cIpAddressIfIndex>
                                                        <cIpAddressType oid="1.3.6.1.4.1.9.10.86.1.1.2.1.4.2.16.254.128.0.0.0.0.0.0.12.0.0.255.254.0.101.0">1</cIpAddressType>
                                                        <cIpAddressPrefix oid="1.3.6.1.4.1.9.10.86.1.1.2.1.5.2.16.254.128.0.0.0.0.0.0.12.0.0.255.254.0.101.0">0.0</cIpAddressPrefix>
                                                        <cIpAddressOrigin oid="1.3.6.1.4.1.9.10.86.1.1.2.1.6.2.16.254.128.0.0.0.0.0.0.12.0.0.255.254.0.101.0">3</cIpAddressOrigin>
                                                        <cIpAddressStatus oid="1.3.6.1.4.1.9.10.86.1.1.2.1.7.2.16.254.128.0.0.0.0.0.0.12.0.0.255.254.0.101.0">1</cIpAddressStatus>
                                                    </cIpAddressEntry>
                                                </cIpAddressTable>
                                            </cIp>
                                        </ciscoIetfIpMIBObjects>
                                    </ciscoIetfIpMIB>
                                </ciscoExperiment>
                            </cisco>
                        </enterprises>
                    </private>
                </internet>
            </dod>
        </org>
    </iso>
</root>
```
