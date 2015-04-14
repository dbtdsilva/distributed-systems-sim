package ServerSide.Logger;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Exec.ProbConst;
import ServerSide.Shop.ShopState;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for generate all the logging information.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Logging {
    /* File where the log will be saved */
    private final File log;
    /* Name for the file where we want to save the log */
    private final String filename;
    
    private PrintWriter pw;
    
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
    
    private boolean console;
    
    /**
     * Initializes the logger file.
     * 
     * @param loggerName Name to be given to the logger to be created. 
     * If the string is null, it creates a pre-defined string with today's date
     * @param nCustomers Number of customers present in the simulation
     * @param nCraftsmen Number of craftsmen present in the simulation
     * @param primeMaterials Number of prime materials present in the simulation
     * @throws java.io.IOException is thrown when fails to write/open the file
     */
    public Logging(String loggerName, 
            int nCustomers, 
            int nCraftsmen,
            int primeMaterials) throws IOException
    {
        console = false;
                      
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
        
        if(loggerName.length() == 0)
        {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyyMMddhhmmss");
            this.filename = "Artesanato_" + date.format(today) + ".log";
        }
        else
            this.filename = loggerName;
        
        log = new File(filename);
        shopDoorState = ShopState.CLOSED;
        entrepState = EntrepreneurState.OPENING_THE_SHOP;
        for(int i = 0; i < nCraftsmen; i++) {
            nManufacturedProds.put(i, 0);
            craftsmen.put(i, CraftsmanState.FETCHING_PRIME_MATERIALS);
        }
        for(int i = 0; i < nCustomers; i++) {
            customers.put(i, CustomerState.CARRYING_OUT_DAILY_CHORES);
            nBoughtGoods.put(i, 0);
        }
        
        InitWriting();
    }
    
    /**
     * Adds a line to the logger with the simulation information updated.
     */
    public synchronized void WriteLine()
    {        
        pw.printf("  %4s   ", entrepState.getAcronym());
        if (console)
            System.out.printf("  %4s   ", entrepState.getAcronym());
        
        for(int i = 0; i < customers.size(); i++) {
            pw.printf("%4s %2d ", customers.get(i).getAcronym(), nBoughtGoods.get(i));
        
            if (console)
                System.out.printf("%4s %2d ", customers.get(i).getAcronym(), nBoughtGoods.get(i));
        }
            
        pw.printf(" ");
        if (console)
            System.out.printf(" ");
        
        for(int i = 0; i < craftsmen.size(); i++) {
            pw.printf("%4s %2d ", craftsmen.get(i).getAcronym(), nManufacturedProds.get(i));
            
            if (console)
                System.out.printf("%4s %2d ", craftsmen.get(i).getAcronym(), nManufacturedProds.get(i));
        }
            
        char r;
        if(reqFetchProds)
            r = 'T';
        else
            r = 'F';
        
        char t;
        if(reqPrimeMaterials)
            t = 'T';
        else
            t = 'F';
        
        pw.printf("  %4s  %2d  %2d  %1c   %1c     ", shopDoorState.getAcronym(), 
                nCustomerIn, nGoodsInDisplay, r, t);
        pw.printf("%2d  %2d  %2d   %2d   %2d", nCurrentPrimeMaterials, 
                nProductsStored, nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                nFinishedProducts);
        pw.println(" | "+Thread.currentThread().getName());
        
        
        if (console) {
            System.out.printf("  %4s  %2d  %2d  %1c   %1c     ", shopDoorState.getAcronym(), 
                nCustomerIn, nGoodsInDisplay, r, t);
            System.out.printf("%2d  %2d  %2d   %2d   %2d", nCurrentPrimeMaterials, 
                nProductsStored, nTimesPrimeMaterialsFetched, nTotalPrimeMaterialsSupplied, 
                nFinishedProducts);
            System.out.println(" | "+Thread.currentThread().getName());
        }
    }
    
    /**
     * Writes the first lines of the header in the logger file created before.
     */
    private void InitWriting()
    {
        try {
            pw = new PrintWriter(log);
            
            StringBuilder sb = new StringBuilder("ENTREPRE");
            StringBuilder sb2 = new StringBuilder("  Stat  ");
            
            for(int i = 0; i < customers.size(); i++)
            {
                sb.append("  CUST_").append(i);
                sb2.append(" Stat BP");
            }
            
            sb.append("  ");
            sb2.append("  ");
            
            for(int i = 0; i < craftsmen.size(); i++)
            {
                sb.append("CRAFT_").append(i).append(" ");
                sb2.append("Stat PP ");
            }
            
            sb.append ("          SHOP                 WORKSHOP       ");
            sb2.append("  Stat NCI NPI PCR PMR  APMI NPI NSPM TAPM TNP");
            
            pw.println(sb.toString());
            pw.println(sb2.toString());
            if (console) {
                System.out.println(sb);
                System.out.println(sb2);
            }
            WriteLine();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Logging.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Writes the end of the logger file.
     */
    public synchronized void EndWriting()
    {
        pw.println("SIMULATION ENDED!");
        pw.flush();
        pw.close();
        if (console) {
            System.out.println("SIMULATION ENDED!");
        }
    }
    
    /**
     * Writes the state of the entrepeneur in the logger file.
     * 
     * @param es The entrepeneur's current state
    */
    public synchronized void UpdateEntreperneurState(EntrepreneurState es)
    {
        this.entrepState = es;
        WriteLine();
    }
        
    /**
     * Writes the state of the craftsman in the logger file.
     * 
     * @param id The craftsman's id
     * @param cs The craftsman's current state
     */
    public synchronized void UpdateCraftsmanState(int id, CraftsmanState cs)
    {
        craftsmen.put(id, cs);
        WriteLine();
    }
    
    /**
     * Writes the state of the customer in the logger file.
     * 
     * @param id The customer's id
     * @param cs The customer's current state
     */
    public synchronized void UpdateCustomerState(int id, CustomerState cs)
    {
        customers.put(id, cs);
        WriteLine();
    }
    
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqPrimeMaterials The current request status
     */
    public synchronized void UpdatePrimeMaterialsRequest(boolean reqPrimeMaterials) {
        this.reqPrimeMaterials = reqPrimeMaterials;
    }
    /**
     * Update prime materials request. (Doesn't write)
     * 
     * @param reqFetchProds The current request status
     */
    public synchronized void UpdateFetchProductsRequest(boolean reqFetchProds) {
        this.reqFetchProds = reqFetchProds;
    }
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * 
     */
    public synchronized void WriteShop(ShopState s, int nCustomerIn, int nGoodsInDisplay,
                                    boolean reqFetchProds, boolean reqPrimeMaterials)
    {
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;
        
        WriteLine();
    }
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The entrepreneur state
     */
    public synchronized void WriteShopAndEntrepreneurStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, EntrepreneurState state) {
        this.entrepState = state;
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;
        
        WriteLine();
    }
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The craftsman state
     * @param idCraft The craftsman identifier
     */
    public synchronized void WriteShopAndCraftsmanStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CraftsmanState state,
                                int idCraft) {
        craftsmen.put(idCraft, state);
        this.shopDoorState = s;
        this.nCustomerIn = nCustomerIn;
        this.nGoodsInDisplay = nGoodsInDisplay;
        this.reqFetchProds = reqFetchProds;
        this.reqPrimeMaterials = reqPrimeMaterials;
        
        WriteLine();
    }
    /**
     * Writes the state of the shop in the logger file.
     * 
     * @param s The shop's current state
     * @param nCustomerIn Number of customer's in the shop
     * @param nGoodsInDisplay Number of products in display at the shop
     * @param reqFetchProds A phone call was made to the shop requesting the transfer of finished products
     * @param reqPrimeMaterials A phone call was made to the shop requesting the supply of prime materials
     * @param state The customer state
     * @param idCust The customer identifier
     * @param nBoughtGoods The number of products bought
     * 
     */
    public synchronized void WriteShopAndCustomerStat(ShopState s, int nCustomerIn, 
                                int nGoodsInDisplay, boolean reqFetchProds, 
                                boolean reqPrimeMaterials, CustomerState state,
                                int idCust, int nBoughtGoods) {
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
        
        WriteLine();
    }
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * 
     */
    public synchronized void WriteWorkshop(int nCurrentPrimeMaterials, int nProductsStored, 
                                        int nTimesPrimeMaterialsFetched,
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts)
    {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        WriteLine();
    }
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * @param state The craftsman state
     * @param idCraft The craftsman identifier
     * @param finishedProduct This field indicates if he did finish a product or not
     * 
     */
    public synchronized void WriteWorkshopAndCraftsmanStat(int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    CraftsmanState state, int idCraft, boolean finishedProduct) {
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
        
        WriteLine();
    }
    /**
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * @param state The entrepreneur state
     * 
     */
    public synchronized void WriteWorkshopAndEntrepreneurStat(int nCurrentPrimeMaterials, 
                    int nProductsStored, int nTimesPrimeMaterialsFetched,
                    int nTotalPrimeMaterialsSupplied, int nFinishedProducts,
                    EntrepreneurState state) {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        
        this.entrepState = state;
        
        WriteLine();
    }
    
    /**
     * Activates the logger to the standard output.
     */
    public void setConsole() {
        console = true;
    }
    /**
     * Checks if the craftsman no longer has conditions to continue its work.
     * 
     * @return Returns false if the craftsman can continue its work; returns 
     * false if otherwise.
     */
    public synchronized int endOperCraft() {
        if (nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies) {                
            if (nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct * nWorkingCraftsmen) {
                nWorkingCraftsmen--;
                if (nWorkingCraftsmen == 0 && nProductsStored != 0 && !reqFetchProds)
                    return 2;
                return 1;
            }
        }
        return 0;
    }
    /**
     * Checks if the customer no longer has conditions to continue.
     * 
     * @return Returns false if the customer can continue; returns 
     * false if otherwise.
     */
    public synchronized boolean endOpCustomer() {
        int totalProductsBought = 0;
        for (int val : nBoughtGoods.values())
            totalProductsBought += val;
        return nGoodsInDisplay == 0 &&
                nProductsStored == 0 &&
                nFinishedProducts == totalProductsBought &&
                nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct &&
                nTotalPrimeMaterialsSupplied - nCurrentPrimeMaterials == 
                        totalProductsBought * ProbConst.primeMaterialsPerProduct &&
                nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies &&
                !reqFetchProds && !reqPrimeMaterials;
    }
    /**
     * Checks if the Entrepreneur no longer has conditions to continue its work.
     * 
     * @return Returns false if the entrepreneur can continue its work; returns 
     * false if otherwise.
     */
    public synchronized boolean endOpEntrep() {
        int totalProductsBought = 0;
        for (int val : nBoughtGoods.values())
            totalProductsBought += val;
        return nGoodsInDisplay == 0 &&
                nProductsStored == 0 &&
                nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct &&
                nTotalPrimeMaterialsSupplied - nCurrentPrimeMaterials == 
                        totalProductsBought * ProbConst.primeMaterialsPerProduct &&
                nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies &&
                !reqFetchProds && !reqPrimeMaterials && 
                nCustomerIn == 0;
    }
    /**
     * This method allows to check if the final status of the simulation is a 
     * valid or not.
     * 
     * @return returns true if simulation values are consistent; returns false
     * otherwise.
     */
        public synchronized boolean isConsist() {
        /* Calculating some values to check consistency */
        int totalProductsBought = 0;
        for (int val : nBoughtGoods.values())
            totalProductsBought += val;
        int nManufacturedProducts = 0;
        for (int val : nManufacturedProds.values())
            nManufacturedProducts += val;
        
        /* Check values consistency */
        return nFinishedProducts == totalProductsBought &&
                nManufacturedProducts == totalProductsBought &&
                nCurrentPrimeMaterials < ProbConst.primeMaterialsPerProduct &&
                nTotalPrimeMaterialsSupplied - nCurrentPrimeMaterials == 
                        totalProductsBought * ProbConst.primeMaterialsPerProduct &&
                nCustomerIn == 0 &&
                !reqFetchProds && 
                !reqPrimeMaterials && 
                nTimesPrimeMaterialsFetched == ProbConst.MAXSupplies &&
                nGoodsInDisplay == 0 &&
                nProductsStored == 0 &&
                nWorkingCraftsmen == 0;
    }
}
