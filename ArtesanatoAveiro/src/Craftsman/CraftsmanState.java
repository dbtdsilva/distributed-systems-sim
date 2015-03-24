/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Craftsman;

/**
 *
 * @author diogosilva
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
