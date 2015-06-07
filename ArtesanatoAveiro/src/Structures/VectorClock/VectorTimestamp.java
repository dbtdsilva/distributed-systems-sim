/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures.VectorClock;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 * @author Diogo Silva (60337)
 * @author Tânia Alves (60340)
 */
public class VectorTimestamp implements Cloneable, Serializable {
    private static final long serialVersionUID = 1001L;
    
    private int ts[];
    private int localindex;
    
    /**
     * Constructor to create an object of type VectorTimestamp
     * @param size size of the vector clock
     * @param localindex local index of the vector clock
     */
    public VectorTimestamp(int size, int localindex) {
        this.localindex = localindex;
        this.ts = new int[size];
    }
    
    /**
     * Increments the local index declared on the constructor.
     */
    public synchronized void increment() {
        ts[localindex]++;
    }
    
    /**
     * Updates the vector clock.
     * @param vt the Vector clock
     */
    public synchronized void update(VectorTimestamp vt) {
        for (int i = 0; i < vt.ts.length; i++) {
            ts[i] = Math.max(vt.ts[i], this.ts[i]);
        }
    }
    
    /**
     * Returns a deep copy of the object.
     * @return deep copy of the object
     */
    public synchronized VectorTimestamp getCopy() {
        return this.clone();
    }
    
    /**
     * Returns the vector clock as an integer array.
     * @return integer array containing the vector clock
     */
    public synchronized int[] toIntArray() {
        return ts;
    }
    
    @Override
    public synchronized VectorTimestamp clone() {
        VectorTimestamp copy = null;        
        try { 
            copy = (VectorTimestamp) super.clone ();
        } catch (CloneNotSupportedException e) {   
            System.err.println(Arrays.toString(e.getStackTrace()));
            System.exit(1);
        }
        copy.localindex = localindex;
        copy.ts = ts.clone();
        return copy;
    }
}
