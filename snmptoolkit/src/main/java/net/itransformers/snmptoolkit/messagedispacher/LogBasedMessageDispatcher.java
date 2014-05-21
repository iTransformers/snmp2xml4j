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

public class LogBasedMessageDispatcher extends MessageDispatcherImpl implements MessageDispatcher{
    private int nextTransactionID;

    public LogBasedMessageDispatcher(int nextTransactionID) {
        this.nextTransactionID = nextTransactionID;
    }

    public synchronized int getNextRequestID() {
      int nextID = nextTransactionID++;
      if (nextID <= 0) {
        nextID = 1;
        nextTransactionID = 2;
      }
      return nextID;
    }

}
