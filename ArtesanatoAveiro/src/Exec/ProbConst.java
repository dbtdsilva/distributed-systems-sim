package Exec;

/**
 * This class contains all the constants.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class ProbConst {
    /**
     * Number of the prime materials needed to make a single product.
     */
    public final static int primeMaterialsPerProduct = 3;
    /**
     * The total number of prime materials in the Warehouse.
     */
    public final static int nPrimeMaterials = 10 * primeMaterialsPerProduct;
    /**
     * The total number of craftsmen working in the Workshop.
     */
    public final static int nCraftsmen = 3;
    /**
     * The total number of customers that can interact with the Shop.
     */
    public final static int nCustomers = 3;
    /**
     * The maximum number of products in the Workshop.
     */
    public final static int MAXproductsInWorkshop = 5;
    /**
     * The minimum number of prime materials in the Workshop before request to 
     * the Entrepreneur.
     */
    public final static int minPM = 3;
    
    /**
     * The maximum number of times that prime materials have been supplied. 
     */
    public final static int nMaxSupplies = 10;
}
