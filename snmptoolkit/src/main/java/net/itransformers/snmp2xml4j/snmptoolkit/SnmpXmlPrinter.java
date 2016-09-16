/*
 * SnmpXmlPrinter.java
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

import net.percederberg.mibble.*;
import net.percederberg.mibble.snmp.SnmpAccess;
import net.percederberg.mibble.snmp.SnmpIndex;
import net.percederberg.mibble.snmp.SnmpObjectType;
import net.percederberg.mibble.type.*;
import net.percederberg.mibble.value.NumberValue;
import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.apache.log4j.Logger;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.Variable;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import java.util.ArrayList;

/**
 * Created by niau on 3/26/16.
 *
 * @author niau
 * @version $Id: $Id
 */
public class SnmpXmlPrinter {
    protected Node node;
    static Logger logger = Logger.getLogger(SnmpXmlPrinter.class);
    protected MibLoader loader;

    /**
     * <p>Constructor for SnmpXmlPrinter.</p>
     *
     * @param loader a {@link net.percederberg.mibble.MibLoader} object.
     * @param node a {@link net.itransformers.snmp2xml4j.snmptoolkit.Node} object.
     */
    public SnmpXmlPrinter(MibLoader loader, Node node) {
        this.loader = loader;
        this.node = node;
    }

