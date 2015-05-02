/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Warehouse;

import Communication.CommConst;
import Communication.Proxy.ClientProxy;
import Communication.ServerComm;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diogosilva
 */
public class WarehouseExec {
    public static void main(String [] args) {
        ServerComm scon, sconi;                             // canais de comunicação
        ClientProxy cliProxy;                               // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerComm(CommConst.whServerPort);    // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        Warehouse wh = new Warehouse();
        WarehouseInterface whInt = new WarehouseInterface(wh);
        System.out.println("Warehouse service has started!");
        System.out.println("Server is listening.");

        /* processamento de pedidos */
        while (true) {
            try {
                sconi = scon.accept();                     // entrada em processo de escuta
                cliProxy = new ClientProxy(scon, sconi, whInt);  // lançamento do agente prestador do serviço
                cliProxy.start();
            } catch (SocketTimeoutException ex) {
                Logger.getLogger(WarehouseExec.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
