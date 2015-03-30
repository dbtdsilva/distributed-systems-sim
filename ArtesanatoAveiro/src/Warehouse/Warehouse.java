package Warehouse;

import Entrepreneur.Entrepreneur;
import Entrepreneur.EntrepreneurState;
import Exec.ProbConst;
import Logger.Logging;

/**
 * The monitor that represents the Warehouse.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Warehouse {
    private final Logging log;
    
    private int nTimesSupplied;
    private final int nTimesPMSupplied[];
    
    public Warehouse(Logging log, int nPrimeMaterials) {
        this.log = log;
        this.nTimesSupplied = 0;
        
        int nPMMin = ProbConst.nCraftsmen * ProbConst.primeMaterialsPerProduct;
        
        nTimesPMSupplied = new int[ProbConst.nMaxSupplies];
        for (int i = 0; i < ProbConst.nMaxSupplies; i++) {
            nTimesPMSupplied[i] = (int) (Math.random() * nPMMin * 3);
        }
        
        if (nTimesPMSupplied[ProbConst.nMaxSupplies-1] < nPMMin)
            nTimesPMSupplied[ProbConst.nMaxSupplies-1] += nPMMin;
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
    public synchronized int visitSuppliers() {
        int n = nTimesPMSupplied[nTimesSupplied];
        nTimesSupplied++;
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        log.UpdateEntreperneurState(EntrepreneurState.AT_THE_SUPPLIERS);
        return n;
    }
}
