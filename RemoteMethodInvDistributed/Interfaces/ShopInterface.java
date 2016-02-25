/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Structures.VectorClock.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This data type defines the interface for the object Shop.
 * @author Diogo Silva (60337)
 * @author Tânia Alves (60340)
 */
public interface ShopInterface extends Remote {
    /**
     * This function is used for the logging to signal the shop to shutdown.
     * 
     * @throws RemoteException may throw during a execution of a remote method call
     */
    public void signalShutdown() throws RemoteException;
    /**
     * The customer goes to the Shopping.
     * 
     * @param id customer identifier
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp goShopping(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * This function allows the customer to check if the door is open or not.
     * 
     * @return returns true if the shop is open; returns false if otherwise.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public boolean isDoorOpen() throws RemoteException;
    
    /**
     * The customers enters in the shop. He updates his state and the number of
     * customers inside the shop.
     * 
     * @param id customer identifier
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp enterShop(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The customer exits the shop, notifying the Entrepreneur that he left. He
     * need to update the number of customers inside the shop and update his state.
     * He will also notify the Entrepreneur to wake up, she might need to leave
     * the shop.
     * 
     * @param id customer identifier 
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp exitShop(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The customer searchs for products inside the Shop.
     * 
     * @return number of products that customer is going to buy (Between 0 and 2)
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] perusingAround(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The customer requests the entrepreneur that he wants to buy a product. 
     * He will wait for the entrepreneur on the waiting line.
     * 
     * @param id customer identifier
     * @param nProducts the number of products bought
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp iWantThis(int id, int nProducts, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The customer will try to enter the shop later.
     * 
     * @param id customer identifier
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp tryAgainLater(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Entrepreneur is preparing to work, she will open the shop.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp prepareToWork(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The entrepreneur will wait until someone request her services.
     * 
     * @return  'C', if customers waiting for service;
     *          'M', if craftsman requested for prime materials;
     *          'T', if craftsman requested to fetch the products in the Workshop;
     *          'E', if the shop is out of business.
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] appraiseSit(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The Entrepreneur address the first customer in the waiting line.
     * 
     * @return the customer identifier
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] addressACustomer(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The entrepreneur says good bye to the customer, waking him up.
     * 
     * @param id customer identifier
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp sayGoodByeToCustomer(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The entrepreneur signals that she will close the shop.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp closeTheDoor(VectorTimestamp vt) throws RemoteException;
    
    /**
     * This function returns true if there's customers inside the shop.
     * 
     * @return returns true if customers inside the shop; returns false otherwise.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public boolean customersInTheShop() throws RemoteException;
    
    /**
     * The entrepreneur prepares to leave the shop.
     * At this point the shop is considered as closed.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp prepareToLeave(VectorTimestamp vt) throws RemoteException;
    
    /**
     * The entrepreneur returns to the shop, she went to fetch products or to deliver
     * prime materials to the craftsman.
     * If she went to fetch products, she still have the products with her and she
     * must to put them on shop stock. After that request is done.
     * 
     * @param nProducts the number of products that she's carrying.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp returnToShop(int nProducts, VectorTimestamp vt) throws RemoteException;    
    
    /**
     * The craftsman tells the Entrepreneur that they're out of prime materials.
     * To do that he needs to wake up entrepreneur.
     * 
     * @param id the craftsman identifier
     * @return returns true if request has been done; returns false if it was 
     * already done by someone before.
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] primeMaterialsNeeded(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
    */
    public VectorTimestamp batchReadyForTransfer(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * This function is used to the Entrepreneur reset the flag prime materials
     * request.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void resetRequestPrimeMaterials() throws RemoteException;
    
    /**
     * This function is used to the Entrepreneur reset the flag requestProducts.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void resetRequestProducts() throws RemoteException;
}
