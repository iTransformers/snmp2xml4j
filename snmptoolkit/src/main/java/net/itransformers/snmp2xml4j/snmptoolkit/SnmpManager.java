/*
 * SnmpManager.java
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

/**
 * Created by niau on 3/14/16.
 *
 * @author niau
 * @version $Id: $Id
 */

import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory;
import net.itransformers.snmp2xml4j.snmptoolkit.transport.TransportMappingAbstractFactory;
import net.percederberg.mibble.*;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.*;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.CounterSupport;
import org.snmp4j.mp.DefaultCounterListener;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.DefaultPDUFactory;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.TableUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

public abstract class SnmpManager {

    protected Snmp snmp = null;
    static Logger logger = Logger.getLogger(SnmpManager.class);

    protected int retries;
    protected int timeout;
    protected int maxSizeRequestPDU = 65535;
    protected int destinationPort = 161;
    private MessageDispatcherAbstractFactory messageDispatcherFactory;
    private TransportMappingAbstractFactory transportMappingAbstractFactory;
    private TransportIpAddress transportLocalIpAddress;
    protected MibLoader loader;
    protected PDUFactory pduFactory;


    public SnmpManager(MibLoader loader, int retries, int timeout, int maxSizeRequestPDU, int destinationPort, TransportMappingAbstractFactory transportMappingAbstractFactory, TransportIpAddress transportLocalIpAddress)  {
        this.retries = retries;
        this.timeout = timeout;
        this.maxSizeRequestPDU = maxSizeRequestPDU;
        this.destinationPort = destinationPort;
        this.messageDispatcherFactory = new DefaultMessageDispatcherFactory();
        this.transportMappingAbstractFactory = transportMappingAbstractFactory;
        this.pduFactory = new DefaultPDUFactory();
        this.transportLocalIpAddress = transportLocalIpAddress;
        this.loader = loader;
    }


    protected abstract void doInit() throws IOException;

    public void init() throws IOException {


        TransportMapping transportMapping = transportMappingAbstractFactory.createTransportMapping(transportLocalIpAddress);
        MessageDispatcher messageDispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
        snmp = new Snmp(messageDispatcher, transportMapping);

        doInit();

        transportMapping.listen();


    }


