package Craftsman;

import Workshop.Workshop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alve, 60340
 */

public class Craftsman extends Thread {
    private CraftsmanState state;
    private int nFinishedProducts;
    private final Workshop ws;
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
        nFinishedProducts = 0;
    }

    @Override
    public void run() {
        do {
            if (!ws.collectingMaterials(id)) {
                ws.primeMaterialsNeeded(id);
                ws.backToWork(id);
            } else {
                ws.prepareToProduce(id);
                shappingItUp();
                ws.goToStore(id);
                if (ws.getnProductsStored() >= ws.MAX_ProductsStored)
                    ws.batchReadyForTransfer(id);
                ws.backToWork(id);
            }
        } while (!endOperCraft());
    }
    
    /**
     * Updates the state of the craftsman.
     * 
     * @param state The new state of the craftsman.
     */
    public void setState(CraftsmanState state) {
        this.state = state;
    }
    
    /**
     * The craftsman is working on the next piece.
     */
    public void shappingItUp() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Craftsman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 
     * After the craftsman finishes the product, the number of products that he manufactured until now.
     * 
     */
    public void updateFinishedProducts() {
        nFinishedProducts++;
    }

    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns false if otherwise.
     */
    private boolean endOperCraft() {
        return (ws.getnCurrentPrimeMaterials() == 0 && ws.isAnyTransferOccuring());
    }
}
