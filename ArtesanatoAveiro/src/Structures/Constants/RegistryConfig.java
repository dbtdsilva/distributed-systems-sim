/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Structures.Constants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class contains all the constants related with the Registry.
 * 
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class RegistryConfig {
    /**
     * Logging name entry on the registry.
     */
    public static String logNameEntry = "LoggingInt";

    /**
     * Shop name entry on the registry.
     */
    public static String shopNameEntry = "ShopInt";

    /**
     * Workshop name entry on the registry.
     */
    public static String workshopNameEntry = "WorkshopInt";

    /**
     * Warehouse name entry on the registry.
     */
    public static String warehouseNameEntry = "WarehouseInt";

    /**
     * RegisterHandler name entry on the registry.
     */
    public static String registerHandler = "RegisterHandler";
    /**
     * Bash property of the file.
     */
    private Properties prop;
    
    public RegistryConfig(String filename) {
        prop = new Properties();
        InputStream in = null;
        try {
            in = new FileInputStream(filename);
            prop.load(in);
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(RegistryConfig.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ex) {
                    Logger.getLogger(RegistryConfig.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
    }
    public String loadParam(String param) {
        return prop.getProperty(param);
    }
    public String registryHost() {
        return loadParam("REGISTER_HOST");
    }
    public int registryPort() {
        return Integer.parseInt(loadParam("REGISTER_PORT"));
    }
    public int objectPort() {
        return Integer.parseInt(loadParam("REGISTER_OBJECT_PORT"));
    }
    public int loggingPort() {
        return Integer.parseInt(loadParam("LOGGING_PORT"));
    }
    public int shopPort() {
        return Integer.parseInt(loadParam("SHOP_PORT"));
    }
    public int workshopPort() {
        return Integer.parseInt(loadParam("WORKSHOP_PORT"));
    }
    public int warehousePort() {
        return Integer.parseInt(loadParam("WAREHOUSE_PORT"));
    }
}
