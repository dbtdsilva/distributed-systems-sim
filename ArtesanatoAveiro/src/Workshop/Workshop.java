/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Workshop;

import Exec.GeneralRepository;
import Shop.Shop;

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
    public final int primeMaterialsPerProduct;
    public final int MAX_ProductsStored;
    public final int MIN_PrimeMaterials;
    
    private GeneralRepository generalRepo;
    private Shop shop;
            
    public Workshop(GeneralRepository gr, Shop shop, int maxProducts, int minPM, 
                        int primeMaterialsPerProduct) {
        this.nProductsStored = 0;
        this.nCurrentPrimeMaterials = 0;
        this.nFinishedProducts = 0;
        this.nTimesPrimeMaterialsFetched = 0;
        this.nTotalPrimeMaterialsSupplied = 0;
        this.MAX_ProductsStored = maxProducts;
        this.MIN_PrimeMaterials = minPM;
        this.primeMaterialsPerProduct = primeMaterialsPerProduct;
        
        this.generalRepo = gr;
        this.shop = shop;
    }
    
    public int getnProductsStored() {
        return nProductsStored;
    }

    public int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }

    public int getnFinishedProducts() {
        return nFinishedProducts;
    }

    public int getnTimesPrimeMaterialsFetched() {
        return nTimesPrimeMaterialsFetched;
    }

    public int getnTotalPrimeMaterialsSupplied() {
        return nTotalPrimeMaterialsSupplied;
    }
}
