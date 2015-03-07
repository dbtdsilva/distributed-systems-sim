/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entrepreneur;

/**
 *
 * @author diogosilva
 */
public enum EntrepreneurState {
    OPENING_THE_SHOP,
    WAITING_FOR_NEXT_TASK,
    ATTENDING_A_CUSTOMER,
    CLOSING_THE_SHOP,
    COLLECTING_A_BATCH_OF_PRODUCTS,
    AT_THE_SUPPLIERS,
    DELIVERING_PRIME_MATERIALS;
    
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
