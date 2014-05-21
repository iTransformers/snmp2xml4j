
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
