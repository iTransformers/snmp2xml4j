/*
 * iTransformer is an open source tool able to discover IP networks
 * and to perform dynamic data data population into a xml based inventory system.
 * Copyright (C) 2010  http://itransformers.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.itransformers.snmptoolkit;

import net.itransformers.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory;
import net.itransformers.snmptoolkit.transport.TransportMappingAbstractFactory;
import net.itransformers.snmptoolkit.transport.UdpTransportMappingFactory;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.CounterSupport;
import org.snmp4j.mp.DefaultCounterListener;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;

import java.io.IOException;
import java.util.Vector;

public class SnmpSet {
    static Logger logger = Logger.getLogger(Walk.class);

    String oid;
    String address;
    String community;
    String value;
    int retries;
    long timeout;
    int version;
    UdpAddress localAddress;
    TransportMappingAbstractFactory transportFactory;
    MessageDispatcherAbstractFactory messageDispatcherFactory;

    public SnmpSet(String oid,
                   String address,
                   int version,
                   int retries,
                   long timeout,
                   String community,
                   String value,
                   TransportMappingAbstractFactory transportFactory,
                   MessageDispatcherAbstractFactory messageDispatcherAbstractFactory
    ) throws IOException {

        this.oid = oid;
        this.address = address;
        this.version = version;
        this.timeout = timeout;
        this.retries = retries;
        this.community = community;
        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherAbstractFactory;
//        localAddress = new UdpAddress(InetAddress.getLocalHost(), 0);
//        new InetAddress("0.0.0.0");
         localAddress = new UdpAddress("0.0.0.0/0");
        this.value=value;
    }

    public String setSNMPValue() throws IOException {

        String result = "";
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
        Variable var = new OctetString(value);
        VariableBinding vb = new VariableBinding(new OID(this.oid),var);
//        VariableBinding vb = new VariableBinding(new OID("1.3.6.1.2.1.31.1.1.1"));
        Vector vbs = new Vector();
        vbs.add(vb);
        TransportMapping transport = transportFactory.createTransportMapping(localAddress);
        MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
//        AbstractTransportMapping transport = new DefaultUdpTransportMapping(localAddress);
        Snmp snmp = new Snmp(dispatcher, transport);
        ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(this.community));

        target.setVersion(version);
        target.setAddress(new UdpAddress(this.address));
//        target.setAddress(new UdpAddress("0.0.0.0/161"));
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setMaxSizeRequestPDU(65535);
        snmp.listen();
        logger.debug("SNMP SET TO"+target+" with OID: "+oid+" and value: "+value);
        try {

            PDU request = new PDU();
            request.setType(PDU.SET);
            for (int i = 0; i < vbs.size(); i++) {
                request.add((VariableBinding) vbs.get(i));
            }

            long startTime = System.currentTimeMillis();
            ResponseEvent responseEvent = snmp.set(request, target);

            PDU response = null;
            if (responseEvent != null) {
                response = responseEvent.getResponse();
                logger.debug("Received response after " + (System.currentTimeMillis() - startTime) + " millis");
            }
            if (response == null){
                throw new RuntimeException("SNMP response is null.");

            } else {
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb1 = response.get(i);
                    result = vb1.getVariable().toString();
                }
                logger.debug("Response value: "+result);
                return result;
            }
        } finally {
            snmp.close();
        }
    }

    public String createSNMPOID(String oid,
                                String address,
                                int port,
                                int version,
                                int retries,
                                long timeout,
                                String community,
                                String value
                                ) throws IOException {
        this.oid = oid;
        this.address = address+"/"+port;
        this.version = version;
        this.timeout = timeout;
        this.retries = retries;
        this.community = community;
        this.messageDispatcherFactory = new DefaultMessageDispatcherFactory();
//        localAddress = new UdpAddress(InetAddress.getLocalHost(), 0);
//        new InetAddress("0.0.0.0");
        localAddress = new UdpAddress("0.0.0.0/0");
        this.value=value;

        String result = "";
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
        Variable var = new OctetString(value);
        VariableBinding vb = new VariableBinding(new OID(this.oid),var);
        Vector vbs = new Vector();
        vbs.add(vb);
        TransportMapping transport = transportFactory.createTransportMapping(localAddress);
        MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
        Snmp snmp = new Snmp(dispatcher, transport);
        ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(this.community));

        target.setVersion(version);
        target.setAddress(new UdpAddress(this.address));
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setMaxSizeRequestPDU(65535);
        snmp.listen();
        logger.debug("SNMP SET TO"+target+" with OID: "+oid+" and value: "+value);
        try {

            PDU request = new PDU();
            request.setType(PDU.SET);
            for (int i = 0; i < vbs.size(); i++) {
                request.add((VariableBinding) vbs.get(i));
            }

            long startTime = System.currentTimeMillis();
            ResponseEvent responseEvent = snmp.set(request, target);

            PDU response = null;
            if (responseEvent != null) {
                response = responseEvent.getResponse();
                logger.debug("Received response after " + (System.currentTimeMillis() - startTime) + " millis");
            }
            if (response == null){
                throw new RuntimeException("SNMP response is null.");

            } else {
                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb1 = response.get(i);
                    result = vb1.getVariable().toString();
                }
                logger.debug("Response value: "+result);
                return result;
            }
        } finally {
            snmp.close();
        }
    }

    public static void main(String[] args) throws IOException {
        LogFactory.setLogFactory(new Log4jLogFactory());
        SnmpSet SetExampe = new SnmpSet(".1.3.6.1.2.1.1.4.0","10.10.10.10/161", SnmpConstants.version1,1,200,"publicw","test123",new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        SetExampe.setSNMPValue();
//        SetExampe.setTableFromSNMP();

    }
}
