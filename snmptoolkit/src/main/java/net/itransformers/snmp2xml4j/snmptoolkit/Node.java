

/*
 * Node.java
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

import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Node class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
public class Node {
    ObjectIdentifierValue objectIdentifierValue;
    List<Node> children = new ArrayList<Node>();
    List<TableEvent> table; //comes from snmp bulk command
    VariableBinding vb; // comes from snmp get command
    boolean doWalk;
    Node parent;

    /**
     * <p>Constructor for Node.</p>
     *
     * @param objectIdentifierValue a {@link net.percederberg.mibble.value.ObjectIdentifierValue} object.
     * @param parent a {@link net.itransformers.snmp2xml4j.snmptoolkit.Node} object.
     */
    public Node(ObjectIdentifierValue objectIdentifierValue, Node parent) {
        this.objectIdentifierValue = objectIdentifierValue;
        this.parent = parent;
    }

    /**
     * <p>Getter for the field <code>children</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Node> getChildren() {
        return children;
    }

    /**
     * <p>addChild.</p>
     *
     * @param child a {@link net.itransformers.snmp2xml4j.snmptoolkit.Node} object.
     */
    public void addChild(Node child){
        children.add(child);
    }

    /**
     * <p>Getter for the field <code>table</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<TableEvent> getTable() {
        return table;
    }

    /**
     * <p>Setter for the field <code>table</code>.</p>
     *
     * @param table a {@link java.util.List} object.
     */
    public void setTable(List<TableEvent> table) {
        this.table = table;
    }

    /**
     * <p>Getter for the field <code>vb</code>.</p>
     *
     * @return a {@link org.snmp4j.smi.VariableBinding} object.
     */
    public VariableBinding getVb() {
        return vb;
    }

    /**
     * <p>Setter for the field <code>vb</code>.</p>
     *
     * @param vb a {@link org.snmp4j.smi.VariableBinding} object.
     */
    public void setVb(VariableBinding vb) {
        this.vb = vb;
    }

    /**
     * <p>isDoWalk.</p>
     *
     * @return a boolean.
     */
    public boolean isDoWalk() {
        return doWalk;
    }

    /**
     * <p>Setter for the field <code>doWalk</code>.</p>
     *
     * @param doWalk a boolean.
     */
    public void setDoWalk(boolean doWalk) {
        this.doWalk = doWalk;
    }

    /**
     * <p>Getter for the field <code>parent</code>.</p>
     *
     * @return a {@link net.itransformers.snmp2xml4j.snmptoolkit.Node} object.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * <p>Getter for the field <code>objectIdentifierValue</code>.</p>
     *
     * @return a {@link net.percederberg.mibble.value.ObjectIdentifierValue} object.
     */
    public ObjectIdentifierValue getObjectIdentifierValue() {
        return objectIdentifierValue;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "Node{" +
                "name=" + getObjectIdentifierValue().getName() + "value="+getObjectIdentifierValue().toDetailString() +
                '}';
    }
}
