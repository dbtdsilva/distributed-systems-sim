/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shop;

import Customer.Customer;
import Customer.CustomerState;
import Logger.Logging;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diogosilva
 */
public class Shop {
    private int nCustomersInside;
    private int nProductsStock;
    private ShopState shopState;
    private final Queue<Integer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    
    private final Logging log;
    
    public Shop(Logging log) {
        this.log = log;
        this.nProductsStock = 0;
        this.shopState = ShopState.CLOSED;
        this.nCustomersInside = 0;
        this.waitingLine = new LinkedList<>();
        this.reqFetchProducts = false;
        this.reqPrimeMaterials = false;
    }
    /* Customer related */
    public synchronized void goShopping(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CHECKING_SHOP_DOOR_OPEN);
        log.UpdateCustomerState(id, CustomerState.CHECKING_SHOP_DOOR_OPEN);
    }
    public synchronized boolean isDoorOpen() {
        return shopState == ShopState.OPEN;
    }
    public synchronized void enterShop() {
        nCustomersInside += 1;
    }
    public synchronized void exitShop(int id) {
        nCustomersInside -= 1;
    }
    public synchronized boolean perusingAround() {
        if (nProductsStock == 0)
            return false;
        return Math.random() > 0.3; // 70% probabilidade
    }
    public void iWantThis(int id) {
        synchronized (this) {
            nProductsStock -= 1;
            waitingLine.add(id);
            // Acordar a dona
        }
        // Adormecer o customer
    }
    public synchronized void tryAgainLater(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        log.UpdateCustomerState(id, CustomerState.CARRYING_OUT_DAILY_CHORES);
    }
    /**************************/
    /** Entrepreneur related **/
    /**************************/
    
    /**
     *
     * @return
     */
    public char appraiseSit() {
        char returnChar;
        while (true) {
            try {
                generalRepo.entrepreneurWake.acquire();
            } catch (InterruptedException ex) {
                Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            synchronized(this) {
                if (!waitingLine.isEmpty()) {
                    returnChar = 'C';
                    break;
                } else if (reqPrimeMaterials) {
                    returnChar = 'M';
                    break;
                } else if (reqFetchProducts) {
                    returnChar = 'T';
                    break;
                }
            }
        }
        return returnChar;
    }
    public synchronized int addressACustomer() {
        if (waitingLine.size() == 0) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, 
                new Exception("Address a customer without nobody to address"));
        }
        return waitingLine.poll();
    }
    public synchronized void sayGoodByeToCustomer(int id) {
        // Acordar customer com este ID
    }
    public synchronized void closeTheDoor() {
        shopState = ShopState.ECLOSED;
    }
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }
    public synchronized void prepareToLeave() {
        shopState = ShopState.CLOSED;
    }
    public synchronized void returnToShop(int nProductsTransfer) {
        if (nProductsTransfer > 0) {
            reqFetchProducts = false;
            nProductsStock += nProductsTransfer;
        } else if (reqPrimeMaterials) {
            reqPrimeMaterials = false;
        }
    }
    
    // Hm
    public int getnProductsStock() {
        return nProductsStock;
    }
    public boolean isReqFetchProducts() {
        return reqFetchProducts;
    }
    public boolean isReqPrimeMaterials() {
        return reqPrimeMaterials;
    }
    public synchronized void RequestFetchProducts()
    {
        this.reqFetchProducts = true;
    }
    public synchronized void RequestPrimeMaterials()
    {
        this.reqPrimeMaterials = true;
    }
}
