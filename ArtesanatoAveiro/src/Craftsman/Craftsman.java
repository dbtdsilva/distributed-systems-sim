/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Craftsman;

import Workshop.Workshop;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alve, 60340
 */
public class Craftsman extends Thread {
    private CraftsmanState state;
    private Workshop ws;
    public int id;
    
    /**
     * Initiliazes the craftsman class with the required information.
     * 
     * @param id The craftsman identifier.
     * @param ws The simulation workshop where the craftsman will work.
     */
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
    
    /**
     * The craftsman is preparing to manufacture a product.
     * 
     * @return If there are available prime materials at the workshop, it returns true; otherwise, it returns false.
     */
    public boolean collectingMaterials() {
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
        //Write craftsman state
        return ws.collectingMaterials();
    }
    
    /**
     * The crafstman found that there are not enough materials at the workshop, and so, it will notify the entreperneur, 
     * asking her to fetch prime materials.
     * 
     */
    public void primeMaterialsNeeded() {
        state = CraftsmanState.CONTACTING_ENTREPRENEUR;
        //Write craftsman state
        ws.primeMaterialsNeeded(id);
    }
    
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     */
    public void backToWork() {
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
        //Write craftsman state
    }
    
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     */
    public void prepareToProduce() {
        state = CraftsmanState.PRODUCING_A_NEW_PIECE;
        //Write craftsman state
    }
    
    /**
     * The craftsman has noticed that the number of products that can be stored has reached the limit 
     * and will notify the entrepreneur.
     */
    public void batchReadyForTransfer() {
        state = CraftsmanState.CONTACTING_ENTREPRENEUR;
        //Write craftsman state
        ws.batchReadyForTransfer(id);
    }
    
    /**
     * The craftsman is working on the next piece.
     */
    public void shappingItUp() {
        //Calculate random time for the craftsman to sleep
    }
    
    /**
     * After the craftsman finished the piece we was working on, it takes it to the workshop storage
     * to begin another one.
     */
    public void goToStore() {
        state = CraftsmanState.STORING_IT_FOR_TRANSFER;
    }

    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns false if otherwise.
     */
    private boolean endOperCraft() {
        return false;
    }
}
