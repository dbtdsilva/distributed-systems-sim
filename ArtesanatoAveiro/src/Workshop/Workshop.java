/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Workshop;

import Exec.GeneralRepository;
import Shop.Shop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Silva, 60337 
 * @author Tânia Alves, 60340
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
            
    /**
     * Initializes the workshop class with the required information.
     * 
     * @param gr The general repository where the semaphores are stored along with some other useful global variables.
     * @param shop The shop that is created in the simulation.
     * @param maxProducts Maximum number of products that can be stored in the workshop.
     * @param minPM Minimum number of prime materials that the workshop can have until the craftsmen notify the entrepreneur.
     * @param primeMaterialsPerProduct Number of prime materials that takes to manufacture a product.
     */
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
    /**
     * If there are enough materials to manufacture the product, the number of available prime materials 
     * is updated. 
     * However, if the prime materials aren't enough, the function returns false.
     * 
     * @return true if there are enough prime materials to manufacture a product or false if there aren't.
     */
    public boolean collectingMaterials() {
        if(nCurrentPrimeMaterials < primeMaterialsPerProduct)
            return false;
        
        nCurrentPrimeMaterials -= primeMaterialsPerProduct;
        return true;
    }

    /**
     * If there are not enough prime materials, the craftsman tells the entrepreneur to fetch more prime materials.
     * 
     */
    public void primeMaterialsNeeded() {
        synchronized(this)
        {
            shop.RequestPrimeMaterials();
            generalRepo.entrepreneurWake.release();
        }
        
        try {
            generalRepo.craftsmenWaitingMaterials.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
    */
    public void batchReadyForTransfer() {
        synchronized(this)
        {
            shop.RequestFetchProducts();
            generalRepo.entrepreneurWake.release();
        }
    }
    
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     */
    public synchronized void storePiece() {
        nFinishedProducts++;
        nProductsStored++;
    }
    
    /*************/
    /** GENERAL **/
    /*************/
    
    /**
     * Get the number of products that are currently in stock at the workshop.
     * 
     * @return Number of stored products.
     */
    public synchronized int getnProductsStored() {
        return nProductsStored;
    }
    
    /**
     * Get the current number of prime materials available in the workshop.
     * 
     * @return Number of current prime materials.
     */
    public synchronized int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }

    /**
     * Checks if there are any prime materials being transfered.
     * @return True if there are prime materials on the move; false, otherwise.
     */
    public synchronized boolean isAnyTransferOccuring() {
        return !shop.isReqPrimeMaterials();
    }
}
