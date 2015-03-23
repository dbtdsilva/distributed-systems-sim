/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Warehouse;

import Exec.GeneralRepository;

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
    
    public int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }
}
