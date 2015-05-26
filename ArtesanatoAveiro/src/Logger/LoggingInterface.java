/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Logger;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import ServerSide.Shop.ShopState;

/**
 *
 * @author diogosilva
 */
public interface LoggingInterface {
    public void WriteLine();
    /**
     * Writes the end of the logger file.
     */
    public void EndWriting();
    /**
     * Writes the state of the entrepeneur in the logger file.
     * 
     * @param es The entrepeneur's current state
    */
    public void UpdateEntreperneurState(EntrepreneurState es);        
    /**
     * Writes the state of the craftsman in the logger file.
     * 
     * @param id The craftsman's id
     * @param cs The craftsman's current state
     */
    public void UpdateCraftsmanState(int id, CraftsmanState cs);
    
    /**
     * Writes the state of the customer in the logger file.
     * 
     * @param id The customer's id
     * @param cs The customer's current state
     */
    public void UpdateCustomerState(int id, CustomerState cs);
    
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqPrimeMaterials The current request status
     */
    public void UpdatePrimeMaterialsRequest(boolean reqPrimeMaterials);
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqFetchProds The current request status
     */
    public void UpdateFetchProductsRequest(boolean reqFetchProds);
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
                                    boolean reqFetchProds, boolean reqPrimeMaterials);
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
                                boolean reqPrimeMaterials, EntrepreneurState state);
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
                                int idCraft);
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
    public void WriteShopAndCustomerStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CustomerState state,
                                int idCust, int nBoughtGoods);
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
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts);
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
                    CraftsmanState state, int idCraft, boolean finishedProduct);
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
                    EntrepreneurState state);    
    /**
     * Activates the logger to the standard output.
     */
    public void setConsole();
    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns 
     * false if otherwise.
     */
    public int endOperCraft();
    /**
     * Checks if the customer no longer has conditions to continue.
     * 
     * @return Returns false if the customer can continue; returns 
     * false if otherwise.
     */
    public boolean endOpCustomer();
    /**
     * Checks if the Entrepreneur no longer has conditions to continue its work.
     * 
     * @return Returns false if the entrepreneur can continue its work; returns 
     * false if otherwise.
     */
    public boolean endOpEntrep();
    /**
     * This method allows to check if the final status of the simulation is a 
     * valid or not.
     * 
     * @return returns true if simulation values are consistent; returns false
     * otherwise.
     */
    public boolean isConsist();
}
