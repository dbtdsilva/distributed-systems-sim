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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */

public class ArtesanatoAveiro {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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
<<<<<<< HEAD
        try {
            entr.join();
        } catch (InterruptedException e) {}
=======
        } catch (IOException ex) {
            Logger.getLogger(ArtesanatoAveiro.class.getName()).log(Level.SEVERE, null, ex);
        }
>>>>>>> 06724bf1a30bcf8f467370134154b73cc11ad924
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