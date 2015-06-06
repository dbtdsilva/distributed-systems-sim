/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package VectorClock;

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
    
    public VectorTimestamp(int size, int localindex) {
        this.localindex = localindex;
        this.ts = new int[size];
    }
    
    public synchronized void increment() {
        ts[localindex]++;
    }
    
    public synchronized void update(VectorTimestamp vt) {
        for (int i = 0; i < vt.ts.length; i++) {
            ts[i] = Math.max(vt.ts[i], this.ts[i]);
        }
    }
    
    public synchronized VectorTimestamp getCopy() {
        return this.clone();
    }
    
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
