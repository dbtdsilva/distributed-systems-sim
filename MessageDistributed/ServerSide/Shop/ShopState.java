package ServerSide.Shop;

/**
 * Enumerate used to represent the state of the Shop.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public enum ShopState {
    /**
     * The Shop is closed.
     */
    CLOSED("CLS"), 
    /**
     * The Shop is open, however, the door remains closed.
     */
    ECLOSED("ECLS"),
    /**
     * The Shop is open for business.
     */
    OPEN("OPEN");
    
    private final String acronym;
    private ShopState(String acronym) 
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
