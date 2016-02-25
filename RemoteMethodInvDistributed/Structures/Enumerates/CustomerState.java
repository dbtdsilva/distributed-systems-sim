package Structures.Enumerates;

/**
 * This enumerate represents the state of entity Customer
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public enum CustomerState {
    /**
     * The Customer is living his life normally.
     */
    CARRYING_OUT_DAILY_CHORES("CODC"),
    /**
     * The Customer approached the door and is checking if the shop is open for business.
     */
    CHECKING_SHOP_DOOR_OPEN("CSDO"),
    /**
     * The Customer is browsing the goods in display and checking if he wants something or not.
     */
    APPRAISING_OFFER_IN_DISPLAY("AOID"),
    /**
     * The Customer has found something he likes and is now in the transaction process.
     */
    BUYING_SOME_GOODS("BSG");
    
    /**
     * Auxiliar variable that allows to transform the state into an acronym.
     *
     * @serialField acronym
     */
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
