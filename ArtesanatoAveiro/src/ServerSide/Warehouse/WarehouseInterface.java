/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Warehouse;

import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Proxy.ServerInterface;

/**
 *
 * @author diogosilva
 */
public class WarehouseInterface implements ServerInterface {
    Warehouse wh;
    public WarehouseInterface(Warehouse wh) {
        this.wh = wh;
    }
    
    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;
        
        switch (inMessage.getType()) {
            case VISIT_SUPPLIERS:
                break;
            default:
                break;
        }
        return outMessage;
    }
}
