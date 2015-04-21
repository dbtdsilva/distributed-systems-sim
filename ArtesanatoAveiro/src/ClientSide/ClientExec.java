/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ClientSide;

import ClientSide.Craftsman.Craftsman;
import ClientSide.Customer.Customer;
import ClientSide.Entrepreneur.Entrepreneur;
import Exec.ProbConst;
import java.util.ArrayList;

/**
 *
 * @author diogosilva
 */
public class ClientExec {
    public static void main(String[] args) {
        ArrayList<Customer> customers = new ArrayList<>(ProbConst.nCustomers);
        ArrayList<Craftsman> craftsmen = new ArrayList<>(ProbConst.nCraftsmen);
        
        Entrepreneur entr = new Entrepreneur();
        for (int i = 0; i < ProbConst.nCustomers; i++)
            customers.add(new Customer(i));
        for (int i = 0; i < ProbConst.nCraftsmen; i++)
            craftsmen.add(new Craftsman(i));
        
        System.out.println("Número de clientes: " + customers.size());
        System.out.println("Número de artesões: " + craftsmen.size());
        
        entr.start();
        for (Customer c : customers)
            c.start();
        for (Craftsman c : craftsmen)
            c.start();

        for (Craftsman c : craftsmen) { 
            try { 
                c.join ();
            } catch (InterruptedException e) {}
        }
        for (Customer c : customers) { 
            try { 
                c.join ();
            } catch (InterruptedException e) {}
        }
        try {
            entr.join();
        } catch (InterruptedException e) {}
    }
}
