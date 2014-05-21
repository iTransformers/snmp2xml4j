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
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.util.Map;
import java.util.Vector;

public class Get {
    static Logger logger = Logger.getLogger(Walk.class);

    String oid;
    String address;
    String community;
    private int retries;
    private long timeout;
    private int version;
    private UdpAddress localAddress;
    private TransportMappingAbstractFactory transportFactory;
    private MessageDispatcherAbstractFactory messageDispatcherFactory;

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
    }

    public String getSNMPValue() throws IOException {

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

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(this.community));

        target.setVersion(version);
        target.setAddress(new UdpAddress(this.address));
//        target.setAddress(new UdpAddress("0.0.0.0/161"));
        target.setRetries(retries);
        target.setTimeout(timeout);
        target.setMaxSizeRequestPDU(65535);
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
                throw new RuntimeException("SNMP response is null.");

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

    CommunityTarget target = new CommunityTarget();
    target.setCommunity(new OctetString(this.community));

    target.setVersion(version);
    target.setAddress(new UdpAddress(this.address));
//        target.setAddress(new UdpAddress("0.0.0.0/161"));
    target.setRetries(retries);
    target.setTimeout(timeout);
    target.setMaxSizeRequestPDU(65535);
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
            throw new RuntimeException("SNMP response is null.");

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
    public static void main1(String[] args) throws IOException {
        Map<CmdOptions,String> opts;
        try {
            opts = CmdParser.parseCmd(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CmdParser.printGetUsage();
            return;
        }
        String oid = opts.get(CmdOptions.OIDS);
        if (oid == null) {
            System.out.println("Missing option \"-"+ CmdOptions.OIDS.getName()+"\"");
            CmdParser.printGetUsage();
        }
        String address = opts.get(CmdOptions.ADDRESS);
        if (address == null) {
            System.out.println("Missing option \"-"+ CmdOptions.ADDRESS.getName()+"\"");
            CmdParser.printGetUsage();
        }
        String community = opts.get(CmdOptions.COMMUNITY);
        if (community == null) {
            System.out.println("Missing option \"-"+ CmdOptions.COMMUNITY.getName()+"\"");
            CmdParser.printGetUsage();
        }
        int versionInt;
        try {
            String version = opts.get(CmdOptions.RETRIES);
            versionInt = Integer.parseInt(version);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid parameter value for \"-"+ CmdOptions.VERSION + "\", int value is required");
            CmdParser.printGetUsage();
            return;
        }
        int retriesInt;
        try {
            String retries = opts.get(CmdOptions.RETRIES);
            retriesInt = Integer.parseInt(retries);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid parameter value for \"-"+ CmdOptions.RETRIES + "\", int value is required");
            CmdParser.printGetUsage();
            return;
        }
        int timeoutInt;
        try {
            String timeout = opts.get(CmdOptions.TIMEOUT);
            timeoutInt = Integer.parseInt(timeout);
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid parameter value for \"-"+ CmdOptions.TIMEOUT + "\", int value is required");
            CmdParser.printGetUsage();
            return;
        }
//        Get get = new Get("1.3.6.1.2.1.1.5", "c82.16.36.1/161", retriesInt, timeoutInt, "public");

        Get get = new Get(oid, address, versionInt, retriesInt, timeoutInt, community, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        String value = get.getSNMPValue();
        System.out.println(value);
    }

    public static void main(String[] args) throws IOException {
        LogFactory.setLogFactory(new Log4jLogFactory());
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
        VariableBinding vb = new VariableBinding(new OID(".1.3.6.1.2.1.1.4.0"));
//        VariableBinding vb = new VariableBinding(new OID("1.3.6.1.2.1.31.1.1.1"));
        Vector vbs = new Vector();
        vbs.add(vb);

        AbstractTransportMapping transport = new DefaultUdpTransportMapping();
        Snmp snmp = new Snmp(transport);
        ((MPv3) snmp.getMessageProcessingModel(MPv3.ID)).setLocalEngineID(new OctetString(MPv3.createLocalEngineID()).getValue());

        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString("public"));

        target.setVersion(SnmpConstants.version2c);
        target.setAddress(new UdpAddress("10.10.10.10/161"));

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
