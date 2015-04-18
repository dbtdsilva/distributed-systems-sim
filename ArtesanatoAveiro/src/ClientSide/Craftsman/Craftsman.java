package ClientSide.Craftsman;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import Exec.ProbConst;
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

        while (!con.open()) // aguarda ligação
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
        if (type != MessageType.POSITIVE || type != MessageType.NEGATIVE) {
            System.out.println("Thread " + getName() + ": Tipo inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();

        return type == MessageType.POSITIVE;
    }

    private void primeMaterialsNeeded(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void backToWork(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void prepareToProduce(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int goToStore(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void batchReadyForTransfer(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int endOperCraft() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
