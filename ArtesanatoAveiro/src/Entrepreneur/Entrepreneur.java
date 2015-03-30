package Entrepreneur;

import Logger.Logging;
import Shop.Shop;
import Warehouse.Warehouse;
import Workshop.Workshop;
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
    private final Warehouse wh;
    private final Workshop ws;
    private final Logging log;
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     * 
     * @param log The general repository
     * @param shop The simulation shop where the entrepreneur will work.
     * @param wh The simulation warehouse where the entrepeneur fetchs prime 
     * materials when requested by the Craftsmen.
     * @param ws The simulation workshop where the craftsmen are located.
     */
    public Entrepreneur(Logging log, Shop shop, Warehouse wh, Workshop ws) {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.wh = wh;
        this.ws = ws;
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
                /*if (shop.getnProductsStock() == 0 
                        && ws.getnCurrentPrimeMaterials() < ProbConst.primeMaterialsPerProduct 
                        && ws.getnProductsStored() == 0
                        && wh.getNTimesSupplied() == ProbConst.nMaxSupplies
                        && !shop.isReqFetchProducts() && !shop.isReqPrimeMaterials()) {
                    shop.setOutOfBusiness();
                }*/
                
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
                int nProducts = ws.goToWorkshop();
                shop.returnToShop(nProducts);
            } else if (sit == 'M') {    /* Materials needed */
                int nMaterials = wh.visitSuppliers();
                ws.replenishStock(nMaterials);
                shop.returnToShop(0);
            }
        } while(!log.endOpEntrep());
        
        System.out.println("Dona acabou execução!");
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
}
