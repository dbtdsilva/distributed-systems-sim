/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Warehouse;

import Communication.CommConst;
import Communication.Proxy.ClientProxy;
import Communication.ServerComm;

/**
 *
 * @author diogosilva
 */
public class WarehouseExec {
    public static void main(String [] args) {
        ServerComm scon, sconi;                             // canais de comunicação
        ClientProxy cliProxy;                               // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerComm(CommConst.shopServerPort);    // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        Warehouse wh = new Warehouse();
        WarehouseInterface whInt = new WarehouseInterface(wh);
        System.out.println("O serviço foi estabelecido!");
        System.out.println("O servidor esta em escuta.");

        /* processamento de pedidos */
        while (true) {
            sconi = scon.accept();                     // entrada em processo de escuta
            cliProxy = new ClientProxy(sconi, whInt);  // lançamento do agente prestador do serviço
            cliProxy.start();
        }
    }
}
