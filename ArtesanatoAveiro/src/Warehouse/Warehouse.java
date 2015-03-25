package Warehouse;

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
    public synchronized int visitSuppliers() {
        int productsLeft = nCurrentPrimeMaterials / ProbConst.primeMaterialsPerProduct;
        return (int) ((Math.random()+1) * (productsLeft % 5) * ProbConst.primeMaterialsPerProduct);
    }
    
    public synchronized int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }  
}
