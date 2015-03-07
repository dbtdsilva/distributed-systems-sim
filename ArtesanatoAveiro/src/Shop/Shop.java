/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Shop;

import Customer.Customer;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author diogosilva
 */
public class Shop {
    private int nCustomersInside;
    private int nProductsStock;
    private ShopDoorState doorState;
    private Queue<Customer> waitingLine;
    private boolean reqFetchProducts;
    private boolean reqPrimeMaterials;
    
    public Shop() {
        this.nProductsStock = 0;
        this.doorState = ShopDoorState.CLOSED;
        this.nCustomersInside = 0;
        this.waitingLine = new LinkedList<>();
        this.reqFetchProducts = false;
        this.reqPrimeMaterials = false;
    }
}
