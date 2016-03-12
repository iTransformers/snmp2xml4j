/*
 * WalkTestCase.java
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

import junit.framework.Assert;
import net.itransformers.snmp2xml4j.snmptoolkit.MibLoaderHolder;
import net.itransformers.snmp2xml4j.snmptoolkit.Node;
import net.itransformers.snmp2xml4j.snmptoolkit.ParemetersAssembler;
import net.itransformers.snmp2xml4j.snmptoolkit.Walk;
import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmp2xml4j.snmptoolkit.transport.UdpTransportMappingFactory;
import net.percederberg.mibble.MibLoaderException;
import org.custommonkey.xmlunit.XMLUnit;
import org.custommonkey.xmlunit.XpathEngine;
import org.custommonkey.xmlunit.exceptions.XpathException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * <p>WalkTestCase class.</p>
 *
 * @author niau
 * @version $Id: $Id
 * @since 1.0
 */
public class WalkTestCase {


    private  static  HashMap<String,String> settings =  new HashMap<String, String>();

    /**
     * <p>prepareSettings.</p>
     */
    @BeforeClass
    public static   void prepareSettings(){
        settings.put("ipAddress","195.218.195.228");
        settings.put("community-ro","public");
        settings.put("version","2c");
        settings.put("retries", "3");
        settings.put("timeout", "1000");
    }



    /**
     * <p>openWrtTestWalk.</p>
     *
     * @throws net.percederberg.mibble.MibLoaderException if any.
     * @throws javax.xml.parsers.ParserConfigurationException if any.
     * @throws org.xml.sax.SAXException if any.
     * @throws javax.xml.xpath.XPathExpressionException if any.
     * @throws java.io.IOException if any.
     * @throws org.custommonkey.xmlunit.exceptions.XpathException if any.
     */
    @Test
       public void openWrtTestWalk() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException, XpathException {
        String oids = "system,host,ifEntry";
        String mibDir = "mibs";

        ParemetersAssembler paremetersAssembler = new ParemetersAssembler(settings);

        MibLoaderHolder holder = new MibLoaderHolder(new File(System.getProperty("base.dir"), mibDir), false);
        Walk walker = new Walk(holder, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        Node root = walker.walk(oids.split(","), paremetersAssembler.getProperties());
        String xml = Walk.printTreeAsXML(root, true);

        String xpath = "/root/iso/org/dod/internet/mgmt/mib-2/system/sysName/value";
        Document doc = XMLUnit.buildControlDocument(xml);
        XpathEngine engine = XMLUnit.newXpathEngine();
        String value = engine.evaluate(xpath, doc);
        Assert.assertEquals(value, "zeus.snmplabs.com");
    }


}
