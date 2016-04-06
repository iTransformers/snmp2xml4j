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

import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
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
import java.util.Map;

/**
 * Created by niau on 3/24/16.
 *
 * @author niau
 * @version $Id: $Id
 */
public class SnmpUdpV3Manager extends SnmpManager {


    protected String ver3Username;
    protected String ver3AuthPasscode;
    protected int ver3mode;

    protected String authenticationProtocol;
    protected String privacyProtocol;
    protected String privacyProtocolPassShare;
    protected UdpAddress udpAddress;



    /**
     * <p>Constructor for SnmpUdpV3Manager.</p>
     *
     * @param loader a {@link net.percederberg.mibble.MibLoader} object.
     * @param ipAddress a {@link java.lang.String} object.
     * @param ver3mode a int.
     * @param ver3Username a {@link java.lang.String} object.
     * @param ver3AuthPasscode a {@link java.lang.String} object.
     * @param authenticationProtocol a {@link java.lang.String} object.
     * @param privacyProtocol a {@link java.lang.String} object.
     * @param privacyProtocolPassShare a {@link java.lang.String} object.
     * @param privacyProtocolPassShare a {@link java.lang.String} object.
     * @param retries a int.
     * @param timeout a int.
     * @param maxSizeRequestPDU a int.
     * @param maxRepetitions a int.
     * @param destinationPort a int.
     * @throws java.io.IOException if any.
     */
    public SnmpUdpV3Manager(MibLoader loader, String ipAddress, int ver3mode, String ver3Username, String ver3AuthPasscode, String authenticationProtocol, String privacyProtocol, String privacyProtocolPassShare,  int retries, int timeout, int maxSizeRequestPDU, int maxRepetitions, int destinationPort) throws IOException {
        super(loader, retries, timeout, maxSizeRequestPDU,maxRepetitions, new UdpTransportMappingFactory(),new UdpAddress("0.0.0.0/0"));
        this.ver3mode = ver3mode;
        this.ver3Username = ver3Username;
        this.ver3AuthPasscode = ver3AuthPasscode;
        this.authenticationProtocol = authenticationProtocol;
        this.privacyProtocol = privacyProtocol;
        this.privacyProtocolPassShare = privacyProtocolPassShare;
        this.udpAddress =  new UdpAddress(ipAddress+"/"+destinationPort);

    }
    /**
     * <p>Constructor for SnmpUdpV3Manager.</p>
     *
     * @param loader a {@link net.percederberg.mibble.MibLoader} object.
     */
    public SnmpUdpV3Manager(MibLoader loader) {
        super(loader, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory(), new UdpAddress("0.0.0.0/0"));
    }
    /** {@inheritDoc} */
    @Override
    public void doSetParameters(Map<String, String> conParams) {
        int destinationPort = super.convertStringToIntParam("destinationPort",conParams.get("destinationPort"),161);
        this.udpAddress = new UdpAddress(conParams.get("ipAddress")+"/"+destinationPort);
        this.ver3Username = conParams.get("ver3Username");

        this.ver3AuthPasscode = conParams.get("ver3AuthPasscode");
        this.authenticationProtocol = conParams.get("authenticationProtocol");
        this.privacyProtocol = conParams.get("privacyProtocol");
        this.privacyProtocolPassShare = conParams.get("privacyProtocolPassShare");
        setVer3Mode(conParams.get("ver3mode"));
    }



    private void setVer3Mode(String ver3ModeString){

        if ("AUTH_PRIV".equals(ver3ModeString)){
            ver3mode=SecurityLevel.AUTH_PRIV;

        }   else if ("AUTH_NOPRIV".equalsIgnoreCase(ver3ModeString)){
            ver3mode=SecurityLevel.AUTH_NOPRIV;


        }
        else {
            ver3mode=SecurityLevel.NOAUTH_NOPRIV;

        }

    }

    /**
     * <p>getTarget.</p>
     *
     * @return a {@link org.snmp4j.Target} object.
     */
    protected Target getTarget() {

        UserTarget target = new UserTarget();

        target.setAddress(udpAddress);

        target.setVersion(SnmpConstants.version3);
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setSecurityLevel(ver3mode);
        target.setSecurityName(new OctetString(ver3Username));

        return target;

    }

