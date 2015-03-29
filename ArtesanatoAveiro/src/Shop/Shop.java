package Shop;

import Craftsman.Craftsman;
import Craftsman.CraftsmanState;
import Customer.Customer;
import Customer.CustomerState;
import Entrepreneur.Entrepreneur;
import Entrepreneur.Entrepreneur.returnType;
import Entrepreneur.EntrepreneurState;
import Logger.Logging;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public class Shop {
    private int nCustomersInside;
    private int nProductsStock;
    private ShopState shopState;
    private final Queue<Integer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    private boolean outOfBusiness;
    private int requestEntrepreneur;
    
    private final Logging log;
    
    public Shop(Logging log) {
        this.requestEntrepreneur = 0;
        this.log = log;
        this.nProductsStock = 0;
        this.shopState = ShopState.CLOSED;
        this.nCustomersInside = 0;
        this.waitingLine = new LinkedList<>();
        this.reqFetchProducts = false;
        this.reqPrimeMaterials = false;
        this.outOfBusiness = false;
    }
    
        /**************/
        /** CUSTOMER **/
        /**************/  
    
    public synchronized void goShopping(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CHECKING_SHOP_DOOR_OPEN);
        log.UpdateCustomerState(id, CustomerState.CHECKING_SHOP_DOOR_OPEN);
    }   
    public synchronized boolean isDoorOpen() {
        return shopState == ShopState.OPEN;
    }
    public synchronized void enterShop(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.APPRAISING_OFFER_IN_DISPLAY);
        log.UpdateCustomerState(id, CustomerState.APPRAISING_OFFER_IN_DISPLAY);
        
        nCustomersInside += 1;
        log.WriteShop(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials);
    }
    public synchronized void exitShop(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        log.UpdateCustomerState(id, CustomerState.CARRYING_OUT_DAILY_CHORES);
        
        nCustomersInside -= 1;
        requestEntrepreneur++;
        notifyAll();        /* Telling entrepreneur */
        
        log.WriteShop(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials);
    }
    public synchronized boolean perusingAround() {
        if (nProductsStock == 0)
            return false;
        if (Math.random() > 0.3) { // 70% probabilidade
            nProductsStock -= 1;
            return true;
        } else {
            return false;
        }
    }
    public synchronized void iWantThis(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.BUYING_SOME_GOODS);
        log.UpdateCustomerState(id, CustomerState.BUYING_SOME_GOODS);
        
        waitingLine.add(id);
        
        log.CustomersBoughtGoods(id);
        log.WriteShop(shopState, nCustomersInside, nProductsStock, 
                    reqFetchProducts, reqPrimeMaterials);
        
        requestEntrepreneur++;
        notifyAll();    // Wake up entrepreneur
        
        while (waitingLine.contains(id)) {
            try {
                wait(); // Sleep customer
            } catch (InterruptedException ex) {
                Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public synchronized void tryAgainLater(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        log.UpdateCustomerState(id, CustomerState.CARRYING_OUT_DAILY_CHORES);
    }
  
        /******************/
        /** ENTREPRENEUR **/
        /******************/  
    public synchronized void prepareToWork() {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        log.UpdateEntreperneurState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        
        shopState = ShopState.OPEN;
        
        log.WriteShop(shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }
    public synchronized char appraiseSit() {
        char returnChar;
        while (true) {
            while (requestEntrepreneur == 0 && !outOfBusiness) {
                try {
                    wait();     // Entrepreneur needs to wait for the next tasks
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            requestEntrepreneur--;
            if (!waitingLine.isEmpty()) {
                returnChar = 'C';
                break;
            } else if (reqPrimeMaterials) {
                returnChar = 'M';
                break;
            } else if (reqFetchProducts) {
                returnChar = 'T';
                break;
            } else if (outOfBusiness) {
                returnChar = 'E';
                break;
            }
        }
        return returnChar;
    }
    public synchronized int addressACustomer() {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.ATTENDING_A_CUSTOMER);
        log.UpdateEntreperneurState(EntrepreneurState.ATTENDING_A_CUSTOMER);
        
        if (waitingLine.size() == 0) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, 
                new Exception("Address a customer without nobody to address"));
        }
        
        return waitingLine.poll();
    }
    public synchronized void sayGoodByeToCustomer(int id) {
        notifyAll();    // Acordar customer com este ID
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        log.UpdateEntreperneurState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
    }
    public synchronized void closeTheDoor() {
        shopState = ShopState.ECLOSED;
        log.WriteShop(this.shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }  
    public synchronized void prepareToLeave() {
        shopState = ShopState.CLOSED;
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.CLOSING_THE_SHOP);
        log.UpdateEntreperneurState(EntrepreneurState.CLOSING_THE_SHOP);
        log.WriteShop(shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }  
    public synchronized void returnToShop(returnType returnType) {
        if (reqFetchProducts && returnType == returnType.ProductsTransfer) {
            reqFetchProducts = false;
            nProductsStock += ((Entrepreneur) Thread.currentThread()).getProductsTransfer();
            ((Entrepreneur) Thread.currentThread()).setProductsTransfer(0);
        } else if (reqPrimeMaterials && returnType == returnType.PrimeMaterials) {
            reqPrimeMaterials = false;
        }
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.OPENING_THE_SHOP);
        log.UpdateEntreperneurState(EntrepreneurState.OPENING_THE_SHOP);
        
        this.shopState = ShopState.OPEN;
        log.WriteShop(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials);
        
    }
    
        /***************/
        /** CRAFTSMAN **/
        /***************/ 
    
    public synchronized boolean primeMaterialsNeeded() {
        if (reqPrimeMaterials)
            return false;
        
        reqPrimeMaterials = true;
        requestEntrepreneur++;
        notifyAll();
        
        log.WriteShop(shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
        return true;
    }
    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
    */
    public synchronized void batchReadyForTransfer(int id) {
        if (reqFetchProducts)
            return;
        
        reqFetchProducts = true;
        requestEntrepreneur++;
        notifyAll();
        
        ((Craftsman) Thread.currentThread()).setState(CraftsmanState.CONTACTING_ENTREPRENEUR);
        log.UpdateCraftsmanState(id, CraftsmanState.CONTACTING_ENTREPRENEUR);
        log.WriteShop(shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }
        /*************/
        /** GENERAL **/
        /*************/
    public synchronized int getnProductsStock() {
        return nProductsStock;
    }
    public synchronized boolean isReqFetchProducts() {
        return reqFetchProducts;
    }
    public synchronized boolean isReqPrimeMaterials() {
        return reqPrimeMaterials;
    }
    public synchronized void setOutOfBusiness() {
        this.outOfBusiness = true;
    }
    public synchronized boolean isOutOfBusiness() {
        return outOfBusiness;
    }
}
