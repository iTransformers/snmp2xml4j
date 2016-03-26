/*
 * SnmpUdpV2Manager.java
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

package net.itransformers.snmp2xml4j.snmptoolkit;

import net.itransformers.snmp2xml4j.snmptoolkit.transport.UdpTransportMappingFactory;
import net.percederberg.mibble.MibLoader;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.io.IOException;

/**
 * Created by niau on 3/24/16.
 */
public class SnmpUdpV2Manager extends SnmpManager {

    protected String snmpCommunity;
    protected UdpAddress udpAddress;



    public SnmpUdpV2Manager(MibLoader loader, String ipAddress, String snmpCommunity, int retries, int timeout, int maxSizeRequestPDU, int destinationPort) throws IOException {
        super(loader, retries, timeout, maxSizeRequestPDU,destinationPort,new UdpTransportMappingFactory(),new UdpAddress("0.0.0.0/0"));
        this.snmpCommunity = snmpCommunity;
        this.udpAddress =  new UdpAddress(ipAddress+"/"+destinationPort);
    }

    @Override
    protected void doInit() {

    }

    protected Target getTarget() {

        CommunityTarget target = new CommunityTarget();

        target.setCommunity(new OctetString(snmpCommunity));

        target.setAddress(this.udpAddress);


        target.setRetries(retries);

        target.setTimeout(timeout);

        target.setVersion(SnmpConstants.version2c);

        return target;
    }
    @Override
    protected PDU createPDU() {
        return  new PDU();

    }

}
