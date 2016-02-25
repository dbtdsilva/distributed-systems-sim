package ClientSide.Customer;

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
import Structures.Constants.ProbConst;
import Structures.Enumerates.CustomerState;
import Structures.VectorClock.VectorTimestamp;
import java.rmi.RemoteException;
>>>>>>> origin/Trabalho3
import java.util.logging.Level;
import java.util.logging.Logger;

/**
<<<<<<< HEAD
 * This class is used to represent the Customer entity.
=======
 * This class is used to represent the entity Craftsman
>>>>>>> origin/Trabalho3
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Customer extends Thread {
<<<<<<< HEAD
    /**
     * Stores the information about this Customer's state.
     *
     * @serialField state
     */
    private CustomerState state;
    
    /**
     * Stores the information about this Customer's identifier.
     *
     * @serialField id
     */
    private final int id;
=======
    private CustomerState state;
    private final int id;
    private final ShopInterface shop;
    private final LoggingInterface log;
    private final VectorTimestamp myClock;
    private VectorTimestamp receivedClock;
>>>>>>> origin/Trabalho3
    
    /**
     * Initiliazes the customer class with the required information.
     * 
     * @param id The customer identifier.
<<<<<<< HEAD
     */
    public Customer(int id) {
        this.setName("Customer "+id);
        this.id = id;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    
=======
     * @param log The general repository
     * @param shop The simulation shop where the customer will buy products.
     */
    public Customer(int id, LoggingInterface log, ShopInterface shop) {
        this.setName("Customer "+id);
        this.id = id;
        this.shop = shop;
        this.log = log;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
        myClock = new VectorTimestamp(ProbConst.nCraftsmen + ProbConst.nCustomers + 1, id + 1);
    }
>>>>>>> origin/Trabalho3
    /**
     * This function represents the life cycle of Customer.
     */
    @Override
    public void run() {
        int nProducts;
<<<<<<< HEAD
        do {
            livingNormalLife();
            goShopping(id);
        
            if(isDoorOpen()) {
                enterShop(id);
                if ((nProducts = perusingAround()) != 0)
                    iWantThis(id, nProducts);
                exitShop(id);
            }
            else {
                tryAgainLater(id);
            }
        } while (!endOpCustomer());
        
        System.out.println("Customer "+id+" ended execution!");
=======
        try {
            do {
                livingNormalLife();
                myClock.increment();
                receivedClock= shop.goShopping(id, myClock.clone());
                myClock.update(receivedClock);

                if(shop.isDoorOpen()) {
                    myClock.increment();
                    receivedClock = shop.enterShop(id, myClock.clone());
                    myClock.update(receivedClock);
                    
                    myClock.increment();
                    Object[] ret = shop.perusingAround(myClock.clone());
                    nProducts = (int)ret[1];
                    myClock.update((VectorTimestamp)ret[0]);
                    
                    if (nProducts != 0)   {
                        myClock.increment();
                        receivedClock = shop.iWantThis(id, nProducts, myClock.clone());
                        myClock.update(receivedClock);
                    }
                    myClock.increment();
                    receivedClock = shop.exitShop(id, myClock.clone());
                    myClock.update(receivedClock);
                }
                else {
                    myClock.increment();
                    receivedClock = shop.tryAgainLater(id, myClock.clone());
                    myClock.update(receivedClock);
                }
            } while (!log.endOpCustomer());
            System.out.println("Cliente "+id+" acabou execução!");
            log.Shutdown();
        } catch(RemoteException e) {
            e.printStackTrace();
        }
>>>>>>> origin/Trabalho3
    }
    
    /**
     * Updates the state of the Customer.
     * 
     * @param state The new state of the customer.
     */
    public void setState(CustomerState state) {
        this.state = state;
    }
<<<<<<< HEAD
   
=======
>>>>>>> origin/Trabalho3
    /**
     * Gets the current state of the customer.
     * 
     * @return The state of the customer.
     */
    public CustomerState getCurrentState() {
        return state;
    }
<<<<<<< HEAD
    
=======
>>>>>>> origin/Trabalho3
    /**
     * The customer waits some time before going to the shop.
     */
    private void livingNormalLife() {
        try {
            Thread.sleep((int) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Customer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
<<<<<<< HEAD

    /**
     * The customer goes to the Shopping.
     * 
     * @param id customer identifier
     */
    private void goShopping(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.GO_SHOPPING, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        CustomerState cs = inMessage.getCustState();
        if (type != MessageType.ACK || cs == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.setState(cs);
        con.close();
    }

    /**
     * This function allows the customer to check if the door is open or not.
     * 
     * @return returns true if the shop is open; returns false if otherwise.
     */
    private boolean isDoorOpen() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.IS_DOOR_OPEN);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        
        if (type != MessageType.POSITIVE && type != MessageType.NEGATIVE) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return type == MessageType.POSITIVE;
    }

    /**
     * The customers enters in the shop. He updates his state and the number of
     * customers inside the shop.
     * 
     * @param id customer identifier
     */
    private void enterShop(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.ENTER_SHOP, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        CustomerState cs = inMessage.getCustState();
        if (type != MessageType.ACK || cs == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.setState(cs);
        con.close();
    }

    /**
     * The customer searchs for products inside the Shop.
     * 
     * @return number of products that customer is going to buy (Between 0 and 2)
     */
    private int perusingAround() {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PERUSING_AROUND);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        int prods = inMessage.getnProducts();
        
        if (type != MessageType.ACK || prods == Message.ERROR_INT) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        return prods;
    }

    /**
     * The customer requests the entrepreneur that he wants to buy a product. 
     * He will wait for the entrepreneur on the waiting line.
     * 
     * @param id customer identifier
     * @param nProducts the number of products bought
     */
    private void iWantThis(int id, int nProducts) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.I_WANT_THIS, id, nProducts);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        CustomerState cs = inMessage.getCustState();
        
        if (type != MessageType.ACK || cs == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        this.setState(cs);
    }

    /**
     * The customer exits the shop, notifying the Entrepreneur that he left. He
     * need to update the number of customers inside the shop and update his state.
     * He will also notify the Entrepreneur to wake up, she might need to leave
     * the shop.
     * 
     * @param id customer identifier 
     */
    private void exitShop(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.EXIT_SHOP, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        CustomerState cs = inMessage.getCustState();
        if (type != MessageType.ACK || cs == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        this.setState(cs);
    }

    /**
     * The customer will try to enter the shop later.
     * 
     * @param id customer identifier
     */
    private void tryAgainLater(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.TRY_AGAIN_LATER, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        
        MessageType type = inMessage.getType();
        CustomerState cs = inMessage.getCustState();
        if (type != MessageType.ACK || cs == null) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        this.setState(cs);
    }

    /**
     * Checks if the customer no longer has conditions to continue.
     * 
     * @return Returns false if the customer can continue; returns 
     * false if otherwise.
     */
    private boolean endOpCustomer() {
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.END_OPER_CUSTOMER);
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
