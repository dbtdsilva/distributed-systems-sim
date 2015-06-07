/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Logger;

import Interfaces.LoggingInterface;
import Interfaces.Register;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConst;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author diogosilva
 */
public class LoggingServer {
    /**
     * The main class for the logging server.
     * @param args No arguments are going to be used.
     */
    public static void main(String[] args) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        rmiRegHostName = RegistryConst.hostRegistry;
        rmiRegPortNumb = RegistryConst.portRegistry;

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        Logging logging = null;                              // barbearia (representa o objecto remoto)
        LoggingInterface logInterface = null;             // interface da barbearia

        try {
            logging = new Logging("", ProbConst.nCustomers, ProbConst.nCraftsmen, ProbConst.nPrimeMaterials);
        } catch (IOException e) {
            System.out.println("Failed to open file for logging: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        try {
            logInterface = (LoggingInterface) UnicastRemoteObject.exportObject(logging, RegistryConst.portLogging);
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para a Logging: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o logging foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConst.registerHandler;
        String nameEntryObject = RegistryConst.logNameEntry;
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
            registry.bind(nameEntryObject, logInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do logging: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O logging já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O logging foi registado!");
    }
}
