/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide.Craftsman;

import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConfig;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This class is the main class of the Craftsman entity.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class CraftsmanClient {

    /**
     * The main class for the craftsman entity.
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
            System.out.println("Exception thrown while locating shop: " + e.getMessage () + "!");
            e.printStackTrace ();
            System.exit (1);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Shop is not registered: " + e.getMessage () + "!");
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
        
       
        Craftsman [] craftsmen = new Craftsman[ProbConst.nCraftsmen];
        
        for(int i = 0; i < ProbConst.nCraftsmen; i++)
        {
            craftsmen[i] = new Craftsman(i, loggingInt, shopInt, wsInt);
            craftsmen[i].start();
        }

        System.out.println("Craftsmen started working.."); 
        for(Craftsman c : craftsmen)
        {
            try {
                c.join();
            } catch (InterruptedException e) {}
            
        }
        System.out.println("Craftsmen finished their job.");
    }
}
