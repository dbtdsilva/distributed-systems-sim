package ClientSide.Entrepreneur;

<<<<<<< HEAD
import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;
=======
import Interfaces.LoggingInterface;
import Interfaces.ShopInterface;
import Interfaces.WarehouseInterface;
import Interfaces.WorkshopInterface;
import Structures.Constants.ProbConst;
import Structures.Enumerates.EntrepreneurState;
import Structures.VectorClock.VectorTimestamp;
import java.rmi.RemoteException;
>>>>>>> origin/Trabalho3
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Entrepreneur
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Entrepreneur extends Thread {
    private EntrepreneurState state;
<<<<<<< HEAD
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     */
    public Entrepreneur() {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
    }
    
=======
    private final ShopInterface shop;
    private final WarehouseInterface warehouse;
    private final WorkshopInterface workshop;
    private final LoggingInterface log;
      
    private final VectorTimestamp myClock;
    private VectorTimestamp receivedClock;
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     * 
     * @param log The general repository
     * @param shop The simulation shop where the entrepreneur will work.
     * @param warehouse The simulation warehouse where the entrepeneur fetchs prime 
     * materials when requested by the Craftsmen.
     * @param workshop The simulation workshop where the craftsmen are located.
     */
    public Entrepreneur(LoggingInterface log, ShopInterface shop, 
            WarehouseInterface warehouse, WorkshopInterface workshop) {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
        this.shop = shop;
        this.warehouse = warehouse;
        this.workshop = workshop;
        this.log = log;
        
        myClock = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, 0);
    }
>>>>>>> origin/Trabalho3
    /**
     * This function represents the life cycle of Entrepreneur.
     */
    @Override
    public void run() {
<<<<<<< HEAD
        do {
            boolean canGoOut = false;
            char sit;
            
            prepareToWork();
            do {                
                sit = appraiseSit();
                switch (sit) {
                    case 'C': 
                        int id = addressACustomer();
                        serviceCustomer(id);
                        sayGoodByeToCustomer(id);
                        break;
                    case 'T':
                    case 'M':
                    case 'E':
                        closeTheDoor();
                        canGoOut = !customersInTheShop();
                        break;
                }
            } while (!canGoOut);
            
            prepareToLeave();
            if (sit == 'T') {           /* Transfer products */
                int nProducts = goToWorkshop();
                returnToShop(nProducts);
            } else if (sit == 'M') {    /* Materials needed */
                int nMaterials = visitSuppliers();
                replenishStock(nMaterials);
                returnToShop(-1);
            }
        } while(!endOpEntrep());
        
        System.out.println("Entrepreneur ended execution!");
    }
    
