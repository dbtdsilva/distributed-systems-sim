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
public class Craftsman extends Thread {
    private CraftsmanState state;
    
    public Craftsman() {
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
    }
    @Override
    public void run() {
        
    }
    public void collectingMaterials() {
        
    }
    public void primeMaterialsNeeded() {
        
    }
    public void backToWork() {
        
    }
    public void prepareToProduce() {
        
    }
    public void batchReadyForTransfer() {
        
    }
    public void shappingItUp() {
        
    }
    public void goToStore() {
        
    }
}
