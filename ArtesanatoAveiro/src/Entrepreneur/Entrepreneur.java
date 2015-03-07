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
        
    }
    public void prepareToWork() {
        
    }
    public void addressACustomer(Customer c) {
        
    }
    public void serviceCustomer(Customer c) {
        
    }
    public void sayGoodByeToCustomer(Customer c) {
     
    }
    public void appraiseSit() {
        
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
    public void takePrimeMaterialsToWorkshop() {
        
    }
}
