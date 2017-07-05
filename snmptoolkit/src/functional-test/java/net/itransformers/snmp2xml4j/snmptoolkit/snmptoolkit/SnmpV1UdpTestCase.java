/*
 * SnmpV1UdpTestCase.java
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.snmp4j.PDU;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;

import java.io.IOException;

/**
 * Created by niau on 3/11/16.
 *
 * @author niau
 * @version $Id: $Id
 * @since 1.0
 */
public class SnmpV1UdpTestCase  {
    private static SnmpManager snmpManager = null;


    /**
     * <p>prepareSettings.</p>
     */

    @BeforeClass
    public static void prepareSettings() throws IOException, MibLoaderException {

        snmpManager = new SnmpUdpV1Manager(TestResources.getMibLoaderHolder().getLoader(), "193.19.175.150", "netTransformer-aaaa", 3, 2000, 65535,10, 161);
        snmpManager.init();

    }

    /**
     * <p>snmpGet.</p>
     *
     * @throws IOException if any.
     */
    @Test
    public void snmpGet() throws IOException {

        OID oid = new OID("1.3.6.1.2.1.1.1.0");
        OID oids[] = new OID[]{oid};
        ResponseEvent responseEvent = snmpManager.snmpGet(oids);
        if (responseEvent !=null){
            PDU response = responseEvent.getResponse();

            VariableBinding vb1 = response.get(0);
            Assert.assertEquals(vb1.toValueString(), "SunOS zeus.snmplabs.com 4.1.3_U1 1 sun4m");

        }



    }


    @Test
    public void snmpGetToString() throws IOException {

        String snmpGetValue= snmpManager.snmpGet("1.3.6.1.2.1.1.1.0");

        Assert.assertEquals("SunOS zeus.snmplabs.com 4.1.3_U1 1 sun4m",snmpGetValue);

    }


    @Test
    public void snmpGetNextToString() throws IOException {


        String snmpGetValue= snmpManager.snmpGetNext("1.3.6.1.2.1.1.5");

        Assert.assertEquals("zeus.snmplabs.com",snmpGetValue);

    }

    /**
     * <p>snmpGetNext.</p>
     *
     * @throws IOException if any.
     */
    @Test
    public void snmpGetNext() throws IOException {

        OID oid = new OID("1.3.6.1.2.1.1.1");
        OID oids[] = new OID[]{oid};
        ResponseEvent responseEvent = snmpManager.snmpGetNext(oids);

        PDU response = responseEvent.getResponse();

        VariableBinding vb1 = response.get(0);
        Assert.assertEquals("SunOS zeus.snmplabs.com 4.1.3_U1 1 sun4m",vb1.toValueString());

    }





}
