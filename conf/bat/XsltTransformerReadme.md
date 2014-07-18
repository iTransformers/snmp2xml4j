#Transforming SNMPwalk xml to something else

###Generic usage

```xsltTransformer.sh /home/test/test.xslt /usr/data/Input.xml /usr/data/Output.xml```


###Usage for the purposes of SevOne device Certification.


####Stage 1 transformation snmp2xml4j snmpwalk to an intermediate xml file

```xsltTransformer.sh ../conf/xslt/devCert1.xslt /usr/data/snmpwalk.xml  intermediate.xml```

####Stage 2 transformation intermediate.xml to SevOne s1o file

```xsltTransformer.sh ../conf/xslt/devCert2.xslt intermediate.xml final.s1o OpenWrt```

The last parameter is Device Operating system name in this case OpenWrt.

Note that you still have to edit the s1o file and to use your own object and description naming!
