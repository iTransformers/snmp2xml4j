
/*
 * DefaultMessageDispatcherFactory.java
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

package net.itransformers.snmp2xml4j.snmptoolkit.messagedispacher;

import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityProtocols;

/**
 * <p>DefaultMessageDispatcherFactory class.</p>
 *
 * @author niau
 * @version $Id: $Id
 */
@SuppressWarnings("ALL")
public class DefaultMessageDispatcherFactory implements MessageDispatcherAbstractFactory{
    /**
     * <p>createMessageDispatcherMapping.</p>
     *
     * @return a {@link org.snmp4j.MessageDispatcher} object.
     */
    public MessageDispatcher createMessageDispatcherMapping() {
        final MessageDispatcherImpl messageDispatcher = new MessageDispatcherImpl();
        initMessageDispatcher(messageDispatcher);
        return messageDispatcher;
    }

    /**
     * <p>initMessageDispatcher.</p>
     *
     * @param logBasedMessageDispatcher a {@link org.snmp4j.MessageDispatcher} object.
     */
    private void initMessageDispatcher(MessageDispatcher logBasedMessageDispatcher) {
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv2c());
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv1());
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv3());
      SecurityProtocols.getInstance().addDefaultProtocols();
    }

}
