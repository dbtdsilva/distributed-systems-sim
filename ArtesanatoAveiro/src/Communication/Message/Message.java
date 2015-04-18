/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication.Message;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import ServerSide.Shop.ShopState;
import java.io.Serializable;

/**
 *
 * @author diogosilva
 */
public class Message implements Serializable {
    public static final int ERROR_INT = Integer.MIN_VALUE;
    
    private static final long serialVersionUID = 1001L;
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
    
    /******************
     ** CONSTRUCTORS **
     ******************/
    
    /**
     * 
     */
    private Message() {
        id = ERROR_INT;
        entrState = null;
        custState = null;
        craftState = null;
        shopState = null;

        filename = null;

        nProducts = ERROR_INT;
        nMaterials = ERROR_INT;

        requestFetchProducts = false;
        requestPrimeMaterials = false;

        nCustomerIn = ERROR_INT;
        nGoodsInDisplay = ERROR_INT;
        
        finishedProduct = false;
        nFinishedProducts = ERROR_INT;
        nTotalPrimeMaterialsSupplied = ERROR_INT;
        nTimesPrimeMaterialsFetched = ERROR_INT;
        nProductsStored = ERROR_INT;
        nCurrentPrimeMaterials = ERROR_INT;
        nBoughtGoods = ERROR_INT;
    }
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
        this();
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
        this();
        this.type = type;
        switch(type)
        {
            case ACK:
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
        this();
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
    
    public Message(MessageType type, boolean requestFetchProducts) {
        this();
        this.type = type;
        this.requestFetchProducts = requestFetchProducts;
    }
    
    public Message(MessageType type, CraftsmanState craftState, int nProducts) {
        this();
        this.type = type;
        this.nProducts = nProducts;
        this.craftState = craftState;
    }
    
    public Message(MessageType type, CustomerState custState, int nProducts) {
        this();
        this.type = type;
        this.nProducts = nProducts;
        this.custState = custState;
    }
    
    public Message(MessageType type, EntrepreneurState entrState, int nMaterials) {
        this();
        this.type = type;
        this.nMaterials = nMaterials;
        this.entrState = entrState;
    }
    
    public Message(MessageType type, EntrepreneurState entrState)
    {
        this();
        this.type = type;
        this.entrState = entrState;
    }
    
    public Message(MessageType type, CraftsmanState craftState)
    {
        this();
        this.type = type;
        this.craftState = craftState;
    }
     
    public Message(MessageType type, CustomerState custState)
    {
        this();
        this.type = type;
        this.custState = custState;
    }
    
    public Message(MessageType type, ShopState s, int nCustomerIn, int nGoodsInDisplay,
                                    boolean reqFetchProds, boolean reqPrimeMaterials)
    {
        this();
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
        this();
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
        this();
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
        this();
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
        this();
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
        this();
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
        this();
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        
        this.entrState = state;
        this.type = type;
        
    }

     
    /*************
     ** GETTERS **  
     *************/
    
    /**
     * 
     * @return 
     */
    public MessageType getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public EntrepreneurState getEntrState() {
        return entrState;
    }

    public CustomerState getCustState() {
        return custState;
    }

    public CraftsmanState getCraftState() {
        return craftState;
    }

    public ShopState getShopState() {
        return shopState;
    }

    public String getFilename() {
        return filename;
    }

    public int getnProducts() {
        return nProducts;
    }

    public int getnMaterials() {
        return nMaterials;
    }

    public boolean isRequestFetchProducts() {
        return requestFetchProducts;
    }

    public boolean isRequestPrimeMaterials() {
        return requestPrimeMaterials;
    }

    public int getnCustomerIn() {
        return nCustomerIn;
    }

    public int getnGoodsInDisplay() {
        return nGoodsInDisplay;
    }

    public int getnBoughtGoods() {
        return nBoughtGoods;
    }

    public int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }

    public int getnProductsStored() {
        return nProductsStored;
    }

    public int getnTimesPrimeMaterialsFetched() {
        return nTimesPrimeMaterialsFetched;
    }

    public int getnTotalPrimeMaterialsSupplied() {
        return nTotalPrimeMaterialsSupplied;
    }

    public int getnFinishedProducts() {
        return nFinishedProducts;
    }

    public boolean isFinishedProduct() {
        return finishedProduct;
    }
}