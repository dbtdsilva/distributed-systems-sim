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
    CARRYING_OUT_DAILY_CHORES("CODC"),
    CHECKING_SHOP_DOOR_OPEN("CSDO"),
    APPRAISING_OFFER_IN_DISPLAY("AOID"),
    BUYING_SOME_GOODS("BSG");
    
    private String acronym;
    
    private CustomerState(String acronym) 
    {
        this.acronym = acronym;
    }
    
    public String getAcronym() {
        return acronym;
    }
    
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
