package ClientSide.Customer;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the Customer entity.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Customer extends Thread {
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
    
    /**
     * Initiliazes the customer class with the required information.
     * 
     * @param id The customer identifier.
     */
    public Customer(int id) {
        this.setName("Customer "+id);
        this.id = id;
        state = CustomerState.CARRYING_OUT_DAILY_CHORES;
    }
    
    /**
     * This function represents the life cycle of Customer.
     */
    @Override
    public void run() {
        int nProducts;
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
    }
    
    /**
     * Updates the state of the Customer.
     * 
     * @param state The new state of the customer.
     */
    public void setState(CustomerState state) {
        this.state = state;
    }
   
    /**
     * Gets the current state of the customer.
     * 
     * @return The state of the customer.
     */
    public CustomerState getCurrentState() {
        return state;
    }
    
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
}
