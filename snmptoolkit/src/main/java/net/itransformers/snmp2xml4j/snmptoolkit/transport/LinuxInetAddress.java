

/*
 * LinuxInetAddress.java
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

package net.itransformers.snmp2xml4j.snmptoolkit.transport;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * <p>LinuxInetAddress class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
class LinuxInetAddress {

	/**
	 * Returns an InetAddress representing the address
	 *of the localhost.
	 * Every attempt is made to find an address for this
	 *host that is not
	 * the loopback address.  If no other address can
	 *be found, the
	 * loopback will be returned.
	 *
	 * @return InetAddress - the address of localhost
	 * @throws java.net.UnknownHostException - if there is a
	 *problem determing the address
	 */
	public static InetAddress getLocalHost() throws
UnknownHostException {
		InetAddress localHost =
InetAddress.getLocalHost();
		if(!localHost.isLoopbackAddress()) return
localHost;
		InetAddress[] addrs =
getAllLocalUsingNetworkInterface();
		for (InetAddress addr : addrs) {
			if (!addr.isLoopbackAddress())
				return addr;
		}
		return localHost;
	}

	/**
	 * This method attempts to find all InetAddresses
	 *for this machine in a
	 * conventional way (via InetAddress).  If only one
	 *address is found
	 * and it is the loopback, an attempt is made to
	 *determine the addresses
	 * for this machine using NetworkInterface.
	 *
	 * @return InetAddress[] - all addresses assigned to
	 *the local machine
	 * @throws java.net.UnknownHostException - if there is a
	 *problem determining addresses
	 */
	public static InetAddress[] getAllLocal() throws
UnknownHostException {
		InetAddress[] iAddresses =
InetAddress.getAllByName("127.0.0.1");
		if(iAddresses.length != 1) return
iAddresses;
		if(!iAddresses[0].isLoopbackAddress())
return iAddresses;
		return getAllLocalUsingNetworkInterface();

	}

	/**
	 * Utility method that delegates to the methods of
NetworkInterface to
	 * determine addresses for this machine.
	 *
	 * @return InetAddress[] - all addresses found from
the NetworkInterfaces
	 * @throws UnknownHostException - if there is a
problem determining addresses
	 */
	private static InetAddress[]
getAllLocalUsingNetworkInterface() throws
UnknownHostException {
		ArrayList addresses = new ArrayList();
		Enumeration e;
		try {
			e =
NetworkInterface.getNetworkInterfaces();
		} catch (SocketException ex) {
			throw new UnknownHostException
("127.0.0.1");
		}
		while(e.hasMoreElements()) {
			NetworkInterface ni =
(NetworkInterface)e.nextElement();
			for(Enumeration e2 =
ni.getInetAddresses(); e2.hasMoreElements();) {
				addresses.add
(e2.nextElement());
			}
		}
		InetAddress[] iAddresses = new
InetAddress[addresses.size()];
		for(int i=0; i<iAddresses.length; i++) {
			iAddresses[i] = (InetAddress)
addresses.get(i);
		}
		return iAddresses;
	}

    // Determine localhostaddress depending of the destination
    /**
     * <p>getLocalHostAddress.</p>
     *
     * @param intendedDestination a {@link java.net.InetAddress} object.
     * @return a {@link java.net.InetAddress} object.
     * @throws java.net.SocketException if any.
     */
    public static InetAddress getLocalHostAddress(InetAddress intendedDestination)
        throws SocketException
    {
        DatagramSocket sock = new DatagramSocket();
        sock.getLocalPort();
        sock.connect(intendedDestination,sock.getLocalPort());
        return sock.getLocalAddress();
    }

}
