package Communication.Proxy;

import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.ServerComm;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This data type defines the service thread for a server-client architecture.
 * The communication is based on messages over TCP sockets.
 * 
 * @author Prof.Rui Borges
 */

public class ClientProxy extends Thread {

    /**
     * Counter for the launched threads.
     *
     * @serialField nProxy
     */
    private static int nProxy;

    /**
     * Communication channel.
     *
     * @serialField sconi
     */
    private final ServerComm sconi;

    /**
     * Server interface.
     *
     * @serialField sInterface
     */
    private final ServerInterface sInterface;
    
    /**
     * Server communication.
     * 
     * @serialField scon
     */
    private final ServerComm scon;

    /**
     * Server interface instantiations.
     *
     * @param scon server communication
     * @param sconi communication channel
     * @param sInterface server interface
     */
    public ClientProxy(ServerComm scon, ServerComm sconi, ServerInterface sInterface) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.sInterface = sInterface;
        this.scon = scon;
    }

    /**
     * Agent's life cycle.
     */
    @Override
    public void run() {
        Message inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject();                     // ler pedido do cliente
        try 
        {
            outMessage = sInterface.processAndReply(inMessage, scon);         // processá-lo
        } catch (MessageException e) 
        {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit(1);
        } catch (SocketException ex) {
            Logger.getLogger(ClientProxy.class.getName()).log(Level.SEVERE, null, ex);
        }
        sconi.writeObject(outMessage);                                // enviar resposta ao cliente
        sconi.close();                                                // fechar canal de comunicação
        
        if(sInterface.serviceEnded())
        {
            System.out.println("Closing service ... Done!");
            System.exit(0);
        }
    }

    /**
     * Instantiation identifier generator.
     *
     * @return instantiation identifier
     */
    private static int getProxyId() {
        // representação do tipo de dados ClientProxy na máquina virtual de java
        Class<ClientProxy> cl = null;
        int proxyId;                              // identificador da instanciação

        try {
            cl = (Class<ClientProxy>) Class.forName("Communication.Proxy.ClientProxy");
        } catch (ClassNotFoundException e) {
            System.out.println("O tipo de dados ClientProxy não foi encontrado!");
            System.exit(1);
        }

        synchronized (cl) {
            proxyId = nProxy;
            nProxy += 1;
        }

        return proxyId;
    }
}
