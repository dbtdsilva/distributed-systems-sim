package Warehouse;

import Entrepreneur.Entrepreneur;
import Entrepreneur.EntrepreneurState;
import Exec.ProbConst;
import Logger.Logging;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alve, 60340
 */
public class Warehouse {
    private Logging log;
    private int nCurrentPrimeMaterials;
    private final int nInitialPrimeMaterials;
    
    public Warehouse(Logging log, int nPrimeMaterials) {
        this.log = log;
        this.nInitialPrimeMaterials = nPrimeMaterials;
        this.nCurrentPrimeMaterials = this.nInitialPrimeMaterials;
    }
    
    /******************/
    /** ENTREPRENEUR **/
    /******************/
    
    /**
     *
     * @return
     */
    public synchronized int visitSuppliers() {
        nCurrentPrimeMaterials -= 30;
        log.WriteWarehouse(nCurrentPrimeMaterials);
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        log.UpdateEntreperneurState(EntrepreneurState.AT_THE_SUPPLIERS);
        return 30;
    }
    
    public synchronized int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }  
}
