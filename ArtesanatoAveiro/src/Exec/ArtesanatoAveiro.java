package Exec;

import Craftsman.Craftsman;
import Customer.Customer;
import Entrepreneur.Entrepreneur;
import Logger.Logging;
import Shop.Shop;
import Warehouse.Warehouse;
import Workshop.Workshop;
import java.util.ArrayList;

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
        Logging log = new Logging("logging.txt", 
                ProbConst.nCustomers, 
                ProbConst.nCraftsmen,
                ProbConst.nPrimeMaterials);
        log.setConsole();
        Shop shop = new Shop(log);
        Warehouse wh = new Warehouse(log, ProbConst.nPrimeMaterials);
        
        /* precisa do shop por causa do telefone */
        Workshop ws = new Workshop(log,
                                shop,
                                ProbConst.MAXproductsInWorkshop, 
                                ProbConst.minPM, 
                                ProbConst.primeMaterialsPerProduct);
        
        ArrayList<Customer> customers = new ArrayList<>(ProbConst.nCustomers);
        ArrayList<Craftsman> craftsmen = new ArrayList<>(ProbConst.nCraftsmen);

            /* 3 repositorios */
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
        
        DebugHelper hack = new DebugHelper();
        hack.start();
        
        ShutdownHook shutdownHook = new ShutdownHook(log);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        
        
        try {
            entr.join();
        } catch (InterruptedException e) {}
        System.out.println("A dona terminou.");
        for (Craftsman c : craftsmen) { 
            try { 
                c.join ();
            } catch (InterruptedException e) {}
            System.out.println("O artesão " + c.id + " terminou.");
        }
        for (Customer c : customers) { 
            try { 
                c.join ();
            } catch (InterruptedException e) {}
            System.out.println("O cliente " + c.id + " terminou.");
        }
        
        hack.interrupt();
    }
}

class DebugHelper extends Thread {
    @Override
    public void run() {
        while(true) {
            System.out.print("");
            unfreeze();
        }
    }
    
    private synchronized void unfreeze() {
        notifyAll();
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