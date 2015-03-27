package Workshop;

import Craftsman.Craftsman;
import Craftsman.CraftsmanState;
import Entrepreneur.Entrepreneur;
import Entrepreneur.EntrepreneurState;
import Logger.Logging;
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
    
    private final Logging log;
    private final Shop shop;
            
    /**
     * Initializes the workshop class with the required information.
     * 
     * @param log The general repository where the semaphores are stored along with some other useful global variables.
     * @param shop The shop that is created in the simulation.
     * @param maxProducts Maximum number of products that can be stored in the workshop.
     * @param minPM Minimum number of prime materials that the workshop can have until the craftsmen notify the entrepreneur.
     * @param primeMaterialsPerProduct Number of prime materials that takes to manufacture a product.
     */
    public Workshop(Logging log, Shop shop, int maxProducts, int minPM, 
                        int primeMaterialsPerProduct) {
        this.nProductsStored = 0;
        this.nCurrentPrimeMaterials = 0;
        this.nFinishedProducts = 0;
        this.nTimesPrimeMaterialsFetched = 0;
        this.nTotalPrimeMaterialsSupplied = 0;
        this.MAX_ProductsStored = maxProducts;
        this.MIN_PrimeMaterials = minPM;
        this.primeMaterialsPerProduct = primeMaterialsPerProduct;
        
        this.log = log;
        this.shop = shop;
    }
    
        /******************/
        /** ENTREPRENEUR **/
        /******************/
    /**
     * asdsad
     * @return asd
     */
    public synchronized int goToWorkshop() {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        log.UpdateEntreperneurState(EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        
        int products = nProductsStored;
        nProductsStored = 0;
        return products;
    }
    public synchronized void replenishStock(int nMaterials) {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.DELIVERING_PRIME_MATERIALS);
        log.UpdateEntreperneurState(EntrepreneurState.DELIVERING_PRIME_MATERIALS);
        
        nTimesPrimeMaterialsFetched++;
        nTotalPrimeMaterialsSupplied += nMaterials;
        nCurrentPrimeMaterials += nMaterials;
        
        notifyAll();    // Wake up craftsmen
        ((Entrepreneur) Thread.currentThread()).resetNMaterialsTransfer();
        
        log.WriteWorkshop(nCurrentPrimeMaterials, nProductsStored, 
                nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, nFinishedProducts);
    }

        /***************/
        /** CRAFTSMEN **/
        /***************/
    /**
     * The craftsman is preparing to manufacture a product.
     * If there are enough materials to manufacture the product, the number of available prime materials 
     * is updated. 
     * However, if the prime materials aren't enough, the function returns false.
     * 
     * @param id The craftsman identifier.
     * @return true if there are enough prime materials to manufacture a product or false if there aren't.
     */
    public synchronized boolean collectingMaterials(int id) {
        if(nCurrentPrimeMaterials < primeMaterialsPerProduct)
            return false;
        
        nCurrentPrimeMaterials -= primeMaterialsPerProduct;
        log.WriteWorkshop(nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, 
                            nTotalPrimeMaterialsSupplied, nFinishedProducts);
        return true;
    }
    /**
     * If there are not enough prime materials, the craftsman tells the entrepreneur to fetch more prime materials.
     * The crafstman found that there are not enough materials at the workshop, and so, it will notify the entreperneur, 
     * asking her to fetch prime materials.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized void primeMaterialsNeeded(int id) {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.CONTACTING_ENTREPRENEUR);
        log.UpdateCraftsmanState(id, CraftsmanState.CONTACTING_ENTREPRENEUR);
        
        try {
            wait();     // Sleep the craftsman
        } catch (InterruptedException ex) {
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
    */
    public synchronized void batchReadyForTransfer(int id) {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.CONTACTING_ENTREPRENEUR);
        log.UpdateCraftsmanState(id, CraftsmanState.CONTACTING_ENTREPRENEUR);
        //Call the shop
        shop.RequestFetchProducts();
        //Wake the entrepreneur to transport finished products
    }
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     */
    public synchronized void goToStore(int id) {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.STORING_IT_FOR_TRANSFER);
        log.UpdateCraftsmanState(id, CraftsmanState.STORING_IT_FOR_TRANSFER);
        
        nFinishedProducts++;
        nProductsStored++;
        
        ((Craftsman) Thread.currentThread()).updateFinishedProducts();
        log.CraftsmanFinishedProduct(id);
    }
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized void backToWork(int id) {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.FETCHING_PRIME_MATERIALS);
        log.UpdateCraftsmanState(id, CraftsmanState.FETCHING_PRIME_MATERIALS);
    } 
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized void prepareToProduce(int id) {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.PRODUCING_A_NEW_PIECE);
        log.UpdateCraftsmanState(id, CraftsmanState.PRODUCING_A_NEW_PIECE);
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
