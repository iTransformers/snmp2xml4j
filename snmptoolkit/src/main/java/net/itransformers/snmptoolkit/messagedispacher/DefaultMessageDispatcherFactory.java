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

package net.itransformers.snmptoolkit.messagedispacher;

import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityProtocols;

public class DefaultMessageDispatcherFactory implements MessageDispatcherAbstractFactory{
    public MessageDispatcher createMessageDispatcherMapping() {
        final MessageDispatcherImpl messageDispatcher = new MessageDispatcherImpl();
        initMessageDispatcher(messageDispatcher);
        return messageDispatcher;
    }

    protected final void initMessageDispatcher(MessageDispatcher logBasedMessageDispatcher) {
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv2c());
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv1());
      logBasedMessageDispatcher.addMessageProcessingModel(new MPv3());
      SecurityProtocols.getInstance().addDefaultProtocols();
    }

}
