/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shop;

/**
 *
 * @author diogosilva
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