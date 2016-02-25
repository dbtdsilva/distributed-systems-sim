package ClientSide.Entrepreneur;

/**
 * This enumerate represents the state of entity Entrepreneur
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public enum EntrepreneurState {
    /**
     * Either it is the start of a new day just came back to the shop. 
     * Either way, the Entrepreneur is opening the shop.
     */
    OPENING_THE_SHOP("OTS"),
    /**
     * Things and calm and the Entrepreneur is pondering what the next task will be.
     */
    WAITING_FOR_NEXT_TASK("WFNT"),
    /**
     * There are customers waiting to buy some goods. She is attending to them.
     */
    ATTENDING_A_CUSTOMER("AAC"),
    /**
     * It is time to close the shop. Either at the end of the day or she has to go do some chores.
     */
    CLOSING_THE_SHOP("CTS"),
    /**
     * A request was made for the Entrepreneur to go empty the storage of the workshop.
     */
    COLLECTING_A_BATCH_OF_PRODUCTS("CABP"),
    /**
     * A request was made to the Entrepreneur and she now collecting the prime materials requestes by the
     * Craftsman.
     */
    AT_THE_SUPPLIERS("ATS"),
    /**
     * She is on her way to the workshop to deliver the prime materials requested.
     */
    DELIVERING_PRIME_MATERIALS("DPM");
    
    /**
     * Auxiliar variable that allows to transform the state into an acronym.
     *
     * @serialField acronym
     */
    private final String acronym;
    
    /**
     * Allows to transform the state into an acronym.
     * @param acronym 
     */
    private EntrepreneurState(String acronym) {
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
