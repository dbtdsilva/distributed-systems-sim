/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Shop.Shop;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diogosilva
 */
public class Customer extends Thread {
    private CustomerState state;
    public int id;
    
    private final Shop shop;
    
    public Customer(int id, Shop shop) {
        this.id = id;
        this.shop = shop;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    
    @Override
    public void run() {
        do {
            livingNormalLife();
            shop.goShopping(id);
        
            if(shop.isDoorOpen()) {
                shop.enterShop(id);
                if (shop.perusingAround())
                    shop.iWantThis(id);
                shop.exitShop(id);
            }
            else
                shop.tryAgainLater(id);
        } while (!endOpCustomer());
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
        return shop.getnProductsStock() == 0;
        // TO DO
    }
}
