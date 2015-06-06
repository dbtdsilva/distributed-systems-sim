/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Static.Enumerates.CraftsmanState;
import Static.Enumerates.CustomerState;
import Static.Enumerates.EntrepreneurState;
import Static.Enumerates.ShopState;
import VectorClock.VectorTimestamp;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author diogosilva
 */
public interface LoggingInterface extends Remote {

    /**
     * Writes a line to the logging file
     * @param vt the Vector clock
     * @throws RemoteException
     */
    public void WriteLine(VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the end of the logger file.
     * @throws java.rmi.RemoteException
     */
    public void EndWriting() throws RemoteException;
    
    /**
     * Writes the state of the entrepeneur in the logger file.
     * 
     * @param es The entrepeneur's current state
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
    */
    public void UpdateEntreperneurState(EntrepreneurState es, VectorTimestamp vt) throws RemoteException;        
    
    /**
     * Writes the state of the craftsman in the logger file.
     * 
     * @param id The craftsman's id
     * @param cs The craftsman's current state
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void UpdateCraftsmanState(int id, CraftsmanState cs, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the customer in the logger file.
     * 
     * @param id The customer's id
     * @param cs The customer's current state
     * @param clk The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void UpdateCustomerState(int id, CustomerState cs, VectorTimestamp clk) throws RemoteException;
    
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqPrimeMaterials The current request status
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void UpdatePrimeMaterialsRequest(boolean reqPrimeMaterials) throws RemoteException;
    
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqFetchProds The current request status
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void UpdateFetchProductsRequest(boolean reqFetchProds) throws RemoteException;
    
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     * 
     */
    public void WriteShop(ShopState s, int nCustomerIn, int nGoodsInDisplay,
                                    boolean reqFetchProds, boolean reqPrimeMaterials, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The entrepreneur state
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void WriteShopAndEntrepreneurStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, EntrepreneurState state, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The craftsman state
     * @param idCraft The craftsman identifier
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public void WriteShopAndCraftsmanStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CraftsmanState state,
                                int idCraft, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The customer state
     * @param idCust The customer identifier
     * @param nBoughtGoods The number of products bought
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     * 
     */
    public void WriteShopAndCustomerStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CustomerState state,
                                int idCust, int nBoughtGoods, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     * 
     */
    public void WriteWorkshop(int nCurrentPrimeMaterials, int nProductsStored, 
                                        int nTimesPrimeMaterialsFetched,
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                                        VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * @param state The craftsman state
     * @param idCraft The craftsman identifier
     * @param finishedProduct This field indicates if he did finish a product or not
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     * 
     */
    public void WriteWorkshopAndCraftsmanStat(int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    CraftsmanState state, int idCraft, boolean finishedProduct, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * @param state The entrepreneur state
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     * 
     */
    public void WriteWorkshopAndEntrepreneurStat(int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    EntrepreneurState state, VectorTimestamp vt) throws RemoteException;
    
    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns 
     * false if otherwise.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public int endOperCraft() throws RemoteException;
    
    /**
     * Checks if the customer no longer has conditions to continue.
     * 
     * @return Returns false if the customer can continue; returns 
     * false if otherwise.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public boolean endOpCustomer() throws RemoteException;
    
    /**
     * Checks if the Entrepreneur no longer has conditions to continue its work.
     * 
     * @return Returns false if the entrepreneur can continue its work; returns 
     * false if otherwise.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public boolean endOpEntrep() throws RemoteException;
    
    /**
     * This method allows to check if the final status of the simulation is a 
     * valid or not.
     * 
     * @return returns true if simulation values are consistent; returns false
     * otherwise.
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public boolean isConsist() throws RemoteException;
}
