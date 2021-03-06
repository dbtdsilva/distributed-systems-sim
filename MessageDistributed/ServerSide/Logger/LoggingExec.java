/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Logger;

import Communication.CommConst;
import Communication.ProbConst;
import Communication.Proxy.ClientProxy;
import Communication.ServerComm;
import java.io.IOException;

/**
 * This file defines the main method to run the Logging server.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class LoggingExec {
    public static void main(String [] args) throws IOException {
        ServerComm scon, sconi;                             // canais de comunicação
        ClientProxy cliProxy;                               // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerComm(CommConst.loggServerPort);    // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        Logging log = new Logging("", ProbConst.nCustomers, 
                ProbConst.nCraftsmen, ProbConst.nPrimeMaterials);
        LoggingInterface logInt = new LoggingInterface(log);
        System.out.println("Logging service has started!");
        System.out.println("Server is listening.");
        
        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                         // entrada em processo de escuta
            cliProxy = new ClientProxy(scon, sconi, logInt);     // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}