=======
        try {
            do {
                boolean canGoOut = false;
                char sit;

                myClock.increment();
                receivedClock = shop.prepareToWork(myClock.clone());
                myClock.update(receivedClock);
                
                Object[] ret;
                
                do {   
                    myClock.increment();
                    ret = shop.appraiseSit(myClock.clone());
                    myClock.update((VectorTimestamp)ret[0]);
                    sit = (char)ret[1];
                    
                    switch (sit) {
                        case 'C': 
                            myClock.increment();
                            ret = shop.addressACustomer(myClock.clone());
                            int id = (int)ret[1];
                            myClock.update((VectorTimestamp)ret[0]);
                            
                            serviceCustomer(id);
                            
                            myClock.increment();
                            receivedClock = shop.sayGoodByeToCustomer(id, myClock.clone());
                            myClock.update(receivedClock);
                            break;
                        case 'T':
                        case 'M':
                        case 'E':
                            myClock.increment();
                            receivedClock = shop.closeTheDoor(myClock.clone());
                            myClock.update(receivedClock);
                            
                            canGoOut = !shop.customersInTheShop();
                            break;
                    }
                } while (!canGoOut);

                myClock.increment();
                receivedClock = shop.prepareToLeave(myClock.clone());
                myClock.update(receivedClock);
                if (sit == 'T') {           /* Transfer products */
                    myClock.increment();
                    ret = workshop.goToWorkshop(myClock.clone());
                    int nProducts = (int)ret[1];
                    myClock.update((VectorTimestamp)ret[0]);
                    
                    myClock.increment();
                    receivedClock = shop.returnToShop(nProducts, myClock.clone());
                    myClock.update(receivedClock);
                    
                } else if (sit == 'M') {    /* Materials needed */
                    ret = warehouse.visitSuppliers(myClock.clone());
                    int nMaterials = (int)ret[1];
                    myClock.update((VectorTimestamp)ret[0]);
                    
                    myClock.increment();
                    receivedClock = workshop.replenishStock(nMaterials, myClock.clone());
                    myClock.update(receivedClock);
                    
                    myClock.increment();
                    receivedClock = shop.returnToShop(-1, myClock.clone());
                    myClock.update(receivedClock);
                }
            } while(!log.endOpEntrep());
            System.out.println("Dona acabou execução!");
            
            log.Shutdown();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
>>>>>>> origin/Trabalho3
    /**
     * Updates the state of the entrepreneur.
     * 
     * @param state The new state of the entrepreneur.
     */
    public void setState(EntrepreneurState state) {
        this.state = state;
    }
<<<<<<< HEAD
    
=======
>>>>>>> origin/Trabalho3
    /**
     * Gets the current state of the entrepreneur.
     * 
     * @return The state of the entrepreneur.
     */
    public EntrepreneurState getCurrentState() {
        return state;
    }
<<<<<<< HEAD
    
=======
>>>>>>> origin/Trabalho3
    /**
     * The entrepreneur services a customer.
     * 
     * @param id the customerIdentifier
     */
    private void serviceCustomer(int id) {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Entrepreneur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
<<<<<<< HEAD
    
    /**
     * Entrepreneur is preparing to work, she will open the shop.
     */
    private void prepareToWork() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PREPARE_TO_WORK);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        if (type != MessageType.ACK || es == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.setState(es);
        con.close();
    }

    /**
     * The entrepreneur will wait until someone request her services.
     * 
     * @return  'C', if customers waiting for service;
     *          'M', if craftsman requested for prime materials;
     *          'T', if craftsman requested to fetch the products in the Workshop;
     *          'E', if the shop is out of business.
     */
    private char appraiseSit() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.APPRAISE_SIT);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        char c = inMessage.getNextTask();
        
        if (type != MessageType.ACK || c == Message.ERROR_CHAR) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return c;
    }

    /**
     * The Entrepreneur address the first customer in the waiting line.
     * 
     * @return the customer identifier
     */
    private int addressACustomer() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.ADDRESS_A_CUSTOMER);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        int id = inMessage.getReturnEntr();
        
        if (type != MessageType.ACK || es == null || id == Message.ERROR_INT) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.setState(es);
        con.close();
        return id;
    }

    /**
     * The entrepreneur says good bye to the customer, waking him up.
     * 
     * @param id customer identifier
     */
    private void sayGoodByeToCustomer(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.SAY_GOODBYE_TO_CUSTOMER, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        if (type != MessageType.ACK || es == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.setState(es);
        con.close();
    }

    /**
     * The entrepreneur signals that she will close the shop.
     */
    private void closeTheDoor() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CLOSE_THE_DOOR);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }

    /**
     * This function returns true if there's customers inside the shop.
     * 
     * @return returns true if customers inside the shop; returns false otherwise.
     */
    private boolean customersInTheShop() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.CUSTOMERS_IN_THE_SHOP);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        
        if (type == MessageType.POSITIVE)
            return true;
        else if(type == MessageType.NEGATIVE)
            return false;
        else
        {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return false;
    }

    /**
     * The entrepreneur prepares to leave the shop.
     * At this point the shop is considered as closed.
     */
    private void prepareToLeave() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PREPARE_TO_LEAVE);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        
        if (type != MessageType.ACK || es == null)  {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        
        this.setState(es);
        con.close();
    }

    /**
     * Entrepreneur goes to the Workshop to fetch the products in there.
     * 
     * @return the number of products that Entrepreneur is going to deliver to 
     * the shop
     */
    private int goToWorkshop() {
        ClientComm con = new ClientComm(CommConst.wsServerName, CommConst.wsServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.GO_TO_WORKSHOP);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        int prods = inMessage.getReturnEntr();
        
        if (type != MessageType.ACK || es == null || prods == Message.ERROR_INT)  {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        
        this.setState(es);
        con.close();
        return prods;
    }

    /**
     * The entrepreneur returns to the shop, she went to fetch products or to deliver
     * prime materials to the craftsman.
     * If she went to fetch products, she still have the products with her and she
     * must to put them on shop stock. After that request is done.
     * 
     * @param nProducts the number of products that she's carrying.
     */
    private void returnToShop(int nProducts) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.RETURN_TO_SHOP, nProducts);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        
        if (type != MessageType.ACK || es == null)  {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        this.setState(es);
    }

    /**
     * The entrepreneur visits the supplies to fetch prime materials for the
     * craftsman.
     * She will fetch a random value of prime materials.
     * 
     * @return the number of prime materials fetched
     */
    private int visitSuppliers() {
        ClientComm con = new ClientComm(CommConst.whServerName, CommConst.whServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.VISIT_SUPPLIERS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        int mats = inMessage.getReturnEntr();
        
        if (type != MessageType.ACK || es == null || mats == Message.ERROR_INT)  {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.setState(es);
        con.close();
        
        return mats;
    }

    /**
     * Entrepreneur goes to the Workshop and returns that prime materials that
     * she fetched from the Warehouse.
     * If nobody is waiting for her, it means that Entrepreneur arrived before
     * the Craftsman to the Workshop and he will not need to wait.
     * 
     * @param nMaterials number of prime materials
     */
    private void replenishStock(int nMaterials) {
        ClientComm con = new ClientComm(CommConst.wsServerName, CommConst.wsServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.REPLENISH_STOCK, nMaterials);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();        
        
        MessageType type = inMessage.getType();
        EntrepreneurState es = inMessage.getEntrState();
        
        if (type != MessageType.ACK || es == null)  {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        this.setState(es);
    }

    /**
     * Checks if the Entrepreneur no longer has conditions to continue its work.
     * 
     * @return Returns false if the entrepreneur can continue its work; returns 
     * false if otherwise.
     */
    private boolean endOpEntrep() {
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.END_OPER_ENTREPRENEUR);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type == MessageType.POSITIVE)
            return true;
        else if(type == MessageType.NEGATIVE)
            return false;
        else
        {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return false;
            
    }
=======
>>>>>>> origin/Trabalho3
}
