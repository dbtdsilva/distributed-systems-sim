package Exec;

import Craftsman.Craftsman;
import Customer.Customer;
import Entrepreneur.Entrepreneur;
import Logger.Logging;
import Shop.Shop;
import Warehouse.Warehouse;
import Workshop.Workshop;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This is the class where main function is located, it is reponsible for
 * launching all the threads
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public class ArtesanatoAveiro {

    /**
     * This is the main function that's going to be called to simulate 
     * "Artesenato de Aveiro"
     * 
     * @param args the command line arguments
     * @throws java.io.IOException is thrown when fails to write/open the file
     */
    public static void main(String[] args) throws IOException {
        String logname = "";
        if (args.length > 0)
            logname = "logs/logging"+args[0]+".txt";
        Logging log = new Logging(logname, 
                ProbConst.nCustomers, 
                ProbConst.nCraftsmen,
                ProbConst.nPrimeMaterials);
        ShutdownHook shutdownHook = new ShutdownHook(log);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        //log.setConsole();
        Shop shop = new Shop(log);
        Warehouse wh = new Warehouse(log, ProbConst.nPrimeMaterials);
        
        Workshop ws = new Workshop(log,
                                shop,
                                ProbConst.MAXproductsInWorkshop, 
                                ProbConst.minPM, 
                                ProbConst.primeMaterialsPerProduct);
        
        ArrayList<Customer> customers = new ArrayList<>(ProbConst.nCustomers);
        ArrayList<Craftsman> craftsmen = new ArrayList<>(ProbConst.nCraftsmen);

        Entrepreneur entr = new Entrepreneur(shop, wh, ws);
        
        for (int i = 0; i < ProbConst.nCustomers; i++)
            customers.add(new Customer(i, shop));
        for (int i = 0; i < ProbConst.nCraftsmen; i++)
            craftsmen.add(new Craftsman(i, shop, ws, wh));
        
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

class ShutdownHook extends Thread {
    Logging log;
    ShutdownHook(Logging log) {
        this.log = log;
    }
    @Override
    public void run() {
        log.EndWriting();
    }
}