package Customer;

import Shop.Shop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public class Customer extends Thread {
    private CustomerState state;
    public int id;
    
    private final Shop shop;
    
    public Customer(int id, Shop shop) {
        this.setName("Customer "+id);
        this.id = id;
        this.shop = shop;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    
    @Override
    public void run() {
        do {
            livingNormalLife();
            System.out.println("living normal life.");
            shop.goShopping(id);
            System.out.println("going shopping.");
        
            if(shop.isDoorOpen()) {
                System.out.println("door is open.");
                shop.enterShop(id);
                if (shop.perusingAround())
                    shop.iWantThis(id);
                shop.exitShop(id);
            }
            else
                shop.tryAgainLater(id);
        } while (!endOpCustomer());
        System.out.println("Cliente "+id+" acabou execução!");
    }
    
    public void setState(CustomerState state) {
        this.state = state;
    }
        
    public void livingNormalLife() {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean endOpCustomer() {
        return false;
        // TO DO
    }
}
