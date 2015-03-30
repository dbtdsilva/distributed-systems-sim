package Craftsman;

import Shop.Shop;
import Warehouse.Warehouse;
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
    private int nFinishedProducts;
    private final Workshop ws;
    private final Shop shop;
    private final Warehouse wh;
    private final int id;
    
    /**
     * Initiliazes the craftsman class with the required information.
     * 
     * @param id The craftsman identifier.
     * @param shop The simulation shop where the craftsmen will request services
     *          to the Entrepreneur
     * @param ws The simulation workshop where the craftsman will work.
     * @param wh The simulation warehouse
     */
    public Craftsman(int id, Shop shop, Workshop ws, Warehouse wh) {
        this.setName("Craftsman "+id);
        this.shop = shop;
        this.id = id;
        this.ws = ws;
        this.wh = wh;
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
        nFinishedProducts = 0;
    }

    /**
     * This function represents the life cycle of Craftsman.
     */
    @Override
    public void run() {
        do {
            if (!ws.collectingMaterials(id)) {
                primeMaterialsNeeded();
                ws.backToWork(id);
            } else {
                ws.prepareToProduce(id);
                shappingItUp();
                ws.goToStore(id);
                if (ws.getnProductsStored() >= ws.MAX_ProductsStored)
                    shop.batchReadyForTransfer(id);
                ws.backToWork(id);
            }
        } while (!endOperCraft());
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
     * The craftsman needs prime materials to work. He will tell the 
     * entrepreneur that needs prime materials and wait for her.
     */
    public void primeMaterialsNeeded() {
        boolean contacted = shop.primeMaterialsNeeded();
        ws.primeMaterialsNeeded(id, contacted);
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
     * After the craftsman finishes the product, the number of products that he manufactured until now.
     */
    public void updateFinishedProducts() {
        nFinishedProducts++;
    }
    /**
     * Returns the number of finished products till the moment.
     * 
     * @return the number of finished products
     */
    public int getFinishedProducts() {
        return nFinishedProducts;
    }

    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns 
     * false if otherwise.
     */
    private boolean endOperCraft() {
        return (ws.getnCurrentPrimeMaterials() == 0 && 
                wh.getnCurrentPrimeMaterials() == 0 &&
                !shop.isReqPrimeMaterials());
    }
}
