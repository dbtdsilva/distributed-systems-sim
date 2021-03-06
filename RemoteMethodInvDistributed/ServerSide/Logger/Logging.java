package ServerSide.Logger;

import Interfaces.LoggingInterface;
import Interfaces.Register;
import Interfaces.ShopInterface;
import Interfaces.WarehouseInterface;
import Interfaces.WorkshopInterface;
import Structures.Constants.ProbConst;
import Structures.Constants.RegistryConfig;
import Structures.Enumerates.CraftsmanState;
import Structures.Enumerates.CustomerState;
import Structures.Enumerates.EntrepreneurState;
import Structures.Enumerates.ShopState;
import Structures.VectorClock.VectorTimestamp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for generate all the logging information.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Logging implements LoggingInterface {
    /* File where the log will be saved */

    private final File log, reorder;
    /* Name for the file where we want to save the log */
    private final String filename;

    private PrintWriter pw, reorder_pw;

    /* Auxiliar variables */
    // Entrepeneur information
    private EntrepreneurState entrepState;

    // Customer information
    private final Map<Integer, CustomerState> customers;
    private final Map<Integer, Integer> nBoughtGoods;

    // Craftsmen information
    private int nWorkingCraftsmen;
    private final Map<Integer, CraftsmanState> craftsmen;
    private final Map<Integer, Integer> nManufacturedProds;

    // Shop information
    private int nCustomerIn;
    private ShopState shopDoorState;
    private int nGoodsInDisplay;
    private boolean reqFetchProds;
    private boolean reqPrimeMaterials;

    // Workshop information
    private int nCurrentPrimeMaterials;
    private int nProductsStored;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private int nFinishedProducts;

    private final ArrayList<Update> updates;
    private int numberEntitiesRunning;
    /**
     * Initializes the logger file.
     *
     * @param loggerName Name to be given to the logger to be created. If the
     * string is null, it creates a pre-defined string with today's date
     * @param nCustomers Number of customers present in the simulation
     * @param nCraftsmen Number of craftsmen present in the simulation
     * @param primeMaterials Number of prime materials present in the simulation
     * @throws java.io.IOException is thrown when fails to write/open the file
     */
    public Logging(String loggerName,
            int nCustomers,
            int nCraftsmen,
            int primeMaterials) throws IOException {
        numberEntitiesRunning = ProbConst.nCustomers + ProbConst.nCraftsmen + 1;
        updates = new ArrayList<>();
        nWorkingCraftsmen = nCraftsmen;
        nCustomerIn = 0;
        nGoodsInDisplay = 0;
        reqFetchProds = false;
        reqPrimeMaterials = false;

        nCurrentPrimeMaterials = 0;
        nProductsStored = 0;
        nTimesPrimeMaterialsFetched = 0;
        nTotalPrimeMaterialsSupplied = 0;
        nFinishedProducts = 0;

        craftsmen = new HashMap<>();
        customers = new HashMap<>();
        nBoughtGoods = new HashMap<>();
        nManufacturedProds = new HashMap<>();

        if (loggerName.length() == 0) {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
            this.filename = "Artesanato_" + date.format(today) + ".log";
        } else {
            this.filename = loggerName;
        }

        String [] arr = filename.split("_");
        reorder = new File(arr[0]+"_vector_"+arr[1]);
        log = new File(filename);
        shopDoorState = ShopState.CLOSED;
        entrepState = EntrepreneurState.OPENING_THE_SHOP;
        for (int i = 0; i < nCraftsmen; i++) {
            nManufacturedProds.put(i, 0);
            craftsmen.put(i, CraftsmanState.FETCHING_PRIME_MATERIALS);
        }
        for (int i = 0; i < nCustomers; i++) {
            customers.put(i, CustomerState.CARRYING_OUT_DAILY_CHORES);
            nBoughtGoods.put(i, 0);
        }

        InitWriting();
    }

    @Override
    public synchronized void Shutdown() {
        numberEntitiesRunning--;
        if (numberEntitiesRunning > 0)
            return;
        
        Register reg = null;
        Registry registry = null;
        
        RegistryConfig rc = new RegistryConfig("../../config.ini");
        String rmiRegHostName = rc.registryHost();
        int rmiRegPortNumb = rc.registryPort();
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }

        String nameEntryBase = RegistryConfig.registerHandler;
        String nameEntryObject = RegistryConfig.logNameEntry;

        try
        {
            ShopInterface shopInt = (ShopInterface) registry.lookup (RegistryConfig.shopNameEntry);
            shopInt.signalShutdown();
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating shop: " + e.getMessage () + "!");
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Shop is not registered: " + e.getMessage () + "!");
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        
        try
        {
            WorkshopInterface wsInt = (WorkshopInterface) registry.lookup (RegistryConfig.workshopNameEntry);
            wsInt.signalShutdown();
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating workshop: " + e.getMessage () + "!");
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Shop is not registered: " + e.getMessage () + "!");
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        
        try
        {
            WarehouseInterface whInt = (WarehouseInterface) registry.lookup (RegistryConfig.warehouseNameEntry);
            whInt.signalShutdown();
        }
        catch (RemoteException e)
        { 
            System.out.println("Exception thrown while locating warehouse: " + e.getMessage () + "!");
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (NotBoundException e)
        { 
            System.out.println("Warehouse is not registered: " + e.getMessage () + "!");
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        
        
        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }
        try {
            // Unregister ourself
            reg.unbind(nameEntryObject);
        } catch (RemoteException e) {
            System.out.println("Logging registration exception: " + e.getMessage());
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        } catch (NotBoundException e) {
            System.out.println("Logging not bound exception: " + e.getMessage());
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, e);
        }

        try {
            // Unexport; this will also remove us from the RMI runtime
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Logging closed.");
        EndWriting();
    }

    /**
     * Adds a line to the logger with the simulation information updated.
     *
     * @param vt Vector clock
     */
    @Override
    public synchronized void WriteLine(VectorTimestamp vt) {
        String toWrite = "";
        toWrite += String.format("  %4s   ", entrepState.getAcronym());
        for (int i = 0; i < customers.size(); i++) {
            toWrite += String.format("%4s %2d ", customers.get(i).getAcronym(), nBoughtGoods.get(i));
        }

        toWrite += String.format(" ");
        for (int i = 0; i < craftsmen.size(); i++) {
            toWrite += String.format("%4s %2d ", craftsmen.get(i).getAcronym(), nManufacturedProds.get(i));
        }

        char r;
        if (reqFetchProds) {
            r = 'T';
        } else {
            r = 'F';
        }

        char t;
        if (reqPrimeMaterials) {
            t = 'T';
        } else {
            t = 'F';
        }

        toWrite += String.format("  %4s  %2d  %2d  %1c   %1c     ", shopDoorState.getAcronym(),
                nCustomerIn, nGoodsInDisplay, r, t);
        toWrite += String.format("%2d  %2d  %2d   %2d   %2d", nCurrentPrimeMaterials,
                nProductsStored, nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied,
                nFinishedProducts);
        int[] arrayClocks = vt.toIntArray();
        for (int i = 0; i < ProbConst.nCraftsmen + ProbConst.nCustomers + 1; i++) {
            toWrite += String.format(" %2d", arrayClocks[i]);
        }
        toWrite += String.format("\n");
        pw.printf(toWrite);
        pw.flush();

        Update upd = new Update(toWrite, vt.toIntArray());
        updates.add(upd);
    }

    /**
     * Writes the first lines of the header in the logger file created before.
     */
    private void InitWriting() {
        try {
            reorder_pw = new PrintWriter(reorder);
            pw = new PrintWriter(log);

            StringBuilder sb = new StringBuilder("ENTREPRE");
            StringBuilder sb2 = new StringBuilder("  Stat  ");

            for (int i = 0; i < customers.size(); i++) {
                sb.append("  CUST_").append(i);
                sb2.append(" Stat BP");
            }

            sb.append("  ");
            sb2.append("  ");

            for (int i = 0; i < craftsmen.size(); i++) {
                sb.append("CRAFT_").append(i).append(" ");
                sb2.append("Stat PP ");
            }

            sb.append("          SHOP                 WORKSHOP       ");
            sb2.append("  Stat NCI NPI PCR PMR  APMI NPI NSPM TAPM TNP");
            sb.append("           V");
            for (int i = 0; i < ProbConst.nCraftsmen + ProbConst.nCustomers + 1; i++) {
                sb2.append("  ").append(i);
            }
            pw.println(sb.toString());
            pw.println(sb2.toString());
            reorder_pw.println(sb.toString());
            reorder_pw.println(sb2.toString());
            WriteLine(new VectorTimestamp(ProbConst.nCustomers + ProbConst.nCraftsmen + 1, 0));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }
        pw.flush();
    }

    /**
     * Writes the end of the logger file.
     */
    @Override
    public synchronized void EndWriting() {
        Map<Integer, Update> tab = new Hashtable<>();
        for (int i = 0; i < this.updates.size(); i++) {
            tab.put(i, updates.get(i));
        }
        ArrayList<Map.Entry<Integer, Update>> l = new ArrayList(tab.entrySet());
        Collections.sort(l, new Comparator<Map.Entry<Integer, Update>>()
        {
            @Override
            public int compare(Map.Entry<Integer, Update> o1, Map.Entry<Integer, Update> o2) 
            {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        
        for (int i = 0; i < l.size(); i++) {
            reorder_pw.printf(l.get(l.size()-i-1).getValue().getText());
        }
        reorder_pw.println("SIMULATION ENDED!");
        reorder_pw.flush();
        reorder_pw.close();
        
        
        pw.println("SIMULATION ENDED!");
        pw.flush();
        pw.close();
    }

    /**
     * Writes the state of the entrepeneur in the logger file.
     *
     * @param es The entrepeneur's current state
     * @param vt The vector clock
     */
    @Override
    public synchronized void UpdateEntreperneurState(EntrepreneurState es, VectorTimestamp vt) {
        this.entrepState = es;
        WriteLine(vt);
    }

    /**
     * Writes the state of the craftsman in the logger file.
     *
     * @param id The craftsman's id
     * @param cs The craftsman's current state
     * @param vt The vector clock
     */
    @Override
    public synchronized void UpdateCraftsmanState(int id, CraftsmanState cs, VectorTimestamp vt) {
        craftsmen.put(id, cs);
        WriteLine(vt);
    }

    /**
     * Writes the state of the customer in the logger file.
     *
     * @param id The customer's id
     * @param cs The customer's current state
     * @param clk The vector clock
     */
    @Override
    public synchronized void UpdateCustomerState(int id, CustomerState cs, VectorTimestamp clk) {
        customers.put(id, cs);
        WriteLine(clk);
    }

    /**
     * Update prime materials request. (Doesn't write)
     *
     * @param reqPrimeMaterials The current request status
     */
    @Override
    public synchronized void UpdatePrimeMaterialsRequest(boolean reqPrimeMaterials) {
        this.reqPrimeMaterials = reqPrimeMaterials;
    }

    /**
     * Update prime materials request. (Doesn't write)
     *
     * @param reqFetchProds The current request status
     */
    @Override
    public synchronized void UpdateFetchProductsRequest(boolean reqFetchProds) {
        this.reqFetchProds = reqFetchProds;
    }

    /**
     * Writes the state of the shop in the logger file.
     *
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the
     * transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the
     * supply of prime materials
     * @param vt The vector clock
     *
     */
    @Override
    public synchronized void WriteShop(ShopState s, int nCustomerIn, int nGoodsInDisplay,
            boolean reqFetchProds, boolean reqPrimeMaterials, VectorTimestamp vt) {
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;

        WriteLine(vt);
    }

    /**
     * Writes the state of the shop in the logger file.
     *
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the
     * transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the
     * supply of prime materials
     * @param state The entrepreneur state
     * @param vt The vector clock
     */
    @Override
    public synchronized void WriteShopAndEntrepreneurStat(ShopState s, int nCustomerIn,
            int nGoodsInDisplay, boolean reqFetchProds,
            boolean reqPrimeMaterials, EntrepreneurState state, VectorTimestamp vt) {
        this.entrepState = state;
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;

        WriteLine(vt);
    }

    /**
     * Writes the state of the shop in the logger file.
     *
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the
     * transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the
     * supply of prime materials
     * @param state The craftsman state
     * @param idCraft The craftsman identifier
     * @param vt The vector clock
     */
    @Override
    public synchronized void WriteShopAndCraftsmanStat(ShopState s, int nCustomerIn,
            int nGoodsInDisplay, boolean reqFetchProds,
            boolean reqPrimeMaterials, CraftsmanState state,
            int idCraft, VectorTimestamp vt) {
        craftsmen.put(idCraft, state);
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;

        WriteLine(vt);
    }

    /**
     * Writes the state of the shop in the logger file.
     *
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the
     * transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the
     * supply of prime materials
     * @param state The customer state
     * @param idCust The customer identifier
     * @param nBoughtGoods The number of products bought
     * @param clk The vector clock
     *
     */
    @Override
    public synchronized void WriteShopAndCustomerStat(ShopState s, int nCustomerIn,
            int nGoodsInDisplay, boolean reqFetchProds,
            boolean reqPrimeMaterials, CustomerState state,
            int idCust, int nBoughtGoods, VectorTimestamp clk) {
        if (nBoughtGoods > 0) {
            int prods = this.nBoughtGoods.get(idCust);
            prods += nBoughtGoods;
            this.nBoughtGoods.put(idCust, prods);
        }
        customers.put(idCust, state);
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;

        WriteLine(clk);
    }

    /**
     * Writes the state of the workshop in the logger file.
     *
     * @param nCurrentPrimeMaterials Amount of prime materials present in the
     * workshop
     * @param nProductsStored Number of finished products present at the
     * workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime
     * materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that
     * have already been supplied
     * @param nFinishedProducts Total number of products that have already been
     * manufactured
     * @param vt The vector clock
     *
     */
    @Override
    public synchronized void WriteWorkshop(int nCurrentPrimeMaterials, int nProductsStored,
            int nTimesPrimeMaterialsFetched,
            int nTotalPrimeMaterialsSupplied, int nFinishedProducts, VectorTimestamp vt) {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        WriteLine(vt);
    }

    /**
     * Writes the state of the workshop in the logger file.
     *
     * @param nCurrentPrimeMaterials Amount of prime materials present in the
     * workshop
     * @param nProductsStored Number of finished products present at the
     * workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime
     * materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that
     * have already been supplied
     * @param nFinishedProducts Total number of products that have already been
     * manufactured
     * @param state The craftsman state
     * @param idCraft The craftsman identifier
     * @param finishedProduct This field indicates if he did finish a product or
     * not
     * @param vt The vector clock
     *
     */
    @Override
    public synchronized void WriteWorkshopAndCraftsmanStat(int nCurrentPrimeMaterials,
            int nProductsStored, int nTimesPrimeMaterialsFetched,
            int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
            CraftsmanState state, int idCraft, boolean finishedProduct, VectorTimestamp vt) {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;

        if (finishedProduct) {
            int prods = nManufacturedProds.get(idCraft);
            prods++;
            nManufacturedProds.put(idCraft, prods);
        }

        WriteLine(vt);
    }

    /**
     * Writes the state of the workshop in the logger file.
     *
     * @param nCurrentPrimeMaterials Amount of prime materials present in the
     * workshop
     * @param nProductsStored Number of finished products present at the
     * workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime
     * materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that
     * have already been supplied
     * @param nFinishedProducts Total number of products that have already been
     * manufactured
     * @param state The entrepreneur state
     * @param vt The vector clock
     *
     */
    @Override
    public synchronized void WriteWorkshopAndEntrepreneurStat(int nCurrentPrimeMaterials,
            int nProductsStored, int nTimesPrimeMaterialsFetched,
            int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
            EntrepreneurState state, VectorTimestamp vt) {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;

        this.entrepState = state;

        WriteLine(vt);
    }

    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     *
     * @return Returns false if the craftsman can continue its work; returns
     * false if otherwise.
     */
    @Override
    public synchronized int endOperCraft() {
        if (nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies) {
            if (nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct * nWorkingCraftsmen) {
                nWorkingCraftsmen--;
                if (nWorkingCraftsmen == 0 && nProductsStored != 0 && !reqFetchProds) {
                    return 2;
                }
                return 1;
            }
        }
        return 0;
    }

    /**
     * Checks if the customer no longer has conditions to continue.
     *
     * @return Returns false if the customer can continue; returns false if
     * otherwise.
     */
    @Override
    public synchronized boolean endOpCustomer() {
        int totalProductsBought = 0;
        for (int val : nBoughtGoods.values()) {
            totalProductsBought += val;
        }
        return nGoodsInDisplay == 0
                && nProductsStored == 0
                && nFinishedProducts == totalProductsBought
                && nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct
                && nTotalPrimeMaterialsSupplied - nCurrentPrimeMaterials
                == totalProductsBought * ProbConst.primeMaterialsPerProduct
                && nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies
                && !reqFetchProds && !reqPrimeMaterials;
    }

    /**
     * Checks if the Entrepreneur no longer has conditions to continue its work.
     *
     * @return Returns false if the entrepreneur can continue its work; returns
     * false if otherwise.
     */
    @Override
    public synchronized boolean endOpEntrep() {
        int totalProductsBought = 0;
        for (int val : nBoughtGoods.values()) {
            totalProductsBought += val;
        }
        return nGoodsInDisplay == 0
                && nProductsStored == 0
                && nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct
                && nTotalPrimeMaterialsSupplied - nCurrentPrimeMaterials
                == totalProductsBought * ProbConst.primeMaterialsPerProduct
                && nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies
                && !reqFetchProds && !reqPrimeMaterials
                && nCustomerIn == 0;
    }

    /**
     * This method allows to check if the final status of the simulation is a
     * valid or not.
     *
     * @return returns true if simulation values are consistent; returns false
     * otherwise.
     */
    @Override
    public synchronized boolean isConsist() {
        /* Calculating some values to check consistency */
        int totalProductsBought = 0;
        for (int val : nBoughtGoods.values()) {
            totalProductsBought += val;
        }
        int nManufacturedProducts = 0;
        for (int val : nManufacturedProds.values()) {
            nManufacturedProducts += val;
        }

        /* Check values consistency */
        return nFinishedProducts == totalProductsBought
                && nManufacturedProducts == totalProductsBought
                && nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct
                && nTotalPrimeMaterialsSupplied - nCurrentPrimeMaterials
                == totalProductsBought * ProbConst.primeMaterialsPerProduct
                && nCustomerIn == 0
                && !reqFetchProds
                && !reqPrimeMaterials
                && nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies
                && nGoodsInDisplay == 0
                && nProductsStored == 0
                && nWorkingCraftsmen == 0;
    }
}
