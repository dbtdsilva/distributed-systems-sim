package ClientSide.Entrepreneur;

/**
 * This enumerate represents the state of entity Entrepreneur
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public enum EntrepreneurState {
    OPENING_THE_SHOP("OTS"),
    WAITING_FOR_NEXT_TASK("WFNT"),
    ATTENDING_A_CUSTOMER("AAC"),
    CLOSING_THE_SHOP("CTS"),
    COLLECTING_A_BATCH_OF_PRODUCTS("CABP"),
    AT_THE_SUPPLIERS("ATS"),
    DELIVERING_PRIME_MATERIALS("DPM");
    
    private final String acronym;
    private EntrepreneurState(String acronym) 
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
