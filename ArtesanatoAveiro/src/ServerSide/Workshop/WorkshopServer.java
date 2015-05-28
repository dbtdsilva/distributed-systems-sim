/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Workshop;

import Static.Constants.RegistryConst;
import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author guesswho
 */
public class WorkshopServer {    
    public static void main(String[] args) {
        String rmiRegHostName = RegistryConst.hostRegistry;
        int rmiRegPortNumb = RegistryConst.portRegistry;
        
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
        
        try {
            Registry registry = LocateRegistry.getRegistry (rmiRegHostName, rmiRegPortNumb);
            shopInt = (ShopInterface) registry.lookup (RegistryConst.shopNameEntry);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while exporting shop interface: " + e.getMessage());
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
            wsInterface = (WorkshopInterface) UnicastRemoteObject.exportObject(workshop, RegistryConst.portWorkshop);
        } catch (RemoteException e) {
            System.out.println("Exception thrown while exporting workshop interface: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Workshop interface exported successfully!");

        /* seu registo no serviço de registo RMI */
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("Exception thrown in the RMI registry creation: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("RMI registry was created!");

        try {
            registry.bind(RegistryConst.workshopNameEntry, wsInterface);
        } catch (RemoteException e) {
            System.out.println("Exception thrown at workshop registry: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("The workshop is already bound: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Workshop was registered!");
}
}
