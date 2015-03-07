/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Workshop;

/**
 *
 * @author diogosilva
 */
public class Workshop {
    private int nProductsStored;
    private int nCurrentPrimeMaterials;
    private int nFinishedProducts;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private final int primeMaterialsPerProduct;
    private final int MAX_ProductsStored;
    private final int MIN_PrimeMaterials;
            
    public Workshop(int maxProducts, int minPM, int primeMaterialsPerProduct) {
        this.nProductsStored = 0;
        this.nCurrentPrimeMaterials = 0; // ?
        this.nFinishedProducts = 0;
        this.nTimesPrimeMaterialsFetched = 0; // ?
        this.nTotalPrimeMaterialsSupplied = 0;
        this.MAX_ProductsStored = maxProducts;
        this.MIN_PrimeMaterials = minPM;
        this.primeMaterialsPerProduct = primeMaterialsPerProduct;
    }
}
