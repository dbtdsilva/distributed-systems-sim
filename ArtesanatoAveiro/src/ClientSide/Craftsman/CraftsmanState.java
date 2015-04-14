package ClientSide.Craftsman;

/**
 * This enumerate represents the state of entity Craftsman
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public enum CraftsmanState {
    FETCHING_PRIME_MATERIALS("FPM"),
    CONTACTING_ENTREPRENEUR("CE"),
    PRODUCING_A_NEW_PIECE("PANP"),
    STORING_IT_FOR_TRANSFER("SIFT");
    
    private final String acronym;
    private CraftsmanState(String acronym) 
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
