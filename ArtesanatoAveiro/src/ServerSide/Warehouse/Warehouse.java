package ServerSide.Warehouse;

<<<<<<< HEAD
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import Communication.ProbConst;
import static java.lang.Thread.sleep;

/**
 * The class that represents the Warehouse.
=======
import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.WarehouseInterface;
import ServerSide.Shop.Shop;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConfig;
import Structures.Enumerates.EntrepreneurState;
import Structures.VectorClock.VectorTimestamp;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The monitor that represents the Warehouse.
>>>>>>> origin/Trabalho3
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
<<<<<<< HEAD
public class Warehouse {   
    /**
     * Counter for the number of times that the Entrepreneur came to get supply.
     */
    private int nTimesSupplied;
    
    /**
     * Array that translates to the number of prime materials fetched at each time the Entrepreneur
     * visits the suppliers.
     */
    private final int nTimesPMSupplied[];
    
    /**
     * Initializes the warehouse class with the required information.
     */
    public Warehouse() {
=======
public class Warehouse implements WarehouseInterface {
    private final LoggingInterface log;
    
    private int nTimesSupplied;
    private final int nTimesPMSupplied[];
    private final VectorTimestamp clocks;
    
    /**
     * Initializes the warehouse class with the required information.
     * 
     * @param log The general repository
     */
    public Warehouse(LoggingInterface log) {
        this.log = log;
>>>>>>> origin/Trabalho3
        this.nTimesSupplied = 0;
        
        int nPMMin = ProbConst.nCraftsmen * ProbConst.primeMaterialsPerProduct;
        
        nTimesPMSupplied = new int[ProbConst.MAXSupplies];
        for (int i = 0; i < nTimesPMSupplied.length; i++) {
            nTimesPMSupplied[i] = (int) (Math.random() * nPMMin * 3 + 1);
        }
        
        if (nTimesPMSupplied[nTimesPMSupplied.length-1] < nPMMin)
            nTimesPMSupplied[nTimesPMSupplied.length-1] += nPMMin;
<<<<<<< HEAD
=======
        
        this.clocks = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
>>>>>>> origin/Trabalho3
    }
    
    /******************/
    /** ENTREPRENEUR **/
    /******************/

    /**
     * The entrepreneur visits the supplies to fetch prime materials for the
     * craftsman.
     * She will fetch a random value of prime materials.
     * 
     * @return the number of prime materials fetched
     */
<<<<<<< HEAD
    public synchronized int visitSuppliers() {
        int n = nTimesPMSupplied[nTimesSupplied];
        nTimesSupplied++;
        
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        //log.UpdateEntreperneurState(((Entrepreneur) Thread.currentThread()).getCurrentState());
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_ENTR_STATE, 
                EntrepreneurState.AT_THE_SUPPLIERS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return n;
=======
    @Override
    public synchronized Object[] visitSuppliers(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        Object[] res = new Object[2];
        
        int n = nTimesPMSupplied[nTimesSupplied];
        nTimesSupplied++;
        
        log.UpdateEntreperneurState(EntrepreneurState.AT_THE_SUPPLIERS, clocks.clone());
        res[0] = clocks.clone();
        res[1] = n;
        return res;
    }
    /**
     * This function is used for the logging to signal the shop to shutdown.
     * 
     * @throws RemoteException may throw during a execution of a remote method call
     */
    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;
        
        RegistryConfig rc = new RegistryConfig("../../config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.warehouseNameEntry;

        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Warehouse registration exception: " + e.getMessage());
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Warehouse not bound exception: " + e.getMessage());
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Warehouse closed.");
>>>>>>> origin/Trabalho3
    }
}
