/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Customer;

/**
 *
 * @author diogosilva
 */
public class Customer extends Thread {
    private CustomerState state;
    
    public Customer() {
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    @Override
    public void run() {
        do {
            livingNormalLife();
            goShopping();
        
            if(isDoorOpen())
            {
                enterShop();
                if(perusingAround())
                    iWantThis();
                
                exitShop();
            }
            else
                tryAgainLater();
        } while (!endOpCustomer());
    }
    public void livingNormalLife() {
        
    }
    public void goShopping() {
        
    }
    public boolean isDoorOpen() {
        return false;
    }
    public void enterShop() {
        
    }
    public boolean perusingAround() {
        return false;
    }
    public void iWantThis() {

    }
    public void tryAgainLater() {
        
    }
    public void exitShop() {
        
    }

    private boolean endOpCustomer() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
