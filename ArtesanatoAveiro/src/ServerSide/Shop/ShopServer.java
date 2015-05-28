/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Shop;

import Static.Constants.RegistryConst;
import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 *
 * @author diogosilva
 */
public class ShopServer {
    static Scanner in = new Scanner(System.in);
    public static void main(String[] args) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        System.out.print("Nome do nó de processamento onde está localizado o serviço de registo? ");
        rmiRegHostName = in.nextLine();
        System.out.print("Número do port de escuta do serviço de registo? ");
        rmiRegPortNumb = in.nextInt();
        
        /* localização por nome do objecto remoto no serviço de registos RMI */
        LoggingInterface loggingInt = null;             // interface da barbearia (objecto remoto)

        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            loggingInt = (LoggingInterface) registry.lookup (RegistryConst.logNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Excepção na localização da barbearia: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("O logging não está registado: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        Shop shop = null;                              // barbearia (representa o objecto remoto)
        ShopInterface shopInterface = null;             // interface da barbearia
        shop = new Shop(loggingInt);
        try {
            shopInterface = (ShopInterface) UnicastRemoteObject.exportObject(shop, 22000);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para a barbearia: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para a barberaria foi gerado!");

        /* seu registo no serviço de registo RMI */
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

        try {
            registry.bind(RegistryConst.shopNameEntry, shopInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo da loja: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("A loja já está registada: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("A loja foi registada!");
    }
}
