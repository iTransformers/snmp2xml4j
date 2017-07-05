package net.itransformers.snmp2xml4j.snmptoolkit;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by cpt2nmi on 15.6.2017 г..
 */
public class SnmpManagerTest {

    SnmpManager snmpManager;

    @Before
    public void setUp() throws Exception {

         snmpManager = new SnmpUdpV1Manager(null, "193.19.175.150", "netTransformer-aa", 1, 1000, 65535,10, 161);
        snmpManager.init();


    }

    @Test
    public void closeSnmp1() throws Exception {


        snmpManager.closeSnmp();

    }




    @Test
    public void doSetParameters1() throws NullPointerException {

        snmpManager.setParameters(null);


    }

    @Test
    public void setParameters1() throws Exception {

    }

    @Test
    public void convertStringToIntParam1() throws Exception {

    }

    @Test
    public void set1() throws Exception {

    }

    @Test
    public void snmpGet3() throws Exception {

    }

    @Test
    public void snmpGet4() throws Exception {

    }

    @Test
    public void snmpGet5() throws Exception {

    }

    @Test
    public void snmpGetNext2() throws Exception {

    }

    @Test
    public void snmpGetNext3() throws Exception {

    }

    @Test
    public void getTarget1() throws Exception {

    }

    @Test
    public void createPDU1() throws Exception {

    }

    @Test
    public void snmpWalk1() throws Exception {

    }

    @Test
    public void snmpWalkToString() throws Exception {

    }

    @Test
    public void fillTreeFromSNMP() throws Exception {

    }

    @Test
    public void main() throws Exception {

    }

    @Test
    public void getSymbolFromMibByOid() throws Exception {

    }

    @Test
    public void getTimeout() throws Exception {

    }

    @Test
    public void setTimeout() throws Exception {

    }

    @Test
    public void getSnmp() throws Exception {

    }

    @Test
    public void setSnmp() throws Exception {

    }

    @Test
    public void getRetries() throws Exception {

    }

    @Test
    public void setRetries() throws Exception {

    }

    @Test
    public void getMaxSizeRequestPDU() throws Exception {

    }

    @Test
    public void setMaxSizeRequestPDU() throws Exception {

    }

    @Test
    public void getMaxRepetitions() throws Exception {

    }

    @Test
    public void setMaxRepetitions() throws Exception {

    }

    @Test
    public void getLoader() throws Exception {

    }

    @Test
    public void setLoader() throws Exception {

    }

    @Test
    public void closeSnmp() throws Exception {

    }

    @Test
    public void doInit() throws Exception {

    }

    @Test
    public void init() throws Exception {

    }

    @Test
    public void doSetParameters() throws Exception {

    }

    @Test
    public void setParameters() throws Exception {

    }

    @Test
    public void convertStringToIntParam() throws Exception {

    }

    @Test
    public void set() throws Exception {

    }

    @Test
    public void snmpGet() throws Exception {

    }

    @Test
    public void snmpGet1() throws Exception {

    }

    @Test
    public void snmpGet2() throws Exception {

    }

    @Test
    public void snmpGetNext() throws Exception {

    }

    @Test
    public void snmpGetNext1() throws Exception {

    }

    @Test
    public void getTarget() throws Exception {

    }

    @Test
    public void createPDU() throws Exception {

    }

    @Test
    public void snmpWalk() throws Exception {

    }

}