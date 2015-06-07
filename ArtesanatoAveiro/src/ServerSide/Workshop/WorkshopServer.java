/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Workshop;

import Structures.Constants.RegistryConfig;
import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the main class for the server Workshop.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class WorkshopServer {  
    /**
     * The main class for the workshop server.
     * @param args No arguments are going to be used.
     */
    public static void main(String[] args) {
        String rmiRegHostName;
        int rmiRegPortNumb;
        
        RegistryConfig rc = new RegistryConfig("../../config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        
        LoggingInterface loggingInt = null;             
        ShopInterface shopInt = null;

        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            loggingInt = (LoggingInterface) registry.lookup (RegistryConfig.logNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating workshop: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Workshop is not registered: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        try {
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            shopInt = (ShopInterface) registry.lookup (RegistryConfig.shopNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while exporting workshop interface: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ShopReg is not registered: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        Workshop workshop = null;                              // barbearia (representa o objecto remoto)
        WorkshopInterface wsInterface = null;             // interface da barbearia
        workshop = new Workshop(loggingInt, shopInt);
        
        try {
            wsInterface = (WorkshopInterface) UnicastRemoteObject.exportObject(workshop, rc.workshopPort());
        } catch (RemoteException e) {
            System.out.println("Exception thrown while exporting workshop interface: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para a oficina foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.workshopNameEntry;
        Registry registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Excepção na criação do registo RMI: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O registo RMI foi criado!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            registry.bind(nameEntryObject, wsInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo da oficina: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("A oficina já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("A oficina foi registada!");
    }
}