    /**
     * <p>set.</p>
     *
     * @param oids  an array of {@link org.snmp4j.smi.OID} objects.
     * @param value a int.
     * @return a {@link org.snmp4j.event.ResponseEvent} object.
     */
    public ResponseEvent set(OID oids[], int value)  {

        PDU pdu = createPDU();

        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid, new Integer32(value)));
        }

        pdu.setType(PDU.SET);
        ResponseEvent event = null;

        try {

            event = snmp.send(pdu, getTarget(), null);

        } catch (IOException ioe) {
            System.out.println("Error SNMP SET");
        }

        if (event != null) {
            return event;
        }
        throw new RuntimeException("SET timed out");
    }

    /**
     * This method is capable of handling multiple OIDs
     *
     * @param oidStrings an array of {@link String} objects.
     * @return a {@link org.snmp4j.event.ResponseEvent} object.
     * @throws java.io.IOException if any.
     */
    public ResponseEvent get(ArrayList<String> oidStrings) throws IOException {
        OID oidOids[] = new OID[oidStrings.size()];
        int i = 0;
        for (String oid : oidStrings) {
            MibValueSymbol symbol = findSymbolFromMibs(oid);
            if (symbol == null) {

                oidOids[i] = new OID(oid);

            }else {
                String oidd = symbol.getValue().toString();

                oidOids[i] = new OID(oidd);
            }
            i++;
        }
        return getNext(oidOids);




    }
    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu =  pduFactory.createPDU(getTarget());

        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }

        pdu.setType(PDU.GET);

        ResponseEvent response;


        response = snmp.send(pdu, getTarget());


        if (response != null) {
            PDU responsePDU = response.getResponse();
            if (responsePDU != null) {
                if (responsePDU.getErrorStatus() == PDU.noError) {
                    return response;
                }
            }
            logger.log(Priority.INFO, "GET reposne from " + getTarget().getAddress() + " was null!");
            return null;

        }
        logger.log(Priority.INFO, "GET from " + getTarget().getAddress() + " has timed out!");
        return null;
    }


    public ResponseEvent getNext(OID oids[]) throws IOException {
        PDU pdu =  pduFactory.createPDU(getTarget());

        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }

        pdu.setType(PDU.GETNEXT);

        ResponseEvent response;


        response = snmp.send(pdu, getTarget());


        if (response != null) {
            PDU responsePDU = response.getResponse();
            if (responsePDU != null) {
                if (responsePDU.getErrorStatus() == PDU.noError) {
                    return response;
                }
            }
            logger.log(Priority.INFO, "GET reposne from " + getTarget().getAddress() + " was null!");
            return null;

        }
        logger.log(Priority.INFO, "GET from " + getTarget().getAddress() + " has timed out!");
        return null;
    }


    protected abstract Target getTarget();

    protected abstract PDU createPDU();

    public Node walk(String[] includes) throws IOException {
        ObjectIdentifierValue oid = loader.getRootOid();
        Set<String> includesSet = new HashSet<String>(Arrays.asList(includes));
        Node rootNode = new Node(oid, null);
        fillTreeFromMib(rootNode);

        fillDoWalk(rootNode, includesSet);
        fillTreeFromSNMP(rootNode);

        return rootNode;
    }

    private void fillTreeFromMib(Node node) {
        ObjectIdentifierValue oid = node.getObjectIdentifierValue();
        ObjectIdentifierValue[] children = oid.getAllChildren();
        for (ObjectIdentifierValue child : children) {
            if (child == null) {  // in case it is not found
                continue;
            }

            Node childNode = new Node(child, node);
            node.addChild(childNode);
            fillTreeFromMib(childNode);
        }
    }

    protected void fillTreeFromSNMP(Node root) throws IOException {
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
//         AbstractTransportMapping transport = new DefaultUdpTransportMapping(localAddress);
        try {
//
            TableUtils tutils = new TableUtils(snmp, pduFactory);
            fillTreeFromSNMP(root, tutils);
        } finally {
            snmp.close();
        }
    }


    private void fillDoWalk(Node node, Set includes) {
        if (includes.contains(node.getObjectIdentifierValue().getName())) {
            // set parents and itself
            Node currentNode = node;
            while (currentNode != null) {
                currentNode.setDoWalk(true);
                currentNode = currentNode.getParent();
            }
            fillDoWalkChildren(node);
        }
        for (Node child : node.getChildren()) {
            fillDoWalk(child, includes);
        }
    }

    private void fillDoWalkChildren(Node node) {
        for (Node child : node.getChildren()) {
            child.setDoWalk(true);
            fillDoWalkChildren(child);
        }
    }


    private void fillTreeFromSNMP(Node node, TableUtils tutils ) throws IOException {
        if (!node.isDoWalk()) return;
        ObjectIdentifierValue oid = node.getObjectIdentifierValue();
        MibValueSymbol mibValueSymbol = oid.getSymbol();
        if (mibValueSymbol != null) {
            MibType mibType = mibValueSymbol.getType();
            if (mibType instanceof SnmpObjectType) {
                SnmpObjectType snmpObjectType = (SnmpObjectType) mibType;
                MibType syntax = snmpObjectType.getSyntax();
                if (syntax instanceof SequenceType) {
                    ArrayList<OID> oidList = new ArrayList<OID>();

                    for (Node child : node.getChildren()) {
                        if (child.isDoWalk()) {
                            ObjectIdentifierValue childOid = child.getObjectIdentifierValue();
                            oidList.add(new OID(childOid.getSymbol().getValue().toString()));
                        }
                    }
                    if (oidList.size() > 0) {
                        OID[] oids = oidList.toArray(new OID[oidList.size()]);
                        List table = tutils.getTable(getTarget(), oids, new OID("0"), null);

                        node.setTable(table);
                    }
                    return;
                } else if (syntax instanceof BitSetType
                        || syntax instanceof BooleanType
                        || syntax instanceof ChoiceType
                        || syntax instanceof IntegerType
                        || syntax instanceof NullType
                        || syntax instanceof ObjectIdentifierType
                        || syntax instanceof RealType
                        || syntax instanceof ElementType
                        || syntax instanceof StringType
                        || syntax instanceof TypeReference
                        ) {

                    final OID oid1 = new OID(node.getObjectIdentifierValue().toString());
                    VariableBinding vb = getSingleVariable(oid1);
                    //logger.debug("Response: " + vb.getVariable().toString());

                    node.setVb(vb);
                }
            }
        }
        for (Node child : node.getChildren()) {
            fillTreeFromSNMP(child, tutils);
        }
    }

    private VariableBinding getSingleVariable(OID oid) throws IOException {
        PDU pdu = this.pduFactory.createPDU(getTarget());
        pdu.setType(PDU.GETNEXT);
        pdu.add(new VariableBinding(oid));
        ResponseEvent responseEvent = snmp.send(pdu, getTarget());
        PDU responsePDU = null;
        if (responseEvent != null) responsePDU = responseEvent.getResponse();
        VariableBinding vb = null;
        if (responsePDU != null) vb = responsePDU.get(0);
        return vb;
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     * @throws net.percederberg.mibble.MibLoaderException if any.

     */
    public static void main(String[] args) throws IOException, MibLoaderException {


        MibLoaderHolder mibLoaderHolder = new MibLoaderHolder(new File("mibs"), false);


        SnmpManager snmpUdpv2Manager = new SnmpUdpV2Manager(mibLoaderHolder.getLoader(), "195.218.195.228", "public", 1, 1000, 65535, 161);
        String oid1 = "1.3.6.1.2.1.1.1.0";
        snmpUdpv2Manager.init();
        OID oid = new OID(oid1);
        OID oids[] = new OID[]{oid};
        ResponseEvent responseEvent = snmpUdpv2Manager.get(oids);

        PDU response;
        if (responseEvent!=null) {
            response = responseEvent.getResponse();
            if (response != null) {

                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb1 = response.get(i);
                    System.out.println(vb1.toString());

                }
            }
        } else {
            System.out.println("PDU response event is null");
        }


        SnmpManager snmpUdpv3Manager = new SnmpUdpV3Manager(mibLoaderHolder.getLoader(), "195.218.195.228", SecurityLevel.AUTH_NOPRIV, "usr-md5-none", "authkey1", "MD5", null, null, 2, 1000, 65535, 161);

        snmpUdpv3Manager.init();
        responseEvent = snmpUdpv3Manager.get(oids);

        if (responseEvent!=null) {
            response = responseEvent.getResponse();
            if (response != null) {

                for (int i = 0; i < response.size(); i++) {
                    VariableBinding vb1 = response.get(i);
                    System.out.println(vb1.getVariable().toString());

                }
            }
        } else {
            System.out.println("PDU response event is null");
        }
       SnmpXmlPrinter xmlPrinter = new SnmpXmlPrinter(mibLoaderHolder.getLoader(),snmpUdpv3Manager.walk(new String[]{"system", "interfaces"}));

      //TODO add a parameter for detailed XML output
       String xml = xmlPrinter.printTreeAsXML(true);


        System.out.println(xml);


    }
    private MibValueSymbol findSymbolFromMibs(String oidName){
        Mib mibs[] = loader.getAllMibs();
        MibValueSymbol symbol11 = null;
        for (Mib mib : mibs) {
            symbol11 = (MibValueSymbol) mib.findSymbol(oidName, true);
            if (symbol11 != null) {
                return symbol11;

            }
        }
        return null;
    }





}
