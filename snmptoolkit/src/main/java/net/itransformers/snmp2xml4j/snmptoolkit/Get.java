/*
 * Get.java
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

import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory;
import net.itransformers.snmp2xml4j.snmptoolkit.transport.TransportMappingAbstractFactory;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.CounterSupport;
import org.snmp4j.mp.DefaultCounterListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Vector;

/**
 * <p>Get class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
public class Get {
    static Logger logger = Logger.getLogger(Walk.class);
    private  UsmUser user = null;

    String oid;
    USM usm;
    private TransportIpAddress localAddress;
    private TransportMappingAbstractFactory transportFactory;
    private MessageDispatcherAbstractFactory messageDispatcherFactory;
    TransportMapping transport;
    String securityName;
    String engineId;

    Target target;

    /**
     * <p>Constructor for v1 and v2 SNMP Get.</p>
     *
     * @param oid a {@link java.lang.String} object.
     * @param target a {@link org.snmp4j.CommunityTarget} object.
     * @param transportFactory a {@link net.itransformers.snmp2xml4j.snmptoolkit.transport.TransportMappingAbstractFactory} object.
     * @param messageDispatcherAbstractFactory a {@link net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory} object.
     */
    public Get(String oid, CommunityTarget target,TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherAbstractFactory)  {
        this.oid = oid;
        this.target = target;

        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
        this.transportFactory = transportFactory;
        if (target.getAddress() instanceof UdpAddress) {
            localAddress = new UdpAddress("0.0.0.0/0");
        }else {
            localAddress = new TcpAddress("0.0.0.0/0");
        }
        try {
            transport = transportFactory.createTransportMapping(localAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public Get(String oid, UserTarget target,TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherAbstractFactory)  {
        this.oid = oid;
        this.target = target;

        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
        this.transportFactory = transportFactory;
        if (target.getAddress() instanceof UdpAddress) {
            localAddress = new UdpAddress("0.0.0.0/0");
        }else {
            localAddress = new TcpAddress("0.0.0.0/0");
        }
        try {
            transport = transportFactory.createTransportMapping(localAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    /**
     * <p>Constructor for v1 and v2 SNMP Get</p>
     *
     * @param oid a {@link java.lang.String} object.
     * @param address a {@link java.lang.String} object.
     * @param version a int.
     * @param retries a int.
     * @param timeout a long.
     * @param community a {@link java.lang.String} object.
     * @param transportFactory a {@link net.itransformers.snmp2xml4j.snmptoolkit.transport.TransportMappingAbstractFactory} object.
     * @param messageDispatcherAbstractFactory a {@link net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory} object.
     * @throws java.io.IOException if any.
     */
    public Get(String oid,
               String address,
               int version,
               int retries,
               long timeout,
               String community,
               TransportMappingAbstractFactory transportFactory,
               MessageDispatcherAbstractFactory messageDispatcherAbstractFactory
    ) throws IOException {

        this.oid = oid;
        CommunityTarget targett = new CommunityTarget();
        targett.setAddress(new UdpAddress(address));
        targett.setRetries(retries);
        targett.setTimeout(timeout);
        targett.setVersion(version);
        targett.setMaxSizeRequestPDU(65535);
        targett.setCommunity(new OctetString(community));
        this.target = targett;

        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
        if (this.target.getAddress() instanceof UdpAddress) {
            this.localAddress = new UdpAddress("0.0.0.0/0");
        }else {
            this.localAddress = new TcpAddress("0.0.0.0/0");
        }
        try {

        this.transport = transportFactory.createTransportMapping(localAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Get(String oid, TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherAbstractFactory){
        this.oid = oid;
        this.target = new UserTarget();
        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
        this.transportFactory = transportFactory;
        if (target.getAddress() instanceof UdpAddress) {
            localAddress = new UdpAddress("0.0.0.0/0");
        }else {
            localAddress = new TcpAddress("0.0.0.0/0");
        }
        try {

            this.transport = transportFactory.createTransportMapping(localAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public Get(String oid,
               String address,
               String securityEngineId,
               String securityName,
               String authenticationPassShare,
               String authenticationProtocol,
               String privacyProtocol,
               String privacyProtocolPassShare,
               int version,
               int retries,
               long timeout,
               TransportMappingAbstractFactory transportFactory,
               MessageDispatcherAbstractFactory messageDispatcherAbstractFactory
    ) throws IOException {

        this.oid = oid;
        this.securityName = securityName;
        UserTarget targett = new UserTarget();
        UdpAddress udpAddress = new UdpAddress(address + "/161");
        if (securityEngineId != null) {
            targett.setAuthoritativeEngineID(hexStringToByteArray(securityEngineId));
        }
        targett.setAddress(udpAddress);
        targett.setRetries(retries);
        targett.setTimeout(timeout);
        targett.setVersion(version);
        targett.setMaxSizeRequestPDU(65535);


        OID authenticationProtocolOID = null;

        if ("MD5".equals(authenticationProtocol)){
            authenticationProtocolOID=AuthMD5.ID;
        }else if ("SHA".equals(authenticationProtocol)){
            authenticationProtocolOID= AuthSHA.ID;
        }
        OID privacyProtocolOID = null;

        if ("DES".equals(privacyProtocol)){
            privacyProtocolOID= PrivDES.ID;
        }else if ("3DES".equals(privacyProtocol)) {
            privacyProtocolOID= Priv3DES.ID;
        }

        if (authenticationProtocol ==null &&  privacyProtocol==null){
            targett.setSecurityLevel(SecurityLevel.NOAUTH_NOPRIV);
            user =  new UsmUser(null, null, null, null, null);

        }else if (authenticationProtocol != null && privacyProtocol == null){
            targett.setSecurityLevel(SecurityLevel.AUTH_NOPRIV);
            user =  new UsmUser(new OctetString(securityName), authenticationProtocolOID, new OctetString(authenticationPassShare), null, null);


        } else {
            targett.setSecurityLevel(SecurityLevel.AUTH_PRIV);
            user =  new UsmUser(new OctetString(securityName), authenticationProtocolOID, new OctetString(authenticationPassShare), privacyProtocolOID, new OctetString(privacyProtocolPassShare));

        }
        targett.setSecurityName(new OctetString(securityName));
        this.target = targett;





        this.usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);


        usm.addUser(new OctetString(securityName), user);

        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;

        if (target.getAddress() instanceof UdpAddress) {
            localAddress = new UdpAddress("0.0.0.0/0");
        }else {
            localAddress = new TcpAddress("0.0.0.0/0");
        }
        this.transport = transportFactory.createTransportMapping(localAddress);

    }




    /**
     * <p>getSNMPValue.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String getSNMPValue() throws IOException {


        String result = "";
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
        VariableBinding vb = new VariableBinding(new OID(this.oid));
        Vector vbs = new Vector();
        vbs.add(vb);

        Snmp snmp = createSession();
        if (target.getVersion() == SnmpConstants.version3){

            //UserTarget targett = (UserTarget)target;

            SecurityModels.getInstance().addSecurityModel(usm);


            snmp.getUSM().addUser(new OctetString(securityName), user);
            //snmp.discoverAuthoritativeEngineID(targett.getAuthoritativeEngineID(),targett.getTimeout());

        }

        snmp.listen();

        try {

            PDU request;
            if (target.getVersion() == SnmpConstants.version3){
              request = new ScopedPDU();
            } else {
               request =new PDU();
            }
            request.setType(PDU.GET);

            for (int i = 0; i < vbs.size(); i++) {
                request.add((VariableBinding) vbs.get(i));
            }

            long startTime = System.currentTimeMillis();


            PDU response = null;

            ResponseEvent responseEvent = snmp.send(request, target);



            if (responseEvent != null) {
                response = responseEvent.getResponse();
                logger.debug("Received response after " + (System.currentTimeMillis() - startTime) + " millis");
            }

            if (response == null){
                logger.debug("SNMP response is null.");
                return null;

            } else {
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb1 = response.get(i);
                    result = vb1.getVariable().toString();
                }
                return result;
            }
        } finally {
            logger.debug("Error " + snmp.toString());

            snmp.close();
        }
    }


    /**
     * <p>getSNMPGetNextValue.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String getSNMPGetNextValue() throws IOException {

    String result = "";
    //CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());

    VariableBinding vb = new VariableBinding(new OID(this.oid));
    Vector vbs = new Vector();
    vbs.add(vb);

    Snmp snmp = createSession();
    snmp.listen();

    try {

        PDU request = new PDU();
        request.setType(PDU.GETNEXT);

        for (int i = 0; i < vbs.size(); i++) {
            request.add((VariableBinding) vbs.get(i));
        }

        long startTime = System.currentTimeMillis();
        ResponseEvent responseEvent = snmp.send(request, target, transport);

        PDU response = null;
        if (responseEvent != null) {
            response = responseEvent.getResponse();
            logger.debug("Received response after " + (System.currentTimeMillis() - startTime) + " millis");
        }

        if (response == null){
            logger.debug("SNMP response is null.");
            return null;

        } else {
            for (int i = 0; i < response.size(); i++) {
                VariableBinding vb1 = response.get(i);
                result = vb1.getVariable().toString();
            }
            return result;
        }
    } finally {
        logger.debug("Error " + snmp.toString());

        snmp.close();
    }
}
    private Snmp createSession() throws IOException {


        MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
        //        AbstractTransportMapping transport = new DefaultUdpTransportMapping(localAddress);
        Snmp snmp = new Snmp(dispatcher, transport);



        ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());

        return snmp;


    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {
        LogFactory.setLogFactory(new Log4jLogFactory());
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
        VariableBinding vb = new VariableBinding(new OID(".1.3.6.1.4.1.9.10.86.1.1.1.1.5.1.2.16.32.1.4.112.31.11.10.189.0.0.0.0.0.0.0.0.96"));
//        VariableBinding vb = new VariableBinding(new OID("1.3.6.1.2.1.31.1.1.1"));
        Vector vbs = new Vector();
        vbs.add(vb);

        AbstractTransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("ciscoIp"));

        target.setVersion(SnmpConstants.version2c);
        target.setAddress(new UdpAddress("192.168.107.33/11161"));

        target.setRetries(1);
        target.setTimeout(2000);
        target.setMaxSizeRequestPDU(65535);
        snmp.listen();

        PDU request = new PDU();
        request.setType(PDU.GET);

        for (int i = 0; i < vbs.size(); i++) {
            request.add((VariableBinding) vbs.get(i));
        }

        long startTime = System.currentTimeMillis();
        ResponseEvent responseEvent = snmp.send(request, target);

        PDU response = null;

        if (responseEvent != null) {
            response = responseEvent.getResponse();
            System.out.println("Received response after " + (System.currentTimeMillis() - startTime) + " millis");
        }


        for (int i = 0; i < response.size(); i++) {
            VariableBinding vb1 = response.get(i);
            System.out.println(vb1.toString());
        }
        snmp.close();


    }
    private static byte[] hexStringToByteArray(String s) {
        if (s!=null){


            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i+1), 16));
            }
            return data;

        }  else {
            return null;
        }
    }

}
