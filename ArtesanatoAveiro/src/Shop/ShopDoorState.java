/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shop;

/**
 *
 * @author diogosilva
 */
public enum ShopDoorState {
    CLOSED, 
    ECLOSED,
    OPEN;
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
