package ServerSide.Warehouse;

import Interfaces.LoggingInterface;
import Interfaces.WarehouseInterface;
import Static.Constants.ProbConst;
import Static.Enumerates.EntrepreneurState;
import VectorClock.VectorTimestamp;
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
    private VectorTimestamp clocks;
    
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
}
