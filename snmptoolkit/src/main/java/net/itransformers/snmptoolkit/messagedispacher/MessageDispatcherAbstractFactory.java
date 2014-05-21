

package net.itransformers.snmptoolkit.messagedispacher;

import org.snmp4j.MessageDispatcher;

public interface MessageDispatcherAbstractFactory {
    MessageDispatcher createMessageDispatcherMapping();
}
