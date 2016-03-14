/*
 * Getv3TestCase.java
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
import net.itransformers.snmp2xml4j.snmptoolkit.Get;
import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmp2xml4j.snmptoolkit.transport.UdpTransportMappingFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by niau on 3/11/16.
 *
 * @author niau
 * @version $Id: $Id
 * @since 1.0
 */
public class Getv3TestCase {
    private  static  HashMap<String,String> settings =  new HashMap<String, String>();

    /**
     * <p>prepareSettings.</p>
     */
    @BeforeClass
    public static   void prepareSettings(){
        settings.put("ipAddress","195.218.195.228");
        settings.put("securityName"," usr-md5-none");
        settings.put("authPassPhrase","authkey1");
        settings.put("version","3");
        settings.put("retries","3");
        settings.put("timeout","1000");
        settings.put("entineId","0x80004fb805636c6f75644dab22cc");

    }

    /**
     * <p>snmpGet.</p>
     *
     * @throws IOException if any.
     */
    @Test
    public void snmpGet() throws IOException {


        String oid = "1.3.6.1.2.1.1.1.0";


        Get get = new Get(oid,settings.get("ipAddress"),settings.get("entineId"),settings.get("securityName"),settings.get("authPassPhrase"),"MD5",null,null,3,3,1000,new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory() );


        String value = get.getSNMPValue();

        Assert.assertEquals("SunOS zeus.snmplabs.com 4.1.3_U1 1 sun4m",value);
    }

    /**
     * <p>snmpGetNext.</p>
     *
     * @throws IOException if any.
     */
//    @Test
//    public void snmpGetNext() throws IOException {
//
//        ParemetersAssembler paremetersAssembler = new ParemetersAssembler(settings);
//
//        Properties parameters = paremetersAssembler.getProperties();
//
//        SnmpConfigurator snmpConfig = new SnmpConfigurator();
//
//        CommunityTarget t = (CommunityTarget) snmpConfig.getTarget(parameters);
//
//        String oid = "1.3.6.1.2.1.1.1";
//
//        Get get = new Get(oid, t, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
//
//        String value = get.getSNMPGetNextValue();
//        System.out.println(value);
//        Assert.assertEquals(value, "SunOS zeus.snmplabs.com 4.1.3_U1 1 sun4m");
//
//    }
}
