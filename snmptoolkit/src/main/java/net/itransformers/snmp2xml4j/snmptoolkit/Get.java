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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
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

    String oid;
    String community;
    private UdpAddress localAddress;
    private TransportMappingAbstractFactory transportFactory;
    private MessageDispatcherAbstractFactory messageDispatcherFactory;
    CommunityTarget target = new CommunityTarget();
    /**
     * <p>Constructor for Get.</p>
     *
     * @param oid a {@link java.lang.String} object.
     * @param target a {@link org.snmp4j.CommunityTarget} object.
     * @param transportFactory a {@link net.itransformers.snmp2xml4j.snmptoolkit.transport.TransportMappingAbstractFactory} object.
     * @param messageDispatcherAbstractFactory a {@link net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory} object.
     */
    public Get(String oid, CommunityTarget target,TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherAbstractFactory){
        this.oid = oid;

        this.target = target;
        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
        this.transportFactory = transportFactory;
        localAddress = new UdpAddress("0.0.0.0/0");

    }
    /**
     * <p>Constructor for Get.</p>
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
        target = new CommunityTarget();
        target.setAddress(new UdpAddress(address));
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setVersion(version);
        target.setMaxSizeRequestPDU(65535);
        target.setCommunity(new OctetString(community));

        this.community = community;
        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
        localAddress = new UdpAddress("0.0.0.0/0");
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
        TransportMapping transport = transportFactory.createTransportMapping(localAddress);
        MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
        Snmp snmp = new Snmp(dispatcher, transport);
        ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());


        snmp.listen();

        try {

            PDU request = new PDU();
            request.setType(PDU.GET);

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


    /**
     * <p>getSNMPGetNextValue.</p>
     *
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String getSNMPGetNextValue() throws IOException {

    String result = "";
    CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
    VariableBinding vb = new VariableBinding(new OID(this.oid));
    //        VariableBinding vb = new VariableBinding(new OID("1.3.6.1.2.1.31.1.1.1"));
    Vector vbs = new Vector();
    vbs.add(vb);
    TransportMapping transport = transportFactory.createTransportMapping(localAddress);
    MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
    //        AbstractTransportMapping transport = new DefaultUdpTransportMapping(localAddress);
    Snmp snmp = new Snmp(dispatcher, transport);
    ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());

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
        target.setTimeout(1000);
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

        System.out.println("Response received with requestID=" +
                response.getRequestID() +
                ", errorIndex=" +
                response.getErrorIndex() + ", " +
                "errorStatus=" + response.getErrorStatusText() +
                "(" + response.getErrorStatus() + ")");

        for (int i = 0; i < response.size(); i++) {
            VariableBinding vb1 = response.get(i);
            System.out.println(vb1.toString());
        }
        snmp.close();


    }

}
