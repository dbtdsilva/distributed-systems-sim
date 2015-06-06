package ClientSide.Craftsman;

import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import Static.Constants.ProbConst;
import Static.Enumerates.CraftsmanState;
import VectorClock.VectorTimestamp;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Craftsman
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Craftsman extends Thread {
    private CraftsmanState state;
    private final WorkshopInterface workshop;
    private final ShopInterface shop;
    private final LoggingInterface log;
    private final int id;
    
    private final VectorTimestamp myClock;
    private VectorTimestamp receivedClock;
    
    /**
     * Initiliazes the craftsman class with the required information.
     * 
     * @param id The craftsman identifier.
     * @param log The general repository
     * @param shop The simulation shop where the craftsmen will request services
     *          to the Entrepreneur
     * @param workshop The simulation workshop where the craftsman will work.
     */
    public Craftsman(int id, LoggingInterface log, ShopInterface shop, 
            WorkshopInterface workshop) {
        this.setName("Craftsman "+id);
        this.shop = shop;
        this.id = id;
        this.workshop = workshop;
        this.log = log;
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
        
        myClock = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, ProbConst.nCustomers + id + 1);
    }

    /**
     * This function represents the life cycle of Craftsman.
     */
    @Override
    public void run() {
        int val;
        Object[] res;
        
        try {
            do {
                myClock.increment();
                res = workshop.collectingMaterials(id, myClock.clone()); 
                myClock.update((VectorTimestamp)res[0]);
                
                if (!(boolean)res[1]) {
                    myClock.increment();
                    res = shop.primeMaterialsNeeded(id, myClock.clone());
                    myClock.update((VectorTimestamp)res[0]);
                    
                    myClock.increment();
                    receivedClock = workshop.backToWork(id, myClock.clone());
                    myClock.update(receivedClock);
                } else {
                    myClock.increment();
                    receivedClock = workshop.prepareToProduce(id, myClock.clone());
                    myClock.update(receivedClock);
                    
                    shappingItUp();
                    
                    myClock.increment();
                    res = workshop.goToStore(id, myClock.clone());
                    int productsStored = (int)res[1];
                    myClock.update((VectorTimestamp)res[0]);
                    
                    if (productsStored >= ProbConst.MAXproductsInWorkshop)  {
                        myClock.increment();
                        receivedClock = shop.batchReadyForTransfer(id, myClock.clone());   
                        myClock.update(receivedClock);
                    }
                    
                    myClock.increment();
                    receivedClock = workshop.backToWork(id, myClock.clone());
                    myClock.update(receivedClock);
                }
            } while ((val = log.endOperCraft()) == 0);

            if (val == 2)   {
                myClock.increment();
                receivedClock = shop.batchReadyForTransfer(id, myClock.clone());   
                myClock.update(receivedClock);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        System.out.println("Artesão "+id+" acabou execução!");
    }
    /**
     * Updates the state of the craftsman.
     * 
     * @param state The new state of the craftsman.
     */
    public void setState(CraftsmanState state) {
        this.state = state;
    }
    /**
     * Gets the current state of the craftsman.
     * 
     * @return The state of the craftsman.
     */
    public CraftsmanState getCurrentState() {
        return state;
    }
    /**
     * The craftsman is working on the next piece.
     */
    private void shappingItUp() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Craftsman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
