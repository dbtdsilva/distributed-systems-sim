package Communication.Message;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import ServerSide.Shop.ShopState;
import java.io.Serializable;


/**
 * This file defines the message class.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Message implements Serializable {
    
    /**
     * Variable that defines the integer value for an error.
     */
    public static final int ERROR_INT = Integer.MIN_VALUE;
    
    /**
     * Variable that defines the character value for an error.
     */
    public static final char ERROR_CHAR = 0xFFFE;
    
    /**
     * Variable that defines the serial version identifier.
     */
    private static final long serialVersionUID = 1001L;
    
    /**
     * Variable that defines the type of the current message.
     */
    private MessageType type;
    
    /**
     * Variable that defines the identifier, if the message requires one.
     */
    private int id;
    
    /**
     * Variable that holds the Entrepreneur state, in case the message requires it.
     */
    private EntrepreneurState entrState;
    
    /**
     * Variable that holds the Customer state, in case the message requires it.
     */
    private CustomerState custState;
    
    /**
     * Variable that holds the Craftsman state, in case the message requires it.
     */
    private CraftsmanState craftState;
    
    /**
     * Variable that holds the Shop state, in case the message requires it.
     */
    private ShopState shopState;
    
    /**
     * Variable that holds the logging file name, in case the message requires it.
     */
    private String filename;
    
    /**
     * Variable that holds the Entrepreneur's next task, in case the message requires it.
     */
    private char nextTask;
    
    /**
     * Variable that holds the number of products, in case the message requires it.
     */
    private int nProducts;
    
    /**
     * Variable that holds the number of materials, in case the message requires it.
     */
    private int nMaterials;
    
    /**
     * FIX_ME!!
     */
    private int returnEntr;
    
    /**
     * Variable that holds the information related to the products requests, in case the message requires it.
     */
    private boolean requestFetchProducts;
    
    /**
     * Variable that holds the information related to the prime materials requests, in case the message requires it.
     */
    private boolean requestPrimeMaterials;
    
    /**
     * Variable that holds the information about the number of customers in the shop, in case the message requires it.
     */
    private int nCustomerIn;
    
    /**
     * Variable that holds the information about the number of goods in display at the shop, in case the message requires it.
     */
    private int nGoodsInDisplay;
    
    /**
     * Variable that holds the information about the number of goods that a costumer bought at the shop, in case the message requires it.
     */
    private int nBoughtGoods;
    
    /**
     * Variable that holds the information about the current number of prime materials present at the workshop,
     * in case the message requires it.
     */
    private int nCurrentPrimeMaterials;
    
    /**
     * Variable that holds the information about the current number of finished products present at the workshop,
     * in case the message requires it.
     */
    private int nProductsStored;
    
    /**
     * Statistic variable that holds the information about the number of times that prime materials were supplied
     * to the workshop, in case the message requires it.
     */
    private int nTimesPrimeMaterialsFetched;
    
    /**
     * Statistic variable that holds the information about the total number of prime materials supplied
     * to the workshop, in case the message requires it.
     */
    private int nTotalPrimeMaterialsSupplied;
    
    /**
     * Statistic variable that holds the information about the number of a Craftsman finished products,
     * in case the message requires it.
     */
    private int nFinishedProducts;
    
    /**
     * FIX_ME!!
     */
    private boolean finishedProduct;
    
    /******************
     ** CONSTRUCTORS **
     ******************/
    
    /**
     * Empty constructor for the message that initializes the default values for the previous variables.
     */
    private Message() {
        id = ERROR_INT;
        nextTask = ERROR_CHAR;
        entrState = null;
        custState = null;
        craftState = null;
        shopState = null;

        filename = null;

        nProducts = ERROR_INT;
        nMaterials = ERROR_INT;
        returnEntr = ERROR_INT;

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
     * Constructor for the message that initializes with the type of the message.
     * This is used in:
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#collectingMaterials(int) collectingMaterials}</ul>
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#primeMaterialsNeeded(int) primeMaterialsNeeded}</ul>
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#endOperCraft() endOperCraft}</ul>
     *  <ul>{@link #ClientSide.Craftsman.CraftsmanExec#main(String[]) CraftsmanExecMain}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#isDoorOpen() isDoorOpen}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#perusingAround() perusingAround}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#endOperCust() endOperCust}</ul>
     *  <ul>{@link #ClientSide.Customer.CustomerExec#main(String[]) CustomerExecMain}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#prepareToWork() prepareToWork}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#appraiseSit() appraiseSit}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#addressACustomer() addressACustomer}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#closeTheDoor() closeTheDoor}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#customersInTheShop() customersInTheShop}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#prepareToLeave() prepareToLeave}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#goToWorkshop() goToWorkshop}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#visitSuppliers() visitSuppliers}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#endOperEntrep() endOperEntrep}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.EntrepreneurExec#main(String[]) EntrepreneurExecMain}</ul>
     *  <ul>{@link #ServerSide.Logger.Logging#terminateServers() terminateServers}</ul>
     *  <ul>{@link #ServerSide.Logger.LoggingInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Warehouse.Warehouse#visitSuppliers() visitSuppliers}</ul>
     *  <ul>{@link #ServerSide.Warehouse.WarehouseInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Workshop.Workshop#goToWorkshop() goToWorkshop}</ul>
     *  <ul>{@link #ServerSide.Workshop.Workshop#replenishStock(int) replenishStock}</ul>
     *  <ul>{@link #ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * @param type
     */
    public Message(MessageType type) {
        this();
        this.type = type;
    }
    
    /**
     * Constructor for the message that initializes with the type of the message and an integer.
     * This is used in:
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#primeMaterialsNeeded(int) primeMaterialsNeeded}</ul>
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#backToWork(int) backToWork}</ul>
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#prepareToProduce(int) prepareToProduce}</ul>
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#goToStore(int) goToStore}</ul>
     *  <ul>{@link #ClientSide.Craftsman.Craftsman#batchReadyForTransfer(int) batchReadyForTransfer}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#goShopping(int) goShopping}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#enterShop(int) enterShop}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#exitShop(int) exitShop}</ul>
     *  <ul>{@link #ClientSide.Customer.Customer#tryAgainLater(int) tryAgainLater}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#sayGoodByeToCustomer(int) syaGoodByeToCustomer}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#returnToShop(int) returnToShop}</ul>
     *  <ul>{@link #ClientSide.Entrepreneur.Entrepreneur#replenishStock(int) replenishStock}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#appraiseSit() appraiseSit}</ul>
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     
     
     * @param type
     * @param value 
     */
    public Message(MessageType type, int value) {
        this();
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
            case ACK:
            case RETURN_TO_SHOP:
            case GO_TO_WORKSHOP:
                this.nProducts = value;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for the message that initializes with the type of the message, an id, and a value.
     * This is used in:
     *  <ul>{@link #ClientSide.Customer.Customer#iWantThis(int, int) iWantThis}</ul>
     * 
     * 
     * @param type
     * @param id
     * @param value 
     */
    public Message(MessageType type, int id, int value) {
        this();
        this.type = type;
        this.id = id;
        
        switch(type)
        {
            case I_WANT_THIS:
                nProducts = value;
                break;

            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * 
     *  <ul>{@link #ServerSide.Logger.LoggingInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#resetRequestPrimeMaterials() resetRequestPrimeMaterials}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#resetRequestProducts() resetRequestProducts}</ul>
     * 
     * @param type
     * @param requestFetchProducts 
     */
    public Message(MessageType type, boolean requestFetchProducts) {
        this();
        this.type = type;
        this.requestFetchProducts = requestFetchProducts;
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * @param type
     * @param nextTask 
     */
    public Message(MessageType type, char nextTask) {
        this();
        this.type = type;
        this.nextTask = nextTask;
    }
    
    /**
     *  <ul>{@link #ServerSide.Workshop.Workshop#backToWork(int) backToWork}</ul>
     *  <ul>{@link #ServerSide.Workshop.Workshop#prepareToProduce(int) prepareToProduce}</ul>
     *  <ul>{@link #ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * @param type
     * @param craftState
     * @param value 
     */
    public Message(MessageType type, CraftsmanState craftState, int value) {
        this();
        this.type = type;
        this.craftState = craftState;
        
        switch (type) {
            case ACK:
                this.nProducts = value;
                break;
            case WRITE_CRAFT_STATE:
                this.id = value;
                break;
            case POSITIVE:
            case NEGATIVE:
                this.id = value;
                this.craftState = craftState;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     *  <ul>{@link #ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * 
     * @param type
     * @param craftState
     * @param id
     * @param value 
     */
    public Message(MessageType type, CraftsmanState craftState, int id, int value) {
        this();
        this.type = type;
        this.craftState = craftState;
        this.id = id;
        
        switch (type) {
            case ACK:
            case POSITIVE:
                this.nProductsStored = value;
                break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.Shop#goShopping(int) goShopping}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#tryAgainLater(int) tryAgainLater}</ul>
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     
     * 
     * @param type
     * @param custState
     * @param value 
     */
    public Message(MessageType type, CustomerState custState, int value) {
        this();
        this.type = type;
        switch (type) {
            case WRITE_CUST_STATE:
                this.id = value;
                break;
            //case PERUSING_AROUND:
            //    this.nProducts = value;
            //    break;
            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
                
        }
        //this.nProducts = nProducts;
        this.custState = custState;
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Warehouse.WarehouseInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * @param type
     * @param entrState
     * @param returnEntr 
     */
    public Message(MessageType type, EntrepreneurState entrState, int returnEntr) {
        this();
        this.type = type;
        this.entrState = entrState;
        
        switch(type)
        {
            case ACK:
                this.returnEntr = returnEntr;
                break;

            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.Shop#addressACustomer() addressACustomer}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#sayGoodByeToCustomer(int) sayGoodByeToCustomer}</ul>
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     *  <ul>{@link #ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * @param type
     * @param entrState
     */
    public Message(MessageType type, EntrepreneurState entrState) {
        this();
        this.type = type;
        this.entrState = entrState;
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm) processAndReply}</ul>
     * @param type
     * @param craftState 
     */
    public Message(MessageType type, CraftsmanState craftState) {
        this();
        this.type = type;
        this.craftState = craftState;
    }
     
    public Message(MessageType type, CustomerState custState) {
        this();
        this.type = type;
        this.custState = custState;
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.Shop#closeTheDoor() closeTheDoor}</ul>
     * 
     * 
     * @param type
     * @param s
     * @param nCustomerIn
     * @param nGoodsInDisplay
     * @param reqFetchProds
     * @param reqPrimeMaterials 
     */
    public Message(MessageType type, ShopState s, int nCustomerIn, int nGoodsInDisplay,
                                    boolean reqFetchProds, boolean reqPrimeMaterials) {
        this();
        this.shopState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.requestFetchProducts = reqFetchProds;
        this.requestPrimeMaterials = reqPrimeMaterials;
        
        this.type = type;
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.Shop#prepareToWork() prepareToWork}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#prepareToLeave() prepareToLeave}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#returnToShop(int) returnToShop}</ul>
     
     * @param type
     * @param s
     * @param nCustomerIn
     * @param nGoodsInDisplay
     * @param reqFetchProds
     * @param reqPrimeMaterials
     * @param state 
     */
    public Message(MessageType type, ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, EntrepreneurState state) {
        this();
        this.entrState = state;
        this.shopState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.requestFetchProducts = reqFetchProds;
        this.requestPrimeMaterials = reqPrimeMaterials;
        
        this.type = type;
        
    }
    
    /**
     *  <ul>{@link #ServerSide.Shop.Shop#primeMaterialsNeeded(int) primeMaterialsNeeded}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#batchReadyForTransfer(int) batchReadyForTransfer}</ul>
     * 
     * 
     * @param type
     * @param s
     * @param nCustomerIn
     * @param nGoodsInDisplay
     * @param reqFetchProds
     * @param reqPrimeMaterials
     * @param state
     * @param idCraft 
     */
    public Message(MessageType type, ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CraftsmanState state,
                                int idCraft) {
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
    
    /**
     * 
     *  <ul>{@link #ServerSide.Shop.Shop#enterShop(int) enterShop}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#exitShop(int) exitShop}</ul>
     *  <ul>{@link #ServerSide.Shop.Shop#iWantThis(int, int) iWantThis}</ul>
     * @param type
     * @param s
     * @param nCustomerIn
     * @param nGoodsInDisplay
     * @param reqFetchProds
     * @param reqPrimeMaterials
     * @param state
     * @param idCust
     * @param nBoughtGoods 
     */
    public Message(MessageType type, ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CustomerState state,
                                int idCust, int nBoughtGoods) {
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
    
    /**
     *  <ul>{@link #ServerSide.Workshop.Workshop#collectingMaterials(int) collectingMaterials}</ul>
     * 
     * @param type
     * @param nCurrentPrimeMaterials
     * @param nProductsStored
     * @param nTimesPrimeMaterialsFetched
     * @param nTotalPrimeMaterialsSupplied
     * @param nFinishedProducts 
     */
    public Message(MessageType type, int nCurrentPrimeMaterials, int nProductsStored, 
                                        int nTimesPrimeMaterialsFetched,
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts) {
        this();
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        
        this.type = type;
        
    }
    
    /**
     *  <ul>{@link #ServerSide.Workshop.Workshop#goToStore(int) goToStore}</ul>
     * 
     * @param type
     * @param nCurrentPrimeMaterials
     * @param nProductsStored
     * @param nTimesPrimeMaterialsFetched
     * @param nTotalPrimeMaterialsSupplied
     * @param nFinishedProducts
     * @param state
     * @param idCraft
     * @param finishedProduct 
     */
    public Message(MessageType type, int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    CraftsmanState state, int idCraft, boolean finishedProduct) {
        this();
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        this.id = idCraft;
        this.craftState = state;
        this.finishedProduct = finishedProduct;
        
        this.type = type;
    }
    
    /**
     *  <ul>{@link #ServerSide.Workshop.Workshop#goToWorkshop() goToWorkshop}</ul>
     *  <ul>{@link #ServerSide.Workshop.Workshop#replenishStock(int) replenishStock}</ul>
     * 
     * @param type
     * @param nCurrentPrimeMaterials
     * @param nProductsStored
     * @param nTimesPrimeMaterialsFetched
     * @param nTotalPrimeMaterialsSupplied
     * @param nFinishedProducts
     * @param state 
     */
     public Message(MessageType type, int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    EntrepreneurState state) {
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
    public int getReturnEntr() {
        return returnEntr;
    }
    public char getNextTask() {
        return nextTask;
    }
    
    @Override
    public String toString() {
        return this.type.toString();
    }
}
