/*
 * SnmpV3UdpAuthPrivDESTestCase.java
 *
 * This work is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation; either version 2 of the License,
 * or (at your option) any later version.
 *
 * This work is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 * USA
 *
 * Copyright (c) 2010-2016 iTransformers Labs. All rights reserved.
 */

package net.itransformers.snmp2xml4j.snmptoolkit.snmptoolkit;

import net.itransformers.snmp2xml4j.snmptoolkit.Node;
import net.itransformers.snmp2xml4j.snmptoolkit.SnmpManager;
import net.itransformers.snmp2xml4j.snmptoolkit.SnmpUdpV3Manager;
import net.itransformers.snmp2xml4j.snmptoolkit.SnmpXmlPrinter;
import net.percederberg.mibble.MibLoaderException;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by niau on 4/4/16.
 */
public class SnmpV3UdpAuthPrivDESTestCase {
    @Test
    public void snmpWalkChangeSettings() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException, XpathException {
        String oids = "system,host,ifEntry";
        Map<String,String> conParams = new HashMap<String,String>();
        conParams.put("ipAddress", "193.19.175.129");
        conParams.put("ver3Username", "nbu");
        conParams.put("ver3mode","AUTH_PRIV");
        conParams.put("authenticationProtocol","MD5");
        conParams.put("ver3AuthPasscode","authkey123");
        conParams.put("privacyProtocol","DES");
        conParams.put("privacyProtocolPassShare", "privkey123");
        SnmpManager snmpManager = new SnmpUdpV3Manager(TestResources.getMibLoaderHolder().getLoader());

        snmpManager.init();
        snmpManager.setParameters(conParams);

        Node rootNode = snmpManager.snmpWalk(oids.split(","));
        SnmpXmlPrinter xmlPrinter = new SnmpXmlPrinter(TestResources.getMibLoaderHolder().getLoader(), rootNode);


        String xml = xmlPrinter.printTreeAsXML(true);

        String xpath = "/root/iso/org/dod/internet/mgmt/mib-2/system/sysName/value";
        Document doc = XMLUnit.buildControlDocument(xml);
        XpathEngine engine = XMLUnit.newXpathEngine();
        String value = engine.evaluate(xpath, doc);
        Assert.assertEquals(value, "vGW1");

    }

}
