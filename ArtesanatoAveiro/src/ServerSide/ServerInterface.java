/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide;

import Message.Message;
import Message.MessageException;

/**
 *
 * @author diogosilva
 */
public interface ServerInterface {
    public Message processAndReply (Message inMessage) throws MessageException;
}
