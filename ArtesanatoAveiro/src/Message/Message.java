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
    
    private MessageType type;
    
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
    
    /**
     * CUSTOMER
     * isDoorOpen(), endOpCustomer(), perusingAround()
     * 
     * CRAFSTMAN:
     * endOpCraftsman()
     * 
     * ENTR:
     * prepareToWork(), appraiseSit(), addressACustomer(), closeTheDoor(), customerInTheShop()
     * prepareToLeave(), goToWorkshop(), visitSuppliers(), endOpEntrepreneur()
     * 
     * WORKSHOP:
     * resetReqPrimeMaterials(), resetReqProducts()
     * 
     * @param type 
     */
    
    public Message(MessageType type) 
    {
        this.type = type;
    }
    
    /**
     * CUSTOMER:
     * goShopping(), enterShop(), exitShop(), tryAgainLater()
     * 
     * CRAFTSMAN:
     * collectingMaterials(), primeMaterialsNEeded(), backToWork(), 
     * prepareToProduce(), goToStore(), batchReadyForTransfer()
     * 
     * ENTR:
     * sayGoodByeToCustomer(), returnToShop(), replesnishStock()
     * 
     * @param type
     * @param value 
     */
    
    public Message(MessageType type, int value) 
    {
        this.type = type;
        switch(type)
        {
            case ENTER_SHOP:
            case EXIT_SHOP:
            case GO_SHOPPING:
            case TRY_AGAIN_LATER:
            case PRIME_MATERIALS_NEEDED:
            case BACK_TO_WORK:
            case COLLECTING_MATERIALS:
            case PREPARE_TO_PRODUCE:
            case BATCH_READY_FOR_TRANSFER:
            case GO_TO_STORE:
            case SAY_GOODBYE_TO_CUSTOMER:
                this.id = value;
                break;
                
            case REPLENISH_STOCK:
                this.nMaterials = value;
                break;
            case RETURN_TO_SHOP:
                this.nProducts = value;
                break;
                
            default:
                this.type = MessageType.ERROR;
                System.err.println("WRONG MESSAGE TYPE!");
                break;
        }
    }
    
    /**
     * CUSTOMER:
     * iWantThis()
     * 
     * 
     * @param type
     * @param id
     * @param value 
     */
    public Message(MessageType type, int id, int value)
    {
        this.type = type;
        this.id = id;
        
        switch(type)
        {
            case I_WANT_THIS:
                nProducts = value;
                break;

            default:
                this.type = MessageType.ERROR;
                System.err.println("WRONG MESSAGE TYPE!");
                break;
        }
    }
    
    public Message(MessageType type, EntrepreneurState es)
    {
        this.type = type;
        this.entrState = es;
    }
    
    public Message(MessageType type, CraftsmanState cs)
    {
        this.type = type;
        this.craftState = cs;
    }
     
    public Message(MessageType type, CustomerState cs)
    {
        this.type = type;
        this.custState = cs;
    }
    
    public Message(MessageType type, ShopState s, int nCustomerIn, int nGoodsInDisplay,
                                    boolean reqFetchProds, boolean reqPrimeMaterials)
    {
        this.shopState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.requestFetchProducts = reqFetchProds;
        this.requestPrimeMaterials = reqPrimeMaterials;
        
        this.type = type;
    }
    
    public Message(MessageType type, ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, EntrepreneurState state) 
    {
        this.entrState = state;
        this.shopState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.requestFetchProducts = reqFetchProds;
        this.requestPrimeMaterials = reqPrimeMaterials;
        
        this.type = type;
        
    }
    
    public Message(MessageType type, ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CraftsmanState state,
                                int idCraft)
    {
        
        this.id = idCraft;
        this.craftState = state;
        this.shopState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.requestFetchProducts = reqFetchProds;
        this.requestPrimeMaterials = reqPrimeMaterials;
        
        this.type = type;
        
    }
    
    public Message(MessageType type, ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CustomerState state,
                                int idCust, int nBoughtGoods) 
    {
       
        this.nBoughtGoods = nBoughtGoods;
        this.id = idCust;
        this.custState = state;
        this.shopState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.requestFetchProducts = reqFetchProds;
        this.requestPrimeMaterials = reqPrimeMaterials;
        
        
        this.type = type;
        
    }
    
    public Message(MessageType type, int nCurrentPrimeMaterials, int nProductsStored, 
                                        int nTimesPrimeMaterialsFetched,
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts)
    {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        
        this.type = type;
        
    }
    
    public Message(MessageType type, int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    CraftsmanState state, int idCraft, boolean finishedProduct) 
    {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        
        
        this.type = type;
        
    }
    
     public Message(MessageType type, int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    EntrepreneurState state) 
    {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        
        this.entrState = state;
        this.type = type;
        
    }
}
