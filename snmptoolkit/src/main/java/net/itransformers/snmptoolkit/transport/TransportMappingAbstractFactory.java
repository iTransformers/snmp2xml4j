

package net.itransformers.snmptoolkit.transport;

import org.snmp4j.TransportMapping;
import org.snmp4j.smi.TransportIpAddress;

import java.io.IOException;

public interface TransportMappingAbstractFactory {
    TransportMapping createTransportMapping(TransportIpAddress transportIpAddress) throws IOException;
}
