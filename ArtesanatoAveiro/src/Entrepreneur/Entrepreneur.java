/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entrepreneur;

import Shop.Shop;
import Warehouse.Warehouse;
import Workshop.Workshop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diogosilva
 */
public class Entrepreneur extends Thread {
    private EntrepreneurState state;
    private Shop shop;
    private Warehouse wh;
    private Workshop ws;
    
    private int nProductsTransfer = 0;
    private int nMaterialsTransfer = 0;
    
    public Entrepreneur(Shop shop, Warehouse wh, Workshop ws) {
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
            prepareToWork();
            boolean canGoOut = false;
            char sit;
            do {
                sit = appraiseSit();
                switch (sit) {
                    case 'C': 
                        int id = addressACustomer();
                        serviceCustomer(id);
                        sayGoodByeToCustomer(id);
                        break;
                    case 'T':
                    case 'M':
                        closeTheDoor();
                        canGoOut = !customersInTheShop();
                        break;
                }
            } while (!canGoOut);
            
            prepareToLeave();
            if (sit == 'T') {
                goToWorkshop();
            } else if (sit == 'M') {
                visitSuppliers();
                replenishStock();
            }
            returnToShop();
        } while(!endOpEntrep());

    }
    public char appraiseSit() {
        return shop.appraiseSit();
    }
    public void prepareToWork() {
        state = EntrepreneurState.WAITING_FOR_NEXT_TASK;
        // saveState()..
    }
    public int addressACustomer() {
        state = EntrepreneurState.ATTENDING_A_CUSTOMER;
        int id = shop.addressACustomer();
        // saveState()
        return id;
    }
    public void serviceCustomer(int id) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrepreneur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void sayGoodByeToCustomer(int id) {
        state = EntrepreneurState.WAITING_FOR_NEXT_TASK;
        shop.sayGoodByeToCustomer(id);
        // saveState()..
    }
    public void closeTheDoor() {
        shop.closeTheDoor();
    }
    public boolean customersInTheShop() {
        return false;
    }
    public void prepareToLeave() {
        state = EntrepreneurState.CLOSING_THE_SHOP;
        shop.prepareToLeave();
        // saveState() ..
    }
    public void goToWorkshop() {
        nProductsTransfer = ws.goToWorkshop();
        state = EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS;
        // saveState;
    }
    public void visitSuppliers() {
        state = EntrepreneurState.AT_THE_SUPPLIERS;
        nMaterialsTransfer = wh.visitSuppliers();
        // saveState;
    }
    public void replenishStock() {
        state = EntrepreneurState.DELIVERING_PRIME_MATERIALS;
        ws.replenishStock(nMaterialsTransfer);
        nMaterialsTransfer = 0;
        // saveState;
    }
    public void returnToShop() {
        shop.returnToShop(nProductsTransfer);
        if (nProductsTransfer > 0)
            nProductsTransfer = 0;
        state = EntrepreneurState.OPENING_THE_SHOP;
        // saveState?
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
}
