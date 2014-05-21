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
import java.util.Map;
import java.util.Properties;

public class WalkTestCase {
    @Test
    public void aptiloTestWalk() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String oids = "radiusAuthServTotalAccessRequests,radiusAuthServTotalInvalidRequests,radiusAuthServTotalDupAccessRequests,radiusAuthServTotalAccessAccepts,radiusAuthServTotalAccessRejects,radiusAuthServTotalAccessChallenges,radiusAuthServTotalMalformedAccessRequests,radiusAuthServTotalBadAuthenticators,radiusAuthServTotalPacketsDropped,radiusAuthServTotalUnknownTypes,radiusAuthClientAddress,radiusAuthClientID,radiusAuthServAccessRequests,radiusAuthServDupAccessRequests," +
                "radiusAuthServAccessAccepts,radiusAuthServAccessRejects," +
                "radiusAuthServAccessChallenges,radiusAuthServMalformedAccessRequests," +
                "radiusAuthServBadAuthenticators,radiusAuthServPacketsDropped," +
                "radiusAuthServUnknownTypes,radiusAccServIdent,radiusAccServUpTime," +
                "radiusAccServResetTime,radiusAccServTotalRequests," +
                "radiusAccServTotalInvalidRequests,radiusAccServTotalDupRequests," +
                "radiusAccServTotalResponses,radiusAccServTotalMalformedRequests," +
                "radiusAccServTotalBadAuthenticators,radiusAccServTotalPacketsDropped," +
                "radiusAccServTotalNoRecords,radiusAccServTotalUnknownTypes," +
                "radiusAccClientAddress,radiusAccClientID,radiusAccServPacketsDropped," +
                "radiusAccServRequests,radiusAccServDupRequests,radiusAccServResponses," +
                "radiusAccServBadAuthenticators,radiusAccServMalformedRequests," +
                "radiusAccServNoRecords,radiusAccServUnknownTypes,authConfigurationState," +
                "authRedundancyState,authNumberOfAllocatedSessions,authNumberOfStartedSessions," +
                "authNumberOfActiveSessions,dbName,dbType,dbUptime,dbDiskSize," +
                "dbPeersConfigured,dbPeersConnected,dbDiskSizeThreshold,dbRequestName," +
                "dbRequestGetSuccess,dbRequestPutSuccess,dbRequestModifySuccess," +
                "dbRequestDeleteSuccess,dbRequestPostSuccess,dbRequestListSuccess," +
                "dbRequestGetNotFound,dbRequestGetFailed,dbRequestPutFailed," +
                "dbRequestModifyFailed,dbRequestDeleteFailed,dbRequestPostFailed," +
                "dbRequestListFailed,dbRequestGetTimeout,dbRequestPutTimeout,dbRequestModifyTimeout," +
                "dbRequestDeleteTimeout,dbRequestPostTimeout,dbRequestListTimeout," +
                "dbRequestGetMinRspTime,dbRequestPutMinRspTime,dbRequestModifyMinRspTime," +
                "dbRequestDeleteMinRspTime,dbRequestPostMinRspTime,dbRequestListMinRspTime," +
                "dbRequestGetMaxRspTime,dbRequestPutMaxRspTime,dbRequestModifyMaxRspTime," +
                "dbRequestDeleteMaxRspTime,dbRequestPostMaxRspTime,dbRequestListMaxRspTime," +
                "dbRequestGetAvgRspTime,dbRequestPutAvgRspTime,dbRequestModifyAvgRspTime," +
                "dbRequestDeleteAvgRspTime,dbRequestPostAvgRspTime,dbRequestListAvgRspTime," +
                "hwFanName,hwFanRpm,hwFanStatus,hwFanRedundancyState,hwPSUName,hwPSUStatus," +
                "hwPSURedundancyState,hwRAIDName,hwRAIDStatus,hwRAIDSize,hwRAIDUsage," +
                "hwRAIDRedundancyState,coreServiceName,coreServiceState,coreServiceUpTime," +
                "coreServiceFailCount,coreServiceStartCount,statRadiusAccountingReqRate," +
                "statRadiusAccountingRspRate,statRadiusAuthReqRate,statRadiusAuthAcceptRate," +
                "statNumberOfAllocatedSessions,statNumberOfStartedSessions," +
                "statNumberOfActiveSessions";
        String mibDir = "snmptoolkit/mibs";

        HashMap<CmdOptions, String> cmdOptions = new HashMap<CmdOptions, String>();
        cmdOptions.put(CmdOptions.MIBS_DIR,mibDir);
        cmdOptions.put(CmdOptions.ADDRESS,"localhost/11161");
        cmdOptions.put(CmdOptions.COMMUNITY,"aptiloAll");
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
        String expectedXML = FileUtils.readFileToString(new File("snmptoolkit/src/test/java/resources/aptilo.xml"));
        Assert.assertEquals(expectedXML,xml);
    }
    @Test
    public void ciscoTestWalk() throws MibLoaderException, ParserConfigurationException, SAXException, XPathExpressionException, IOException {
        String oids = "ipv6Forwarding, ipv6IfIndex,ipv6AddrEntry,ipv6NetToMediaEntry,ipv6RouteEntry,cIpAddressEntry";
        String mibDir = "snmptoolkit/mibs";

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
}
