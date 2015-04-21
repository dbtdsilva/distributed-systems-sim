package ServerSide.Warehouse;

import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import Exec.ProbConst;
import static java.lang.Thread.sleep;

/**
 * The monitor that represents the Warehouse.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Warehouse {    
    private int nTimesSupplied;
    private final int nTimesPMSupplied[];
    
    /**
     * Initializes the warehouse class with the required information.
     */
    public Warehouse() {
        this.nTimesSupplied = 0;
        
        int nPMMin = ProbConst.nCraftsmen * ProbConst.primeMaterialsPerProduct;
        
        nTimesPMSupplied = new int[ProbConst.MAXSupplies];
        for (int i = 0; i < nTimesPMSupplied.length; i++) {
            nTimesPMSupplied[i] = (int) (Math.random() * nPMMin * 3 + 1);
        }
        
        if (nTimesPMSupplied[nTimesPMSupplied.length-1] < nPMMin)
            nTimesPMSupplied[nTimesPMSupplied.length-1] += nPMMin;
    }
    
    /******************/
    /** ENTREPRENEUR **/
    /******************/

    /**
     * The entrepreneur visits the supplies to fetch prime materials for the
     * craftsman.
     * She will fetch a random value of prime materials.
     * 
     * @return the number of prime materials fetched
     */
    public synchronized int visitSuppliers() {
        int n = nTimesPMSupplied[nTimesSupplied];
        nTimesSupplied++;
        
        //((Entrepreneur) Thread.currentThread()).setState(EntrepreneurState.AT_THE_SUPPLIERS);
        //log.UpdateEntreperneurState(((Entrepreneur) Thread.currentThread()).getCurrentState());
        
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        Message inMessage, outMessage;

        while (!con.open())
        {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.WRITE_ENTR_STATE, 
                EntrepreneurState.AT_THE_SUPPLIERS);
        con.writeObject(outMessage);
        
        inMessage = (Message) con.readObject();
        MessageType type = inMessage.getType();
        
        if (type != MessageType.ACK) {
            System.out.println("Tipo de mensagem inválido!");
            System.out.println(inMessage.toString());
            System.exit(1);
        }
        con.close();
        
        return n;
    }
}
