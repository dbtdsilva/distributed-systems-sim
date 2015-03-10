/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entrepreneur;

import Customer.Customer;

/**
 *
 * @author diogosilva
 */
public class Entrepreneur extends Thread {
    private EntrepreneurState state;
    
    public Entrepreneur() {
        state = EntrepreneurState.OPENING_THE_SHOP;
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
    public void prepareToWork() {
        
    }
    public int addressACustomer() {
        return 0;
    }
    public void serviceCustomer(int id) {
        
    }
    public void sayGoodByeToCustomer(int id) {
     
    }
    public char appraiseSit() {
        return 't';
    }
    public void closeTheDoor() {
        
    }
    public boolean customersInTheShop() {
        return false;
    }
    public void prepareToLeave() {
        
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
