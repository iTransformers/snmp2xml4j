

/*
 * LogBasedTransportMappingFactory.java
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
