/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Message;

import Craftsman.CraftsmanState;
import Customer.CustomerState;
import Entrepreneur.EntrepreneurState;
import Shop.ShopState;

/**
 *
 * @author diogosilva
 */
public class Message {
    /**
     * 
     */
    private final MessageType type;
    
    private int id;
    private EntrepreneurState entrState;
    private CustomerState custState;
    private CraftsmanState craftState;
    private ShopState shopState;
    
    private String filename;
    
    private int nProducts;
    private int nMaterials;
    
    private boolean requestFetchProducts;
    private boolean requestPrimeMaterials;
    
    private int nCustomerIn;
    private int nGoodsInDisplay;
    private int nBoughtGoods;
    
    private int nCurrentPrimeMaterials;
    private int nProductsStored;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private int nFinishedProducts;
    
    private boolean finishedProduct;
    
    public Message(MessageType type) {
        this.type = type;
    }
    public Message(MessageType type, int value) {
        this.type = type;
        this.id = value;
    }x
}
