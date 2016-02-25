package Customer;

/**
 * This enumerate represents the state of entity Customer
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public enum CustomerState {
    CARRYING_OUT_DAILY_CHORES("CODC"),
    CHECKING_SHOP_DOOR_OPEN("CSDO"),
    APPRAISING_OFFER_IN_DISPLAY("AOID"),
    BUYING_SOME_GOODS("BSG");
    
    private final String acronym;
    private CustomerState(String acronym) 
    {
        this.acronym = acronym;
    }
    
    /**
     * Returns a smaller representation of the state (an acronym)
     * 
     * @return state as an acronym
     */
    public String getAcronym() {
        return acronym;
    }
    
    /**
     * The method is used to get a String representation of the state
     * 
     * @return the state as String
     */
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
