/*
 * iTransformer is an open source tool able to discover IP networks
 * and to perform dynamic data data population into a xml based inventory system.
 * Copyright (C) 2010  http://itransformers.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.itransformers.snmptoolkit; import net.itransformers.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmptoolkit.transport.UdpTransportMappingFactory;
import net.percederberg.mibble.MibLoaderException;
import org.snmp4j.util.SnmpConfigurator;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class MainClass {

    public static void XML_parser(String input_file) throws Exception {
        XPath xpath = XPathFactory.newInstance().newXPath();
        String discoveredNeighbors = "//DiscoveredDevice/object/object[objectType = 'Discovered Neighbor']/name";

       //String discoveredMacs = "//DiscoveredDevice/object/object[objectType = 'Discovered Neighbor']/name";

        InputSource inputSource = new InputSource(input_file);

        NodeList nodes = (NodeList) xpath
                .evaluate(discoveredNeighbors, inputSource, XPathConstants.NODESET);

        int j = nodes.getLength();

        for (int i = 0; i < j; i++) {
            System.out.println(nodes.item(i).getTextContent());
        }

    }
    public static void main(String[] args) throws TransformerException,
            TransformerConfigurationException, FileNotFoundException, IOException, MibLoaderException {
//        if (args.length != 3) {
//            System.out.println("Usage: java jaxpTransform inXML inXSLT outFile");
//            System.exit(0);
//        }
        FileOutputStream outstream = new FileOutputStream("device.xml");
          //java -classpath "output\snmp-utils.jar;lib\mibble-parser-2.9.2.jar;lib\snmp4j-1.11.2.jar" Walk -md mibs -a 10.129.3.1/161 -c public -v 2c -t 1000 -r 1 -m 100 -o "ifIndex ifDescr ifOperStatus ifAdminStatus ifNumber ifAlias ifPhysAddress ifType dot1dTpFdb dot1dTpFdbAddress dot1dTpFdbStatus dot1dTpFdbPort dot1dBasePort dot1dBasePortIfIndex system dot1dBaseBridgeAddress dot1dStpPort ipNetToMediaTable ipAddrTable lldpRemoteSystemsData cdpCacheDevicePort cdpCacheDevicePlatform cdpCacheDeviceId cdpCacheIfIndex"
//        Walk.main(new String[]{"-md","mibs","-a","10.129.3.1/161","-c","public","-v","2c -t 1000 -r 1 -m 100 -o "ifIndex ifDescr ifOperStatus ifAdminStatus ifNumber ifAlias ifPhysAddress ifType dot1dTpFdb dot1dTpFdbAddress dot1dTpFdbStatus dot1dTpFdbPort dot1dBasePort dot1dBasePortIfIndex system dot1dBaseBridgeAddress dot1dStpPort ipNetToMediaTable ipAddrTable lldpRemoteSystemsData cdpCacheDevicePort cdpCacheDevicePlatform cdpCacheDeviceId cdpCacheIfIndex"});
        Walk walker = new Walk(new File("snmptoolkit/mibs"), false, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        Properties parameters = new Properties();
            String[] includes = new String[]{
                    "bgpPeerTable"
            };


//        parameters.put(SnmpConfigurator.O_ADDRESS, Arrays.asList("c82.16.36.1/161"));
        parameters.put(SnmpConfigurator.O_ADDRESS, Arrays.asList("88.203.203.225/161"));
        parameters.put(SnmpConfigurator.O_COMMUNITY, Arrays.asList("test-r"));
        parameters.put(SnmpConfigurator.O_VERSION, Arrays.asList("2c"));

        parameters.put(SnmpConfigurator.O_TIMEOUT, Arrays.asList(1000));
        parameters.put(SnmpConfigurator.O_RETRIES, Arrays.asList(1));
        parameters.put(SnmpConfigurator.O_MAX_REPETITIONS, Arrays.asList(65535));

        Node root = walker.walk(includes, parameters);
        String xml = Walk.printTreeAsXML(root);
        System.out.println(xml);
//        PrintWriter writer = new PrintWriter("out.xml");
//        writer.print(xml);
//        writer.flush();
//        writer.close();
//
//        TransformerFactory tfactory = TransformerFactory.newInstance();
//        StreamSource insource = new StreamSource("out.xml");
//        StreamSource inxsl = new StreamSource("transformator.xslt");
//        StreamResult sresult = new StreamResult(outstream);
//        Transformer transformer = tfactory.newTransformer(inxsl);
//        transformer.transform(insource, sresult);
//        try {
//            XML_parser("device.xml");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}

