package ServerSide.Workshop;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;

/**
 *
 * @author guesswho
 */
public class WorkshopInterface implements ServerInterface {

    private Workshop ws;
    
    public WorkshopInterface(Workshop ws)
    {
        this.ws = ws;
    }
    
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        
        Message outMessage = null;
        
        switch(inMessage.getType())
        {
            case GO_TO_WORKSHOP:
                int prods = ws.goToWorkshop();
                outMessage = new Message(MessageType.GO_TO_WORKSHOP, EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS, prods);
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
                    outMessage = new Message(MessageType.POSITIVE, CraftsmanState.FETCHING_PRIME_MATERIALS, inMessage.getId()); /**/
                }
                break;
            
            case GO_TO_STORE:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    int id = inMessage.getId();
                    int nProdsStored = ws.goToStore(id);
                    outMessage = new Message(MessageType.POSITIVE, CraftsmanState.PRODUCING_A_NEW_PIECE, id, nProdsStored);
                }
                break;
            
            case BACK_TO_WORK:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    ws.backToWork(inMessage.getId());
                    outMessage = new Message(MessageType.POSITIVE, CraftsmanState.FETCHING_PRIME_MATERIALS, inMessage.getId());
                }
                break;
            
            case PREPARE_TO_PRODUCE:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    ws.prepareToProduce(inMessage.getId());
                    outMessage = new Message(MessageType.POSITIVE, CraftsmanState.PRODUCING_A_NEW_PIECE, inMessage.getId());
                }
                break;
                
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }
        
        return outMessage;
    }
    
}
