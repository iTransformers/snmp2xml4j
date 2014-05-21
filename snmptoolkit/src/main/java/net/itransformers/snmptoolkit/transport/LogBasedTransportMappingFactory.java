

package net.itransformers.snmptoolkit.transport;

import org.apache.log4j.Logger;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.TransportIpAddress;

import java.io.*;

public class LogBasedTransportMappingFactory implements TransportMappingAbstractFactory{
    private File log;
    private BufferedReader reader;
    static Logger logger = Logger.getLogger(LogBasedTransportMappingFactory.class);

    public LogBasedTransportMappingFactory(File log) {
        this.log = log;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(log)));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }

    }

    public TransportMapping createTransportMapping(TransportIpAddress transportIpAddress) throws IOException {
        LogBasedTransportMapping logBasedTransportMapping1 = new LogBasedTransportMapping(reader, transportIpAddress);
        return logBasedTransportMapping1;
    }
}
