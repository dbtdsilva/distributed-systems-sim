/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

/**
 *
 * @author diogosilva
 */
public enum CustomerState {
    CARRYING_OUT_DAILY_CHORES,
    CHECKING_SHOP_DOOR_OPEN,
    APPRAISING_OFFER_IN_DISPLAY,
    BUYING_SOME_GOODS;
    
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
