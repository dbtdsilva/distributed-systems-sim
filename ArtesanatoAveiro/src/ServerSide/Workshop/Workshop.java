package ServerSide.Workshop;

import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import Static.Constants.ProbConst;
import Static.Enumerates.CraftsmanState;
import Static.Enumerates.EntrepreneurState;
import VectorClock.VectorTimestamp;
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
    
    private VectorTimestamp clocks;
    
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
        
        this.clocks = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
        
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
    public synchronized Object[] goToWorkshop(VectorTimestamp vt) throws RemoteException {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        clocks.update(vt);
        Object[] res = new Object[2];
        
        int n = nProductsStored;
        nProductsStored = 0;
        
        shop.resetRequestProducts();
        log.WriteWorkshopAndEntrepreneurStat(nCurrentPrimeMaterials, nProductsStored, 
                    nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                    nFinishedProducts, EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS, clocks.clone());
        
        res[1] = n;
        res[0] = clocks.clone();
        return res;
    }
    
    /**
     * Entrepreneur goes to the Workshop and returns that prime materials that
     * she fetched from the Warehouse.
     * If nobody is waiting for her, it means that Entrepreneur arrived before
     * the Craftsman to the Workshop and he will not need to wait.
     * 
     * @param nMaterials number of prime materials
     */
    public synchronized VectorTimestamp replenishStock(int nMaterials, VectorTimestamp vt) throws RemoteException {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.DELIVERING_PRIME_MATERIALS);

        clocks.update(vt);
        
        nTimesPrimeMaterialsFetched++;
        nTotalPrimeMaterialsSupplied += nMaterials;
        nCurrentPrimeMaterials += nMaterials;
        
        shop.resetRequestPrimeMaterials();
        waitingEntrepreneur = false;
        
        log.WriteWorkshopAndEntrepreneurStat(nCurrentPrimeMaterials, nProductsStored, 
                    nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                    nFinishedProducts,EntrepreneurState.DELIVERING_PRIME_MATERIALS, clocks.clone());
                
        notifyAll();    // Wake up craftsmen
        
        return clocks.clone();
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
    public synchronized Object[] collectingMaterials(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        
        Object[] res = new Object[2];
        res[0] = clocks.clone();
        res[1] = true;
        
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
            res[1] = false;
            return res;
        }
        
        nCurrentPrimeMaterials -= ProbConst.primeMaterialsPerProduct;
        
        log.WriteWorkshop(nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, 
                            nTotalPrimeMaterialsSupplied, nFinishedProducts, clocks.clone());
        return res;
    }   
    
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     * 
     * @return the number of products stored in workshop.
     */
    public synchronized Object[] goToStore(int id, VectorTimestamp vt) throws RemoteException {
        //((Craftsman) Thread.currentThread()).setState(CraftsmanState.STORING_IT_FOR_TRANSFER);
        clocks.update(vt);
        Object[] res = new Object[2];
        res[0] = clocks.clone();
        
        nFinishedProducts++;
        nProductsStored++;
        
        log.WriteWorkshopAndCraftsmanStat(nCurrentPrimeMaterials, nProductsStored, 
                nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, nFinishedProducts,
                CraftsmanState.STORING_IT_FOR_TRANSFER, id, true, clocks.clone());
        
        res[1] = nProductsStored;
        
        return res;
    }
    
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized VectorTimestamp backToWork(int id, VectorTimestamp vt) throws RemoteException {
        //((Craftsman) Thread.currentThread()).setState(CraftsmanState.FETCHING_PRIME_MATERIALS);
        clocks.update(vt);
        log.UpdateCraftsmanState(id, CraftsmanState.FETCHING_PRIME_MATERIALS, clocks.clone());
        return clocks.clone();
    } 
    
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     */
    public synchronized VectorTimestamp prepareToProduce(int id, VectorTimestamp vt) throws RemoteException {
        //((Craftsman) Thread.currentThread()).setState(CraftsmanState.PRODUCING_A_NEW_PIECE);
        clocks.update(vt);
        log.UpdateCraftsmanState(id, CraftsmanState.PRODUCING_A_NEW_PIECE, clocks.clone());
        return clocks.clone();
    } 
}
