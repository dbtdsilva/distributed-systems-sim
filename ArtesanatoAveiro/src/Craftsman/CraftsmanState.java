/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Craftsman;

/**
 *
 * @author diogosilva
 */
public enum CraftsmanState {
    FETCHING_PRIME_MATERIALS,
    CONTACTING_ENTREPRENEUR,
    PRODUCING_A_NEW_PIECE,
    STORING_IT_FOR_TRANSFER;
    
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
