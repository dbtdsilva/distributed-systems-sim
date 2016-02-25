package ServerSide.Workshop;

<<<<<<< HEAD
import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import Communication.ProbConst;
import static java.lang.Thread.sleep;
=======
import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.ShopInterface;
import Interfaces.WorkshopInterface;
import ServerSide.Warehouse.Warehouse;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConfig;
import Structures.Enumerates.CraftsmanState;
import Structures.Enumerates.EntrepreneurState;
import Structures.VectorClock.VectorTimestamp;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
>>>>>>> origin/Trabalho3
import java.util.logging.Level;
import java.util.logging.Logger;

/**
<<<<<<< HEAD
 * The class that represents the Workshop.
=======
 * The monitor that represents the Workshop.
>>>>>>> origin/Trabalho3
 * 
 * @author Diogo Silva, 60337 
 * @author Tânia Alves, 60340
 */
<<<<<<< HEAD
public class Workshop {
    /**
     * Number of finished products that are currently stored at the workshop.
     */
    private int nProductsStored;
    
    /**
     * Number of current prime materials present in the workshop.
     */
    private int nCurrentPrimeMaterials;
   
    /**
     * Total count of finished products during the simulation.
     */
    private int nFinishedProducts;
    
    /**
     * Statistical counter that keeps track of the number of times that prime materials were fetched.
     */
    private int nTimesPrimeMaterialsFetched;
    
    /**
     * Statistical counter that keeps track of the number of times that prime materials were supplied.
     */
    private int nTotalPrimeMaterialsSupplied;
    
    /**
     * Boolean variable that says if the Entrepreneur is waiting or not.
     */
    private boolean waitingEntrepreneur;
    
