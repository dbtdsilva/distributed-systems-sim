package Communication.Proxy;

import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.ServerComm;
import java.net.SocketException;

/**
 * This file defines the server interface.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public interface ServerInterface {
    /**
     * Processes the received messages and replies to the entity that sent it.
     * 
     * @param inMessage The received message.
     * @param scon Server communication.
     * @return Returns the reply to the received message.
     * @throws MessageException
     * @throws SocketException 
     */
    public Message processAndReply (Message inMessage, ServerComm scon) throws MessageException, SocketException;
    
    /**
     * Tell the service if it is allowed to end or not.
     * @return True if the system can terminate, false otherwise.
     */
    public boolean serviceEnded();
}