    /** {@inheritDoc} */
    @Override
    protected PDU createPDU() {
        return  new ScopedPDU();

    }




    /** {@inheritDoc} */
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
            OID privacyProtocolOID;

            if ("DES".equals(privacyProtocol)) {
                privacyProtocolOID = PrivDES.ID;
            } else if ("3DES".equals(privacyProtocol)) {
                privacyProtocolOID = Priv3DES.ID;
            }else {
                privacyProtocolOID = PrivAES128.ID;

            }


            if (ver3mode == SecurityLevel.NOAUTH_NOPRIV) {
                snmp.getUSM().addUser(new OctetString(ver3Username),
                        new UsmUser(new OctetString(ver3Username), null, null, null, null));

            }
            else if (ver3mode == SecurityLevel.AUTH_NOPRIV) {
                snmp.getUSM().addUser(new OctetString(ver3Username),
                        new UsmUser(new OctetString(ver3Username), authenticationProtocolOID, new OctetString(ver3AuthPasscode), null, null));


            } else if (ver3mode == SecurityLevel.AUTH_PRIV) {

                snmp.getUSM().addUser(new OctetString(ver3Username),
                        new UsmUser(new OctetString(ver3Username), authenticationProtocolOID, new OctetString(ver3AuthPasscode), privacyProtocolOID, new OctetString(privacyProtocolPassShare)));

            }



    }

    /**
     * <p>Getter for the field <code>ver3Username</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getVer3Username() {
        return ver3Username;
    }

    /**
     * <p>Setter for the field <code>ver3Username</code>.</p>
     *
     * @param ver3Username a {@link java.lang.String} object.
     */
    public void setVer3Username(String ver3Username) {
        this.ver3Username = ver3Username;
    }

    /**
     * <p>Getter for the field <code>ver3AuthPasscode</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getVer3AuthPasscode() {
        return ver3AuthPasscode;
    }

    /**
     * <p>Setter for the field <code>ver3AuthPasscode</code>.</p>
     *
     * @param ver3AuthPasscode a {@link java.lang.String} object.
     */
    public void setVer3AuthPasscode(String ver3AuthPasscode) {
        this.ver3AuthPasscode = ver3AuthPasscode;
    }

    /**
     * <p>Getter for the field <code>ver3mode</code>.</p>
     *
     * @return a int.
     */
    public int getVer3mode() {
        return ver3mode;
    }

    /**
     * <p>Setter for the field <code>ver3mode</code>.</p>
     *
     * @param ver3mode a int.
     */
    public void setVer3mode(int ver3mode) {
        this.ver3mode = ver3mode;
    }

    /**
     * <p>Getter for the field <code>authenticationProtocol</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAuthenticationProtocol() {
        return authenticationProtocol;
    }

    /**
     * <p>Setter for the field <code>authenticationProtocol</code>.</p>
     *
     * @param authenticationProtocol a {@link java.lang.String} object.
     */
    public void setAuthenticationProtocol(String authenticationProtocol) {
        this.authenticationProtocol = authenticationProtocol;
    }

    /**
     * <p>Getter for the field <code>privacyProtocol</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPrivacyProtocol() {
        return privacyProtocol;
    }

    /**
     * <p>Setter for the field <code>privacyProtocol</code>.</p>
     *
     * @param privacyProtocol a {@link java.lang.String} object.
     */
    public void setPrivacyProtocol(String privacyProtocol) {
        this.privacyProtocol = privacyProtocol;
    }

    /**
     * <p>Getter for the field <code>privacyProtocolPassShare</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPrivacyProtocolPassShare() {
        return privacyProtocolPassShare;
    }

    /**
     * <p>Setter for the field <code>privacyProtocolPassShare</code>.</p>
     *
     * @param privacyProtocolPassShare a {@link java.lang.String} object.
     */
    public void setPrivacyProtocolPassShare(String privacyProtocolPassShare) {
        this.privacyProtocolPassShare = privacyProtocolPassShare;
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
