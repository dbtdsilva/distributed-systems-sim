/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Shop;

import Message.Message;
import Message.MessageException;
import ServerSide.ServerInterface;

/**
 *
 * @author diogosilva
 */
public class ShopInterface implements ServerInterface {
    private Shop shop;
    
    public ShopInterface(Shop shop) {
        
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