    /**
     * <p>printTreeAsXML.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String printTreeAsXML() {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" ?>\n");
        sb.append("<root>\n");
        printTreeAsXML(node, "", sb, false);
        sb.append("</root>");
        logger.trace(sb.toString());
        return sb.toString();
    }

    /**
     * <p>printTreeAsXML.</p>
     *
     * @param detailedInfoFlag a boolean.
     * @return a {@link java.lang.String} object.
     */
    public String printTreeAsXML(boolean detailedInfoFlag) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" ?>\n");
        sb.append("<root>\n");
        printTreeAsXML(node, "", sb, detailedInfoFlag);
        sb.append("</root>");
        logger.trace(sb.toString());
        return sb.toString();
    }

    private void printTreeAsXML(Node node, String tabs, StringBuilder sb, boolean oidFlag) {
        if (node == null) return;
        if (!node.isDoWalk()) return;
        final ObjectIdentifierValue objectIdentifierValue = node.getObjectIdentifierValue();
        if (objectIdentifierValue == null) return;
        String tagName = objectIdentifierValue.getName();
        final MibValueSymbol symbol = node.getObjectIdentifierValue().getSymbol();
        String syntaxString="";
        String snmpSyntax = "";
        String accessString = "";
        String description = "";
        String units = "";
        if (null!=symbol && symbol.getType() instanceof SnmpObjectType){
            SnmpObjectType symbolType = (SnmpObjectType) symbol.getType();
            syntaxString = symbolType.getName();
            snmpSyntax =  determineSyntaxType(symbolType.getSyntax());
            SnmpAccess access = symbolType.getAccess();
            accessString = access.toString();
            if (symbolType.getDescription()!=null) {
                description = symbolType.getDescription().replaceAll("\\n", " ");
            }
            units = symbolType.getUnits();
        } else if (null!=symbol && symbol.getType() instanceof ObjectIdentifierType){
            ObjectIdentifierType symbolType = (ObjectIdentifierType) symbol.getType();
            syntaxString = symbolType.getName();
            snmpSyntax =  determineSyntaxType(symbolType);
            accessString = "not-accessible";
            description = "";
        }
        if (node.getChildren() == null) return;
        if (node.getChildren().size() == 0) {
            final VariableBinding vb1 = node.getVb();
            if (vb1 == null) return;
            final Variable variable = vb1.getVariable();
            final String vb = variable != null ? escapeForXML(variable.toString()) : "";
            if (oidFlag) {
                //    sb.append(String.format("%s<%s oid=\"%s\" primitiveSyntax=\"%s\" snmpSyntax=\"%s\" access=\"%s\">", tabs, tagName, objectIdentifierValue,syntaxString,snmpSyntax,accessString));
                sb.append(String.format("\t%s<%s oid=\"%s\" primitiveSyntax=\"%s\" snmpSyntax =\"%s\" access=\"%s\" units=\"%s\">", tabs, tagName, objectIdentifierValue, syntaxString,snmpSyntax, accessString,units));
                sb.append(String.format("\n\t\t%s<description><![CDATA[%s]]></description>",tabs,description));
                sb.append(String.format("\n\t\t%s<value>%s</value>",tabs,vb));
                sb.append(String.format("\n\t%s</%s>",tabs ,tagName));


            } else {
                sb.append(String.format("\t%s<%s>%s</%s>", tabs, tagName,vb,tagName));
//                sb.append(String.format("\n\t\t%s<value>%s</value>",tabs,vb));
//                sb.append(String.format("\n\t%s</%s>",tabs ,tagName));


            }
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
                    sb.append(String.format("%s<%s>", tabs, tagName));
                }
                sb.append('\n');
                sb.append(sb1);
                sb.append(String.format("%s</%s>", tabs, tagName));
                sb.append('\n');
            }
        }
    }

    static String escapeForXML(String s) {
        String xml10pattern = "[^"
                + "\u0009\r\n"
                + "\u0020-\uD7FF"
                + "\uE000-\uFFFD"
                + "\ud800\udc00-\udbff\udfff"
                + "]";
        return  s.replaceAll(xml10pattern,"").replaceAll("&", "&amp;").replaceAll("\"", "&quot;").
                replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("'", "&apos;").replaceAll("\u001c", "").replaceAll("\u000c","").replaceAll("\u001a", "");
    }

    private void printNodeTableAsXML(Node node, String tabs, StringBuilder sb, boolean oidFlag) {
        String tagName = node.getObjectIdentifierValue().getName();
        int i = 0;
        for (TableEvent tableEvent : node.getTable()) {
            i++;
            StringBuilder sbTable = new StringBuilder();
            StringBuilder sbIndex = new StringBuilder();
            StringBuilder sbRows = new StringBuilder();
            printTableIndexAsXML(node, tabs, sbIndex, tableEvent.getIndex(), i, oidFlag);
            printTableRowsAsXML(node, tabs, sbRows, tableEvent.getColumns(), oidFlag);

            final MibValueSymbol symbol = node.getObjectIdentifierValue().getSymbol();
            SnmpObjectType symbolType = (SnmpObjectType) symbol.getType();
            MibType syntax = symbolType.getSyntax();
            String syntaxString = syntax.getName();
            String snmpSyntax =  determineSyntaxType(syntax);
            SnmpAccess access = symbolType.getAccess();
            String accessString = access.toString();
            String description = symbolType.getDescription().replaceAll("\\n"," ");
            String units = symbolType.getUnits();
            if (oidFlag) {

                sbTable.append(String.format("%s<%s oid=\"%s\" primitiveSyntax=\"%s\" snmpSyntax =\"%s\" access=\"%s\" units=\"%s\">", tabs, tagName, node.getObjectIdentifierValue(), syntaxString,snmpSyntax, accessString, units));
                sbTable.append(String.format("\n\t%s<description><![CDATA[%s]]></description>",tabs,description));
            } else {
                sbTable.append(String.format("%s<%s>", tabs, tagName));

            }
            sbTable.append('\n');
            sbTable.append(sbIndex); // append index
            sbTable.append(sbRows); // append rows
            sbTable.append(String.format("\n%s</%s>", tabs, tagName));
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
                //SnmpObjectType snmpObjectType = (SnmpObjectType) mibValueSymbol.getType();
                final ObjectIdentifierValue childByName = objectIdentifierValue.getChildByName(childTagName);
                final MibValueSymbol symbol = childByName.getSymbol();
                SnmpObjectType symbolType = (SnmpObjectType) symbol.getType();
                MibType syntax = symbolType.getSyntax();
                String syntaxString = syntax.getName();
                String snmpSyntax = determineSyntaxType(syntax);
                SnmpAccess access = symbolType.getAccess();
                String description = symbolType.getDescription().replaceAll("\\n"," ");
                String accessString = access.toString();
                String units = symbolType.getUnits();
//                sb4.append(String.format("\t%s<%s oid=\"%s\">", tabs, childTagName, vb.getOid()));

                sb4.append(String.format("\t%s<%s oid=\"%s\" primitiveSyntax=\"%s\" snmpSyntax =\"%s\" access=\"%s\" units=\"%s\">", tabs, childTagName, vb.getOid(), syntaxString,snmpSyntax, accessString,units));
                sb4.append(String.format("\n\t\t%s<description><![CDATA[%s]]></description>",tabs,description));
                sb4.append(String.format("\n\t\t%s<value>%s</value>",tabs,var));
                sb4.append(String.format("\n\t%s</%s>",tabs, childTagName));

            } else {
                sb4.append(String.format("\n\t%s<%s>%s</%s>", tabs, childTagName,var,childTagName));
                //sb4.append(String.format("%s</%s>",var, childTagName));

            }

        }
    }

    private void printTableIndexAsXML(Node node, String tabs, StringBuilder sb, OID indexOID, int instanceIndex, boolean oidFlag) {
        if (node == null) return;
        final ObjectIdentifierValue objectIdentifierValue = node.getObjectIdentifierValue();
        if (objectIdentifierValue == null) return;
        final MibValueSymbol mibValueSymbol = objectIdentifierValue.getSymbol();
        if (mibValueSymbol == null) return;
        SnmpObjectType snmpObjectType = (SnmpObjectType) mibValueSymbol.getType();
        if (snmpObjectType == null) return;
        ArrayList indexes = snmpObjectType.getIndex();
        if (indexOID != null) {
            StringBuilder instance = new StringBuilder();
            StringBuilder instanceValues = new StringBuilder();


            if (indexes != null && indexes.size() > 0) {
                int pos = 0;
                for (int i = 0; i < indexes.size(); i++) {
                    try {
                        SnmpIndex index = (SnmpIndex) indexes.get(i);
                        String indexName = index.getValue().getName();


                        final ObjectIdentifierValue childByName = objectIdentifierValue.getChildByName(indexName);
                        //Why do we need childbyName ! To determine the syntax and access... But there are cases in which this does not work.Thus for those we set the syntax and access to UNKNOWN

                        MibValueSymbol symbol;
                        String syntaxString = "UNKNOWN";
                        String accessString = "UNKNOWN";
                        String snmpSyntax = "UNKNOWN";
                        if (childByName != null) {


                            symbol = childByName.getSymbol();
                            SnmpObjectType indexType = (SnmpObjectType) symbol.getType();
                            MibType syntax = indexType.getSyntax();
                            syntaxString = syntax.getName();

                            SnmpAccess access = indexType.getAccess();
                            accessString = access.toString();
                            String indexVal = new OID(indexOID.getValue(), pos, 1).toString();
                            snmpSyntax = determineSyntaxType(syntax);

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
                                                indexVal = OctetString.fromString(new OID(indexOID.getValue(), i + 1, indexOID.getValue().length - (i + 1)).toString(), '.', 10).toString();

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

                                sb.append(String.format("\t%s<index name=\"%s\" primitiveSyntax=\"%s\" snmpSyntax =\"%s\" oid=\"%s\" access=\"%s\">%s</index>\n", tabs, indexName, syntaxString, snmpSyntax, index, accessString, escapeForXML(indexVal)));
                            } else {
                                sb.append(String.format("\t%s<index name=\"%s\">%s</index>\n", tabs, indexName, escapeForXML(indexVal)));
                            }
                            if (i != indexes.size() - 1) {
                                instance.append(indexName + "");
                                instanceValues.append(indexVal).append("");
                            } else {
                                instance.append(indexName);
                                instanceValues.append(indexVal);

                            }

                            if (!posIncremented) {
                                pos++;
                            }
                        } else {
                            MibValueSymbol symbol11 = findSymbolFromMibs(indexName);

                            if(symbol11!=null){
                                MibType syntax = symbol11.getType();


                                SnmpAccess access = ((SnmpObjectType) syntax).getAccess();
                                accessString = access.toString();
                                syntaxString = ((SnmpObjectType) syntax).getSyntax().getName();

//                            String indexVal = new OID(indexOID.getValue(), pos, 1).toString();
                                snmpSyntax = determineSyntaxType(((SnmpObjectType) syntax).getSyntax());

                            }
                            OID indexVal = new OID(indexOID.getValue(), pos, 1);
                            if (oidFlag) {
                                sb.append(String.format("\t%s<index name=\"%s\" primitiveSyntax=\"%s\" snmpSyntax =\"%s\" oid=\"%s\" access=\"%s\">%s</index>\n", tabs, indexName, syntaxString,snmpSyntax, index, accessString, escapeForXML(indexVal.toString())));
                            } else {
                                sb.append(String.format("\t%s<index name=\"%s\">%s</index>\n", tabs, indexName, escapeForXML(indexVal.toString())));
                            }


                            if (i != indexes.size() - 1) {
                                instance.append(indexName).append("");
                                instanceValues.append(indexVal).append("");
                            } else {
                                instance.append(indexName).append(".");
                                instanceValues.append(indexVal).append(".");

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
//                                            Constraint constraint1 = (Constraint) list.snmpGet(0);
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
            sb.append(String.format("\t%s<instance instanceIndex=\"%s\" instanceName=\"%s\" instanceValue=\"%s\">%s</instance>\n", tabs, instanceIndex, instance.toString(), escapeForXML(instanceValues.toString()),escapeForXML(indexOID.toString())));

        }
    }
    private static String determineSyntaxType(MibType syntax) {
        if(syntax.getTag()!=null){

            if (syntax.getTag().getCategory() == MibTypeTag.APPLICATION_CATEGORY) {
                return syntax.getReferenceSymbol().getName();

            } else if (syntax.getTag().getCategory() == MibTypeTag.UNIVERSAL_CATEGORY) {
                return syntax.getName();

            } else if (syntax.getTag().getCategory() == MibTypeTag.CONTEXT_SPECIFIC_CATEGORY) {
                return syntax.getName();
            } else {

                return syntax.getName();
            }
        }else{
            return syntax.getName();
        }

    }
    private MibValueSymbol findSymbolFromMibs(String oidName){
        Mib mibs[] = this.loader.getAllMibs();
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
