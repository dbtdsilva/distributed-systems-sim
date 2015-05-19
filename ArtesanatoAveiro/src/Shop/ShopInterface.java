/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shop;

import java.rmi.Remote;

/**
 *
 * @author diogosilva
 */
public interface ShopInterface extends Remote {
    
        /**************/
        /** CUSTOMER **/
        /**************/
    
    /**
     * The customer goes to the Shopping.
     * 
     * @param id customer identifier
     */
    public void goShopping(int id);
    /**
     * This function allows the customer to check if the door is open or not.
     * 
     * @return returns true if the shop is open; returns false if otherwise.
     */
    public boolean isDoorOpen();
    /**
     * The customers enters in the shop. He updates his state and the number of
     * customers inside the shop.
     * 
     * @param id customer identifier
     */
    public void enterShop(int id);
    /**
     * The customer exits the shop, notifying the Entrepreneur that he left. He
     * need to update the number of customers inside the shop and update his state.
     * He will also notify the Entrepreneur to wake up, she might need to leave
     * the shop.
     * 
     * @param id customer identifier 
     */
    public void exitShop(int id);
    /**
     * The customer searchs for products inside the Shop.
     * 
     * @return number of products that customer is going to buy (Between 0 and 2)
     */
    public int perusingAround();
    /**
     * The customer requests the entrepreneur that he wants to buy a product. 
     * He will wait for the entrepreneur on the waiting line.
     * 
     * @param id customer identifier
     * @param nProducts the number of products bought
     */
    public void iWantThis(int id, int nProducts);
    /**
     * The customer will try to enter the shop later.
     * 
     * @param id customer identifier
     */
    public void tryAgainLater(int id);
  
        /******************/
        /** ENTREPRENEUR **/
        /******************/  
    
    /**
     * Entrepreneur is preparing to work, she will open the shop.
     */
    public void prepareToWork();
    /**
     * The entrepreneur will wait until someone request her services.
     * 
     * @return  'C', if customers waiting for service;
     *          'M', if craftsman requested for prime materials;
     *          'T', if craftsman requested to fetch the products in the Workshop;
     *          'E', if the shop is out of business.
     */
    public char appraiseSit();
    /**
     * The Entrepreneur address the first customer in the waiting line.
     * 
     * @return the customer identifier
     */
    public int addressACustomer();
    /**
     * The entrepreneur says good bye to the customer, waking him up.
     * 
     * @param id customer identifier
     */
    public void sayGoodByeToCustomer(int id);
    /**
     * The entrepreneur signals that she will close the shop.
     */
    public void closeTheDoor();
    /**
     * This function returns true if there's customers inside the shop.
     * 
     * @return returns true if customers inside the shop; returns false otherwise.
     */
    public boolean customersInTheShop();
    /**
     * The entrepreneur prepares to leave the shop.
     * At this point the shop is considered as closed.
     */
    public void prepareToLeave();
    /**
     * The entrepreneur returns to the shop, she went to fetch products or to deliver
     * prime materials to the craftsman.
     * If she went to fetch products, she still have the products with her and she
     * must to put them on shop stock. After that request is done.
     * 
     * @param nProducts the number of products that she's carrying.
     */
    public void returnToShop(int nProducts);
    
        /***************/
        /** CRAFTSMAN **/
        /***************/ 
    
    /**
     * The craftsman tells the Entrepreneur that they're out of prime materials.
     * To do that he needs to wake up entrepreneur.
     * 
     * @param id the craftsman identifier
     * @return returns true if request has been done; returns false if it was 
     * already done by someone before.
     */
    public boolean primeMaterialsNeeded(int id);
    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
    */
    public void batchReadyForTransfer(int id);
    
        /*************/
        /** GENERAL **/
        /*************/
    
    /**
     * This function is used to the Entrepreneur reset the flag prime materials
     * request.
     */
    public void resetRequestPrimeMaterials();
    /**
     * This function is used to the Entrepreneur reset the flag requestProducts.
     */
    public void resetRequestProducts();
}
