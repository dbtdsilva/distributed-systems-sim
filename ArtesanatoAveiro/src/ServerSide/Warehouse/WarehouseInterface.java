/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Warehouse;

import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;
import Communication.ServerComm;
import java.net.SocketException;

/**
 *
 * @author diogosilva
 */
public class WarehouseInterface implements ServerInterface {
    private final Warehouse wh;
    private boolean serviceEnded;
    
    public WarehouseInterface(Warehouse wh) {
        this.wh = wh;
        this.serviceEnded = false;
    }
    
    @Override
    public Message processAndReply(Message inMessage, ServerComm scon) throws MessageException, SocketException {
        Message outMessage = null;
        
        switch (inMessage.getType()) {
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                serviceEnded = true;
                break;
            case VISIT_SUPPLIERS:
                int n = wh.visitSuppliers();
                outMessage = new Message(MessageType.ACK, EntrepreneurState.AT_THE_SUPPLIERS, n);
                break;
            default:
                break;
        }
        return outMessage;
    }

    @Override
    public boolean serviceEnded() {
        return serviceEnded;
    }
}
