package Craftsman;

import Logger.Logging;
import Shop.Shop;
import Workshop.Workshop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Craftsman
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Craftsman extends Thread {
    private CraftsmanState state;
    private final Workshop workshop;
    private final Shop shop;
    private final Logging log;
    private final int id;
    
    /**
     * Initiliazes the craftsman class with the required information.
     * 
     * @param id The craftsman identifier.
     * @param log The general repository
     * @param shop The simulation shop where the craftsmen will request services
     *          to the Entrepreneur
     * @param workshop The simulation workshop where the craftsman will work.
     */
    public Craftsman(int id, Logging log, Shop shop, Workshop workshop) {
        this.setName("Craftsman "+id);
        this.shop = shop;
        this.id = id;
        this.workshop = workshop;
        this.log = log;
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
    }

    /**
     * This function represents the life cycle of Craftsman.
     */
    @Override
    public void run() {
        do {
            if (!workshop.collectingMaterials(id)) {
                workshop.primeMaterialsNeeded(id);
                workshop.backToWork(id);
            } else {
                workshop.prepareToProduce(id);
                shappingItUp();
                workshop.goToStore(id);
                if (workshop.getnProductsStored() >= workshop.MAX_ProductsStored)
                    shop.batchReadyForTransfer(id);
                workshop.backToWork(id);
            }
        } while (!log.endOperCraft());
        
        if (log.getNumberWorkingCraftsmen() == 0
                && workshop.getnProductsStored() != 0) {
            shop.batchReadyForTransfer(id);
        }
        System.out.println("Artesão "+id+" acabou execução!");
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
     * Gets the current state of the craftsman.
     * 
     * @return The state of the craftsman.
     */
    public CraftsmanState getCurrentState() {
        return state;
    }
    /**
     * The craftsman is working on the next piece.
     */
    private void shappingItUp() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Craftsman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
