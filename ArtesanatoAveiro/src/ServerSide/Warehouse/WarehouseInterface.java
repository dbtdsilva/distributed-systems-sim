/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Warehouse;

import java.rmi.Remote;

/**
 * 
 * @author Diogo Silva (60337)
 * @author Tânia Alves (60340)
 */
public interface WarehouseInterface extends Remote {
    /**
     * The entrepreneur visits the supplies to fetch prime materials for the
     * craftsman.
     * She will fetch a random value of prime materials.
     * 
     * @return the number of prime materials fetched
     */
    public int visitSuppliers();
}
