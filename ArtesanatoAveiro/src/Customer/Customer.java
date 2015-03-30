package Customer;

import Shop.Shop;
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
    private final Shop shop;
    
    /**
     * Initiliazes the customer class with the required information.
     * 
     * @param id The customer identifier.
     * @param shop The simulation shop where the customer will buy products.
     */
    public Customer(int id, Shop shop) {
        this.setName("Customer "+id);
        this.id = id;
        this.shop = shop;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    /**
     * This function represents the life cycle of Customer.
     */
    @Override
    public void run() {
        do {
            livingNormalLife();
            shop.goShopping(id);
        
            if(shop.isDoorOpen()) {
                shop.enterShop(id);
                if (shop.perusingAround())
                {
                    shop.iWantThis(id);
                }
                shop.exitShop(id);
            }
            else {
                shop.tryAgainLater(id);
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
    public void livingNormalLife() {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    /**
     * Checks if the customer no longer has conditions to continue.
     * 
     * @return Returns false if the customer can continue; returns false if otherwise.
     */
    private boolean endOpCustomer() {
        return shop.isOutOfBusiness();
    }
}
