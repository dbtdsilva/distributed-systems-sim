/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Message;

/**
 *
 * @author diogosilva
 */
public enum MessageType {
    /**
     * Auxiliars
     */
    ACK,
    ERROR,    
    /**
     * Craftsmen
     */
    COLLECTING_MATERIALS,
    PRIME_MATERIALS_NEEDED,
    BACK_TO_WORK,
    PREPARE_TO_PRODUCE,
    GO_TO_STORE,
    BATCH_READY_FOR_TRANSFER,
    /**
     * Customers
     */
    GO_SHOPPING,
    IS_DOOR_OPEN,
    ENTER_SHOP,
    PERUSING_AROUND,
    I_WANT_THIS,
    EXIT_SHOP,
    TRY_AGAIN_LATER,
    /**
     * Entrepreneur
     */
    PREPARE_TO_WORK,
    APPRAISE_SIT,
    ADDRESS_A_CUSTOMER,
    SAY_GOODBYE_TO_CUSTOMER,
    CLOSE_THE_DOOR,
    CUSTOMERS_IN_THE_SHOP,
    PREPARE_TO_LEAVE,
    GO_TO_WORKSHOP,
    RETURN_TO_SHOP,
    VISIT_SUPPLIERS,
    REPLENISH_STOCK,
    /**
     * Shop
     */
    RESET_REQ_PMATERIALS,
    RESET_REQ_PRODUCTS,
    /**
     * Logging
     */
    INIT_WRITE,
    END_WRITE,
    
    END_OPER_CUSTOMER,
    END_OPER_CRAFTSMAN,
    END_OPER_ENTREPRENEUR,
    
    UPDATE_REQ_PMATERIALS,
    UPDATE_REQ_PRODUCTS,
    
    WRITE_ENTR_STATE,
    WRITE_CRAFT_STATE,
    WRITE_CUST_STATE,
    
    WRITE_SHOP,
    WRITE_SHOP_ENTR_STATE,
    WRITE_SHOP_CRAFT_STATE,
    WRITE_SHOP_CUST_STATE,
    
    WRITE_WSHOP,
    WRITE_WSHOP_CRAFT_STATE,
    WRITE_WSHOP_ENTR_STATE;
}
