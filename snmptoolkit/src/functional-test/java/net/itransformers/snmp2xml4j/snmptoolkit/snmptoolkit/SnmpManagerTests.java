/*
 * SnmpManagerTests.java
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

import net.itransformers.snmp2xml4j.snmptoolkit.SnmpManager;
import net.itransformers.snmp2xml4j.snmptoolkit.SnmpUdpV1Manager;
import net.percederberg.mibble.MibLoaderException;
import org.junit.Assert;
import org.junit.Test;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by niau on 4/2/16.
 */


public class SnmpManagerTests {



    @Test(expected=NullPointerException.class)
    public void getSymbolFromMibByOid() throws IOException, MibLoaderException {

        SnmpManager snmpManager = new SnmpUdpV1Manager(null, "193.19.175.150", "netTransformer-aaa", 1, 1000, 65535,10, 161);
        snmpManager.init();
        snmpManager.getSymbolFromMibByOid("system","sysName");

    }

    @Test
    public void AssertSymbolFromMibByOid() throws IOException, MibLoaderException {

        SnmpManager snmpManager = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader(), "193.19.175.150", "netTransformer-aaa", 1, 1000, 65535,10, 161);
        snmpManager.init();
        String symbol = snmpManager.getSymbolFromMibByOid("SNMPv2-MIB","1.3.6.1.2.1.1.5");

        Assert.assertEquals(symbol, "sysName");


    }




    @Test
    public void AssertSymbolFromMibByOidOnUnknownOid() throws IOException, MibLoaderException {

        SnmpManager snmpManager = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader(), "193.19.175.150", "netTransformer-r", 1, 1000, 65535,10, 161);
        snmpManager.init();
        String oidName = snmpManager.getSymbolFromMibByOid("SNMPv2-MIB","1.3.6.1.2.1.1.5.11");

        Assert.assertEquals(oidName, "sysName");


    }

    @Test
    public void     AssertNullOnSymbolFromMibByOidFromUnknownMib() throws IOException, MibLoaderException {

        SnmpManager snmpManager = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader(), "193.19.175.150", "netTransformer-r", 1, 1000, 65535,10, 161);
        snmpManager.init();
        String oid = snmpManager.getSymbolFromMibByOid("system","sysName");

        Assert.assertEquals(oid,null);


    }


    @Test(expected=IllegalArgumentException.class)
    public void IllegalArgumentExceptionOnIpAddressMissing() throws IOException {

        Map<String,String> conParams = new HashMap<String,String>();

        SnmpManager snmpManager1 = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader());
        snmpManager1.init();
        snmpManager1.setParameters(conParams);


    }

    @Test(expected=NullPointerException.class)
    public void NullPointerExceptionOnGetDueToMissingCommunityString() throws IOException {

        Map<String,String> conParams = new HashMap<String,String>();
        conParams.put("ipAddress","192.168.1.1");
        OID oid = new OID("1.3.6.1.2.1.1.1");
        OID oids[] = new OID[]{oid};

        SnmpManager snmpManager1 = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader());
        snmpManager1.init();
        snmpManager1.setParameters(conParams);
        snmpManager1.snmpGet(oids);
    }



    @Test
    public void IllegalTimeoutSetToDefault() throws IOException, MibLoaderException {

        Map<String,String> conParams = new HashMap<String,String>();
        conParams.put("ipAddress","192.168.1.1");
        conParams.put("snmpCommunity","netTransformer-r");
        conParams.put("timeout","illegalTimeout");
        OID oid = new OID("1.3.6.1.2.1.1.1");

        SnmpManager snmpManager1 = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader());
        snmpManager1.init();
        snmpManager1.setParameters(conParams);
        Assert.assertEquals(1000, snmpManager1.getTimeout());

    }

    @Test
    public void IllegalRetriestSetToDefault() throws IOException, MibLoaderException {

        Map<String,String> conParams = new HashMap<String,String>();
        conParams.put("ipAddress","192.168.1.1");
        conParams.put("snmpCommunity","netTransformer-r");
        conParams.put("retries","illegalTimeout");
        OID oid = new OID("1.3.6.1.2.1.1.1");

        SnmpManager snmpManager1 = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader());
        snmpManager1.init();
        snmpManager1.setParameters(conParams);
        Assert.assertEquals(3, snmpManager1.getRetries());

    }



    @Test()
    public void AssertNullResponseOnSnmpGet() throws IOException {

        Map<String,String> conParams = new HashMap<String,String>();
        conParams.put("ipAddress","192.168.1.1");
        conParams.put("snmpCommunity","netTransformer-r");

        OID oid = new OID("1.3.6.1.2.1.1.1");
        OID oids[] = new OID[]{oid};

        SnmpManager snmpManager1 = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader());
        snmpManager1.init();
        snmpManager1.setParameters(conParams);

        ResponseEvent responseEvent =  snmpManager1.snmpGet(oids);

        org.junit.Assert.assertEquals(responseEvent, null);
    }

    @Test()
    public void AssertNotNullResponseOnSnmpGet() throws IOException {

        Map<String,String> conParams = new HashMap<String,String>();
        conParams.put("ipAddress","192.168.1.1");
        conParams.put("snmpCommunity","netTransformer-r");

        OID oid = new OID("1.3.6.1.2.1.1.1");
        OID oids[] = new OID[]{oid};

        SnmpManager snmpManager1 = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader());
        snmpManager1.init();
        snmpManager1.setParameters(conParams);

        ResponseEvent responseEvent =  snmpManager1.snmpGet(oids);

        org.junit.Assert.assertEquals(responseEvent, null);
    }

}
