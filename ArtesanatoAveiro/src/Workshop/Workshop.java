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
    /******************/
    /** ENTREPRENEUR **/
    /******************/
    public synchronized int goToWorkshop() {
        int products = nProductsStored;
        nProductsStored = 0;
        return products;
    }

    public synchronized void replenishStock(int nMaterials) {
        nTimesPrimeMaterialsFetched++;
        nTotalPrimeMaterialsSupplied += nMaterials;
        nCurrentPrimeMaterials += nMaterials;
        
        for (int i = 0; i < generalRepo.craftsmenWaiting; i++)
            generalRepo.craftsmenWaitingMaterials.release();
        generalRepo.craftsmenWaiting = 0;
    }
    
    /***************/
    /** CRAFTSMEN **/
    /***************/
    public boolean collectingMaterials() {
        return false;
    }
    
    /*************/
    /** GENERAL **/
    /*************/
    public int getnProductsStored() {
        return nProductsStored;
    }
    public int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }
}
