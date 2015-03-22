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
    
    public Entrepreneur(Shop shop, Warehouse wh, Workshop ws) {
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.wh = wh;
        this.ws = ws;
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
                    case 'E': 
                        closeTheDoor();
                        canGoOut = !customersInTheShop();
                        break;
                    default:
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
        } while(!endOpEntrep());

    }
    public char appraiseSit() {
        return 't';
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
    public void returnToShop() {
        
    }
    public void goToWorkshop() {
        
    }
    public void visitSuppliers() {
        
    }
    public void replenishStock() {
        
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
