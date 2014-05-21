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

package net.itransformers.snmptoolkit.transport;

import org.apache.log4j.Logger;
import org.snmp4j.SNMP4JSettings;
import org.snmp4j.TransportMapping;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.transport.UdpTransportMapping;
import org.snmp4j.util.WorkerTask;

import java.io.*;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogBasedTransportMapping extends UdpTransportMapping implements TransportMapping {
    public static Pattern p = Pattern.compile("^(.*DefaultUdpTransportMapping.*Received message from /(.*) with length.*: )(.*)$");

    static Logger logger = Logger.getLogger(DefaultUdpTransportMapping.class);

    protected WorkerTask listener;
    protected ListenThread listenerThread;

    private int receiveBufferSize = 0; // not set by default
    private BufferedReader reader;

    /**
     * Creates a UDP transport with an arbitrary local port on all local
     * interfaces.
     *
     * @throws IOException if socket binding fails.
     */
    public LogBasedTransportMapping(BufferedReader reader, TransportIpAddress transportIpAddress) throws IOException {
        super(new UdpAddress(InetAddress.getLocalHost(), 0));
        this.reader = reader;
    }

    public void sendMessage(Address targetAddress, byte[] message)
            throws java.io.IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending message to " + this.udpAddress + " with length " +
                    message.length + ": " +
                    new OctetString(message).toHexString());
        }
    }

    /**
     * Closes the socket and stops the listener thread.
     *
     * @throws IOException
     */
    public void close() throws IOException {
        boolean interrupted = false;
        WorkerTask l = listener;
        if (l != null) {
            l.terminate();
            l.interrupt();
            try {
                l.join();
            } catch (InterruptedException ex) {
                interrupted = true;
                logger.warn(ex);
            }
        }
        listener = null;
        if (interrupted) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Starts the listener thread that accepts incoming messages. The thread is
     * started in daemon mode and thus it will not block application terminated.
     * Nevertheless, the {@link #close()} method should be called to stop the
     * listen thread gracefully and free associated ressources.
     *
     * @throws IOException
     */
    public synchronized void listen() throws IOException {
        if (listener != null) {
            throw new SocketException("Port already listening");
        }
        listenerThread = new ListenThread();
        listener = SNMP4JSettings.getThreadFactory().createWorkerThread(
                "DefaultUDPTransportMapping_" + getAddress(), listenerThread, true);
        listener.run();
    }

    /**
     * Changes the priority of the listen thread for this UDP transport mapping.
     * This method has no effect, if called before {@link #listen()} has been
     * called for this transport mapping.
     *
     * @param newPriority the new priority.
     * @see Thread
     * @since 1.2.2
     */
    public void setPriority(int newPriority) {
        WorkerTask lt = listener;
        if (lt instanceof Thread) {
            ((Thread) lt).setPriority(newPriority);
        }
    }

    /**
     * Returns the priority of the internal listen thread.
     *
     * @return a value between {@link Thread#MIN_PRIORITY} and
     *         {@link Thread#MAX_PRIORITY}.
     * @since 1.2.2
     */
    public int getPriority() {
        WorkerTask lt = listener;
        if (lt instanceof Thread) {
            return ((Thread) lt).getPriority();
        } else {
            return Thread.NORM_PRIORITY;
        }
    }

    /**
     * Sets the name of the listen thread for this UDP transport mapping.
     * This method has no effect, if called before {@link #listen()} has been
     * called for this transport mapping.
     *
     * @param name the new thread name.
     * @since 1.6
     */
    public void setThreadName(String name) {
        WorkerTask lt = listener;
        if (lt instanceof Thread) {
            ((Thread) lt).setName(name);
        }
    }

    /**
     * Returns the name of the listen thread.
     *
     * @return the thread name if in listening mode, otherwise <code>null</code>.
     * @since 1.6
     */
    public String getThreadName() {
        WorkerTask lt = listener;
        if (lt instanceof Thread) {
            return ((Thread) lt).getName();
        } else {
            return null;
        }
    }

    public void setMaxInboundMessageSize(int maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }

    /**
     * Gets the requested receive buffer size for the underlying UDP socket.
     * This size might not reflect the actual size of the receive buffer, which
     * is implementation specific.
     *
     * @return <=0 if the default buffer size of the OS is used, or a value >0 if the
     *         user specified a buffer size.
     */
    public int getReceiveBufferSize() {
        return receiveBufferSize;
    }

    /**
     * Sets the receive buffer size, which should be > the maximum inbound message
     * size. This method has to be called before {@link #listen()} to be
     * effective.
     *
     * @param receiveBufferSize an integer value >0 and > {@link #getMaxInboundMessageSize()}.
     */
    public void setReceiveBufferSize(int receiveBufferSize) {
        if (receiveBufferSize <= 0) {
            throw new IllegalArgumentException("Receive buffer size must be > 0");
        }
        this.receiveBufferSize = receiveBufferSize;
    }

    public boolean isListening() {
        return (listener != null);
    }

    class ListenThread implements WorkerTask {

        private byte[] buf;
        private volatile boolean stop = false;


        public ListenThread() throws SocketException {
            buf = new byte[getMaxInboundMessageSize()];
        }

        public void run() {
            logger.debug("UDP receive buffer size for socket " +getAddress() + " is set to: ...");
            ByteBuffer bis;
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
                throw new RuntimeException(e.getMessage(), e);
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

            // If messages are processed asynchronously (i.e. multi-threaded)
            // then we have to copy the buffer's content here!
            if (isAsyncMsgProcessingSupported()) {
                byte[] bytes = new byte[packet.length];
                System.arraycopy(packet, 0, bytes, 0, bytes.length);
                bis = ByteBuffer.wrap(bytes);
            } else {
                bis = ByteBuffer.wrap(packet);
            }
            fireProcessMessage(new UdpAddress(addr), bis);
            synchronized (LogBasedTransportMapping.this) {
                listener = null;
                stop = true;
                if (logger.isDebugEnabled()) {
                    logger.debug("Worker task stopped:" + getClass().getName());
                }
            }
        }

        public void close() {
            stop = true;
        }

        public void terminate() {
            close();
            if (logger.isDebugEnabled()) {
                logger.debug("Terminated worker task: " + getClass().getName());
            }
        }

        public void join() throws InterruptedException {
            if (logger.isDebugEnabled()) {
                logger.debug("Joining worker task: " + getClass().getName());
            }
        }

        public void interrupt() {
            if (logger.isDebugEnabled()) {
                logger.debug("Interrupting worker task: " + getClass().getName());
            }
            close();
        }
    }
}
