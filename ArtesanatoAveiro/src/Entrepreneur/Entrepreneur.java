package Entrepreneur;

import Exec.ProbConst;
import Shop.Shop;
import Warehouse.Warehouse;
import Workshop.Workshop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
    
    public enum returnType{PrimeMaterials, ProductsTransfer};
    
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
    public void serviceCustomer(int id) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrepreneur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean endOpEntrep() {
        return shop.isOutOfBusiness()
                        && !shop.customersInTheShop();
        /*return  shop.getnProductsStock() == 0 &&
                !shop.isReqPrimeMaterials() &&
                !shop.isReqFetchProducts() &&
                wh.getnCurrentPrimeMaterials() < ProbConst.primeMaterialsPerProduct &&
                ws.getnCurrentPrimeMaterials() < ProbConst.primeMaterialsPerProduct &&
                ws.getnProductsStored() == 0 &&
                !shop.customersInTheShop();*/
    }

    public void setState(EntrepreneurState state) {
        this.state = state;
    }
    
    public void setProductsTransfer(int val) {
        nProductsTransfer = val;
    }
    public void setNMaterialsTranfer(int val) {
        nMaterialsTransfer = val;
    }
    public int getProductsTransfer() {
        return nProductsTransfer;
    }
    public int getNMaterialsTranfer() {
        return nMaterialsTransfer;
    }
}
