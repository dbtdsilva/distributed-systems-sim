package ServerSide.Workshop;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import Exec.ProbConst;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The monitor that represents the Workshop.
 * 
 * @author Diogo Silva, 60337 
 * @author Tânia Alves, 60340
 */
public class Workshop {
    private int nProductsStored;
    private int nCurrentPrimeMaterials;
    private int nFinishedProducts;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private boolean waitingEntrepreneur;
    
    /**
     * Initializes the workshop class with the required information.
     */
    public Workshop() {
        this.nProductsStored = 0;
        this.nCurrentPrimeMaterials = 0;
        this.nFinishedProducts = 0;
        this.nTimesPrimeMaterialsFetched = 0;
        this.nTotalPrimeMaterialsSupplied = 0;
        this.waitingEntrepreneur = false;
    }
    
        /******************/
        /** ENTREPRENEUR **/
        /******************/
    
    /**
     * Entrepreneur goes to the Workshop to fetch the products in there.
     * 
     * @return 
     */
    public synchronized int goToWorkshop() {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
        int n = nProductsStored;
        nProductsStored = 0;
        
        Message inMessage, outMessage;
        ClientComm con = new ClientComm(CommConst.shopServerName, CommConst.shopServerPort);
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
        outMessage = new Message(MessageType.WRITE_WSHOP_ENTR_STATE, nCurrentPrimeMaterials, nProductsStored, nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, nFinishedProducts, EntrepreneurState.COLLECTING_A_BATCH_OF_PRODUCTS);
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
    }
    
    /**
     * Entrepreneur goes to the Workshop and returns that prime materials that
     * she fetched from the Warehouse.
     * If nobody is waiting for her, it means that Entrepreneur arrived before
     * the Craftsman to the Workshop and he will not need to wait.
     * 
     * @param nMaterials
     */
    public synchronized void replenishStock(int nMaterials) {
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.DELIVERING_PRIME_MATERIALS);
        nTimesPrimeMaterialsFetched++;
        nTotalPrimeMaterialsSupplied += nMaterials;
        nCurrentPrimeMaterials += nMaterials;
        
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
        con.close();
        
        notifyAll();
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
    public synchronized boolean collectingMaterials(int id) {
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
            return false;
        }
        
        nCurrentPrimeMaterials -= ProbConst.primeMaterialsPerProduct;
        
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
    }   
    
    /**
     * 
     * After the craftsman finishes the piece and stores it in the workshop.
     * @param id The craftsman identifier.
     * 
     * @return the number of products stored in workshop.
     */
    public synchronized int goToStore(int id) {
        nFinishedProducts++;
        nProductsStored++;
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message outMessage = new Message(MessageType.WRITE_WSHOP_CRAFT_STATE,
                nCurrentPrimeMaterials, nProductsStored, 
                nTimesPrimeMaterialsFetched, 
                nTotalPrimeMaterialsSupplied, nFinishedProducts, CraftsmanState.STORING_IT_FOR_TRANSFER, id, true);
        
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
    }
    
    /**
     * The craftsman has finished its latest task and is now ready to go fetch more prime materials.
     * 
     * @param id The craftsman identifier.
     */
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
        
    } 
    
    /**
     * The craftsman has the prime materials that he needs, and will now start producing another piece.
     * 
     * @param id The craftsman identifier.
     */
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
    } 
}
