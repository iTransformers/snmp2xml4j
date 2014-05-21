

package net.itransformers.snmptoolkit;

import net.percederberg.mibble.value.ObjectIdentifierValue;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;

import java.util.ArrayList;
import java.util.List;

public class Node {
    ObjectIdentifierValue objectIdentifierValue;
    List<Node> children = new ArrayList<Node>();
    List<TableEvent> table; //comes from snmp bulk command
    VariableBinding vb; // comes from snmp get command
    boolean doWalk;
    Node parent;

    public Node(ObjectIdentifierValue objectIdentifierValue, Node parent) {
        this.objectIdentifierValue = objectIdentifierValue;
        this.parent = parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void addChild(Node child){
        children.add(child);
    }

    public List<TableEvent> getTable() {
        return table;
    }

    public void setTable(List<TableEvent> table) {
        this.table = table;
    }

    public VariableBinding getVb() {
        return vb;
    }

    public void setVb(VariableBinding vb) {
        this.vb = vb;
    }

    public boolean isDoWalk() {
        return doWalk;
    }

    public void setDoWalk(boolean doWalk) {
        this.doWalk = doWalk;
    }

    public Node getParent() {
        return parent;
    }

    public ObjectIdentifierValue getObjectIdentifierValue() {
        return objectIdentifierValue;
    }

    @Override
    public String toString() {
        return "Node{" +
                "name=" + getObjectIdentifierValue().getName() +
                '}';
    }
}
