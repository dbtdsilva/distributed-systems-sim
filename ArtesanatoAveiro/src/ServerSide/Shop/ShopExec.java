/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Shop;

import Communication.CommConst;
import Communication.Proxy.ClientProxy;
import Communication.ServerComm;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 *
 * @author diogosilva
 */
public class ShopExec {

    public static void main(String[] args) throws SocketException {
        ServerComm scon, sconi;                             // canais de comunicação
        ClientProxy cliProxy;                               // thread agente prestador do serviço

        /* estabelecimento do servico */
        scon = new ServerComm(CommConst.shopServerPort);    // criação do canal de escuta e sua associação
        scon.start();                                       // com o endereço público
        Shop shop = new Shop();
        ShopInterface shopInt = new ShopInterface(shop);
        System.out.println("Shop service has started!");
        System.out.println("Server is listening.");

        /* processamento de pedidos */
        while (true) {
            //scon.setTimeout(500);
            try {
                sconi = scon.accept();                         // entrada em processo de escuta
                cliProxy = new ClientProxy(scon, sconi, shopInt);    // lançamento do agente prestador do serviço
                cliProxy.start();
            } catch (SocketTimeoutException ex) {
                System.exit(0);
            }
        }
    }
}
