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

import java.util.Set;

public class Device {
    private String hostname;
    private String address;
    private Set<Device> neighbours;
    private Set<String> neighbourMacAddresses;

    public Device(String hostname, String address) {
        this.hostname = hostname;
        this.address = address;
    }


    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Device> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Set<Device> neighbours) {
        this.neighbours = neighbours;
    }

    public String getWalkXmlFileName(){
        return getHostname() + ".xml";
    }

    public Set<String> getNeighbourMacAddresses() {
        return neighbourMacAddresses;
    }

    public void setNeighbourMacAddresses(Set<String> neighbourMacAddresses) {
        this.neighbourMacAddresses = neighbourMacAddresses;
    }
}
