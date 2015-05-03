package ServerSide.Workshop;

import Communication.CommConst;
import Communication.Proxy.ClientProxy;
import Communication.ServerComm;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This file defines the main method to run the Workshop server.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class WorkshopExec {
    public static void main(String [] args) {
        ServerComm scon, sconi;                             // canais de comunicação
        ClientProxy cliProxy;                               // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerComm(CommConst.wsServerPort);    // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        Workshop ws = new Workshop();
        WorkshopInterface wsInt = new WorkshopInterface(ws);
        System.out.println("Workshop service has started!");
        System.out.println("Server is listening.");

        /* processamento de pedidos */
        while (true) {
            try {
                sconi = scon.accept();                     // entrada em processo de escuta
                cliProxy = new ClientProxy(scon, sconi, wsInt);  // lançamento do agente prestador do serviço
                cliProxy.start();
            } catch (SocketTimeoutException ex) {
                Logger.getLogger(WorkshopExec.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
