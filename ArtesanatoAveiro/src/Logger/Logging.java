package Logger;

import Craftsman.*;
import Customer.*;
import Entrepreneur.*;
import Shop.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class Logging {
    /* File where the log will be saved */
    private File log;
    /* Name for the file where we want to save the log */
    private String filename;
    
    /* Auxiliar variables */
    //Entrepeneur information
    private EntrepreneurState entrepState;
    
    //Customer information
    private CustomerState customerState;
    private int nBoughtGoods;
    
    //Craftsmen information
    private CraftsmanState craftmState;
    private int nManufacturedProds;
    
    //Shop information
    private int nCustomerIn;
    private ShopDoorState shopDoorState;
    private int nGoodsInDisplay;
    private boolean reqFetchProds;
    private boolean reqPrimeMaterials;
    
    //Workshop information
    private int nCurrentPrimeMaterials;
    private int nProductsStored;
    private int nTimesPrimeMaterialsFetched;
    private int nTotalPrimeMaterialsSupplied;
    private int nFinishedProducts;
    

    /**
     * Initializes the logger file.
     * 
     * @param loggerName Name to be given to the logger to be created. If the string is null, it creates a pre-defined string with today's date
     * @param nCustomers Number of customers present in the simulation
     * @param nCraftsmen Number of crafstmen present in the simulation
     */
    public Logging(String loggerName, int nCustomers, int nCraftsmen)
    {
        if(loggerName.length() == 0)
        {
            Date today = Calendar.getInstance().getTime();
            SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd_hh:mm");
            this.filename = "Artesanato_" + date.format(today) + ".log";
        }
        else
            this.filename = loggerName;
        
        log = new File(filename);
    }
    
    /**
     * Adds a line to the logger with the simulation information updated.
     */
    public synchronized void WriteLine()
    {
        
    }
    
    /**
     * Writes the first lines of the header in the logger file created before.
     */
    public void InitWriting()
    {
        
    }
    
    /**
     * Writes the end of the logger file.
     */
    public void EndWriting()
    {
        
    }
    
    /**
     * Writes the state of the entrepeneur in the logger file.
     * 
     * @param es The entrepeneur's current state
    */
    public synchronized void WriteEntreperneur(EntrepreneurState es)
    {
        this.entrepState = es;
        WriteLine();
    }
    
    /**
     * Writes the state of the craftsman in the logger file.
     * 
     * @param cs The craftsman's current state
     * @param nManufacturedProds Craftsman's total number of manufactured products
     */
    public synchronized void WriteCraftsman(CraftsmanState cs, int nManufacturedProds)
    {
        this.craftmState = cs;
        this.nManufacturedProds = nManufacturedProds;
        WriteLine();
    }
    
    /**
     * Writes the state of the craftsman in the logger file.
     * 
     * @param cs The customer's current state
     * @param nBoughtGoods Number of shop bought goods
     */
    public synchronized void WriteCustomer(CustomerState cs, int nBoughtGoods)
    {
        this.customerState = cs;
        this.nBoughtGoods = nBoughtGoods;
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
     * 
     */
    public synchronized void WriteShop(ShopDoorState s, int nCustomerIn, int nGoodsInDisplay,
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
     * Writes the state of the workshop in the logger file.
     * 
     * @param nCurrentPrimeMaterials Amount of prime materials present in the workshop
     * @param nProductsStored Number of finished products present at the workshop
     * @param nTimesPrimeMaterialsFetched Number of times that a supply of prime materials was delivered to the workshop
     * @param nTotalPrimeMaterialsSupplied Total amount of prime materials that have already been supplied
     * @param nFinishedProducts Total number of products that have already been manufactured
     * 
     */
    public synchronized void WriteWorkshop(int nCurrentPrimeMaterials, int nProductsStored, int nTimesPrimeMaterialsFetched,
                                        int nTotalPrimeMaterialsSupplied, int nFinishedProducts)
    {
        this.nCurrentPrimeMaterials = nCurrentPrimeMaterials;
        this.nProductsStored = nProductsStored;
        this.nTimesPrimeMaterialsFetched = nTimesPrimeMaterialsFetched;
        this.nTotalPrimeMaterialsSupplied = nTotalPrimeMaterialsSupplied;
        this.nFinishedProducts = nFinishedProducts;
        WriteLine();
    }
    
    
}
