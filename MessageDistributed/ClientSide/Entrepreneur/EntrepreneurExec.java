package ClientSide.Entrepreneur;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import static java.lang.Thread.sleep;
import java.util.Arrays;

/**
 * This file defines the main method to run the Entrepreneur entity.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class EntrepreneurExec {
    public static void main(String[] args) {
        System.out.println("Entrepreneur started execution");
        Entrepreneur entr = new Entrepreneur();
        entr.start();
        try {
            entr.join();
        } catch (InterruptedException e) {
        }
        System.out.println("Sending TERMINATE message to the logging");

        Message inMessage, outMessage;
        ClientComm con = new ClientComm(CommConst.loggServerName, CommConst.loggServerPort);
        while (!con.open()) {
            try {
                sleep((long) (10));
            } catch (InterruptedException e) {
            }
        }
        outMessage = new Message(MessageType.TERMINATE);
        con.writeObject(outMessage);
        inMessage = (Message) con.readObject();
        if (inMessage.getType() != MessageType.ACK) {
            System.out.println("Tipo Inválido. Message:" + inMessage.toString());
            System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
            System.exit(1);
        }
        con.close();
    }
}
