package ClientSide.Customer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Craftsman
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Customer extends Thread {
    private CustomerState state;
    private final int id;
    
    /**
     * Initiliazes the customer class with the required information.
     * 
     * @param id The customer identifier.
     */
    public Customer(int id) {
        this.setName("Customer "+id);
        this.id = id;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    /**
     * This function represents the life cycle of Customer.
     */
    @Override
    public void run() {
        int nProducts;
        do {
            livingNormalLife();
            goShopping(id);
        
            if(isDoorOpen()) {
                enterShop(id);
                if ((nProducts = perusingAround()) != 0)
                    iWantThis(id, nProducts);
                exitShop(id);
            }
            else {
                tryAgainLater(id);
            }
        } while (!endOpCustomer());
        System.out.println("Cliente "+id+" acabou execução!");
    }
    
    /**
     * Updates the state of the Customer.
     * 
     * @param state The new state of the customer.
     */
    public void setState(CustomerState state) {
        this.state = state;
    }
    /**
     * Gets the current state of the customer.
     * 
     * @return The state of the customer.
     */
    public CustomerState getCurrentState() {
        return state;
    }
    /**
     * The customer waits some time before going to the shop.
     */
    private void livingNormalLife() {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void goShopping(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean isDoorOpen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void enterShop(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int perusingAround() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void iWantThis(int id, int nProducts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void exitShop(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void tryAgainLater(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean endOpCustomer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
