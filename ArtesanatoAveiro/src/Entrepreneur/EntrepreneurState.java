/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entrepreneur;

/**
 *
 * @author diogosilva
 */
public enum EntrepreneurState {
    OPENING_THE_SHOP("OTS"),
    WAITING_FOR_NEXT_TASK("WFNT"),
    ATTENDING_A_CUSTOMER("AAC"),
    CLOSING_THE_SHOP("CTS"),
    COLLECTING_A_BATCH_OF_PRODUCTS("CABP"),
    AT_THE_SUPPLIERS("ATS"),
    DELIVERING_PRIME_MATERIALS("DPM");
    
    private String acronym;
    
    private EntrepreneurState(String acronym) 
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
