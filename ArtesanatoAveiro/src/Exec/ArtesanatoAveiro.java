package Exec;

import ClientSide.Craftsman.Craftsman;
import ClientSide.Customer.Customer;
import ClientSide.Entrepreneur.Entrepreneur;
import Logger.Logging;
import ServerSide.Shop.Shop;
import ServerSide.Warehouse.Warehouse;
import ServerSide.Workshop.Workshop;
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
     * @param args the command line arguments, it accepts one argument if needed,
     * it will introduce the string on argument 0 to the filename of the logger.
     * @throws java.io.IOException is thrown when fails to write/open the file
     */
    public static void main(String[] args) throws IOException {
        String logname = "";
        if (args.length > 0)
            logname = "logging"+args[0]+".txt";
        
        Logging log = new Logging(logname, 
                ProbConst.nCustomers, 
                ProbConst.nCraftsmen,
                ProbConst.nPrimeMaterials);
        
        // This hook allows the simulation always close the logging file even 
        // when the simulation is forced to close.
        ShutdownHook shutdownHook = new ShutdownHook(log);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        
        // Used to debug on standard output.
        //log.setConsole();
        
        Shop shop = new Shop(log);
        Warehouse warehouse = new Warehouse(log);
        
        Workshop workshop = new Workshop(log,
                                shop);
        
        ArrayList<Customer> customers = new ArrayList<>(ProbConst.nCustomers);
        ArrayList<Craftsman> craftsmen = new ArrayList<>(ProbConst.nCraftsmen);

        Entrepreneur entr = new Entrepreneur(log, shop, warehouse, workshop);
        
        for (int i = 0; i < ProbConst.nCustomers; i++)
            customers.add(new Customer(i, log, shop));
        for (int i = 0; i < ProbConst.nCraftsmen; i++)
            craftsmen.add(new Craftsman(i, log, shop, workshop));
        
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
        
        if (!log.isConsist()) {
            System.out.println("Simulation values are not consistent.");
        }
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