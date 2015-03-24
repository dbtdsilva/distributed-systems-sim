/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Warehouse;

import Exec.GeneralRepository;
import Exec.ProbConst;

/**
 *
 * @author diogosilva
 */
public class Warehouse {
    private GeneralRepository gr;
    private int nCurrentPrimeMaterials;
    private final int nInitialPrimeMaterials;
    
    public Warehouse(GeneralRepository rep, int nPrimeMaterials) {
        this.gr = rep;
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
