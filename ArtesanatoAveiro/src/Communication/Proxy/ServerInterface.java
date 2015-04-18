/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication.Proxy;

import Communication.Message.Message;
import Communication.Message.MessageException;

/**
 *
 * @author diogosilva
 */
public interface ServerInterface {
    public Message processAndReply (Message inMessage) throws MessageException;
}
