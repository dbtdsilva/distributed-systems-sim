package ClientSide.Customer;

import ServerSide.Logger.Logging;
import ServerSide.Shop.Shop;
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
    private final Logging log;
    
    /**
     * Initiliazes the customer class with the required information.
     * 
     * @param id The customer identifier.
     * @param log The general repository
     * @param shop The simulation shop where the customer will buy products.
     */
    public Customer(int id, Logging log, Shop shop) {
        this.setName("Customer "+id);
        this.id = id;
        this.shop = shop;
        this.log = log;
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
            shop.goShopping(id);
        
            if(shop.isDoorOpen()) {
                shop.enterShop(id);
                if ((nProducts = shop.perusingAround()) != 0)
                    shop.iWantThis(id, nProducts);
                shop.exitShop(id);
            }
            else {
                shop.tryAgainLater(id);
            }
        } while (!log.endOpCustomer());
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
}
