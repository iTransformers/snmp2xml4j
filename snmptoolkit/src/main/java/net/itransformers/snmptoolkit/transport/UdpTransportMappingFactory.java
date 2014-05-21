

package net.itransformers.snmptoolkit.transport;

import org.snmp4j.TransportMapping;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

public class UdpTransportMappingFactory implements TransportMappingAbstractFactory {
    public TransportMapping createTransportMapping(TransportIpAddress transportIpAddress) throws IOException {
        AbstractTransportMapping transport = new DefaultUdpTransportMapping((UdpAddress) transportIpAddress);
        return transport;
    }
}
