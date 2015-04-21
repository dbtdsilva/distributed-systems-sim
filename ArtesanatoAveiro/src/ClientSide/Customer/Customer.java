package ClientSide.Customer;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Craftsman
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Customer extends Thread {
    private CustomerState state;
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
        System.out.println("Cliente "+id+" acabou execução!");
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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        this.setState(cs);
    }

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
        CustomerState cs = inMessage.getCustState();
        
        if(type == MessageType.POSITIVE && cs != null)
        {
            this.setState(cs);
            return true;
        }
        else if(type == MessageType.NEGATIVE && cs != null)
        {
            this.setState(cs);
            return false;
        }
        else
        {
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        return false;
    }

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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        this.setState(cs);
    }

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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        
        return prods;
    }

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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        this.setState(cs);
    }

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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        this.setState(cs);
    }

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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        this.setState(cs);
    }

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
            System.out.println("Tipo inválido!");
            System.exit(1);
        }
        return false;
    }
}
