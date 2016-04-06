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

import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmp2xml4j.snmptoolkit.transport.UdpTransportMappingFactory;
import net.percederberg.mibble.MibLoader;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.io.IOException;
import java.util.Map;

/**
 * Created by niau on 3/24/16.
 *
 * @author niau
 * @version $Id: $Id
 */
public class SnmpUdpV2Manager extends SnmpManager {

    protected String snmpCommunity;
    protected UdpAddress udpAddress;



    /**
     * <p>Constructor for SnmpUdpV2Manager.</p>
     *
     * @param loader a {@link net.percederberg.mibble.MibLoader} object.
     * @param ipAddress a {@link java.lang.String} object.
     * @param snmpCommunity a {@link java.lang.String} object.
     * @param retries a int.
     * @param timeout a int.
     * @param maxSizeRequestPDU a int.
     * @param maxRepetitions a int.
     * @param destinationPort a int.
     * @throws java.io.IOException if any.
     */
    public SnmpUdpV2Manager(MibLoader loader, String ipAddress, String snmpCommunity, int retries, int timeout, int maxSizeRequestPDU, int maxRepetitions, int destinationPort) throws IOException {
        super(loader, retries, timeout, maxSizeRequestPDU,maxRepetitions, new UdpTransportMappingFactory(),new UdpAddress("0.0.0.0/0"));
        this.snmpCommunity = snmpCommunity;
        this.udpAddress =  new UdpAddress(ipAddress+"/"+destinationPort);
    }

    /**
     * <p>Constructor for SnmpUdpV2Manager.</p>
     *
     * @param loader a {@link net.percederberg.mibble.MibLoader} object.
     */
    public SnmpUdpV2Manager(MibLoader loader) {
        super(loader,new UdpTransportMappingFactory(),new DefaultMessageDispatcherFactory(),new UdpAddress("0.0.0.0/0"));
    }


    /** {@inheritDoc} */
    @Override
    protected void doInit() {

    }
    /** {@inheritDoc} */
    @Override
    public void doSetParameters(Map<String, String> conParams) {
        int destinationPort = super.convertStringToIntParam("destinationPort",conParams.get("destinationPort"),161);
        this.udpAddress = new UdpAddress(conParams.get("ipAddress")+"/"+destinationPort);
        this.snmpCommunity=conParams.get("snmpCommunity");
    }

    /**
     * <p>getTarget.</p>
     *
     * @return a {@link org.snmp4j.Target} object.
     */
    protected Target getTarget() {

        CommunityTarget target = new CommunityTarget();

        target.setCommunity(new OctetString(snmpCommunity));

        target.setAddress(this.udpAddress);


        target.setRetries(retries);
        target.setMaxSizeRequestPDU(maxSizeRequestPDU);
        target.setTimeout(timeout);

        target.setVersion(SnmpConstants.version2c);

        return target;
    }
    /** {@inheritDoc} */
    @Override
    protected PDU createPDU() {
        return  new PDU();

    }

    /**
     * <p>Getter for the field <code>snmpCommunity</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getSnmpCommunity() {
        return snmpCommunity;
    }

    /**
     * <p>Setter for the field <code>snmpCommunity</code>.</p>
     *
     * @param snmpCommunity a {@link java.lang.String} object.
     */
    public void setSnmpCommunity(String snmpCommunity) {
        this.snmpCommunity = snmpCommunity;
    }

    /**
     * <p>Getter for the field <code>udpAddress</code>.</p>
     *
     * @return a {@link org.snmp4j.smi.UdpAddress} object.
     */
    public UdpAddress getUdpAddress() {
        return udpAddress;
    }

    /**
     * <p>Setter for the field <code>udpAddress</code>.</p>
     *
     * @param udpAddress a {@link org.snmp4j.smi.UdpAddress} object.
     */
    public void setUdpAddress(UdpAddress udpAddress) {
        this.udpAddress = udpAddress;
    }
}
