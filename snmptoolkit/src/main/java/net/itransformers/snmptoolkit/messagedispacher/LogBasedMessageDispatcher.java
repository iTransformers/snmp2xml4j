

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
