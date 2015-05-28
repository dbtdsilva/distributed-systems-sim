package ServerSide.Warehouse;

import Static.Constants.RegistryConst;
import Interfaces.LoggingInterface;
import Interfaces.WarehouseInterface;
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
public class WarehouseServer {

    public static void main(String[] args) {
        /* obtenção da localização do serviço de registo RMI */
        String rmiRegHostName;                      // nome do sistema onde está localizado o serviço de registos RMI
        int rmiRegPortNumb;                         // port de escuta do serviço

        rmiRegHostName = RegistryConst.hostRegistry;
        rmiRegPortNumb = RegistryConst.portRegistry;

        /* localização por nome do objecto remoto no serviço de registos RMI */
        LoggingInterface loggingInt = null;             // interface da barbearia (objecto remoto)

        try {
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
            loggingInt = (LoggingInterface) registry.lookup(RegistryConst.logNameEntry);
        } catch (RemoteException e) {
            System.out.println("Excepção na localização da barbearia: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("O logging não está registado: " + e.getMessage() + "!");
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
            whInterface = (WarehouseInterface) UnicastRemoteObject.exportObject(warehouse, RegistryConst.objectRegister);
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
            registry.bind(RegistryConst.warehouseNameEntry, whInterface);
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