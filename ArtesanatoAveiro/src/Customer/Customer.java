/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

import Exec.GeneralRepository;
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
    private GeneralRepository rep;
    
    public Customer(int id, GeneralRepository rep, Shop shop) {
        this.id = id;
        this.rep = rep;
        this.shop = shop;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    @Override
    public void run() {
        //rep.log.UpdateCustomerState(id, state);
        do {
            livingNormalLife();
            goShopping();
        
            if(isDoorOpen())
            {
                enterShop();
                if (perusingAround())
                    iWantThis();
                exitShop();
            }
            else
                tryAgainLater();
        } while (!endOpCustomer());
    }
    public void livingNormalLife() {
        // state = CustomerState.CARRYING_OUT_DAILY_CHORES;
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void goShopping() {
        state = CustomerState.CHECKING_SHOP_DOOR_OPEN;
        //saveState()
    }
    public boolean isDoorOpen() {
        return shop.isDoorOpen();
    }
    public void enterShop() {
        state = CustomerState.APPRAISING_OFFER_IN_DISPLAY;
        shop.enterShop();
        // saveState?
    }
    public boolean perusingAround() {
        return shop.perusingAround();
    }
    public void iWantThis() {
        state = CustomerState.BUYING_SOME_GOODS;
        // saveState?
        shop.iWantThis(id);
    }
    public void exitShop() {
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
        shop.exitShop();
        // saveState?
    }
    public void tryAgainLater() {
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
        // saveState?
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private boolean endOpCustomer() {
        // WIP
        return shop.getnProductsStock() == 0;
    }
}
