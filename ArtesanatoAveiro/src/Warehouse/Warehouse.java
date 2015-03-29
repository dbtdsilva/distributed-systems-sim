package Warehouse;

import Entrepreneur.Entrepreneur;
import Entrepreneur.EntrepreneurState;
import Exec.ProbConst;
import Logger.Logging;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
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
        int n = 0;
        if(nCurrentPrimeMaterials < ProbConst.nPrimeMaterialToTransfer)
        {
            n = nCurrentPrimeMaterials;
            nCurrentPrimeMaterials = 0;
            return n;
        }
        else    {
            n = ProbConst.nPrimeMaterialToTransfer;
            nCurrentPrimeMaterials -= ProbConst.nPrimeMaterialToTransfer;   
        }
        
        log.WriteWarehouse(nCurrentPrimeMaterials);
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        log.UpdateEntreperneurState(EntrepreneurState.AT_THE_SUPPLIERS);
        return n;
    }
    
    public synchronized int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }  
}
