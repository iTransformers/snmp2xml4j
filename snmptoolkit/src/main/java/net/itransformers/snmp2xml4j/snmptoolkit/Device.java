

/*
 * Device.java
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

import java.util.Set;

/**
 * <p>Device class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
public class Device {
    private String hostname;
    private String address;
    private Set<Device> neighbours;
    private Set<String> neighbourMacAddresses;

    /**
     * <p>Constructor for Device.</p>
     *
     * @param hostname a {@link java.lang.String} object.
     * @param address a {@link java.lang.String} object.
     */
    public Device(String hostname, String address) {
        this.hostname = hostname;
        this.address = address;
    }


    /**
     * <p>Getter for the field <code>hostname</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * <p>Setter for the field <code>hostname</code>.</p>
     *
     * @param hostname a {@link java.lang.String} object.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * <p>Getter for the field <code>address</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAddress() {
        return address;
    }

    /**
     * <p>Setter for the field <code>address</code>.</p>
     *
     * @param address a {@link java.lang.String} object.
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * <p>Getter for the field <code>neighbours</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<Device> getNeighbours() {
        return neighbours;
    }

    /**
     * <p>Setter for the field <code>neighbours</code>.</p>
     *
     * @param neighbours a {@link java.util.Set} object.
     */
    public void setNeighbours(Set<Device> neighbours) {
        this.neighbours = neighbours;
    }

    /**
     * <p>getWalkXmlFileName.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getWalkXmlFileName(){
        return getHostname() + ".xml";
    }

    /**
     * <p>Getter for the field <code>neighbourMacAddresses</code>.</p>
     *
     * @return a {@link java.util.Set} object.
     */
    public Set<String> getNeighbourMacAddresses() {
        return neighbourMacAddresses;
    }

    /**
     * <p>Setter for the field <code>neighbourMacAddresses</code>.</p>
     *
     * @param neighbourMacAddresses a {@link java.util.Set} object.
     */
    public void setNeighbourMacAddresses(Set<String> neighbourMacAddresses) {
        this.neighbourMacAddresses = neighbourMacAddresses;
    }
}
