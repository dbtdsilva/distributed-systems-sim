package ClientSide.Entrepreneur;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Entrepreneur
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Entrepreneur extends Thread {
    private EntrepreneurState state;
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     */
    public Entrepreneur() {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
    }
    /**
     * This function represents the life cycle of Entrepreneur.
     */
    @Override
    public void run() {
        do {
            boolean canGoOut = false;
            char sit;
            
            prepareToWork();
            do {                
                sit = appraiseSit();
                switch (sit) {
                    case 'C': 
                        int id = addressACustomer();
                        serviceCustomer(id);
                        sayGoodByeToCustomer(id);
                        break;
                    case 'T':
                    case 'M':
                    case 'E':
                        closeTheDoor();
                        canGoOut = !customersInTheShop();
                        break;
                }
            } while (!canGoOut);
            
            prepareToLeave();
            if (sit == 'T') {           /* Transfer products */
                int nProducts = goToWorkshop();
                returnToShop(nProducts);
            } else if (sit == 'M') {    /* Materials needed */
                int nMaterials = visitSuppliers();
                replenishStock(nMaterials);
                returnToShop(-1);
            }
        } while(!endOpEntrep());
        System.out.println("Dona acabou execução!");
    }
    /**
     * Updates the state of the entrepreneur.
     * 
     * @param state The new state of the entrepreneur.
     */
    public void setState(EntrepreneurState state) {
        this.state = state;
    }
    /**
     * Gets the current state of the entrepreneur.
     * 
     * @return The state of the entrepreneur.
     */
    public EntrepreneurState getCurrentState() {
        return state;
    }
    /**
     * The entrepreneur services a customer.
     * 
     * @param id the customerIdentifier
     */
    private void serviceCustomer(int id) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrepreneur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void prepareToWork() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private char appraiseSit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int addressACustomer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void sayGoodByeToCustomer(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void closeTheDoor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean customersInTheShop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void prepareToLeave() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int goToWorkshop() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void returnToShop(int nProducts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int visitSuppliers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void replenishStock(int nMaterials) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean endOpEntrep() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
