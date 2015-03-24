/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shop;

import Customer.Customer;
import Exec.GeneralRepository;
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
    private ShopDoorState doorState;
    private final Queue<Integer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    
    private final GeneralRepository generalRepo;
    
    public Shop(GeneralRepository gr) {
        this.generalRepo = gr;
        this.nProductsStock = 0;
        this.doorState = ShopDoorState.CLOSED;
        this.nCustomersInside = 0;
        this.waitingLine = new LinkedList<>();
        this.reqFetchProducts = false;
        this.reqPrimeMaterials = false;
    }
    /* Customer related */
    public synchronized boolean isDoorOpen() {
        return doorState == ShopDoorState.OPEN;
    }
    public synchronized void enterShop() {
        nCustomersInside += 1;
    }
    public synchronized void exitShop() {
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
            generalRepo.entrepreneurWake.release();
        }
        
        // Adormecer o customer
        try {
            generalRepo.customersWaiting[id].acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        generalRepo.customersWaiting[id].release();
    }
    public synchronized void closeTheDoor() {
        doorState = ShopDoorState.ECLOSED;
    }
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }
    public synchronized void prepareToLeave() {
        doorState = ShopDoorState.CLOSED;
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
