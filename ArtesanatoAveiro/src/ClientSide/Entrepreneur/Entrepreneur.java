package ClientSide.Entrepreneur;

import ServerSide.Logger.Logging;
import ServerSide.Shop.Shop;
import ServerSide.Warehouse.Warehouse;
import ServerSide.Workshop.Workshop;
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
    private final Shop shop;
    private final Warehouse warehouse;
    private final Workshop workshop;
    private final Logging log;
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     * 
     * @param log The general repository
     * @param shop The simulation shop where the entrepreneur will work.
     * @param warehouse The simulation warehouse where the entrepeneur fetchs prime 
     * materials when requested by the Craftsmen.
     * @param workshop The simulation workshop where the craftsmen are located.
     */
    public Entrepreneur(Logging log, Shop shop, Warehouse warehouse, Workshop workshop) {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.warehouse = warehouse;
        this.workshop = workshop;
        this.log = log;
    }
    /**
     * This function represents the life cycle of Entrepreneur.
     */
    @Override
    public void run() {
        do {
            boolean canGoOut = false;
            char sit;
            
            shop.prepareToWork();
            do {                
                sit = shop.appraiseSit();
                switch (sit) {
                    case 'C': 
                        int id = shop.addressACustomer();
                        serviceCustomer(id);
                        shop.sayGoodByeToCustomer(id);
                        break;
                    case 'T':
                    case 'M':
                    case 'E':
                        shop.closeTheDoor();
                        canGoOut = !shop.customersInTheShop();
                        break;
                }
            } while (!canGoOut);
            
            shop.prepareToLeave();
            if (sit == 'T') {           /* Transfer products */
                int nProducts = workshop.goToWorkshop();
                shop.returnToShop(nProducts);
            } else if (sit == 'M') {    /* Materials needed */
                int nMaterials = warehouse.visitSuppliers();
                workshop.replenishStock(nMaterials);
                shop.returnToShop(-1);
            }
        } while(!log.endOpEntrep());
        System.out.println("Dona acabou execução!");
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
