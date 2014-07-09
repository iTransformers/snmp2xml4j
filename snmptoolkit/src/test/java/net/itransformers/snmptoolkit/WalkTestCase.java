package net.itransformers.snmptoolkit;

import net.itransformers.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmptoolkit.transport.UdpTransportMappingFactory;
import net.percederberg.mibble.MibLoaderException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

public class WalkTestCase {


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
}
