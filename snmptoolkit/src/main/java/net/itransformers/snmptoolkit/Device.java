

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
