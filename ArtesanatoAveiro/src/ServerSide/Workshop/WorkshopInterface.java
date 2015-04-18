package ServerSide.Workshop;

import Message.Message;
import Message.MessageException;

/**
 *
 * @author guesswho
 */
public class WorkshopInterface implements ServerSide.ServerInterface{

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
                //TODO
                break;
            
            case REPLENISH_STOCK:
                if(inMessage.getnMaterials() < 0) {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else    {
                    //TODO
                }
                break;
            
            case COLLECTING_MATERIALS:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    boolean mats = ws.collectingMaterials(inMessage.getId());
                    outMessage = new Message();
                }
                break;
            
            case GO_TO_STORE:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    //TODO
                }
                break;
            
            case BACK_TO_WORK:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    //TODO
                }
                break;
            
            case PREPARE_TO_PRODUCE:
                if(inMessage.getId() < 0)   {
                    throw new MessageException("Invalid argument", inMessage);
                } 
                else   {
                    //TODO
                }
                break;
                
            default:
                throw new MessageException("Invalid message type!", inMessage);
        }
        
        return outMessage;
    }
    
}