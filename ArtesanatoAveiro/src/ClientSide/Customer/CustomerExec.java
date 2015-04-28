/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide.Customer;

import Communication.ClientComm;
import Communication.CommConst;
import Communication.Message.Message;
import Communication.Message.MessageType;
import Communication.ProbConst;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author diogosilva
 */
public class CustomerExec {
    public static void main(String [] args) {
        ArrayList<Customer> customers = new ArrayList<>(ProbConst.nCustomers);
        for (int i = 0; i < ProbConst.nCustomers; i++)
            customers.add(new Customer(i));
        
        System.out.println("Number of customers: " + customers.size());
        
        for (Customer c : customers)
            c.start();
        
        for (Customer c : customers) { 
            try { 
                c.join ();
            } catch (InterruptedException e) {}
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
