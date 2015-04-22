package ClientSide.Craftsman;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import static Communication.Message.Message.ERROR_INT;
import Communication.Message.MessageType;
import Exec.ProbConst;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent the entity Craftsman
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Craftsman extends Thread {

    private CraftsmanState state;
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

        System.out.println("Artesão " + id + " acabou execução!");
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
