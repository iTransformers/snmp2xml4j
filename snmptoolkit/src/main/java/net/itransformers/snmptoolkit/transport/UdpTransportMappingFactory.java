

/*
 * Copyright (c) 2014. iTransformers Labs http://itransformers.net
 *
 * snmp2xml is an open source tool written by Vasil Yordanov and Nikolay Milovanov
 * in JAVA programing languadge.
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
