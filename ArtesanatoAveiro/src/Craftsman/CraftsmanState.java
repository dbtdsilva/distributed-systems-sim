package Craftsman;

/**
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
    
    public String getAcronym() {
        return acronym;
    }
    
    @Override
    public String toString() {
        return this.name().replace("_", " ").toLowerCase();
    }
}
