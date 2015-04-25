/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication.Proxy;

import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.ServerComm;

/**
 *
 * @author diogosilva
 * @author tania
 */
/**
 * Este tipo de dados define o thread agente prestador de serviço para uma
 * solução do Problema dos Barbeiros Sonolentos que implementa o modelo
 * cliente-servidor de tipo 2 (replicação do servidor) com lançamento estático
 * dos threads barbeiro. A comunicação baseia-se em passagem de mensagens sobre
 * sockets usando o protocolo TCP.
 */
public class ClientProxy extends Thread {

    /**
     * Contador de threads lançados
     *
     * @serialField nProxy
     */

    private static int nProxy;

    /**
     * Canal de comunicação
     *
     * @serialField sconi
     */
    private final ServerComm sconi;

    /**
     * Interface à barbearia
     *
     * @serialField bShopInter
     */
    private final ServerInterface sInterface;

    /**
     * Instanciação do interface à barbearia.
     *
     * @param sconi canal de comunicação
     * @param sInterface interface à barbearia
     */
    public ClientProxy(ServerComm sconi, ServerInterface sInterface) {
        super("Proxy_" + getProxyId());

        this.sconi = sconi;
        this.sInterface = sInterface;
    }

    /**
     * Ciclo de vida do thread agente prestador de serviço.
     */
    @Override
    public void run() {
        Message inMessage = null, // mensagem de entrada
                outMessage = null;                      // mensagem de saída

        inMessage = (Message) sconi.readObject();                     // ler pedido do cliente
        try 
        {
            outMessage = sInterface.processAndReply(inMessage);         // processá-lo
        } catch (MessageException e) 
        {
            System.out.println("Thread " + getName() + ": " + e.getMessage() + "!");
            System.out.println(e.getMessageVal().toString());
            System.exit(1);
        }
        sconi.writeObject(outMessage);                                // enviar resposta ao cliente
        sconi.close();                                                // fechar canal de comunicação
        
        if (inMessage.getType() == MessageType.TERMINATE) {
            System.out.println("Terminated service.");
            System.exit(0);
        }
    }

    /**
     * Geração do identificador da instanciação.
     *
     * @return identificador da instanciação
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
