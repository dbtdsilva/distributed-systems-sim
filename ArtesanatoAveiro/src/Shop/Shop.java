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
    private Queue<Integer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    
    private GeneralRepository generalRepo;
    
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
    public synchronized void iWantThis(int id) {
        nProductsStock -= 1;
        waitingLine.add(id);
        // Acordar a dona???
        try {
            wait();
        } catch (InterruptedException ex) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /* Entrepreneur related */
    public synchronized int addressACustomer() {
        if (waitingLine.size() == 0) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, 
                new Exception("Address a customer without nobody to address"));
        }
        return waitingLine.poll();
    }
    public synchronized void sayGoodByeToCustomer(int id) {
        // Acordar o customer com este id
    }
    public synchronized void closeTheDoor() {
        doorState = ShopDoorState.ECLOSED;
    }
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }
    public void prepareToLeave() {
        doorState = ShopDoorState.CLOSED;
    }
    
    public int getnProductsStock() {
        return nProductsStock;
    }
    public boolean isReqFetchProducts() {
        return reqFetchProducts;
    }
    public boolean isReqPrimeMaterials() {
        return reqPrimeMaterials;
    }
}
