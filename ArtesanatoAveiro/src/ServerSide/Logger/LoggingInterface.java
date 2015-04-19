/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Logger;

import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Proxy.ServerInterface;

/**
 *
 * @author diogosilva
 */
public class LoggingInterface implements ServerInterface {
    private final Logging log;
    
    public LoggingInterface(Logging log) {
        this.log = log;
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;
        
        switch (inMessage.getType()) {
            default:
                System.out.println("Mensagem inválida recebida: " + inMessage);
                break;
        }
        return outMessage;
    }
}
