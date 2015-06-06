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
    public void WriteLine() throws RemoteException;
    /**
     * Writes the end of the logger file.
     * @throws java.rmi.RemoteException
     */
    public void EndWriting() throws RemoteException;
    /**
     * Writes the state of the entrepeneur in the logger file.
     * 
     * @param es The entrepeneur's current state
    */
    public void UpdateEntreperneurState(EntrepreneurState es) throws RemoteException;        
    /**
     * Writes the state of the craftsman in the logger file.
     * 
     * @param id The craftsman's id
     * @param cs The craftsman's current state
     */
    public void UpdateCraftsmanState(int id, CraftsmanState cs) throws RemoteException;
    
    /**
     * Writes the state of the customer in the logger file.
     * 
     * @param id The customer's id
     * @param cs The customer's current state
     */
    public VectorTimestamp UpdateCustomerState(int id, CustomerState cs, VectorTimestamp clk) throws RemoteException;
    
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqPrimeMaterials The current request status
     */
    public void UpdatePrimeMaterialsRequest(boolean reqPrimeMaterials) throws RemoteException;
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqFetchProds The current request status
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
     * 
     */
    public void WriteShop(ShopState s, int nCustomerIn, int nGoodsInDisplay,
                                    boolean reqFetchProds, boolean reqPrimeMaterials) throws RemoteException;
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The entrepreneur state
     */
    public void WriteShopAndEntrepreneurStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, EntrepreneurState state) throws RemoteException;
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
     */
    public void WriteShopAndCraftsmanStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CraftsmanState state,
                                int idCraft) throws RemoteException;
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
     * 
     */
    public VectorTimestamp WriteShopAndCustomerStat(ShopState s, int nCustomerIn, 
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
     * 
     */
    public void WriteWorkshop(int nCurrentPrimeMaterials, int nProductsStored, 
                                        int nTimesPrimeMaterialsFetched,
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts) throws RemoteException;
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
     * 
     */
    public void WriteWorkshopAndCraftsmanStat(int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    CraftsmanState state, int idCraft, boolean finishedProduct) throws RemoteException;
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * @param state The entrepreneur state
     * 
     */
    public void WriteWorkshopAndEntrepreneurStat(int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    EntrepreneurState state) throws RemoteException;
    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns 
     * false if otherwise.
     */
    public int endOperCraft() throws RemoteException;
    /**
     * Checks if the customer no longer has conditions to continue.
     * 
     * @return Returns false if the customer can continue; returns 
     * false if otherwise.
     */
    public boolean endOpCustomer() throws RemoteException;
    /**
     * Checks if the Entrepreneur no longer has conditions to continue its work.
     * 
     * @return Returns false if the entrepreneur can continue its work; returns 
     * false if otherwise.
     */
    public boolean endOpEntrep() throws RemoteException;
    /**
     * This method allows to check if the final status of the simulation is a 
     * valid or not.
     * 
     * @return returns true if simulation values are consistent; returns false
     * otherwise.
     */
    public boolean isConsist() throws RemoteException;
}
