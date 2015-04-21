package ClientSide.Entrepreneur;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;
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
    
    /**
     * Initiliazes the entrepreneur class with the required information.
     */
    public Entrepreneur() {
        this.setName("Entrepreneur");
        
        state = EntrepreneurState.OPENING_THE_SHOP;
    }
    
    /**
     * This function represents the life cycle of Entrepreneur.
     */
    @Override
    public void run() {
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
        System.out.println("Dona acabou execução!");
    }
    
    /**
     * Updates the state of the entrepreneur.
     * 
     * @param state The new state of the entrepreneur.
     */
    public void setState(EntrepreneurState state) {
        this.state = state;
    }
    
    /**
     * Gets the current state of the entrepreneur.
     * 
     * @return The state of the entrepreneur.
     */
    public EntrepreneurState getCurrentState() {
        return state;
    }
    
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
        int id = inMessage.getId();
        
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
        int prods = inMessage.getnProducts();
        
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
        int mats = inMessage.getnMaterials();
        
        if (type != MessageType.ACK || es == null || mats == Message.ERROR_INT)  {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
        this.setState(es);
        
        return mats;
    }

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
}
