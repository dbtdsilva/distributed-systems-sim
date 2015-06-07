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
    /**
     * Constructor that receives the file with the configurations.
     * @param filename path for the configuration file
     */
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
    /**
     * Loads a parameter from the bash.
     * @param param parameter name
     * @return parameter value
     */
    public String loadParam(String param) {
        return prop.getProperty(param);
    }
    
    /** 
     * Loads the parameter REGISTER_HOST from the configuration file.
     * @return parameter value
     */
    public String registryHost() {
        return loadParam("REGISTER_HOST");
    }
    /** 
     * Loads the parameter REGISTER_PORT from the configuration file.
     * @return parameter value
     */
    public int registryPort() {
        return Integer.parseInt(loadParam("REGISTER_PORT"));
    }
    /** 
     * Loads the parameter REGISTER_OBJECT_PORT from the configuration file.
     * @return parameter value
     */
    public int objectPort() {
        return Integer.parseInt(loadParam("REGISTER_OBJECT_PORT"));
    }
    /** 
     * Loads the parameter LOGGING_PORT from the configuration file.
     * @return parameter value
     */
    public int loggingPort() {
        return Integer.parseInt(loadParam("LOGGING_PORT"));
    }
    /** 
     * Loads the parameter SHOP_PORT from the configuration file.
     * @return parameter value
     */
    public int shopPort() {
        return Integer.parseInt(loadParam("SHOP_PORT"));
    }
    /** 
     * Loads the parameter WORKSHOP_PORT from the configuration file.
     * @return parameter value
     */
    public int workshopPort() {
        return Integer.parseInt(loadParam("WORKSHOP_PORT"));
    }
    /** 
     * Loads the parameter WAREHOUSE_PORT from the configuration file.
     * @return parameter value
     */
    public int warehousePort() {
        return Integer.parseInt(loadParam("WAREHOUSE_PORT"));
    }
}
