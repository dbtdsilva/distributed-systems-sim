package ServerSide.Warehouse;

import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;
import Communication.ServerComm;
import java.net.SocketException;

/**
 * This file defines the warehouse implementation of the server interface.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class WarehouseInterface implements ServerInterface {
    /**
     * Warehouse entity.
     */
    private final Warehouse wh;
    
    /**
     * Tells the service if it can end or not.
     */
    private boolean serviceEnded;
    
    /**
     * Constructor for the warehouse server.
     * @param wh Warehouse entity
     */
    public WarehouseInterface(Warehouse wh) {
        this.wh = wh;
        this.serviceEnded = false;
    }
    
    /**
     * Processes the received messages and replies to the entity that sent it.
     * 
     * @param inMessage The received message.
     * @param scon Server communication.
     * @return Returns the reply to the received message.
     * @throws MessageException
     * @throws SocketException
     */
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

    /**
     * Tell the service if it is allowed to end or not.
     * @return True if the system can terminate, false otherwise.
     */
    @Override
    public boolean serviceEnded() {
        return serviceEnded;
    }
}
