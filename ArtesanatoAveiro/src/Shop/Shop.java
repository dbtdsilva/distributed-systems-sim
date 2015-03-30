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
 * The monitor that represents the Shop.
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
    
    /**
     * The customer goes to the Shopping.
     * 
     * @param id customer identifier
     */
    public synchronized void goShopping(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CHECKING_SHOP_DOOR_OPEN);
        log.UpdateCustomerState(id, CustomerState.CHECKING_SHOP_DOOR_OPEN);
    }
    /**
     * This function allows the customer to check if the door is open or not.
     * 
     * @return returns true if the shop is open; returns false if otherwise.
     */
    public synchronized boolean isDoorOpen() {
        return shopState == ShopState.OPEN;
    }
    /**
     * The customers enters in the shop. He updates his state and the number of
     * customers inside the shop.
     * 
     * @param id customer identifier
     */
    public synchronized void enterShop(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.APPRAISING_OFFER_IN_DISPLAY);
        log.UpdateCustomerState(id, CustomerState.APPRAISING_OFFER_IN_DISPLAY);
        
        nCustomersInside += 1;
        log.WriteShop(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials);
    }
    /**
     * The customer exits the shop, notifying the Entrepreneur that he left. He
     * need to update the number of customers inside the shop and update his state.
     * He will also notify the Entrepreneur to wake up, she might need to leave
     * the shop.
     * 
     * @param id customer identifier 
     */
    public synchronized void exitShop(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        log.UpdateCustomerState(id, CustomerState.CARRYING_OUT_DAILY_CHORES);
        
        nCustomersInside -= 1;
        requestEntrepreneur++;
        notifyAll();        /* Telling entrepreneur */
        
        log.WriteShop(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials);
    }
    /**
     * The customer searchs for a products inside the Shop.
     * 
     * @return If there's no products, returns false. If there's products, it will return
     * true 70% of the cases.
     */
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
    /**
     * The customer requests the entrepreneur that he wants to buy a product. 
     * He will wait for the entrepreneur on the waiting line.
     * 
     * @param id customer identifier
     */
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
    /**
     * The customer will try to enter the shop later.
     * 
     * @param id customer identifier
     */
    public synchronized void tryAgainLater(int id) {
        ((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        log.UpdateCustomerState(id, CustomerState.CARRYING_OUT_DAILY_CHORES);
    }
  
        /******************/
        /** ENTREPRENEUR **/
        /******************/  
    
    /**
     * Entrepreneur is preparing to work, she will open the shop.
     */
    public synchronized void prepareToWork() {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        log.UpdateEntreperneurState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        
        shopState = ShopState.OPEN;
        
        log.WriteShop(shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }
    /**
     * The entrepreneur will wait until someone request her services.
     * 
     * @return  'C', if customers waiting for service;
     *          'M', if craftsman requested for prime materials;
     *          'T', if craftsman requested to fetch the products in the Workshop;
     *          'E', if the shop is out of business.
     */
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
    /**
     * The Entrepreneur address the first customer in the waiting line.
     * 
     * @return the customer identifier
     */
    public synchronized int addressACustomer() {
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.ATTENDING_A_CUSTOMER);
        log.UpdateEntreperneurState(EntrepreneurState.ATTENDING_A_CUSTOMER);
        
        if (waitingLine.size() == 0) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, 
                new Exception("Address a customer without nobody to address"));
        }
        
        return waitingLine.poll();
    }
    /**
     * The entrepreneur says good bye to the customer, waking him up.
     * 
     * @param id 
     */
    public synchronized void sayGoodByeToCustomer(int id) {
        notifyAll();    // Acordar customer com este ID
        
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        log.UpdateEntreperneurState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
    }
    /**
     * The entrepreneur signals that she will close the shop.
     */
    public synchronized void closeTheDoor() {
        shopState = ShopState.ECLOSED;
        log.WriteShop(this.shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }
    /**
     * This function returns true if there's customers inside the shop.
     * 
     * @return returns true if customers inside the shop; returns false otherwise.
     */
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }
    /**
     * The entrepreneur prepares to leave the shop.
     * At this point the shop is considered as closed.
     */
    public synchronized void prepareToLeave() {
        shopState = ShopState.CLOSED;
        ((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.CLOSING_THE_SHOP);
        log.UpdateEntreperneurState(EntrepreneurState.CLOSING_THE_SHOP);
        log.WriteShop(shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
    }  
    /**
     * The entrepreneur returns to the shop, she went to fetch products or to deliver
     * prime materials to the craftsman.
     * If she went to deliver prime materials, then she already delivered everything
     * and request is done.
     * If she went to fetch products, she still have the products with her and she
     * must to put them on shop stock. After that request is done.
     * 
     * @param returnType type of the request, fetch products or deliver prime materials.
     */
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
    
    /**
     * The craftsman tells the Entrepreneur that they're out of prime materials.
     * To do that he needs to wake up entrepreneur.
     * 
     * @return 
     */
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
    
    /**
     * This function returns the total number of products in the shop stock.
     * 
     * @return the total number of products in stock
     */
    public synchronized int getnProductsStock() {
        return nProductsStock;
    }
    /**
     * This function returns true if there's a request to fetch products from
     * the Workshop.
     * 
     * @return returns true if Craftsman requested to fetch products; returns
     * false otherwise.
     */
    public synchronized boolean isReqFetchProducts() {
        return reqFetchProducts;
    }
    /**
     * This function returns true if there's a request to deliver prime materials
     * to the Workshop.
     * 
     * @return returns true if Craftsman request to deliver prime materials to the
     * Workshop; returns false otherwise.
     */
    public synchronized boolean isReqPrimeMaterials() {
        return reqPrimeMaterials;
    }
    /**
     * This function defines the Shop as out of business, it means that it doesn't
     * have any more products to sell (and there's no more materials to produce
     * them) and it will close soon.
     */
    public synchronized void setOutOfBusiness() {
        this.outOfBusiness = true;
    }
    /**
     * This function returns true if the Shop is out of business, it means that it
     * doesn't have any more products to sell (and there's no more materials to
     * produce them) and it will close soon.
     * 
     * @return returns true if Shop is out of business, returns false otherwise. 
     */
    public synchronized boolean isOutOfBusiness() {
        return outOfBusiness;
    }
}
