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

package net.itransformers.snmptoolkit;

public class WalkTestCase {


//        @Test
//       public void openWrtTestWalk() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
//        String oids = "ifEntry,host";
//        String mibDir = "mibs";
//
//        HashMap<CmdOptions, String> cmdOptions = new HashMap<CmdOptions, String>();
//        cmdOptions.put(CmdOptions.MIBS_DIR,mibDir);
//        cmdOptions.put(CmdOptions.ADDRESS,"193.19.172.133/161");
//        cmdOptions.put(CmdOptions.COMMUNITY,"netTransformer-r");
//        cmdOptions.put(CmdOptions.VERSION,"2c");
//        cmdOptions.put(CmdOptions.TIMEOUT,"1000");
//        cmdOptions.put(CmdOptions.RETRIES,"100");
//        cmdOptions.put(CmdOptions.MAX_REPETITIONS,"100");
//        cmdOptions.put(CmdOptions.OIDS,oids);
//        cmdOptions.put(CmdOptions.OUTPUT_FILE,"./snmptoolkit/src/test/java/resources/openwrt.xml");
//
//        Properties parameters = new Properties();
//        Walk.fillParams(cmdOptions, parameters);
//        Walk walker = new Walk(new File(mibDir), false, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
//        Node root = walker.walk(oids.split(","), parameters);
//        String xml = Walk.printTreeAsXML(root, true);
//        Walk.outputXml(cmdOptions,xml);
//        String expectedXML = FileUtils.readFileToString(new File("./snmptoolkit/src/test/java/resources/openwrt.xml"));
//        Assert.assertEquals(expectedXML, xml);
//    }
}
