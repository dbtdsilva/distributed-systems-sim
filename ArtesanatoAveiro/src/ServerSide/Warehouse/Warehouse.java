package ServerSide.Warehouse;

import ClientSide.Entrepreneur.Entrepreneur;
import Static.Enumerates.EntrepreneurState;
import Static.Constants.ProbConst;
import Interfaces.LoggingInterface;
import Interfaces.WarehouseInterface;
import java.rmi.RemoteException;

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
    public synchronized int visitSuppliers() throws RemoteException {
        int n = nTimesPMSupplied[nTimesSupplied];
        nTimesSupplied++;
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        log.UpdateEntreperneurState(((Entrepreneur) Thread.currentThread()).getCurrentState());
        return n;
    }
}
