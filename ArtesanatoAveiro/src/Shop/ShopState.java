package Shop;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public enum ShopState {
    CLOSED("CLS"), 
    ECLOSED("ECLS"),
    OPEN("OPEN");
    
    private String acronym;
    
    private ShopState(String acronym) 
    {
        this.acronym = acronym;
    }
    
    public String getAcronym() {
        return acronym;
    }
    
    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
