package ServerSide.Warehouse;

import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.WarehouseInterface;
import ServerSide.Shop.Shop;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConst;
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
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
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
        this.nTimesSupplied = 0;
        
        int nPMMin = ProbConst.nCraftsmen * ProbConst.primeMaterialsPerProduct;
        
        nTimesPMSupplied = new int[ProbConst.MAXSupplies];
        for (int i = 0; i < nTimesPMSupplied.length; i++) {
            nTimesPMSupplied[i] = (int) (Math.random() * nPMMin * 3 + 1);
        }
        
        if (nTimesPMSupplied[nTimesPMSupplied.length-1] < nPMMin)
            nTimesPMSupplied[nTimesPMSupplied.length-1] += nPMMin;
        
        this.clocks = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
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

        String rmiRegHostName = RegistryConst.hostRegistry;
        int rmiRegPortNumb = RegistryConst.portRegistry;
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(Warehouse.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConst.registerHandler;
        String nameEntryObject = RegistryConst.warehouseNameEntry;

        
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
    }
}
