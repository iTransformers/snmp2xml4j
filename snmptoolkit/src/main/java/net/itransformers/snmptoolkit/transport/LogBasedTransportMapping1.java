

package net.itransformers.snmptoolkit.transport;

import org.apache.log4j.Logger;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.AbstractTransportMapping;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogBasedTransportMapping1 extends AbstractTransportMapping implements TransportMapping {
    public static Pattern p = Pattern.compile("^(.*DefaultUdpTransportMapping.*Received message from /(.*) with length.*: )(.*)$");
    static Logger logger = Logger.getLogger(LogBasedTransportMapping1.class);
    protected TransportIpAddress udpAddress;
    private boolean isListening = false;
    private Thread listeningThread;
    private BufferedReader reader;

    public LogBasedTransportMapping1(BufferedReader reader, TransportIpAddress transportIpAddress) {
        this.udpAddress = transportIpAddress;
        this.reader = reader;
    }

    public Class getSupportedAddressClass() {
        return UdpAddress.class;
    }

    public Address getListenAddress() {
        return udpAddress;
    }

    @Override
    public void close() throws IOException {
        isListening = false;
    }

    @Override
    public void listen() throws IOException {
        isListening = true;
        logger.debug("UDP receive buffer size for socket " +udpAddress + " is set to: ...");
    }

    public boolean isListening() {
        return isListening;
    }

    @Override
    public void sendMessage(Address address, byte[] message) throws IOException {
//        ByteBuffer buf = ByteBuffer.wrap(message);
//        BERInputStream is = new BERInputStream(buf);
//        BER.MutableByte pduType = new BER.MutableByte();
//        int length = BER.decodeHeader(is, pduType);
//        Integer32 requestID = new Integer32();
//        requestID.decodeBER(is);

          logger.debug("Sending message to "+this.udpAddress+" with length "+
                       message.length+": "+
                       new OctetString(message).toHexString());
        listeningThread = createTrehad();
        listeningThread.run();
    }

    private Thread createTrehad() {
        return new Thread(){
            @Override
            public void run() {
                isListening = true;
                String msg = null;
                String addr = null;
                String s;
                try {
                    while ((s = reader.readLine()) != null) {
                        Matcher m = p.matcher(s);
                        if (m.find()) {
    //                String text =  m.group(1);
                            addr = m.group(2);
                            msg = m.group(3);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
                if (msg == null) {
                    return;
                }
                final OctetString message = OctetString.fromHexString(msg, ':');
                byte[] packet = message.getValue();
                logger.debug("Received message from "+addr+
                             " with length "+packet.length+": "+
                             new OctetString(packet, 0,
                                             packet.length).toHexString());


                fireProcessMessage(new UdpAddress(addr), ByteBuffer.wrap(message.getValue()));
            }
        };
    }
}
