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
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
public class SnmpManager{
    Snmp snmp    = null;
    String address    = null;

    int  SNMPversion   = 3;

    String ver3Username  = "usr-md5-none";
    String ver3AuthPasscode = "authkey1";

    /**
     * Constructor
     *
     * @param add a {@link java.lang.String} object.
     */
    public SnmpManager(String add)
    {
        address = add;
    }

    /**
     * Start the Snmp session. If you forget the listen() method you will not get any answers because the communication is asynchronous and the
     * listen() method listens for answers.
     *
     * @throws java.io.IOException if any.
     */
    public void start() throws IOException {
        TransportMapping transport = new DefaultUdpTransportMapping();

        if (SNMPversion == 3) {
            USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);
            SecurityModels.getInstance().addSecurityModel(usm);
        }
        snmp = new Snmp(transport);

        if (SNMPversion == 3)
            snmp.getUSM().addUser(new OctetString(ver3Username),
                    new UsmUser(new OctetString(ver3Username), AuthMD5.ID, new OctetString(ver3AuthPasscode), null, null));

        // Do not forget this line!
        transport.listen();
    }

    /**
     * Method which takes a single OID and returns the response from the agent as a String.
     *
     * @param oid a {@link org.snmp4j.smi.OID} object.
     * @throws java.io.IOException if any.
     * @return a {@link java.lang.String} object.
     */
    public String getAsString(OID oid) throws IOException {
        ResponseEvent event = get(new OID[] { oid });
        return event.getResponse().get(0).getVariable().toString();
    }

    /**
     * <p>setIntFromString.</p>
     *
     * @param value a int.
     * @param oid a {@link org.snmp4j.smi.OID} object.
     * @return a {@link java.lang.String} object.
     * @throws java.io.IOException if any.
     */
    public String setIntFromString(int value, OID oid) throws IOException {

        ResponseEvent event = set(new OID[] { oid }, value);

        return event.getResponse().get(0).getVariable().toString();

    }

    /**
     * <p>set.</p>
     *
     * @param oids an array of {@link org.snmp4j.smi.OID} objects.
     * @param value a int.
     * @return a {@link org.snmp4j.event.ResponseEvent} object.
     * @throws java.io.IOException if any.
     */
    public ResponseEvent set(OID oids[], int value) throws IOException {

        PDU pdu;

        if (SNMPversion == 3)
            pdu = new ScopedPDU();
        else
            pdu = new PDU();

        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid, new Integer32(value)));
        }

        pdu.setType(PDU.SET);
        ResponseEvent event = null;

        try {
            if (SNMPversion == 3)
                event = snmp.send(pdu, getSNMPv3Target(), null);
            else
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
     * @param oids an array of {@link org.snmp4j.smi.OID} objects.
     * @throws java.io.IOException if any.
     * @return a {@link org.snmp4j.event.ResponseEvent} object.
     */
    public ResponseEvent get(OID oids[]) throws IOException {
        PDU pdu;

        if (SNMPversion == 3)
            pdu = new ScopedPDU();
        else
            pdu = new PDU();

        for (OID oid : oids) {
            pdu.add(new VariableBinding(oid));
        }

        pdu.setType(PDU.GET);

        ResponseEvent response;

        if (SNMPversion == 3)
            response = snmp.send(pdu, getSNMPv3Target());
        else
            response = snmp.send(pdu, getTarget());

        if (response != null) {
            PDU responsePDU = response.getResponse();
            if (responsePDU != null) {
                if (responsePDU.getErrorStatus() == PDU.noError) {
                    return response;
                }
            }
            throw new RuntimeException("reposne was null");
        }
        throw new RuntimeException("GET timed out");
    }

    /**
     * This method returns a Target, which contains information about where the data should be fetched and how.
     *
     * @return
     */

    private Target getSNMPv3Target() {
        Address targetAddress = GenericAddress.parse(address);
        UserTarget target = new UserTarget();

        target.setAddress(targetAddress);

        target.setVersion(SnmpConstants.version3); // SnmpConstants.version3
        target.setRetries(2);
        target.setTimeout(2500);
        target.setSecurityLevel(SecurityLevel.AUTH_NOPRIV); // SecurityLevel.AUTH_NOPRIV
        target.setSecurityName(new OctetString(ver3Username));

        return target;
    }

    private Target getTarget() {

        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();

        target.setCommunity(new OctetString("public"));

        target.setAddress(targetAddress);

        target.setRetries(2);

        target.setTimeout(1500);

        target.setVersion(SnmpConstants.version2c);

        return target;
    }

    /**
     * <p>main.</p>
     *
     * @param args an array of {@link java.lang.String} objects.
     * @throws java.io.IOException if any.
     */
    public static void main(String[] args) throws IOException {

        SnmpManager snmpManager = new SnmpManager("195.218.195.228/161");

        String oid1 = "1.3.6.1.2.1.1.1.0";
        snmpManager.start();
        OID oid = new OID(oid1);
        OID oids[] = new OID[]{oid};
        ResponseEvent responseEvent = snmpManager.get(oids);
        PDU response = responseEvent.getResponse();

        for (int i = 0; i < response.size(); i++) {
            VariableBinding vb1 = response.get(i);
            System.out.println(vb1.toString());
        }


        //System.out.println(responseEvent.getResponse().toString());


    }

}
