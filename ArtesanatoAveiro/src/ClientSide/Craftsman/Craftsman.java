package ClientSide.Craftsman;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import static Communication.Message.Message.ERROR_INT;
import Communication.Message.MessageType;
import Communication.ProbConst;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class defines the methods for the Craftsman thread.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Craftsman extends Thread {

    /**
     * Stores the information about this Craftsman's state.
     *
     * @serialField state
     */
    private CraftsmanState state;
    
    /**
     * Stores the information about this Craftsman's identifier.
     *
     * @serialField id
     */
    private final int id;

    /**
     * Initiliazes the craftsman class with the required information.
     *
     * @param id The craftsman identifier.
     */
    public Craftsman(int id) {
        this.setName("Craftsman " + id);
        this.id = id;
        state = CraftsmanState.FETCHING_PRIME_MATERIALS;
    }

    /**
     * This function represents the life cycle of Craftsman.
     */
    @Override
    public void run() {
        int val;
        do {
            if (!collectingMaterials(id)) {
                primeMaterialsNeeded(id);
                backToWork(id);
            } else {
                prepareToProduce(id);
                shappingItUp();
                int productsStored = goToStore(id);
                if (productsStored >= ProbConst.MAXproductsInWorkshop) {
                    batchReadyForTransfer(id);
                }
                backToWork(id);
            }
        } while ((val = endOperCraft()) == 0);

        if (val == 2) {
            batchReadyForTransfer(id);
        }

        System.out.println("Craftsman " + id + " ended execution!");
    }

    /**
     * Updates the state of the craftsman.
     *
     * @param state The new state of the craftsman.
     */
    public void setState(CraftsmanState state) {
        this.state = state;
    }

    /**
     * Gets the current state of the craftsman.
     *
     * @return The state of the craftsman.
     */
    public CraftsmanState getCurrentState() {
        return state;
    }

    /**
     * The craftsman is working on the next piece.
     */
    private void shappingItUp() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException ex) {
            Logger.getLogger(Craftsman.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The craftsman is preparing to manufacture a product.
     * If there are enough materials to manufacture the product, the number of available prime materials 
     * is updated. 
     * However, if the prime materials aren't enough, the function returns false.
     * 
     * @param id The craftsman identifier.
     * @return true if there are enough prime materials to manufacture a product or false if there aren't.
     */
    private boolean collectingMaterials(int id) {
        ClientComm con = new ClientComm(CommConst.wsServerName, CommConst.wsServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.COLLECTING_MATERIALS, id);
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
     * The craftsman tells the Entrepreneur that they're out of prime materials.
     * To do that he needs to wake up entrepreneur.
     * 
     * @param id the craftsman identifier
     * @return returns true if request has been done; returns false if it was 
     * already done by someone before.
     */
    private void primeMaterialsNeeded(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PRIME_MATERIALS_NEEDED, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        CraftsmanState stat = inMessage.getCraftState();
       
        if (type != MessageType.ACK) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        if (stat == null) {
            System.out.println("Thread " + getName() + ": Parametros da mensagem errados");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.state = stat;
        con.close();
    }

    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     */
    private void backToWork(int id) {
        ClientComm con = new ClientComm(CommConst.wsServerName, CommConst.wsServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.BACK_TO_WORK, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        CraftsmanState stat = inMessage.getCraftState();
        
        if (type != MessageType.ACK) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        if (stat == null) {
            System.out.println("Thread " + getName() + ": Parametros da mensagem errados");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.state = stat;
        con.close();
    }

    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     */
    private void prepareToProduce(int id) {
        ClientComm con = new ClientComm(CommConst.wsServerName, CommConst.wsServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.PREPARE_TO_PRODUCE, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        CraftsmanState stat = inMessage.getCraftState();
        
        if (type != MessageType.ACK) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        if (stat == null) {
            System.out.println("Thread " + getName() + ": Parametros da mensagem errados");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.state = stat;
        con.close();
    }

    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     * 
     * @return the number of products stored in workshop.
     */
    private int goToStore(int id) {
        ClientComm con = new ClientComm(CommConst.wsServerName, CommConst.wsServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.GO_TO_STORE, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        CraftsmanState stat = inMessage.getCraftState();
        
        if (type != MessageType.ACK) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        if (stat == null || inMessage.getnProductsStored()== ERROR_INT) {
            System.out.println("Thread " + getName() + ": Parametros da mensagem errados");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.state = stat;
        con.close();
        
        return inMessage.getnProductsStored();
    }

    /**
     * The store is at full capacity, the craftsman asks the entrepreneur to go get the batch that is ready.
     * 
     * @param id The craftsman identifier.
    */
    private void batchReadyForTransfer(int id) {
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.BATCH_READY_FOR_TRANSFER, id);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        CraftsmanState stat = inMessage.getCraftState();
        
        if (type != MessageType.ACK) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        if (stat == null) {
            System.out.println("Thread " + getName() + ": Parametros da mensagem errados");
            System.out.println("Message:"+ inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        this.state = stat;
        con.close();
    }

    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns 0 if the Craftsman cannot end its operation. 1 if the Craftsman can end its operation without doing anything else.
     * 2 if the Craftsman can end the operation but has to ask the Entrepreneur for a transfer of finished products.
     * 
     */
    private int endOperCraft() {
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.END_OPER_CRAFTSMAN);
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
        
        if (type == MessageType.POSITIVE) {
            if (inMessage.isRequestFetchProducts())
                return 2;
            return 1;
        } else {
            return 0;
        }
    }
}
