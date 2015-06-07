/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

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
     * @param vt The vector clock
     * @throws java.rmi.RemoteException may throw during a execution of a remote method call
     */
    public Object[] visitSuppliers(Structures.VectorClock.VectorTimestamp vt) throws RemoteException;
}
