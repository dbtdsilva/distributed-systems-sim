package ServerSide.Shop;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import static java.lang.Thread.sleep;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The class that represents the Shop.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public class Shop {
    private int nCustomersInside;
    private int nProductsStock;
    private ShopState shopState;
    private final Queue<Integer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    private int requestEntrepreneur;
    
    /**
     * Initializes the shop class with the required information.
     * 
     */
    public Shop() {
        this.requestEntrepreneur = 0;
        this.nProductsStock = 0;
        this.shopState = ShopState.CLOSED;
        this.nCustomersInside = 0;
        this.waitingLine = new LinkedList<>();
        this.reqFetchProducts = false;
        this.reqPrimeMaterials = false;
    }
    
        /**************/
        /** CUSTOMER **/
        /**************/  
    
    /**
     * The customer goes to the Shopping.
     * 
     * @param id customer identifier
     */
    public synchronized void goShopping(int id) {
        //((Customer) Thread.currentThread()).setState(CustomerState.CHECKING_SHOP_DOOR_OPEN);
        //log.UpdateCustomerState(id, CustomerState.CHECKING_SHOP_DOOR_OPEN);
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_CUST_STATE, 
                CustomerState.CHECKING_SHOP_DOOR_OPEN, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
    /**
     * This function allows the customer to check if the door is open or not.
     * 
     * @return returns true if the shop is open; returns false if otherwise.
     */
    public synchronized boolean isDoorOpen() {
        return shopState == ShopState.OPEN;
    }
    /**
     * The customers enters in the shop. He updates his state and the number of
     * customers inside the shop.
     * 
     * @param id customer identifier
     */
    public synchronized void enterShop(int id) {
        //((Customer) Thread.currentThread()).setState(CustomerState.APPRAISING_OFFER_IN_DISPLAY);
        nCustomersInside += 1;
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_SHOP_CUST_STATE, 
                shopState, nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, CustomerState.APPRAISING_OFFER_IN_DISPLAY, id,
                0);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
        //log.WriteShopAndCustomerStat(shopState, nCustomersInside, nProductsStock, 
        //            reqFetchProducts, reqPrimeMaterials, 
        //            CustomerState.APPRAISING_OFFER_IN_DISPLAY,
        //            id, 0);
    }
    /**
     * The customer exits the shop, notifying the Entrepreneur that he left. He
     * need to update the number of customers inside the shop and update his state.
     * He will also notify the Entrepreneur to wake up, she might need to leave
     * the shop.
     * 
     * @param id customer identifier 
     */
    public synchronized void exitShop(int id) {
        //((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        nCustomersInside -= 1;
        requestEntrepreneur++;
        notifyAll();        /* Telling entrepreneur */
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_SHOP_CUST_STATE, 
                shopState, nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, CustomerState.CARRYING_OUT_DAILY_CHORES, id,
                0);
        con.writeObject(outMessage);
        
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
        //log.WriteShopAndCustomerStat(shopState, nCustomersInside, nProductsStock, 
        //            reqFetchProducts, reqPrimeMaterials, 
        //            CustomerState.CARRYING_OUT_DAILY_CHORES,
        //            id, 0);
    }
    /**
     * The customer searchs for products inside the Shop.
     * 
     * @return number of products that customer is going to buy (Between 0 and 2)
     */
    public synchronized int perusingAround() {
        double val = Math.random();
        if (val > 0.7 && nProductsStock >= 2) {
            nProductsStock -= 2;
            return 2;
        } else if (val > 0.2 && nProductsStock >= 1) {
            nProductsStock -= 1;
            return 1;
        } else {
            return 0;
        }
    }
    /**
     * The customer requests the entrepreneur that he wants to buy a product. 
     * He will wait for the entrepreneur on the waiting line.
     * 
     * @param id customer identifier
     * @param nProducts the number of products bought
     */
    public synchronized void iWantThis(int id, int nProducts) {
        //((Customer) Thread.currentThread()).setState(CustomerState.BUYING_SOME_GOODS);        
        waitingLine.add(id);
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_SHOP_CUST_STATE, 
                shopState, nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, CustomerState.BUYING_SOME_GOODS, id,
                nProducts);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
        //log.WriteShopAndCustomerStat(shopState, nCustomersInside, nProductsStock, 
        //            reqFetchProducts, reqPrimeMaterials, 
        //            ((Customer) Thread.currentThread()).getCurrentState(),
        //            id, nProducts);
        
        requestEntrepreneur++;
        notifyAll();    // Wake up entrepreneur
        
        while (waitingLine.contains(id)) {
            try {
                wait(); // Sleep customer
            } catch (InterruptedException ex) {
                Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    /**
     * The customer will try to enter the shop later.
     * 
     * @param id customer identifier
     */
    public synchronized void tryAgainLater(int id) {
        //((Customer) Thread.currentThread()).setState(CustomerState.CARRYING_OUT_DAILY_CHORES);
        //log.UpdateCustomerState(id, ((Customer) Thread.currentThread()).getCurrentState());
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_CUST_STATE, 
                CustomerState.CARRYING_OUT_DAILY_CHORES, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
  
        /******************/
        /** ENTREPRENEUR **/
        /******************/  
    
    /**
     * Entrepreneur is preparing to work, she will open the shop.
     */
    public synchronized void prepareToWork() {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        shopState = ShopState.OPEN;
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_SHOP_ENTR_STATE, shopState, nCustomersInside, 
                nProductsStock, reqFetchProducts, reqPrimeMaterials, 
                EntrepreneurState.WAITING_FOR_NEXT_TASK);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
        //log.WriteShopAndEntrepreneurStat(this.shopState, nCustomersInside, 
        //        nProductsStock, reqFetchProducts, reqPrimeMaterials, 
        //        ((Entrepreneur) Thread.currentThread()).getCurrentState());
    }
    /**
     * The entrepreneur will wait until someone request her services.
     * 
     * @return  'C', if customers waiting for service;
     *          'M', if craftsman requested for prime materials;
     *          'T', if craftsman requested to fetch the products in the Workshop;
     *          'E', if the shop is out of business.
     */
    public synchronized char appraiseSit() {
        char returnChar;
        while (true) {
            while (requestEntrepreneur == 0) {
                try {
                    wait();     // Entrepreneur needs to wait for the next tasks
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            /* Calculating endOperResult */
            ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
            while (!con.open())
            {
                try {
                    sleep((long) (10));
                } catch (InterruptedException e) {
                }
            }
            Message outMessage = new Message(MessageType.END_OPER_ENTREPRENEUR);
            con.writeObject(outMessage);
            Message inMessage = (Message) con.readObject();
            MessageType type = inMessage.getType();
            if (type != MessageType.POSITIVE && type != MessageType.NEGATIVE) {
                System.out.println("Tipo inválido!");
                System.exit(1);
            }
            con.close();
            
            boolean endOpEntr = MessageType.POSITIVE == type;
            requestEntrepreneur--;
            if (!waitingLine.isEmpty()) {
                returnChar = 'C';
                break;
            } else if (reqPrimeMaterials) {
                returnChar = 'M';
                break;
            } else if (reqFetchProducts) {
                returnChar = 'T';
                break;
            } else if (endOpEntr) {
                returnChar = 'E';
                break;
            }
        }
        return returnChar;
    }
    /**
     * The Entrepreneur address the first customer in the waiting line.
     * 
     * @return the customer identifier
     */
    public synchronized int addressACustomer() {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.ATTENDING_A_CUSTOMER);
        //log.UpdateEntreperneurState(EntrepreneurState.ATTENDING_A_CUSTOMER);
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_ENTR_STATE, EntrepreneurState.ATTENDING_A_CUSTOMER);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
        
        if (waitingLine.size() == 0) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, 
                new Exception("Address a customer without nobody to address"));
        }
        
        return waitingLine.poll();
    }
    /**
     * The entrepreneur says good bye to the customer, waking him up.
     * 
     * @param id customer identifier
     */
    public synchronized void sayGoodByeToCustomer(int id) {
        notifyAll();    // Acordar customer com este ID
        
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.WAITING_FOR_NEXT_TASK);
        //log.UpdateEntreperneurState(((Entrepreneur) Thread.currentThread()).getCurrentState());
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_ENTR_STATE, EntrepreneurState.WAITING_FOR_NEXT_TASK);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
    /**
     * The entrepreneur signals that she will close the shop.
     */
    public synchronized void closeTheDoor() {
        shopState = ShopState.ECLOSED;
        
        //log.WriteShop(this.shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials);
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_SHOP, shopState, nCustomersInside, 
                nProductsStock, reqFetchProducts, reqPrimeMaterials);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
    /**
     * This function returns true if there's customers inside the shop.
     * 
     * @return returns true if customers inside the shop; returns false otherwise.
     */
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }
    /**
     * The entrepreneur prepares to leave the shop.
     * At this point the shop is considered as closed.
     */
    public synchronized void prepareToLeave() {
        shopState = ShopState.CLOSED;
        
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.CLOSING_THE_SHOP);
        //log.WriteShopAndEntrepreneurStat(shopState, nCustomersInside, nProductsStock, 
        //        reqFetchProducts, reqPrimeMaterials, 
        //        ((Entrepreneur) Thread.currentThread()).getCurrentState());
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_SHOP_ENTR_STATE, shopState, 
                nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, EntrepreneurState.CLOSING_THE_SHOP);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }  
    /**
     * The entrepreneur returns to the shop, she went to fetch products or to deliver
     * prime materials to the craftsman.
     * If she went to fetch products, she still have the products with her and she
     * must to put them on shop stock. After that request is done.
     * 
     * @param nProducts the number of products that she's carrying.
     */
    public synchronized void returnToShop(int nProducts) {
        if (nProducts >= 0) {
            nProductsStock += nProducts;
        }
        
        this.shopState = ShopState.OPEN;
        
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.OPENING_THE_SHOP);
        //log.WriteShopAndEntrepreneurStat(shopState, nCustomersInside, nProductsStock, 
        //        reqFetchProducts, reqPrimeMaterials, 
        //        ((Entrepreneur) Thread.currentThread()).getCurrentState());
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_SHOP_ENTR_STATE, shopState, 
                nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, EntrepreneurState.OPENING_THE_SHOP);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
    
        /***************/
        /** CRAFTSMAN **/
        /***************/ 
    
    /**
     * The craftsman tells the Entrepreneur that they're out of prime materials.
     * To do that he needs to wake up entrepreneur.
     * 
     * @param id the craftsman identifier
     */
    public synchronized void primeMaterialsNeeded(int id) {
        if (reqPrimeMaterials)
            return;
        
        reqPrimeMaterials = true;
        requestEntrepreneur++;
        
        notifyAll();
        
        //((Craftsman) Thread.currentThread()).setState(CraftsmanState.CONTACTING_ENTREPRENEUR);
        //log.WriteShopAndCraftsmanStat(shopState, nCustomersInside, nProductsStock, 
        //        reqFetchProducts, reqPrimeMaterials, 
        //        ((Craftsman) Thread.currentThread()).getCurrentState(), id);
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_SHOP_CRAFT_STATE, shopState, 
                nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, CraftsmanState.CONTACTING_ENTREPRENEUR, id);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
    */
    public synchronized void batchReadyForTransfer(int id) {
        if (reqFetchProducts)
            return;
        
        reqFetchProducts = true;
        requestEntrepreneur++;
        notifyAll();
        
        //((Craftsman) Thread.currentThread()).setState(CraftsmanState.CONTACTING_ENTREPRENEUR);
        //log.WriteShopAndCraftsmanStat(shopState, nCustomersInside, nProductsStock, 
        //        reqFetchProducts, reqPrimeMaterials, 
        //        ((Craftsman) Thread.currentThread()).getCurrentState(), id);
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_SHOP_CRAFT_STATE, shopState, 
                nCustomersInside, nProductsStock, reqFetchProducts, 
                reqPrimeMaterials, CraftsmanState.CONTACTING_ENTREPRENEUR, id);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
        /*************/
        /** GENERAL **/
        /*************/
    
    /**
     * This function is used to the Entrepreneur reset the flag prime materials
     * request.
     */
    public synchronized void resetRequestPrimeMaterials() {
        reqPrimeMaterials = false;
        //log.UpdatePrimeMaterialsRequest(reqPrimeMaterials);
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.UPDATE_REQ_PMATERIALS, reqPrimeMaterials);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        con.close();
    }
    /**
     * This function is used to the Entrepreneur reset the flag requestProducts.
     */
    public synchronized void resetRequestProducts() {
        reqFetchProducts = false;
        //log.UpdateFetchProductsRequest(reqFetchProducts);
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.UPDATE_REQ_PRODUCTS, reqFetchProducts);
        con.writeObject(outMessage);
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
    }
}
