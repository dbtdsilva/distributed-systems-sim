package ServerSide.Shop;

import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.ShopInterface;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConst;
import Structures.Enumerates.CraftsmanState;
import Structures.Enumerates.CustomerState;
import Structures.Enumerates.EntrepreneurState;
import Structures.Enumerates.ShopState;
import Structures.VectorClock.VectorTimestamp;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The monitor that represents the Shop.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public class Shop implements ShopInterface {
    private int nCustomersInside;
    private int nProductsStock;
    private ShopState shopState;
    private final Queue<Integer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    private int requestEntrepreneur;
    
    private final VectorTimestamp clocks;
    
    private final LoggingInterface log;
    
    /**
     * Initializes the shop class with the required information.
     * 
     * @param log The general repository
     */
    public Shop(LoggingInterface log) {
        this.requestEntrepreneur = 0;
        this.log = log;
        this.nProductsStock = 0;
        this.shopState = ShopState.CLOSED;
        this.nCustomersInside = 0;
        this.waitingLine = new LinkedList<>();
        this.reqFetchProducts = false;
        this.reqPrimeMaterials = false;
        
        this.clocks = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
    }
    
        /**************/
        /** CUSTOMER **/
        /**************/  
    
    /**
     * The customer goes to the Shopping.
     * 
     * @param id customer identifier
     */
    @Override
    public synchronized VectorTimestamp goShopping(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        log.UpdateCustomerState(id, CustomerState.CHECKING_SHOP_DOOR_OPEN, clocks.clone());
        return clocks.clone();
    }
    
    /**
     * This function allows the customer to check if the door is open or not.
     * 
     * @return returns true if the shop is open; returns false if otherwise.
     */
    @Override
    public synchronized boolean isDoorOpen() {
        return shopState == ShopState.OPEN;
    }
    
    /**
     * The customers enters in the shop. He updates his state and the number of
     * customers inside the shop.
     * 
     * @param id customer identifier
     */
    @Override
    public synchronized VectorTimestamp enterShop(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        
        nCustomersInside += 1;
        
        log.WriteShopAndCustomerStat(shopState, nCustomersInside, nProductsStock, 
                    reqFetchProducts, reqPrimeMaterials, 
                    CustomerState.APPRAISING_OFFER_IN_DISPLAY,
                    id, 0, clocks.clone());
        
        return clocks.clone();
    }
    
    /**
     * The customer exits the shop, notifying the Entrepreneur that he left. He
     * need to update the number of customers inside the shop and update his state.
     * He will also notify the Entrepreneur to wake up, she might need to leave
     * the shop.
     * 
     * @param id customer identifier 
     */
    @Override
    public synchronized VectorTimestamp exitShop(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        
        nCustomersInside -= 1;
        requestEntrepreneur++;
        notifyAll();        /* Telling entrepreneur */
        
        log.WriteShopAndCustomerStat(shopState, nCustomersInside, nProductsStock, 
                    reqFetchProducts, reqPrimeMaterials, 
                    CustomerState.CARRYING_OUT_DAILY_CHORES,
                    id, 0, clocks.clone());
        
        return clocks.clone();
    }
    
    /**
     * The customer searchs for products inside the Shop.
     * 
     * @return number of products that customer is going to buy (Between 0 and 2)
     */
    @Override
    public synchronized Object[] perusingAround(VectorTimestamp vt) {
        clocks.update(vt);
        Object[] res = new Object[2];
        res[0] = clocks.clone();
        
        double val = Math.random();
        if (val > 0.7 && nProductsStock >= 2) {
            nProductsStock -= 2;
            res[1] = 2;
        } else if (val > 0.2 && nProductsStock >= 1) {
            nProductsStock -= 1;
            res[1] = 1;
        } else {
            res[1] = 0;
        }
        
        return res;
    }
    
    /**
     * The customer requests the entrepreneur that he wants to buy a product. 
     * He will wait for the entrepreneur on the waiting line.
     * 
     * @param id customer identifier
     * @param nProducts the number of products bought
     */
    @Override
    public synchronized VectorTimestamp iWantThis(int id, int nProducts, VectorTimestamp vt) throws RemoteException {    
        clocks.update(vt);
        waitingLine.add(id);
        
        log.WriteShopAndCustomerStat(shopState, nCustomersInside, nProductsStock, 
                    reqFetchProducts, reqPrimeMaterials, 
                    CustomerState.BUYING_SOME_GOODS,
                    id, nProducts, clocks.clone());
        
        requestEntrepreneur++;
        notifyAll();    // Wake up entrepreneur
        
        while (waitingLine.contains(id)) {
            try {
                wait(); // Sleep customer
            } catch (InterruptedException ex) {
                Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return clocks.clone();
    }
    
    /**
     * The customer will try to enter the shop later.
     * 
     * @param id customer identifier
     */
    @Override
    public synchronized VectorTimestamp tryAgainLater(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        log.UpdateCustomerState(id, CustomerState.CARRYING_OUT_DAILY_CHORES, clocks.clone());
        
        return clocks.clone();
    }
  
        /******************/
        /** ENTREPRENEUR **/
        /******************/  
    
    /**
     * Entrepreneur is preparing to work, she will open the shop.
     */
    @Override
    public synchronized VectorTimestamp prepareToWork(VectorTimestamp vt) throws RemoteException {
        
        clocks.update(vt);
        shopState = ShopState.OPEN;
        
        log.WriteShopAndEntrepreneurStat(this.shopState, nCustomersInside, 
                nProductsStock, reqFetchProducts, reqPrimeMaterials, 
                EntrepreneurState.WAITING_FOR_NEXT_TASK, clocks.clone());
        return clocks.clone();
    }
    
    /**
     * The entrepreneur will wait until someone request her services.
     * 
     * @return  'C', if customers waiting for service;
     *          'M', if craftsman requested for prime materials;
     *          'T', if craftsman requested to fetch the products in the Workshop;
     *          'E', if the shop is out of business.
     */
    @Override
    public synchronized Object[] appraiseSit(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        Object[] res = new Object[2];
        char returnChar;
        
        while (true) {
            while (requestEntrepreneur == 0) {
                try {
                    wait();     // Entrepreneur needs to wait for the next tasks
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
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
            } else if (log.endOpEntrep()) {
                returnChar = 'E';
                break;
            }
        }
        
        res[0] = clocks.clone();
        res[1] = returnChar;
        
        return res;
    }
    
    /**
     * The Entrepreneur address the first customer in the waiting line.
     * 
     * @return the customer identifier
     */
    @Override
    public synchronized Object[] addressACustomer(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        Object[] res = new Object[2];
        log.UpdateEntreperneurState(EntrepreneurState.ATTENDING_A_CUSTOMER, clocks.clone());
        
        if (waitingLine.size() == 0) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, 
                new Exception("Address a customer without nobody to address"));
        }
        
        res[1] = waitingLine.poll();
        res[0] = clocks.clone();
        return res;
    }
    
    /**
     * The entrepreneur says good bye to the customer, waking him up.
     * 
     * @param id customer identifier
     */
    @Override
    public synchronized VectorTimestamp sayGoodByeToCustomer(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        notifyAll();    // Acordar customer com este ID
        
        log.UpdateEntreperneurState(EntrepreneurState.WAITING_FOR_NEXT_TASK, clocks.clone());
        
        return clocks.clone();
    }
    
    /**
     * The entrepreneur signals that she will close the shop.
     */
    @Override
    public synchronized VectorTimestamp closeTheDoor(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        shopState = ShopState.ECLOSED;
        log.WriteShop(this.shopState, nCustomersInside, nProductsStock, reqFetchProducts, reqPrimeMaterials, clocks.clone());
        
        return clocks.clone();
    }
    
    /**
     * This function returns true if there's customers inside the shop.
     * 
     * @return returns true if customers inside the shop; returns false otherwise.
     */
    @Override
    public synchronized boolean customersInTheShop() {
        return nCustomersInside > 0;
    }
    
    /**
     * The entrepreneur prepares to leave the shop.
     * At this point the shop is considered as closed.
     */
    @Override
    public synchronized VectorTimestamp prepareToLeave(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        shopState = ShopState.CLOSED;
        
        log.WriteShopAndEntrepreneurStat(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials, 
                EntrepreneurState.CLOSING_THE_SHOP, clocks.clone());
        
        return clocks.clone();
    }  
    
    /**
     * The entrepreneur returns to the shop, she went to fetch products or to deliver
     * prime materials to the craftsman.
     * If she went to fetch products, she still have the products with her and she
     * must to put them on shop stock. After that request is done.
     * 
     * @param nProducts the number of products that she's carrying.
     */
    @Override
    public synchronized VectorTimestamp returnToShop(int nProducts, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        if (nProducts >= 0) {
            nProductsStock += nProducts;
        }
        
        this.shopState = ShopState.OPEN;
        
        log.WriteShopAndEntrepreneurStat(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials, 
                EntrepreneurState.OPENING_THE_SHOP, clocks.clone());
        
        return clocks.clone();
    }
    
        /***************/
        /** CRAFTSMAN **/
        /***************/ 
    
    /**
     * The craftsman tells the Entrepreneur that they're out of prime materials.
     * To do that he needs to wake up entrepreneur.
     * 
     * @param id the craftsman identifier
     * @return returns true if request has been done; returns false if it was 
     * already done by someone before.
     */
    @Override
    public synchronized Object[] primeMaterialsNeeded(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        Object[] res = new Object[2];
        res[0] = clocks.clone();
        
        if (reqPrimeMaterials)  {
            res[1] = false;   
            return res;
        }
        
        reqPrimeMaterials = true;
        requestEntrepreneur++;
        
        notifyAll();
        
        
        log.WriteShopAndCraftsmanStat(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials, 
                CraftsmanState.CONTACTING_ENTREPRENEUR, id, clocks.clone());
        
        res[1] = true;
        return res;
    }
    
    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
    */
    @Override
    public synchronized VectorTimestamp batchReadyForTransfer(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        
        if (reqFetchProducts)
            return clocks.clone();
        
        reqFetchProducts = true;
        requestEntrepreneur++;
        notifyAll();
        
        log.WriteShopAndCraftsmanStat(shopState, nCustomersInside, nProductsStock, 
                reqFetchProducts, reqPrimeMaterials, 
                CraftsmanState.CONTACTING_ENTREPRENEUR, id, clocks.clone());
        
        return clocks.clone();
    }
        /*************/
        /** GENERAL **/
        /*************/
    
    /**
     * This function is used to the Entrepreneur reset the flag prime materials
     * request.
     */
    @Override
    public synchronized void resetRequestPrimeMaterials() throws RemoteException {
        reqPrimeMaterials = false;
        log.UpdatePrimeMaterialsRequest(reqPrimeMaterials);
    }
    
    /**
     * This function is used to the Entrepreneur reset the flag requestProducts.
     */
    @Override
    public synchronized void resetRequestProducts() throws RemoteException {
        reqFetchProducts = false;
        log.UpdateFetchProductsRequest(reqFetchProducts);
    }
    /**
     * This function is used for the logging to signal the shop to shutdown.
     * 
     * @throws RemoteException may throw during a execution of a remote method call
     */
    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName = RegistryConst.hostRegistry;
        int rmiRegPortNumb = RegistryConst.portRegistry;
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConst.registerHandler;
        String nameEntryObject = RegistryConst.shopNameEntry;

        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Shop registration exception: " + e.getMessage());
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Shop not bound exception: " + e.getMessage());
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Shop closed.");
    }
}
