package Entrepreneur;

import Exec.ProbConst;
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
     
    private int nProductsTransfer = 0;
    private int nMaterialsTransfer = 0;
    
    /**
     * Enumerate used to represent the type of return when Entrepreneur is 
     * requested by the Craftsmen, she needs to know what she have done in case
     * of being request to do both operations at the same time.
     */
    public enum returnType{PrimeMaterials, ProductsTransfer};
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     * 
     * @param shop The simulation shop where the entrepreneur will work.
     * @param wh The simulation warehouse where the entrepeneur fetchs prime 
     * materials when requested by the Craftsmen.
     * @param ws The simulation workshop where the craftsmen are located.
     */
    public Entrepreneur(Shop shop, Warehouse wh, Workshop ws) {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.wh = wh;
        this.ws = ws;
        nProductsTransfer = 0;
        nMaterialsTransfer = 0;
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
                if (shop.getnProductsStock() == 0 
                        && ws.getnCurrentPrimeMaterials() < ProbConst.primeMaterialsPerProduct 
                        && ws.getnProductsStored() == 0
                        && wh.getnCurrentPrimeMaterials() < ProbConst.primeMaterialsPerProduct
                        && !shop.isReqFetchProducts() && !shop.isReqPrimeMaterials()) {
                    shop.setOutOfBusiness();
                }
                
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
                ws.goToWorkshop();
                shop.returnToShop(returnType.ProductsTransfer);
            } else if (sit == 'M') {    /* Materials needed */
                wh.visitSuppliers();
                ws.replenishStock();
                shop.returnToShop(returnType.PrimeMaterials);
            }
        } while(!endOpEntrep());
        System.out.println("Dona acabou execução!");
    }
    /**
     * The entrepreneur services a customer.
     * 
     * @param id the customerIdentifier
     */
    public void serviceCustomer(int id) {
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
    /**
     * Sets the number of products being transfered from the workshop to the shop.
     * 
     * @param nProducts the number of products being transfered
     */
    public void setProductsTransfer(int nProducts) {
        nProductsTransfer = nProducts;
    }
    /**
     * Sets the number of prime materials being transfered from the warehouse
     * to the workshop.
     * 
     * @param nPrimeMaterials the number of prime materials being transfered 
     */
    public void setNMaterialsTranfer(int nPrimeMaterials) {
        nMaterialsTransfer = nPrimeMaterials;
    }
    /**
     * Gets the number of products being transfered from the workshop to the shop.
     * 
     * @return number of products being transfered.
     */
    public int getProductsTransfer() {
        return nProductsTransfer;
    }
    /**
     * Gets the number of prime materials being transfered from the warehouse to
     * the workshop.
     * 
     * @return number of prime materials being transfered.
     */
    public int getNMaterialsTranfer() {
        return nMaterialsTransfer;
    }
    /**
     * Checks if the entrepeneur no longer has conditions to continue its work.
     * 
     * @return Returns false if the entrepreneur can continue its work; returns 
     * false if otherwise.
     */
    private boolean endOpEntrep() {
        return shop.isOutOfBusiness() & !shop.customersInTheShop();
    }
}
