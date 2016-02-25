package ClientSide.Entrepreneur;

import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WarehouseInterface;
import Interfaces.WorkshopInterface;
import Structures.Constants.ProbConst;
import Structures.Enumerates.EntrepreneurState;
import Structures.VectorClock.VectorTimestamp;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Entrepreneur
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Entrepreneur extends Thread {
    private EntrepreneurState state;
    private final ShopInterface shop;
    private final WarehouseInterface warehouse;
    private final WorkshopInterface workshop;
    private final LoggingInterface log;
      
    private final VectorTimestamp myClock;
    private VectorTimestamp receivedClock;
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     * 
     * @param log The general repository
     * @param shop The simulation shop where the entrepreneur will work.
     * @param warehouse The simulation warehouse where the entrepeneur fetchs prime 
     * materials when requested by the Craftsmen.
     * @param workshop The simulation workshop where the craftsmen are located.
     */
    public Entrepreneur(LoggingInterface log, ShopInterface shop, 
            WarehouseInterface warehouse, WorkshopInterface workshop) {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.warehouse = warehouse;
        this.workshop = workshop;
        this.log = log;
        
        myClock = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
    }
    /**
     * This function represents the life cycle of Entrepreneur.
     */
    @Override
    public void run() {
        try {
            do {
                boolean canGoOut = false;
                char sit;

                myClock.increment();
                receivedClock = shop.prepareToWork(myClock.clone());
                myClock.update(receivedClock);
                
                Object[] ret;
                
                do {   
                    myClock.increment();
                    ret = shop.appraiseSit(myClock.clone());
                    myClock.update((VectorTimestamp)ret[0]);
                    sit = (char)ret[1];
                    
                    switch (sit) {
                        case 'C': 
                            myClock.increment();
                            ret = shop.addressACustomer(myClock.clone());
                            int id = (int)ret[1];
                            myClock.update((VectorTimestamp)ret[0]);
                            
                            serviceCustomer(id);
                            
                            myClock.increment();
                            receivedClock = shop.sayGoodByeToCustomer(id, myClock.clone());
                            myClock.update(receivedClock);
                            break;
                        case 'T':
                        case 'M':
                        case 'E':
                            myClock.increment();
                            receivedClock = shop.closeTheDoor(myClock.clone());
                            myClock.update(receivedClock);
                            
                            canGoOut = !shop.customersInTheShop();
                            break;
                    }
                } while (!canGoOut);

                myClock.increment();
                receivedClock = shop.prepareToLeave(myClock.clone());
                myClock.update(receivedClock);
                if (sit == 'T') {           /* Transfer products */
                    myClock.increment();
                    ret = workshop.goToWorkshop(myClock.clone());
                    int nProducts = (int)ret[1];
                    myClock.update((VectorTimestamp)ret[0]);
                    
                    myClock.increment();
                    receivedClock = shop.returnToShop(nProducts, myClock.clone());
                    myClock.update(receivedClock);
                    
                } else if (sit == 'M') {    /* Materials needed */
                    ret = warehouse.visitSuppliers(myClock.clone());
                    int nMaterials = (int)ret[1];
                    myClock.update((VectorTimestamp)ret[0]);
                    
                    myClock.increment();
                    receivedClock = workshop.replenishStock(nMaterials, myClock.clone());
                    myClock.update(receivedClock);
                    
                    myClock.increment();
                    receivedClock = shop.returnToShop(-1, myClock.clone());
                    myClock.update(receivedClock);
                }
            } while(!log.endOpEntrep());
            System.out.println("Dona acabou execução!");
            
            log.Shutdown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    /**
     * Updates the state of the entrepreneur.
     * 
     * @param state The new state of the entrepreneur.
     */
    public void setState(EntrepreneurState state) {
        this.state = state;
    }
    /**
     * Gets the current state of the entrepreneur.
     * 
     * @return The state of the entrepreneur.
     */
    public EntrepreneurState getCurrentState() {
        return state;
    }
    /**
     * The entrepreneur services a customer.
     * 
     * @param id the customerIdentifier
     */
    private void serviceCustomer(int id) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrepreneur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
