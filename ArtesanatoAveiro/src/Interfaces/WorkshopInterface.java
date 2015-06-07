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
 * 
 * @author Diogo Silva (60337)
 * @author Tânia Alves (60340)
 */
public interface WorkshopInterface extends Remote {
    /**
     * Entrepreneur goes to the Workshop to fetch the products in there.
     * 
     * @return the number of products that Entrepreneur is going to deliver to 
     * the shop
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] goToWorkshop(VectorTimestamp vt) throws RemoteException;
    
    /**
     * Entrepreneur goes to the Workshop and returns that prime materials that
     * she fetched from the Warehouse.
     * If nobody is waiting for her, it means that Entrepreneur arrived before
     * the Craftsman to the Workshop and he will not need to wait.
     * 
     * @param nMaterials number of prime materials
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp replenishStock(int nMaterials, VectorTimestamp vt) throws RemoteException;    
    
    /**
     * The craftsman is preparing to manufacture a product.
     * If there are enough materials to manufacture the product, the number of available prime materials 
     * is updated. 
     * However, if the prime materials aren't enough, the function returns false.
     * 
     * @param id The craftsman identifier.
     * @return true if there are enough prime materials to manufacture a product or false if there aren't.
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] collectingMaterials(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     * 
     * @return the number of products stored in workshop.
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] goToStore(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp backToWork(int id, VectorTimestamp vt) throws RemoteException;
    
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     * @param vt The vector clock
     * @return the resulting vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public VectorTimestamp prepareToProduce(int id, VectorTimestamp vt) throws RemoteException;
}
