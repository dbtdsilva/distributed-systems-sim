/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Exec;

import Logger.Logging;
import java.util.concurrent.Semaphore;

/**
 *
 * @author diogosilva
 */
public class GeneralRepository {
    public Logging log;
    
    public Semaphore entrepreneurWake;
    public Semaphore customersWaiting[];
    public Semaphore craftsmenWaitingMaterials;
    public int craftsmenWaiting = 0;
    
    GeneralRepository(String loggerName, int nCustomers, int nCraftsmen) {
        log = new Logging(loggerName, nCustomers, nCraftsmen);
        craftsmenWaiting = 0;
        
        entrepreneurWake = new Semaphore(1);
        customersWaiting = new Semaphore[nCustomers];
        for (int i = 0; i < nCustomers; i++)
            customersWaiting[i] = new Semaphore(1);
        craftsmenWaitingMaterials = new Semaphore(1);
    }
}
