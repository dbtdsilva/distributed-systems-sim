/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide.Entrepreneur;

import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WarehouseInterface;
import Interfaces.WorkshopInterface;
import Structures.Constants.RegistryConfig;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class is the main class of the Entrepreneur entity.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class EntrepreneurClient {
    /**
     * The main class for the entrepreneur entity.
     * @param args No arguments are going to be used.
     */
    public static void main(String [] args) {
        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        RegistryConfig rc = new RegistryConfig("../../config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        
        LoggingInterface loggingInt = null;
        ShopInterface shopInt = null;
        WarehouseInterface whInt = null;
        WorkshopInterface wsInt = null;
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            loggingInt = (LoggingInterface) registry.lookup (RegistryConfig.logNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating logger: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Logger is not registered: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            shopInt = (ShopInterface) registry.lookup (RegistryConfig.shopNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating logger: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Logger is not registered: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            wsInt = (WorkshopInterface) registry.lookup (RegistryConfig.workshopNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating logger: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Logger is not registered: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            whInt = (WarehouseInterface) registry.lookup (RegistryConfig.warehouseNameEntry);
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating logger: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Logger is not registered: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit(1);
        }
        Entrepreneur entr = new Entrepreneur(loggingInt, shopInt, whInt, wsInt);
        
        System.out.println("Entrepreneur started working..");    
        entr.start();
        try {
            entr.join();
        } catch (InterruptedException e) {}
        System.out.println("Entrepreneur finished the job.");
    }
}
