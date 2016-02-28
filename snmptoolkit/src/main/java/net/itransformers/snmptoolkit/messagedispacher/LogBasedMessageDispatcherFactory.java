
/*
 * LogBasedMessageDispatcherFactory.java
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

package net.itransformers.snmptoolkit.messagedispacher;

import org.apache.log4j.Logger;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityProtocols;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogBasedMessageDispatcherFactory implements MessageDispatcherAbstractFactory{
    private File file;
    public static Pattern p = Pattern.compile("^(.*Running pending sync request with handle PduHandle\\[(.*)\\] and retry count left)(.*)$");

    private int instNum = 1;
    private BufferedReader reader;
    static Logger logger = Logger.getLogger(LogBasedMessageDispatcherFactory.class);

    public LogBasedMessageDispatcherFactory(File file) {
        this.file = file;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
    }

    public MessageDispatcher createMessageDispatcherMapping() {
        int id = getNextId();
        logger.debug("Creating new message dispatcher id="+id);
        final LogBasedMessageDispatcher logBasedMessageDispatcher = new LogBasedMessageDispatcher(id);
        initMessageDispatcher(logBasedMessageDispatcher);
        return logBasedMessageDispatcher;
    }
    private int getNextId(){
        String msgIdStr = null;
        String s;
        int i = 0;
        try {
            while ((s = reader.readLine()) != null) {
                Matcher m = p.matcher(s);
                if (m.find()) {
                    i++;
                    if (i == instNum) {
                        msgIdStr = m.group(2);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        return Integer.parseInt(msgIdStr);

    }

    protected final void initMessageDispatcher(MessageDispatcher logBasedMessageDispatcher) {
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv2c());
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv1());
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv3());
      SecurityProtocols.getInstance().addDefaultProtocols();
    }

}
