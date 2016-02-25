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
     * Possible message value.
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
     * Tells if a Craftsman has a finished product or not.
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
     * Constructor for the message (1st form).
     * This is used in:
     *  @see ClientSide.Craftsman.Craftsman#collectingMaterials(int)
     *  @see ClientSide.Craftsman.Craftsman#primeMaterialsNeeded(int)
     *  @see ClientSide.Craftsman.Craftsman#endOperCraft()
     *  @see ClientSide.Craftsman.CraftsmanExec#main(String[])
     *  @see ClientSide.Customer.Customer#isDoorOpen()
     *  @see ClientSide.Customer.Customer#perusingAround()
     *  @see ClientSide.Customer.Customer#endOpCustomer()
     *  @see ClientSide.Customer.CustomerExec#main(String[])
     *  @see ClientSide.Entrepreneur.Entrepreneur#prepareToWork()
     *  @see ClientSide.Entrepreneur.Entrepreneur#appraiseSit()
     *  @see ClientSide.Entrepreneur.Entrepreneur#addressACustomer()
     *  @see ClientSide.Entrepreneur.Entrepreneur#closeTheDoor()
     *  @see ClientSide.Entrepreneur.Entrepreneur#customersInTheShop()
     *  @see ClientSide.Entrepreneur.Entrepreneur#prepareToLeave()
     *  @see ClientSide.Entrepreneur.Entrepreneur#goToWorkshop()
     *  @see ClientSide.Entrepreneur.Entrepreneur#visitSuppliers()
     *  @see ClientSide.Entrepreneur.Entrepreneur#endOpEntrep()
     *  @see ClientSide.Entrepreneur.EntrepreneurExec#main(String[])
     *  @see ServerSide.Logger.Logging#terminateServers()
     *  @see ServerSide.Logger.LoggingInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Warehouse.Warehouse#visitSuppliers()
     *  @see ServerSide.Warehouse.WarehouseInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Workshop.Workshop#goToWorkshop()
     *  @see ServerSide.Workshop.Workshop#replenishStock(int)
     *  @see ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm)
     * 
     * @param type Message type for the created message.
     */
    public Message(MessageType type) {
        this();
        this.type = type;
    }
    
    /**
     * Constructor for the message (2nd form).
     * This is used in:
     *  @see ClientSide.Craftsman.Craftsman#primeMaterialsNeeded(int)
     *  @see ClientSide.Craftsman.Craftsman#backToWork(int)
     *  @see ClientSide.Craftsman.Craftsman#prepareToProduce(int)
     *  @see ClientSide.Craftsman.Craftsman#goToStore(int)
     *  @see ClientSide.Craftsman.Craftsman#batchReadyForTransfer(int)
     *  @see ClientSide.Customer.Customer#goShopping(int)
     *  @see ClientSide.Customer.Customer#enterShop(int)
     *  @see ClientSide.Customer.Customer#exitShop(int)
     *  @see ClientSide.Customer.Customer#tryAgainLater(int)
     *  @see ClientSide.Entrepreneur.Entrepreneur#sayGoodByeToCustomer(int)
     *  @see ClientSide.Entrepreneur.Entrepreneur#returnToShop(int)
     *  @see ClientSide.Entrepreneur.Entrepreneur#replenishStock(int)
     *  @see ServerSide.Shop.Shop#appraiseSit()
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     
     
     * @param type Message type for the created message.
     * @param value Possible value for the message.
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
     * Constructor for the message (3rd form).
     * This is used in:
     *  @see ClientSide.Customer.Customer#iWantThis(int, int)
     * 
     * @param type Message type for the created message.
     * @param id Entity identifier for the message.
     * @param value Possible value for the message.
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
     * Constructor for the message (4th form).
     * This is used in:
     * 
     *  @see ServerSide.Logger.LoggingInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Shop.Shop#resetRequestPrimeMaterials()
     *  @see ServerSide.Shop.Shop#resetRequestProducts()
     * 
     * @param type Message type for the created message.
     * @param request Boolean to reset requests.
     */
    public Message(MessageType type, boolean request) {
        this();
        this.type = type;
        this.requestFetchProducts = request;
    }
    
    /**
     * Constructor for the message (5th form).
     * This is used in:
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     *
     * @param type Message type for the created message.
     * @param nextTask Character that translates to the Entrepreneur's next task.
     */
    public Message(MessageType type, char nextTask) {
        this();
        this.type = type;
        this.nextTask = nextTask;
    }
    
    /**
     * Constructor for the message (6th form).
     * This is used in:
     *  @see ServerSide.Workshop.Workshop#backToWork(int)
     *  @see ServerSide.Workshop.Workshop#prepareToProduce(int)
     *  @see ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm)
     * 
     * @param type Message type for the created message.
     * @param craftState Craftsman state.
     * @param value Possible value for the message.
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
     * Constructor for the message (7th form).
     * This is used in:
     *  @see ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm)
     * 
     * @param type Message type for the created message.
     * @param craftState Craftsman state.
     * @param id Identifier for the Craftsman entity.
     * @param value Possible value for the message.
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
     * Constructor for the message (8th form).
     * This is used in:
     * 
     *  @see ServerSide.Shop.Shop#goShopping(int)
     *  @see ServerSide.Shop.Shop#tryAgainLater(int)
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     
     * 
     * @param type Message type for the created message.
     * @param custState Customer state.
     * @param value Possible value for the message.
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
     * Constructor for the message (9th form).
     * This is used in:
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Warehouse.WarehouseInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm)
     *
     * @param type Message type for the created message.
     * @param entrState Entrepreneur state.
     * @param value Possible return value.
     */
    public Message(MessageType type, EntrepreneurState entrState, int value) {
        this();
        this.type = type;
        this.entrState = entrState;
        
        switch(type)
        {
            case ACK:
                this.returnEntr = value;
                break;

            default:
                System.err.println(type + ", wrong message type!");
                this.type = MessageType.ERROR;
                break;
        }
    }
    
    /**
     * Constructor for the message (10th form).
     * This is used in:
     *  @see ServerSide.Shop.Shop#addressACustomer()
     *  @see ServerSide.Shop.Shop#sayGoodByeToCustomer(int)
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     *  @see ServerSide.Workshop.WorkshopInterface#processAndReply(Message, ServerComm)
     * 
     * @param type Message type for the created message.
     * @param entrState Entrepreneur state.
     */
    public Message(MessageType type, EntrepreneurState entrState) {
        this();
        this.type = type;
        this.entrState = entrState;
    }
    
    /**
     * Constructor for the message (11th form).
     * This is used in:
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     * 
     * @param type Message type for the created message.
     * @param craftState Craftsman state.
     */
    public Message(MessageType type, CraftsmanState craftState) {
        this();
        this.type = type;
        this.craftState = craftState;
    }
     
    /**
     * Constructor for the message (12th form).
     * This is used in:
     *  @see ServerSide.Shop.ShopInterface#processAndReply(Message, ServerComm)
     * 
     * @param type Message type for the created message.
     * @param custState Customer state.
     */
    public Message(MessageType type, CustomerState custState) {
        this();
        this.type = type;
        this.custState = custState;
    }
    
    /**
     * Constructor for the message (13th form).
     * This is used in:
     *  @see ServerSide.Shop.Shop#closeTheDoor()
     * 
     * 
     * @param type Message type for the created message.
     * @param s Shop state
     * @param nCustomerIn Number of customers in the shop.
     * @param nGoodsInDisplay Number of goods in display at the shop.
     * @param reqFetchProds Boolean that translates to a pending request or not.
     * @param reqPrimeMaterials Boolean that translates to a pending request or not.
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
     * Constructor for the message (14th form).
     * This is used in:
     *  @see ServerSide.Shop.Shop#prepareToWork()
     *  @see ServerSide.Shop.Shop#prepareToLeave()
     *  @see ServerSide.Shop.Shop#returnToShop(int)
     
     * @param type Message type for the created message.
     * @param s Shop state.
     * @param nCustomerIn Number of customers in the shop.
     * @param nGoodsInDisplay Number of goods in display at the shop.
     * @param reqFetchProds Boolean that translates to a pending request or not.
     * @param reqPrimeMaterials Boolean that translates to a pending request or not.
     * @param state Entrepreneur state.
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
     * Constructor for the message (15th form).
     * This is used in:
     *  @see ServerSide.Shop.Shop#primeMaterialsNeeded(int)
     *  @see ServerSide.Shop.Shop#batchReadyForTransfer(int)
     * 
     * 
     * @param type Message type for the created message.
     * @param s Shop state.
     * @param nCustomerIn Number of customers in the shop.
     * @param nGoodsInDisplay Number of goods in display.
     * @param reqFetchProds Boolean that translates to a pending request or not.
     * @param reqPrimeMaterials Boolean that translates to a pending request or not.
     * @param state Craftsman state.
     * @param idCraft Craftsman identifier.
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
     * Constructor for the message (16th form).
     * This is used in:
     *  @see ServerSide.Shop.Shop#enterShop(int)
     *  @see ServerSide.Shop.Shop#exitShop(int)
     *  @see ServerSide.Shop.Shop#iWantThis(int, int)
     * 
     * @param type Message type for the created message.
     * @param s Shop state.
     * @param nCustomerIn Number of customers in the shop.
     * @param nGoodsInDisplay Number of goods in display.
     * @param reqFetchProds Boolean that translates to a pending request or not.
     * @param reqPrimeMaterials Boolean that translates to a pending request or not.
     * @param state Customer state.
     * @param idCust Customer identifier.
     * @param nBoughtGoods Number of the customer's bought goods.
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
     * Constructor for the message (17th form).
     * This is used in:
     *  @see ServerSide.Workshop.Workshop#collectingMaterials(int)
     * 
     * @param type Message type for the created message.
     * @param nCurrentPrimeMaterials Number of the prime materials currently in the workshop.
     * @param nProductsStored Number of products currently stored in the workshop.
     * @param nTimesPrimeMaterialsFetched Number of times that the prime materials were fetched.
     * @param nTotalPrimeMaterialsSupplied Number of the total prime materials supplied to the workshop.
     * @param nFinishedProducts Number of finished products.
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
     * Constructor for the message (18th form).
     * This is used in:
     *  @see ServerSide.Workshop.Workshop#goToStore(int)
     * 
     * @param type Message type for the created message.
     * @param nCurrentPrimeMaterials Number of the prime materials currently in the workshop.
     * @param nProductsStored Number of products currently stored in the workshop.
     * @param nTimesPrimeMaterialsFetched Number of times that the prime materials were fetched.
     * @param nTotalPrimeMaterialsSupplied Number of the total prime materials supplied to the workshop.
     * @param nFinishedProducts Number of finished products.
     * @param state Craftsman state.
     * @param idCraft Craftsman identifier.
     * @param finishedProduct Tells if the product is finished or not.
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
     * Constructor for the message (19th form).
     * This is used in:
     *  @see ServerSide.Workshop.Workshop#goToWorkshop()
     *  @see ServerSide.Workshop.Workshop#replenishStock(int)
     * 
     * @param type Message type for the created message.
     * @param nCurrentPrimeMaterials Number of the prime materials currently in the workshop.
     * @param nProductsStored Number of products currently stored in the workshop.
     * @param nTimesPrimeMaterialsFetched Number of times that the prime materials were fetched.
     * @param nTotalPrimeMaterialsSupplied Number of the total prime materials supplied to the workshop.
     * @param nFinishedProducts Number of finished products.
     * @param state Entrepreneur state.
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
    
    /**
     * Get the current message type.
     * @return Message type.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Get the current message's id.
     * @return Message identifier.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the information about the Entrepreneur state present in the current message.
     * @return Entrepreneur state.
     */
    public EntrepreneurState getEntrState() {
        return entrState;
    }

    /**
     * Get the information about the Customer state present in the current message.
     * @return Customer state.
     */
    public CustomerState getCustState() {
        return custState;
    }

    /**
     * Get the information about the Craftsman state present in the current message.
     * @return Craftsman state.
     */
    public CraftsmanState getCraftState() {
        return craftState;
    }

    /**
     * Get the information about the shop state present in the current message.
     * @return The Shop state.
     */
    public ShopState getShopState() {
        return shopState;
    }

    /**
     * Get the logger file name.
     * @return Logger file name.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get the number of products present in the message.
     * @return Number of products.
     */
    public int getnProducts() {
        return nProducts;
    }

    /**
     * Get the number of prime materials present in the message.
     * @return Number of prime materials.
     */
    public int getnMaterials() {
        return nMaterials;
    }

    /**
     * Get information about the requests.
     * @return True if there is a request to fetch products, false otherwise.
     */
    public boolean isRequestFetchProducts() {
        return requestFetchProducts;
    }

    /**
     * Get information about the requests.
     * @return True if there is a request to ask for prime materials, false otherwise.
     */
    public boolean isRequestPrimeMaterials() {
        return requestPrimeMaterials;
    }

    /**
     * Get information about customers in the shop.
     * @return The number of customers presently in the shop.
     */
    public int getnCustomerIn() {
        return nCustomerIn;
    }

    /**
     * Get the number of goods in display at the shop.
     * @return The number of goods
     */
    public int getnGoodsInDisplay() {
        return nGoodsInDisplay;
    }

    /**
     * Get the number of goods bought by a customer.
     * @return The number of goods that a customer has bought so far.
     */
    public int getnBoughtGoods() {
        return nBoughtGoods;
    }

    /**
     * Get the number of current prime materials present in the workshop.
     * @return The number of prime materials in the workshop.
     */
    public int getnCurrentPrimeMaterials() {
        return nCurrentPrimeMaterials;
    }

    /**
     * Get the number of products stored in the workshop.
     * @return The number of products stored in the workshop.
     */
    public int getnProductsStored() {
        return nProductsStored;
    }

    /**
     * Get the number of prime materials transfers to the workshop.
     * @return The number of transfers of prime materials.
     */
    public int getnTimesPrimeMaterialsFetched() {
        return nTimesPrimeMaterialsFetched;
    }

     /**
     * Get the number of prime materials supplied to the workshop.
     * @return The number of supplies of prime materials.
     */
    public int getnTotalPrimeMaterialsSupplied() {
        return nTotalPrimeMaterialsSupplied;
    }

    /**
     * Get the total number of finished products.
     * @return Total number of finished products.
     */
    public int getnFinishedProducts() {
        return nFinishedProducts;
    }

    /**
     * See if the Craftsman has a finished product.
     * @return True if the product is finished, false otherwise.
     */
    public boolean isFinishedProduct() {
        return finishedProduct;
    }
    
    /**
     * Get value of the returnEntr variable.
     * @return Value of the returnEntr.
     */
    public int getReturnEntr() {
        return returnEntr;
    }
    
    /**
     * Get the next task for the Entrepreneur.
     * @return Character that translates to the next task of the Entrepreneur.
     */
    public char getNextTask() {
        return nextTask;
    }
    
    /**
     * Convert the message type to a readable/writable format.
     * @return Message type as a string.
     */
    @Override
    public String toString() {
        return this.type.toString();
    }
}
