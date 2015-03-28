package Entrepreneur;

import Shop.Shop;
import Warehouse.Warehouse;
import Workshop.Workshop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alve, 60340
 */
public class Entrepreneur extends Thread {
    private EntrepreneurState state;
    private final Shop shop;
    private final Warehouse wh;
    private final Workshop ws;
    
    private int nProductsTransfer = 0;
    private int nMaterialsTransfer = 0;
    
    public Entrepreneur(Shop shop, Warehouse wh, Workshop ws) {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.wh = wh;
        this.ws = ws;
        nProductsTransfer = 0;
        nMaterialsTransfer = 0;
    }
    
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
                        shop.closeTheDoor();
                        canGoOut = !shop.customersInTheShop();
                        break;
                }
            } while (!canGoOut);
            
            shop.prepareToLeave();
            if (sit == 'T') {
                nProductsTransfer = ws.goToWorkshop();
            } else if (sit == 'M') {
                nMaterialsTransfer = wh.visitSuppliers();
                ws.replenishStock(nMaterialsTransfer);
            }
            shop.returnToShop(nProductsTransfer);
        } while(!endOpEntrep());
        System.out.println("Dona acabou execução!");
    }
    public void serviceCustomer(int id) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrepreneur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean endOpEntrep() {
        return  !shop.customersInTheShop() &&
                shop.getnProductsStock() == 0 &&
                !shop.isReqPrimeMaterials() &&
                !shop.isReqFetchProducts() &&
                wh.getnCurrentPrimeMaterials() == 0 &&
                ws.getnCurrentPrimeMaterials() < ws.primeMaterialsPerProduct &&
                ws.getnProductsStored() == 0;
    }

    public void setState(EntrepreneurState state) {
        this.state = state;
    }
    
    public void resetNMaterialsTransfer() {
        nMaterialsTransfer = 0;
    }

    public void productsTransferedToShop() {
        nProductsTransfer = 0;
    }
}
