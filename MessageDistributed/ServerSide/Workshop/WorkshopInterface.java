package ServerSide.Workshop;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;
import Communication.ServerComm;
import java.net.SocketException;

/**
 * This file defines the workshop implementation of the server interface.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class WorkshopInterface implements ServerInterface {

    /**
     * Workshop entity.
     */
    private Workshop ws;
    
    /**
     * Tells the service if it can end or not.
     */
    private boolean serviceEnded;
    
    /**
     * Constructor for the workshop server.
     * @param ws Workshop entity.
     */
    public WorkshopInterface(Workshop ws) {
        this.ws = ws;
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
        
        switch(inMessage.getType())
        {
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                serviceEnded = true;
                break;
            case GO_TO_WORKSHOP:
                int prods = ws.goToWorkshop();
                outMessage = new Message(MessageType.ACK, EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS, prods);
                break;
            
            case REPLENISH_STOCK:
                if(inMessage.getnMaterials() < 0) {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else    {
                    int mats = inMessage.getnMaterials();
                    ws.replenishStock(mats);
                    outMessage = new Message(MessageType.ACK, EntrepreneurState.DELIVERING_PRIME_MATERIALS);
                }
                break;
            
            case COLLECTING_MATERIALS:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    boolean mats = ws.collectingMaterials(inMessage.getId());
                    outMessage = new Message(mats ? MessageType.POSITIVE : MessageType.NEGATIVE, 
                            CraftsmanState.FETCHING_PRIME_MATERIALS, inMessage.getId()); /**/
                }
                break;
            
            case GO_TO_STORE:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    int id = inMessage.getId();
                    int nProdsStored = ws.goToStore(id);
                    outMessage = new Message(MessageType.ACK, CraftsmanState.PRODUCING_A_NEW_PIECE, id, nProdsStored);
                }
                break;
            
            case BACK_TO_WORK:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    ws.backToWork(inMessage.getId());
                    outMessage = new Message(MessageType.ACK, CraftsmanState.FETCHING_PRIME_MATERIALS, inMessage.getId());
                }
                break;
            
            case PREPARE_TO_PRODUCE:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    ws.prepareToProduce(inMessage.getId());
                    outMessage = new Message(MessageType.ACK, CraftsmanState.PRODUCING_A_NEW_PIECE, inMessage.getId());
                }
                break;
                
            default:
                throw new MessageException("Invalid message type!", inMessage);
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
