package ClientSide.Craftsman;

/**
 * This enumerate represents the state of entity Craftsman
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public enum CraftsmanState {
    /**
     * The Craftsman is going to get some prime materials to start working on another piece.
     */
    FETCHING_PRIME_MATERIALS("FPM"),
    /**
     * The Craftsman noticed that there were either prime materials missing or the storage 
     * for the finished products is full, so, he is now contacting the Entrepreneur.
    */
    CONTACTING_ENTREPRENEUR("CE"),
    /**
     * The Craftsman has collected the prime materials and is now working on the piece.
     */
    PRODUCING_A_NEW_PIECE("PANP"),
    /**
     * The Craftsman finished his new piece and is now storing in the workshop.
     */
    STORING_IT_FOR_TRANSFER("SIFT");
    
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
    private CraftsmanState(String acronym) {
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