    /**
     * Initializes the workshop class with the required information.
     */
    public Workshop() {
=======
public class Workshop implements WorkshopInterface {
    private int nProductsStored;
    private int nCurrentPrimeMaterials;
    private int nFinishedProducts;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private boolean waitingEntrepreneur;
    
    private final VectorTimestamp clocks;
    
    private final LoggingInterface log;
    private final ShopInterface shop;
            
    /**
     * Initializes the workshop class with the required information.
     * 
     * @param log The general repository where the semaphores are stored along with some other useful global variables.
     * @param shop The shop that is created in the simulation.
     */
    public Workshop(LoggingInterface log, ShopInterface shop) {
>>>>>>> origin/Trabalho3
        this.nProductsStored = 0;
        this.nCurrentPrimeMaterials = 0;
        this.nFinishedProducts = 0;
        this.nTimesPrimeMaterialsFetched = 0;
        this.nTotalPrimeMaterialsSupplied = 0;
        this.waitingEntrepreneur = false;
<<<<<<< HEAD
=======
        
        this.clocks = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
        
        this.log = log;
        this.shop = shop;
>>>>>>> origin/Trabalho3
    }
    
        /******************/
        /** ENTREPRENEUR **/
        /******************/
<<<<<<< HEAD
    
    /**
     * Entrepreneur goes to the Workshop to fetch the products in there.
     * 
     * @return The number of finished products that the Entrepreneur is bringing back.
     */
    public synchronized int goToWorkshop() {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        int n = nProductsStored;
        nProductsStored = 0;
        
        Message inMessage, outMessage;
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        while (!con.open()) {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.RESET_REQ_PRODUCTS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open()) {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_WSHOP_ENTR_STATE, nCurrentPrimeMaterials, 
                nProductsStored, nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                nFinishedProducts, EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        return n;
=======
    /**
     * This function is used for the logging to signal the shop to shutdown.
     * 
     * @throws RemoteException may throw during a execution of a remote method call
     */
    @Override
    public void signalShutdown() throws RemoteException {
        Register reg = null;
        Registry registry = null;

        String rmiRegHostName;
        int rmiRegPortNumb;
        RegistryConfig rc = new RegistryConfig("../../config.ini");
        rmiRegHostName = rc.registryHost();
        rmiRegPortNumb = rc.registryPort();
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.workshopNameEntry;

        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Workshop registration exception: " + e.getMessage());
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Workshop not bound exception: " + e.getMessage());
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Workshop closed.");
    }
    /**
     * Entrepreneur goes to the Workshop to fetch the products in there.
     * 
     * @return the number of products that Entrepreneur is going to deliver to 
     * the shop
     */
    @Override
    public synchronized Object[] goToWorkshop(VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        Object[] res = new Object[2];
        
        int n = nProductsStored;
        nProductsStored = 0;
        
        shop.resetRequestProducts();
        log.WriteWorkshopAndEntrepreneurStat(nCurrentPrimeMaterials, nProductsStored, 
                    nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                    nFinishedProducts, EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS, clocks.clone());
        
        res[1] = n;
        res[0] = clocks.clone();
        return res;
>>>>>>> origin/Trabalho3
    }
    
    /**
     * Entrepreneur goes to the Workshop and returns that prime materials that
     * she fetched from the Warehouse.
     * If nobody is waiting for her, it means that Entrepreneur arrived before
     * the Craftsman to the Workshop and he will not need to wait.
     * 
<<<<<<< HEAD
     * @param nMaterials Number of materials that the Entrepreneur is delivering.
     */
    public synchronized void replenishStock(int nMaterials) {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.DELIVERING_PRIME_MATERIALS);
=======
     * @param nMaterials number of prime materials
     */
    @Override
    public synchronized VectorTimestamp replenishStock(int nMaterials, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        
>>>>>>> origin/Trabalho3
        nTimesPrimeMaterialsFetched++;
        nTotalPrimeMaterialsSupplied += nMaterials;
        nCurrentPrimeMaterials += nMaterials;
        
<<<<<<< HEAD
        waitingEntrepreneur = false;
        
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        Message outMessage = new Message(MessageType.RESET_REQ_PMATERIALS);
        con.writeObject(outMessage);
        
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(MessageType.WRITE_WSHOP_ENTR_STATE, 
                nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, 
                nTotalPrimeMaterialsSupplied, nFinishedProducts, EntrepreneurState.DELIVERING_PRIME_MATERIALS);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        notifyAll();
=======
        shop.resetRequestPrimeMaterials();
        waitingEntrepreneur = false;
        
        log.WriteWorkshopAndEntrepreneurStat(nCurrentPrimeMaterials, nProductsStored, 
                    nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                    nFinishedProducts,EntrepreneurState.DELIVERING_PRIME_MATERIALS, clocks.clone());
                
        notifyAll();    // Wake up craftsmen
        
        return clocks.clone();
>>>>>>> origin/Trabalho3
    }

        /***************/
        /** CRAFTSMEN **/
        /***************/
    
    /**
     * The craftsman is preparing to manufacture a product.
     * If there are enough materials to manufacture the product, the number of available prime materials 
     * is updated. 
     * However, if the prime materials aren't enough, the function returns false.
     * 
     * @param id The craftsman identifier.
     * @return true if there are enough prime materials to manufacture a product or false if there aren't.
     */
<<<<<<< HEAD
    public synchronized boolean collectingMaterials(int id) {
=======
    @Override
    public synchronized Object[] collectingMaterials(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        
        Object[] res = new Object[2];
        res[0] = clocks.clone();
        res[1] = true;
        
>>>>>>> origin/Trabalho3
        while (waitingEntrepreneur && nCurrentPrimeMaterials < ProbConst.nPrimeMaterials) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Workshop.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (nCurrentPrimeMaterials < ProbConst.MINprimeMaterials && !waitingEntrepreneur &&
                nTimesPrimeMaterialsFetched < ProbConst.MAXSupplies) {
            waitingEntrepreneur = true;
<<<<<<< HEAD
            return false;
=======
            res[1] = false;
            return res;
>>>>>>> origin/Trabalho3
        }
        
        nCurrentPrimeMaterials -= ProbConst.primeMaterialsPerProduct;
        
<<<<<<< HEAD
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(MessageType.WRITE_WSHOP, nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, 
                            nTotalPrimeMaterialsSupplied, nFinishedProducts);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return true;
=======
        log.WriteWorkshop(nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, 
                            nTotalPrimeMaterialsSupplied, nFinishedProducts, clocks.clone());
        return res;
>>>>>>> origin/Trabalho3
    }   
    
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     * 
     * @return the number of products stored in workshop.
     */
<<<<<<< HEAD
    public synchronized int goToStore(int id) {
        nFinishedProducts++;
        nProductsStored++;
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        Message outMessage = new Message(MessageType.WRITE_WSHOP_CRAFT_STATE,
                nCurrentPrimeMaterials, nProductsStored, 
                nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                nFinishedProducts, CraftsmanState.STORING_IT_FOR_TRANSFER, id, true);
        
        con.writeObject(outMessage);
        
        Message inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return nProductsStored;
=======
    @Override
    public synchronized Object[] goToStore(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        Object[] res = new Object[2];
        res[0] = clocks.clone();
        
        nFinishedProducts++;
        nProductsStored++;
        
        log.WriteWorkshopAndCraftsmanStat(nCurrentPrimeMaterials, nProductsStored, 
                nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, nFinishedProducts,
                CraftsmanState.STORING_IT_FOR_TRANSFER, id, true, clocks.clone());
        
        res[1] = nProductsStored;
        
        return res;
>>>>>>> origin/Trabalho3
    }
    
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     */
<<<<<<< HEAD
    public synchronized void backToWork(int id) {
        //((Craftsman) Thread.currentThread()).setState(CraftsmanState.FETCHING_PRIME_MATERIALS);
        //log.UpdateCraftsmanState(id, ((Craftsman) Thread.currentThread()).getCurrentState());
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(MessageType.WRITE_CRAFT_STATE, CraftsmanState.FETCHING_PRIME_MATERIALS, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
=======
    @Override
    public synchronized VectorTimestamp backToWork(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        log.UpdateCraftsmanState(id, CraftsmanState.FETCHING_PRIME_MATERIALS, clocks.clone());
        return clocks.clone();
>>>>>>> origin/Trabalho3
    } 
    
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     */
<<<<<<< HEAD
    public synchronized void prepareToProduce(int id) {
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        
        outMessage = new Message(MessageType.WRITE_CRAFT_STATE, CraftsmanState.PRODUCING_A_NEW_PIECE, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
=======
    @Override
    public synchronized VectorTimestamp prepareToProduce(int id, VectorTimestamp vt) throws RemoteException {
        clocks.update(vt);
        log.UpdateCraftsmanState(id, CraftsmanState.PRODUCING_A_NEW_PIECE, clocks.clone());
        return clocks.clone();
>>>>>>> origin/Trabalho3
    } 
}
