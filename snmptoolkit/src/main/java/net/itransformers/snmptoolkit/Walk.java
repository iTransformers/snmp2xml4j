/*
 * iTransformer is an open source tool able to discover and transform
 *  IP network infrastructures.
 *  Copyright (C) 2012  http://itransformers.net
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.itransformers.snmptoolkit;

import net.itransformers.snmptoolkit.messagedispacher.DefaultMessageDispatcherFactory;
import net.itransformers.snmptoolkit.messagedispacher.MessageDispatcherAbstractFactory;
import net.itransformers.snmptoolkit.transport.TransportMappingAbstractFactory;
import net.itransformers.snmptoolkit.transport.UdpTransportMappingFactory;
import net.percederberg.mibble.*;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpIndex;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.*;
import net.percederberg.mibble.value.NumberValue;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.apache.log4j.Logger;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.log.Log4jLogFactory;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.CounterSupport;
import org.snmp4j.mp.DefaultCounterListener;
import org.snmp4j.smi.*;
import org.snmp4j.util.PDUFactory;
import org.snmp4j.util.SnmpConfigurator;
import org.snmp4j.util.TableEvent;
import org.snmp4j.util.TableUtils;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.util.*;

public class Walk {
    static Logger logger = Logger.getLogger(Walk.class);

    private MibLoaderHolder loader;
    private UdpAddress localAddress;
    private TransportMappingAbstractFactory transportFactory;
    private MessageDispatcherAbstractFactory messageDispatcherFactory;

    public Walk(MibLoader loader, TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherFactory) throws IOException, MibLoaderException {
        this(new MibLoaderHolder(loader), transportFactory, messageDispatcherFactory);
        final InetAddress localHost = InetAddress.getLocalHost();
        logger.info("Local address: " + localHost);
        localAddress = new UdpAddress(localHost, 0);
    }

    public Walk(File mibDir, boolean failOnError, TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherFactory) throws IOException, MibLoaderException {
        this(new MibLoaderHolder(mibDir, failOnError), transportFactory, messageDispatcherFactory);
        final InetAddress localHost = InetAddress.getLocalHost();
        logger.info("Local address: " + localHost);
        localAddress = new UdpAddress("0.0.0.0/0");
//        localAddress = new UdpAddress(localHost, 0);
    }

    public Walk(MibLoaderHolder loader, TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherFactory) throws IOException {
        this.loader = loader;
        final InetAddress localHost = InetAddress.getLocalHost();
        logger.info("Local address: " + localHost);
//        localAddress = new UdpAddress(localHost, 0);
        localAddress = new UdpAddress("0.0.0.0/0");
        this.transportFactory = transportFactory;
        this.messageDispatcherFactory = messageDispatcherFactory;
    }

    public Walk(String[] mibFiles, TransportMappingAbstractFactory transportFactory, MessageDispatcherAbstractFactory messageDispatcherFactory) throws IOException, MibLoaderException {
        this(new MibLoaderHolder(mibFiles), transportFactory, messageDispatcherFactory);
        final InetAddress localHost = InetAddress.getLocalHost();
        logger.info("Local address: " + localHost);
        localAddress = new UdpAddress(localHost, 0);
    }

    public MibLoader getLoader() {
        return loader.getLoader();
    }

    public Node walk(String[] includes, Map parameters) throws IOException {
        ObjectIdentifierValue oid = getLoader().getRootOid();
        Set<String> includesSet = new HashSet<String>(Arrays.asList(includes));
        Node rootNode = new Node(oid, null);
        setSNMPTable(rootNode, parameters);
        fillTreeFromMib(rootNode);
        fillDoWalk(rootNode, includesSet);
        fillTreeFromSNMP(rootNode, parameters);

        return rootNode;
    }

    private void fillTreeFromSNMP(Node root, Map parameters) throws IOException {
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
//         AbstractTransportMapping transport = new DefaultUdpTransportMapping(localAddress);
        TransportMapping transport = transportFactory.createTransportMapping(localAddress);
        MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
        Snmp snmp = new Snmp(dispatcher, transport);
        snmp.listen();
        try {
            SnmpConfigurator snmpConfig = new SnmpConfigurator();
            Target t = snmpConfig.getTarget(parameters);
            PDUFactory pduFactory = snmpConfig.getPDUFactory(parameters);
            TableUtils tutils = new TableUtils(snmp, pduFactory);
            fillTreeFromSNMP(snmp, pduFactory, root, tutils, t);
        } finally {
            snmp.close();
        }
    }

    private void setSNMPTable(Node root, Map parameters) throws IOException {
        CounterSupport.getInstance().addCounterListener(new DefaultCounterListener());
//         AbstractTransportMapping transport = new DefaultUdpTransportMapping(localAddress);
        TransportMapping transport = transportFactory.createTransportMapping(localAddress);
        MessageDispatcher dispatcher = messageDispatcherFactory.createMessageDispatcherMapping();
        Snmp snmp = new Snmp(dispatcher, transport);
        snmp.listen();
        try {
            SnmpConfigurator snmpConfig = new SnmpConfigurator();
            Target t = snmpConfig.getTarget(parameters);
            PDUFactory pduFactory = snmpConfig.getPDUFactory(parameters);
            TableUtils tutils = new TableUtils(snmp, pduFactory);
            createTableRow(snmp, pduFactory, root, tutils, t);
        } finally {
            snmp.close();
        }
    }

    private void createTableRow(Snmp snmp, PDUFactory pduFactory, Node node, TableUtils tutils, Target t) throws IOException {
        OID rowStatusOid = new OID("1.3.6.1.2.1.80.1.2.1.23");
        OID indexOid = new OID("1");
//         VariableBinding[] rowValue = new VariableBinding[]{
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.2"),
//                 new OctetString("")),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.3"),
//                 new OctetString("")),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.4"),
//new Integer32(5)),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.5"),
//new Integer32(5001)),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.6"),
//new Integer32(65)),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.7"),
//new Integer32(5001)),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.8"),
//new Integer32(2)),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.10"),
//new Integer32(1)),
//                 new VariableBinding(new OID("1.3.6.1.4.1.9.9.42.1.2.1.1.11"),
//new OctetString(""))
//         };

        VariableBinding[] rowValue = new VariableBinding[]{
                //pingCtlRowStatus
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.23.1"), new Integer32(1)),
                //pingCtlTargetAddress
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.4.1"), new OctetString("4.2.2.2")),
                //pingCtlTargetAddressType
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.3.1"), new Integer32(1)),
//                        pingCtlProbeCount
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.7.1"), new Integer32(3)),
                //pingCtlDataSize
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.5.1"), new Integer32(64)),
                //pingCtlFrequency
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.10.1"), new Integer32(1)),
                //pingCtlMaxRows
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.11.1"), new Integer32(2)),
                //pingCtlAdminStatus    1.3.6.1.2.1.80.1.2.1.8
                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.2.1.8.1"), new Integer32(1)),

                new VariableBinding(new OID(".1.3.6.1.2.1.80.1.1.0"), new OctetString("1"))
        };

        ResponseEvent responseEvent;
        responseEvent = tutils.createRow(t, rowStatusOid, indexOid, rowValue);
        logger.info("CreateRow response: " + responseEvent.getResponse());
    }

    private void fillTreeFromSNMP(Snmp snmp, PDUFactory pduFactory, Node node, TableUtils tutils, Target t) throws IOException {
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
                        List table = tutils.getTable(t, oids, new OID("0"), null);

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
                    VariableBinding vb = getSingleVariable(snmp, pduFactory, t, oid1);
//                    logger.debug("Response: "+vb.getVariable().toString());
                    node.setVb(vb);
                }
            }
        }
        for (Node child : node.getChildren()) {
            fillTreeFromSNMP(snmp, pduFactory, child, tutils, t);
        }
    }

    private VariableBinding getSingleVariable(Snmp snmp, PDUFactory pduFactory, Target t, OID oid) throws IOException {
        PDU pdu = pduFactory.createPDU(t);
        pdu.setType(PDU.GETNEXT);
        pdu.add(new VariableBinding(oid));
        ResponseEvent responseEvent = snmp.send(pdu, t);
        PDU responsePDU = null;
        if (responseEvent != null) responsePDU = responseEvent.getResponse();
        VariableBinding vb = null;
        if (responsePDU != null) vb = responsePDU.get(0);
        return vb;
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

    public static String printTreeAsXML(Node node) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" ?>\n");
        sb.append("<root>\n");
        printTreeAsXML(node, "", sb, false);
        sb.append("</root>");
        logger.trace(sb.toString());
        return sb.toString();
    }

    public static String printTreeAsXML(Node node, boolean oidFlag) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" ?>\n");
        sb.append("<root>\n");
        printTreeAsXML(node, "", sb, oidFlag);
        sb.append("</root>");
        logger.trace(sb.toString());
        return sb.toString();
    }

    private static void printTreeAsXML(Node node, String tabs, StringBuilder sb, boolean oidFlag) {
        if (node == null) return;
        if (!node.isDoWalk()) return;
        final ObjectIdentifierValue objectIdentifierValue = node.getObjectIdentifierValue();
        if (objectIdentifierValue == null) return;
        String tagName = objectIdentifierValue.getName();
        if (node.getChildren() == null) return;
        if (node.getChildren().size() == 0) {
            final VariableBinding vb1 = node.getVb();
            if (vb1 == null) return;
            final Variable variable = vb1.getVariable();
            final String vb = variable != null ? escapeForXML(variable.toString()) : "";
            if (oidFlag) {
                sb.append(String.format("%s<%s oid=\"%s\">", tabs, tagName, objectIdentifierValue));
            } else {
                sb.append(String.format("%s<%s>", tabs, tagName));

            }
            sb.append(vb);
            sb.append(String.format("</%s>", tagName));
            sb.append('\n');
            logger.trace(sb.toString());
        } else {
            if (node.getTable() != null) {
                printNodeTableAsXML(node, tabs, sb, oidFlag);
                logger.trace(sb.toString());

            } else {
                StringBuilder sb1 = new StringBuilder();
                for (Node child : node.getChildren()) {
                    printTreeAsXML(child, tabs + "\t", sb1, oidFlag);
                }
                if (oidFlag) {
                    sb.append(String.format("%s<%s oid=\"%s\">", tabs, tagName, objectIdentifierValue));
                } else {
                    sb.append(String.format("%s<%s>", tabs, tagName, objectIdentifierValue));

                }
                sb.append('\n');
                sb.append(sb1);
                sb.append(String.format("%s</%s>", tabs, tagName));
                sb.append('\n');
            }
        }
    }

    static String escapeForXML(String s) {
        return s.replaceAll("&", "&amp;").replaceAll("\"", "&quot;").
                replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\u001c", "");
    }

    private static void printNodeTableAsXML(Node node, String tabs, StringBuilder sb, boolean oidFlag) {
        String tagName = node.getObjectIdentifierValue().getName();
        int i = 0;
        for (TableEvent tableEvent : node.getTable()) {
            i++;
            StringBuilder sbTable = new StringBuilder();
            StringBuilder sbIndex = new StringBuilder();
            StringBuilder sbRows = new StringBuilder();
            printTableIndexAsXML(node, tabs, sbIndex, tableEvent.getIndex(), i, oidFlag);
            printTableRowsAsXML(node, tabs, sbRows, tableEvent.getColumns(), oidFlag);
            if (oidFlag) {
                sbTable.append(String.format("%s<%s oid=\"%s\" syntax=\"%s\" >", tabs, tagName, node.getObjectIdentifierValue(), node.getObjectIdentifierValue().getSymbol().getType().getName()));
            } else {
                sbTable.append(String.format("%s<%s>", tabs, tagName));

            }
            sbTable.append('\n');
            sbTable.append(sbIndex); // append index
            sbTable.append(sbRows); // append rows
            sbTable.append(String.format("%s</%s>", tabs, tagName));
            sbTable.append('\n');
            sb.append(sbTable);
            // logger.debug(sbTable.toString());
        }
    }

    private static void printTableRowsAsXML(Node node, String tabs, StringBuilder sb4, VariableBinding[] cols, boolean oidFlag) {
        if (cols == null) return;
        for (VariableBinding vb : cols) {
            if (vb == null) continue;
            String childTagName = null;
            for (Node child : node.getChildren()) {
                final ObjectIdentifierValue objectIdentifierValue = child.getObjectIdentifierValue();
                if (objectIdentifierValue == null) continue;
                try {
                    if (vb.getOid() != null && vb.getOid().startsWith(new OID(objectIdentifierValue.toString()))) {
                        childTagName = objectIdentifierValue.getName();
                    }
                } catch (RuntimeException rte) {
                    logger.error(rte.getMessage(), rte);
                }
            }
            if (childTagName == null) continue;
            final Variable variable = vb.getVariable();
            if (variable == null) continue;
            final String var = escapeForXML(variable.toString());
            if (oidFlag) {
                ObjectIdentifierValue objectIdentifierValue = node.getObjectIdentifierValue();
                final MibValueSymbol mibValueSymbol = objectIdentifierValue.getSymbol();
                if (mibValueSymbol == null)
                    return;
                SnmpObjectType snmpObjectType = (SnmpObjectType) mibValueSymbol.getType();
                final ObjectIdentifierValue childByName = objectIdentifierValue.getChildByName(childTagName);
                final MibValueSymbol symbol = childByName.getSymbol();
                SnmpObjectType indexType = (SnmpObjectType) symbol.getType();
                MibType syntax = indexType.getSyntax();
                String syntaxString = syntax.getName();

                SnmpAccess access = indexType.getAccess();

                sb4.append(String.format("\t%s<%s oid=\"%s\" syntax=\"%s\" access=\"%s\">", tabs, childTagName, vb.getOid(), syntaxString, access.toString()));
            } else {
                sb4.append(String.format("\t%s<%s>", tabs, childTagName));
            }
            sb4.append(var);
            sb4.append(String.format("</%s>", childTagName));
            sb4.append('\n');
        }
    }

    private static void printTableIndexAsXML(Node node, String tabs, StringBuilder sb, OID indexOID, int instanceIndex, boolean oidFlag) {
        if (node == null) return;
        final ObjectIdentifierValue objectIdentifierValue = node.getObjectIdentifierValue();
        if (objectIdentifierValue == null) return;
        final MibValueSymbol mibValueSymbol = objectIdentifierValue.getSymbol();
        if (mibValueSymbol == null) return;
        SnmpObjectType snmpObjectType = (SnmpObjectType) mibValueSymbol.getType();
        if (snmpObjectType == null) return;
        ArrayList indexes = snmpObjectType.getIndex();
        if (indexOID != null) {
            StringBuffer instance = new StringBuffer();
            StringBuffer instanceValues = new StringBuffer();


            if (indexes != null && indexes.size() > 0) {
                int pos = 0;
                for (int i = 0; i < indexes.size(); i++) {
                    try {
                        SnmpIndex index = (SnmpIndex) indexes.get(i);
                        String indexName = index.getValue().getName();
                        //Why do we need childbyName ! To determine the syntax... But there are cases in which this does not work.


                        final ObjectIdentifierValue childByName = objectIdentifierValue.getChildByName(indexName);
                        MibValueSymbol symbol = null;
                        String syntaxString = "UNKNOWN";
                        String accessString = "UNKNOWN";
                        if (childByName != null) {


                            symbol = childByName.getSymbol();
                            SnmpObjectType indexType = (SnmpObjectType) symbol.getType();
                            MibType syntax = indexType.getSyntax();
                            syntaxString = syntax.getName();

                            SnmpAccess access = indexType.getAccess();
                            accessString = access.toString();
                            String indexVal = new OID(indexOID.getValue(), pos, 1).toString();
                            boolean posIncremented = false;
                            if (syntax instanceof StringType) {
                                if (syntaxString.equals("OCTET STRING")) {
                                    Constraint constraint = ((StringType) syntax).getConstraint();
                                    if (constraint instanceof SizeConstraint) {
                                        ArrayList list = ((SizeConstraint) constraint).getValues();
                                        if (list.size() == 1) {
                                            Constraint constraint1 = (Constraint) list.get(0);
                                            if (constraint1 instanceof ValueConstraint) {
                                                MibValue val = ((ValueConstraint) constraint1).getValue();
                                                if (val instanceof NumberValue) {
                                                    NumberValue numVal = (NumberValue) val;
                                                    Number number = (Number) numVal.toObject();
                                                    int size = number.intValue();
                                                    indexVal = new OID(indexOID.getValue(), pos, size).toString();
                                                    pos += size;
                                                    posIncremented = true;
                                                }
                                            } else if (constraint1 instanceof ValueRangeConstraint) {
//                                                if (syntax.getTag())
                                                indexVal = OctetString.fromString(new OID(indexOID.getValue(), 1, indexOID.getValue().length - 1).toString(), '.', 10).toString();
                                            } else {
                                                indexVal = new OID(indexOID.getValue()).toString();
                                            }
                                        } else {
                                            indexVal = new OID(indexOID.getValue()).toString();
                                        }
                                    } else {
                                        indexVal = new OID(indexOID.getValue()).toString();
                                    }

                                }
                            }
                            if (oidFlag) {
                                sb.append(String.format("\t%s<index name=\"%s\" syntax=\"%s\" oid=\"%s\" access=\"%s\">%s</index>\n", tabs, indexName, syntaxString, index, accessString, indexVal));
                            } else {
                                sb.append(String.format("\t%s<index name=\"%s\">%s</index>\n", tabs, indexName, indexVal));
                            }
                            if (i != indexes.size() - 1) {
                                instance.append(indexName + ".");
                                instanceValues.append(indexVal + ".");
                            } else {
                                instance.append(indexName);
                                instanceValues.append(indexVal);

                            }

                            if (!posIncremented) {
                                pos++;
                            }
                        } else {
                            OID indexVal = new OID(indexOID.getValue(), pos, 1);
                            if (oidFlag) {
                                sb.append(String.format("\t%s<index name=\"%s\" syntax=\"%s\" oid=\"%s\" access=\"%s\">%s</index>\n", tabs, indexName, syntaxString, index, accessString, indexVal.toString()));
                            } else {
                                sb.append(String.format("\t%s<index name=\"%s\">%s</index>\n", tabs, indexName, indexVal.toString()));
                            }


                            if (i != indexes.size() - 1) {
                                instance.append(indexName + ".");
                                instanceValues.append(indexVal + ".");
                            } else {
                                instance.append(indexName);
                                instanceValues.append(indexVal);

                            }

                        }


//                        if (childByName == null) {
//
//
//                        } else {
////                            final MibValueSymbol symbol = childByName.getSymbol();
////                            SnmpObjectType indexType = (SnmpObjectType) symbol.getType();
////                            MibType syntax = indexType.getSyntax();
//                            String indexVal = new OID(indexOID.getValue(), pos, 1).toString();
////                            String syntaxString = syntax.getName();
//                            boolean posIncremented = false;
//                            if (syntax instanceof StringType) {
//                                if (syntaxString.equals("OCTET STRING")) {
//                                    Constraint constraint = ((StringType) syntax).getConstraint();
//                                    if (constraint instanceof SizeConstraint) {
//                                        ArrayList list = ((SizeConstraint) constraint).getValues();
//                                        if (list.size() == 1) {
//                                            Constraint constraint1 = (Constraint) list.get(0);
//                                            if (constraint1 instanceof ValueConstraint) {
//                                                MibValue val = ((ValueConstraint) constraint1).getValue();
//                                                if (val instanceof NumberValue) {
//                                                    NumberValue numVal = (NumberValue) val;
//                                                    Number number = (Number) numVal.toObject();
//                                                    int size = number.intValue();
//                                                    indexVal = new OID(indexOID.getValue(), pos, size).toString();
//                                                    pos += size;
//                                                    posIncremented = true;
//                                                }
//                                            } else if (constraint1 instanceof ValueRangeConstraint) {
////                                                if (syntax.getTag())
//                                                indexVal = OctetString.fromString(new OID(indexOID.getValue(), 1, indexOID.getValue().length - 1).toString(), '.', 10).toString();
//                                            } else {
//                                                indexVal = new OID(indexOID.getValue()).toString();
//                                            }
//                                        } else {
//                                            indexVal = new OID(indexOID.getValue()).toString();
//                                        }
//                                    } else {
//                                        indexVal = new OID(indexOID.getValue()).toString();
//                                    }
//
//                                }
//                            }
//                            if (oidFlag) {
//                                sb.append(String.format("\t%s<index name=\"%s\" syntax=\"%s\" oid=\"%s\" access=\"%s\">%s</index>\n", tabs, indexName, syntaxString, index, access.toString(), indexVal));
//                            } else {
//                                sb.append(String.format("\t%s<index name=\"%s\">%s</index>\n", tabs, indexName, indexVal));
//                            }
//                            if (i != indexes.size() - 1) {
//                                instance.append(indexName + ".");
//                                instanceValues.append(indexVal + ".");
//                            } else {
//                                instance.append(indexName);
//                                instanceValues.append(indexVal);
//
//                            }
//
//                            if (!posIncremented) {
//                                pos++;
//                            }
//                        }

                    } catch (RuntimeException e) {
                        logger.trace(sb.toString());
                        logger.error(e.getMessage(), e);
                    }
                }
            }
            sb.append(String.format("\t%s<instance instanceIndex=\"%s\" instanceName=\"%s\">%s</instance>\n", tabs, instanceIndex, instance.toString(), instanceValues.toString()));

        }
    }

    public static void main(String[] args) throws IOException, MibLoaderException, XPathExpressionException, SAXException, ParserConfigurationException {
        LogFactory.setLogFactory(new Log4jLogFactory());
        boolean oidFlag = true;
        Map<CmdOptions, String> opts;
        try {
            opts = CmdParser.parseCmd(args);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            CmdParser.printWalkUsage();
            return;
        }
        String mibDir = opts.get(CmdOptions.MIBS_DIR);
        if (mibDir == null) {
            System.out.println("Missing option \"-" + CmdOptions.MIBS_DIR.getName() + "\"");
            CmdParser.printWalkUsage();
            return;
        }

        Walk walker = new Walk(new File(mibDir), false, new UdpTransportMappingFactory(), new DefaultMessageDispatcherFactory());
        if (opts.containsKey(CmdOptions.PRINT_LOADED_MIBS)) {
            Mib[] allMibs = walker.getLoader().getAllMibs();
            for (Mib mib : allMibs) {
                System.out.println("MIB: " + mib.getName() + ", file=" + mib.getFile());
            }
        }
        Properties parameters = new Properties();
        if (fillParams(opts, parameters)) return;

        StringBuffer finalXmlBuffer = new StringBuffer();
        ArrayList<String> includesList = new ArrayList<String>();
        String oids = opts.get(CmdOptions.OIDS);
        if (oids == null) {
            System.out.println("Missing option \"-" + CmdOptions.OIDS.getName());
            CmdParser.printWalkUsage();
            return;
        }

        StringTokenizer tokenizer = new StringTokenizer(oids, " ,;");
        while (tokenizer.hasMoreTokens()) {
            includesList.add(tokenizer.nextToken());
        }
        Node root = walker.walk(includesList.toArray(new String[includesList.size()]), parameters);
        String xml = Walk.printTreeAsXML(root, oidFlag);

        finalXmlBuffer.append(xml);
        outputXml(opts, finalXmlBuffer.toString());
    }

    private static void outputXml(Map<CmdOptions, String> opts, String finalXml) throws FileNotFoundException {
        String outputFile = opts.get(CmdOptions.OUTPUT_FILE);
        if (outputFile == null) {
            System.out.println(finalXml);
        } else {
            PrintWriter writer = new PrintWriter(outputFile);
            writer.print(finalXml);
            writer.flush();
            writer.close();
        }
    }

    static boolean fillParams(Map<CmdOptions, String> opts, Properties parameters) {


        String address = opts.get(CmdOptions.ADDRESS);
        if (address != null) {
            parameters.put(SnmpConfigurator.O_ADDRESS, Arrays.asList(address));
        } else {
            System.out.println("Missing option \"-" + CmdOptions.ADDRESS.getName() + "\"");
            CmdParser.printWalkUsage();
            return true;
        }
        String community = opts.get(CmdOptions.COMMUNITY);
        if (community != null) {
            parameters.put(SnmpConfigurator.O_COMMUNITY, Arrays.asList(community));
        } else {
            System.out.println("Missing option \"-" + CmdOptions.COMMUNITY.getName() + "\"");
            CmdParser.printWalkUsage();
            return true;
        }
        String version = opts.get(CmdOptions.VERSION);
        if (version != null) {
            parameters.put(SnmpConfigurator.O_VERSION, Arrays.asList(version));
        } else {
            System.out.println("Missing option \"-" + CmdOptions.VERSION.getName() + "\"");
            CmdParser.printWalkUsage();
            return true;
        }
        String timeout = opts.get(CmdOptions.TIMEOUT);
        if (timeout != null) {
            int timeoutInt;
            try {
                timeoutInt = Integer.parseInt(timeout);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid parameter value for \"-" + CmdOptions.RETRIES + "\", int value is required");
                CmdParser.printWalkUsage();
                return true;
            }
            parameters.put(SnmpConfigurator.O_TIMEOUT, Arrays.asList(timeoutInt));
        } else {
            System.out.println("Using default timeout: 1000");
            parameters.put(SnmpConfigurator.O_TIMEOUT, Arrays.asList(1000));
        }
        String retries = opts.get(CmdOptions.RETRIES);
        if (retries != null) {
            int retriesInt;
            try {
                retriesInt = Integer.parseInt(retries);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid parameter value for \"-" + CmdOptions.RETRIES + "\", int value is required");
                CmdParser.printWalkUsage();
                return true;
            }
            parameters.put(SnmpConfigurator.O_RETRIES, Arrays.asList(retriesInt));
        } else {
            System.out.println("Using default retries: 1");
            parameters.put(SnmpConfigurator.O_RETRIES, Arrays.asList(1));
        }
        String maxRepetitions = opts.get(CmdOptions.MAX_REPETITIONS);
        if (maxRepetitions != null) {
            int maxRepetitionsInt;
            try {
                maxRepetitionsInt = Integer.parseInt(maxRepetitions);
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid parameter value for \"-" + CmdOptions.MAX_REPETITIONS + "\", int value is required");
                CmdParser.printWalkUsage();
                return true;
            }
            parameters.put(SnmpConfigurator.O_MAX_REPETITIONS, Arrays.asList(maxRepetitionsInt));
        } else {
            System.out.println("Using default maxRepetitions: 10");
            parameters.put(SnmpConfigurator.O_MAX_REPETITIONS, Arrays.asList(1000));
        }
        return false;
    }

}
//            String[] includes = new String[]{
//                    "ifIndex", "ifDescr", "ifOperStatus", "ifAdminStatus", "ifNumber","ifAlias","ifPhysAddress","ifType",
//                    "dot1dTpFdb","dot1dTpFdbAddress","dot1dTpFdbStatus","dot1dTpFdbPort",
//                    "dot1dBasePort","dot1dBasePortIfIndex",
//                    "system",
//                    "dot1dBaseBridgeAddress","dot1dStpPort",
//                    "ipNetToMediaTable",
//                    "ipAddrTable",
//                    "lldpRemoteSystemsData",
//                    "cdpCacheDevicePort","cdpCacheDevicePlatform","cdpCacheDeviceId","cdpCacheIfIndex"
//            };


//        parameters.put(SnmpConfigurator.O_ADDRESS, Arrays.asList("10.10.10.10/161"));
//        parameters.put(SnmpConfigurator.O_ADDRESS, Arrays.asList("10.10.10.10/161"));
//        parameters.put(SnmpConfigurator.O_COMMUNITY, Arrays.asList("public"));
//        parameters.put(SnmpConfigurator.O_VERSION, Arrays.asList("2c"));
//
//        parameters.put(SnmpConfigurator.O_TIMEOUT, Arrays.asList(1000));
//        parameters.put(SnmpConfigurator.O_RETRIES, Arrays.asList(1));
//        parameters.put(SnmpConfigurator.O_MAX_REPETITIONS, Arrays.asList(65535));
