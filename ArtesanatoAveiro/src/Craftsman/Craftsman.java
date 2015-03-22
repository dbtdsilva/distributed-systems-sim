/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Craftsman;

import Customer.CustomerState;
import Workshop.Workshop;

/**
 *
 * @author diogosilva
 */
public class Craftsman extends Thread {
    private CraftsmanState state;
    private Workshop ws;
    public int id;
    
    public Craftsman(int id, Workshop ws) {
        this.id = id;
        this.ws = ws;
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
    }

    @Override
    public void run() {
        do {
            if (!collectingMaterials()) {
                primeMaterialsNeeded();
                backToWork();
            } else {
                prepareToProduce();
                shappingItUp();
                goToStore();
                if (ws.getnProductsStored() >= ws.MAX_ProductsStored)
                    batchReadyForTransfer();
                backToWork();
            }
        } while (!endOperCraft());
    }
    public boolean collectingMaterials() {
        return false;
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

    private boolean endOperCraft() {
        return false;
    }
}
