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
     * The entrepreneur visits the supplies to fetch prime materials for the
     * craftsman.
     * She will fetch a maximum of nPrimeMaterialsToTransfer.
     */
    public synchronized void visitSuppliers() {
        int n;
        if (nCurrentPrimeMaterials < ProbConst.nPrimeMaterialToTransfer)
        {
            n = nCurrentPrimeMaterials;
            nCurrentPrimeMaterials = 0;
        }
        else
        {
            n = ProbConst.nPrimeMaterialToTransfer;
            nCurrentPrimeMaterials -= ProbConst.nPrimeMaterialToTransfer;   
        }
        
        log.WriteWarehouse(nCurrentPrimeMaterials);
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        log.UpdateEntreperneurState(EntrepreneurState.AT_THE_SUPPLIERS);
        ((Entrepreneur) Thread.currentThread()).setNMaterialsTranfer(n);
    }
    
    /**
     * This function returns the total number of prime materials in the Warehouse.
     * 
     * @return the number of prime materials in the Warehouse.
     */
    public synchronized int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }  
}
