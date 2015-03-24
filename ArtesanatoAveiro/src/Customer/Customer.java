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
    public void setState(CustomerState state) {
        this.state = state;
    }
    @Override
    public void run() {
        do {
            livingNormalLife();
            shop.goShopping(id);
        
            if(shop.isDoorOpen()) {
                shop.enterShop();
                if (shop.perusingAround())
                    shop.iWantThis(id);
                shop.exitShop(id);
            }
            else
                tryAgainLater();
        } while (!endOpCustomer());
    }
    public void livingNormalLife() {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void goShopping() {
        state = CustomerState.CHECKING_SHOP_DOOR_OPEN;
        //rep.log.UpdateCustomerState(id, state);
    }
    public boolean isDoorOpen() {
        return shop.isDoorOpen();
    }
    public void enterShop() {
        state = CustomerState.APPRAISING_OFFER_IN_DISPLAY;
        shop.enterShop();
        //rep.log.UpdateCustomerState(id, state);
    }
    public boolean perusingAround() {
        return shop.perusingAround();
    }
    public void iWantThis() {
        state = CustomerState.BUYING_SOME_GOODS;
        //rep.log.UpdateCustomerState(id, state);
        shop.iWantThis(id);
    }
    public void exitShop() {
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
        shop.exitShop();
        //rep.log.UpdateCustomerState(id, state);
    }
    
    
    
    public void tryAgainLater() {
        shop.tryAgainLater(id);
        
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
