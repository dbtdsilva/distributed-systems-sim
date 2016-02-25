package ServerSide.Warehouse;

import Structures.Constants.RegistryConfig;
import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.WarehouseInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the main class for the server Warehouse.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class WarehouseServer {
    /**
     * The main class for the warehouse server.
     * @param args No arguments are going to be used.
     */
    public static void main(String[] args) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        RegistryConfig rc = new RegistryConfig("../../config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();

        /* localização por nome do objecto remoto no serviço de registos RMI */
        LoggingInterface loggingInt = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            loggingInt = (LoggingInterface) registry.lookup(RegistryConfig.logNameEntry);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização do armazém: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O armazém não está registado: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }

        /* instanciação e instalação do gestor de segurança */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }

        /* instanciação do objecto remoto que representa a barbearia e geração de um stub para ele */
        Warehouse warehouse = null;                              // barbearia (representa o objecto remoto)
        WarehouseInterface whInterface = null;             // interface da barbearia
        warehouse = new Warehouse(loggingInt);

        try {
            whInterface = (WarehouseInterface) UnicastRemoteObject.exportObject(warehouse, rc.warehousePort());
        } catch (RemoteException e) {
            System.out.println("Excepção na geração do stub para o armazém: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O stub para o armazém foi gerado!");

        /* seu registo no serviço de registo RMI */
        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.warehouseNameEntry;
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
            reg.bind(nameEntryObject, whInterface);
        } catch (RemoteException e) {
            System.out.println("Excepção no registo do armazém: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("O armazém já está registado: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("O armazém foi registado!");
    }
}
