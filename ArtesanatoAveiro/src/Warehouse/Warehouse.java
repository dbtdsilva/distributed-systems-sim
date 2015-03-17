/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Warehouse;

/**
 *
 * @author diogosilva
 */
public class Warehouse {
    private int nCurrentPrimeMaterials;
    private final int nInitialPrimeMaterials;
    
    public Warehouse(int nPrimeMaterials) {
        this.nInitialPrimeMaterials = nPrimeMaterials;
        this.nCurrentPrimeMaterials = this.nInitialPrimeMaterials;
    }
    
    public int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }
}
