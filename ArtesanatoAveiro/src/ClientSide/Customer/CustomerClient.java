/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide.Customer;

import ClientSide.Entrepreneur.Entrepreneur;
import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WarehouseInterface;
import Interfaces.WorkshopInterface;
import Static.Constants.ProbConst;
import Static.Constants.RegistryConst;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author diogosilva
 */
public class CustomerClient {
    public static void main(String [] args) {
        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        rmiRegHostName = RegistryConst.hostRegistry;
        rmiRegPortNumb = RegistryConst.portRegistry;
        
        LoggingInterface loggingInt = null;
        ShopInterface shopInt = null;
        
        try
        { 
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            loggingInt = (LoggingInterface) registry.lookup (RegistryConst.logNameEntry);
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
            shopInt = (ShopInterface) registry.lookup (RegistryConst.shopNameEntry);
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
        
        Customer [] customers = new Customer[ProbConst.nCustomers];
        for (int i = 0; i < customers.length; i++) {
            customers[i] = new Customer(i, loggingInt, shopInt);
            customers[i].start();
        }
        
        System.out.println("Customers started working..");
        for (Customer c : customers) {
            try {
                c.join();
            } catch (InterruptedException e) {}
        }
        
        System.out.println("Customers finished their job.");
    }
}
