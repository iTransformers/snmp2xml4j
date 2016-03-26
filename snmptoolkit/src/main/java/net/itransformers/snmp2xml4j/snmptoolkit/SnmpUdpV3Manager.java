/*
 * SnmpUdpV3Manager.java
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
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Target;
import org.snmp4j.UserTarget;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.io.IOException;

/**
 * Created by niau on 3/24/16.
 */
public class SnmpUdpV3Manager extends SnmpManager {


    protected String ver3Username;
    protected String ver3AuthPasscode;
    protected int ver3mode;

    protected String authenticationProtocol;
    protected String privacyProtocol;
    protected String privacyProtocolPassShare;
    protected UdpAddress udpAddress;



    public SnmpUdpV3Manager(MibLoader loader, String ipAddress, int ver3mode, String ver3Username, String ver3AuthPasscode, String authenticationProtocol, String privacyProtocol, String privacyProtocolPassShare,  int retries, int timeout, int maxSizeRequestPDU, int destinationPort) throws IOException {
        super(loader, retries, timeout, maxSizeRequestPDU,destinationPort,new UdpTransportMappingFactory(),new UdpAddress("0.0.0.0/0"));
        this.ver3mode = ver3mode;
        this.ver3Username = ver3Username;
        this.ver3AuthPasscode = ver3AuthPasscode;
        this.authenticationProtocol = authenticationProtocol;
        this.privacyProtocol = privacyProtocol;
        this.privacyProtocolPassShare = privacyProtocolPassShare;
        this.udpAddress =  new UdpAddress(ipAddress+"/"+destinationPort);

    }

    protected Target getTarget() {

        UserTarget target = new UserTarget();

        target.setAddress(udpAddress);

        target.setVersion(SnmpConstants.version3); // SnmpConstants.version3
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setSecurityLevel(ver3mode); // SecurityLevel.AUTH_NOPRIV
        target.setSecurityName(new OctetString(ver3Username));

        return target;

    }

    @Override
    protected PDU createPDU() {
        return  new ScopedPDU();

    }


    @Override
    protected void doInit() {



            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
            OID authenticationProtocolOID = null;

            if ("MD5".equals(this.authenticationProtocol)) {
                authenticationProtocolOID = AuthMD5.ID;
            } else if ("SHA".equals(authenticationProtocol)) {
                authenticationProtocolOID = AuthSHA.ID;
            }
            OID privacyProtocolOID = null;

            if ("DES".equals(privacyProtocol)) {
                privacyProtocolOID = PrivDES.ID;
            } else if ("3DES".equals(privacyProtocol)) {
                privacyProtocolOID = Priv3DES.ID;
            }


            if (ver3mode == SecurityLevel.NOAUTH_NOPRIV) {
                snmp.getUSM().addUser(new OctetString(ver3Username),
                        new UsmUser(new OctetString(ver3Username), null, null, null, null));

            }
            else if (ver3mode == SecurityLevel.AUTH_NOPRIV) {
                snmp.getUSM().addUser(new OctetString(ver3Username),
                        new UsmUser(new OctetString(ver3Username), authenticationProtocolOID, new OctetString(ver3AuthPasscode), null, null));


            } else {

                snmp.getUSM().addUser(new OctetString(ver3Username),
                        new UsmUser(new OctetString(ver3Username), authenticationProtocolOID, new OctetString(ver3AuthPasscode), privacyProtocolOID, new OctetString(privacyProtocolPassShare)));

            }



    }

}
