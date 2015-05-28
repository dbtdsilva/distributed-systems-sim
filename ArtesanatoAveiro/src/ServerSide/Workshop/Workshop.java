package ServerSide.Workshop;

import ClientSide.Craftsman.Craftsman;
import Static.Enumerates.CraftsmanState;
import ClientSide.Entrepreneur.Entrepreneur;
import Static.Enumerates.EntrepreneurState;
import Static.Constants.ProbConst;
import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The monitor that represents the Workshop.
 * 
 * @author Diogo Silva, 60337 
 * @author Tânia Alves, 60340
 */
public class Workshop implements WorkshopInterface {
    private int nProductsStored;
    private int nCurrentPrimeMaterials;
    private int nFinishedProducts;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private boolean waitingEntrepreneur;
    
    private final LoggingInterface log;
    private final ShopInterface shop;
            
    /**
     * Initializes the workshop class with the required information.
     * 
     * @param log The general repository where the semaphores are stored along with some other useful global variables.
     * @param shop The shop that is created in the simulation.
     */
    public Workshop(LoggingInterface log, ShopInterface shop) {
        this.nProductsStored = 0;
        this.nCurrentPrimeMaterials = 0;
        this.nFinishedProducts = 0;
        this.nTimesPrimeMaterialsFetched = 0;
        this.nTotalPrimeMaterialsSupplied = 0;
        this.waitingEntrepreneur = false;
        
        this.log = log;
        this.shop = shop;
    }
    
        /******************/
        /** ENTREPRENEUR **/
        /******************/
    
    /**
     * Entrepreneur goes to the Workshop to fetch the products in there.
     * 
     * @return the number of products that Entrepreneur is going to deliver to 
     * the shop
     */
    public synchronized int goToWorkshop() throws RemoteException {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        
        int n = nProductsStored;
        nProductsStored = 0;
        
        shop.resetRequestProducts();
        log.WriteWorkshopAndEntrepreneurStat(nCurrentPrimeMaterials, nProductsStored, 
                    nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                    nFinishedProducts, ((Entrepreneur) Thread.currentThread()).getCurrentState());
        return n;
    }
    /**
     * Entrepreneur goes to the Workshop and returns that prime materials that
     * she fetched from the Warehouse.
     * If nobody is waiting for her, it means that Entrepreneur arrived before
     * the Craftsman to the Workshop and he will not need to wait.
     * 
     * @param nMaterials number of prime materials
     */
    public synchronized void replenishStock(int nMaterials) throws RemoteException {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.DELIVERING_PRIME_MATERIALS);

        nTimesPrimeMaterialsFetched++;
        nTotalPrimeMaterialsSupplied += nMaterials;
        nCurrentPrimeMaterials += nMaterials;
        
        shop.resetRequestPrimeMaterials();
        waitingEntrepreneur = false;
        
        log.WriteWorkshopAndEntrepreneurStat(nCurrentPrimeMaterials, nProductsStored, 
                    nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                    nFinishedProducts, ((Entrepreneur) Thread.currentThread()).getCurrentState());
                
        notifyAll();    // Wake up craftsmen
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
    public synchronized boolean collectingMaterials(int id) throws RemoteException {
        while (waitingEntrepreneur && nCurrentPrimeMaterials < ProbConst.nPrimeMaterials) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (nCurrentPrimeMaterials < ProbConst.MINprimeMaterials && !waitingEntrepreneur &&
                nTimesPrimeMaterialsFetched < ProbConst.MAXSupplies) {
            waitingEntrepreneur = true;
            return false;
        }
        
        nCurrentPrimeMaterials -= ProbConst.primeMaterialsPerProduct;
        
        log.WriteWorkshop(nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, 
                            nTotalPrimeMaterialsSupplied, nFinishedProducts);
        return true;
    }   
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     * 
     * @return the number of products stored in workshop.
     */
    public synchronized int goToStore(int id) throws RemoteException {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.STORING_IT_FOR_TRANSFER);
        
        nFinishedProducts++;
        nProductsStored++;
        
        log.WriteWorkshopAndCraftsmanStat(nCurrentPrimeMaterials, nProductsStored, 
                nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, nFinishedProducts,
                ((Craftsman) Thread.currentThread()).getCurrentState(), id, true);
        return nProductsStored;
    }
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized void backToWork(int id) throws RemoteException {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.FETCHING_PRIME_MATERIALS);
        log.UpdateCraftsmanState(id, ((Craftsman) Thread.currentThread()).getCurrentState());
    } 
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized void prepareToProduce(int id) throws RemoteException {
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.PRODUCING_A_NEW_PIECE);
        log.UpdateCraftsmanState(id, ((Craftsman) Thread.currentThread()).getCurrentState());
    } 
}
