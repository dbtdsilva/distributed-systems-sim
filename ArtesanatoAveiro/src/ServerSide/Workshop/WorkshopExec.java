/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Workshop;

import Communication.CommConst;
import Communication.Proxy.ClientProxy;
import Communication.ServerComm;

/**
 *
 * @author diogosilva
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
            sconi = scon.accept();                     // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, wsInt);  // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
