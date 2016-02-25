/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication.Message;

/**
 * This file defines the message types.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public enum MessageType {
    /**
     * This message type allows the sender of a previous message to make sure the message was received by the receiver.
     */
    ACK,
    /**
     * Simulates the boolean functionality for the messages. This is the equivalent to true.
     */
    POSITIVE,
    /**
     * Simulates the boolean functionality for the messages. This is the equivalent to false.
     */
    NEGATIVE,
    /**
     * Alerts that the present message is an error message.
     */
    ERROR,
    /**
     * Alerts the logger that the clients are finishing.
     */
    TERMINATE,
    /**
     * Alerts the servers that the simulation has ended.
     */
    TERMINATED,
    
    /**
     * Message used when the Craftsman is collecting materials.
     */
    COLLECTING_MATERIALS,
    /**
     * Message used when the Craftsman requests more prime materials.
     */
    PRIME_MATERIALS_NEEDED,
    /**
     * Message used when the Craftsman is going back to work.
     */
    BACK_TO_WORK,
    /**
     * Message used when the Craftsman is preparing to produce.
     */
    PREPARE_TO_PRODUCE,
    /**
     * Message used when the Craftsman is storing the finished piece.
     */
    GO_TO_STORE,
    /**
     * Message used when the Craftsman requests a transfer for the finished products.
     */
    BATCH_READY_FOR_TRANSFER,
    /**
     * Message used when the Customer is going shopping.
     */
    GO_SHOPPING,
    /**
     * Message used to tell whether the shop door is closed or not.
     */
    IS_DOOR_OPEN,
    /**
     * Message used when the Customer is entering the shop.
     */
    ENTER_SHOP,
    /**
     * Message used when the Customer is browsing the goods at the shop.
     */
    PERUSING_AROUND,
    /**
     * Message used when the Customer has chosen something to buy.
     */
    I_WANT_THIS,
    /**
     * Message used when the Customer is exiting the shop.
     */
    EXIT_SHOP,
    /**
     * Message used when the Customer finds the shop closed.
     */
    TRY_AGAIN_LATER,
    /**
     * Message used when the Entrepreneur is preparing to work.
     */
    PREPARE_TO_WORK,
    /**
     * Message used when the Entrepreneur is pondering what to do.
     */
    APPRAISE_SIT,
    /**
     * Message used when the Entrepreneur is talking to a Customer.
     */
    ADDRESS_A_CUSTOMER,
    /**
     * Message used when the Entrepreneur has finished attending the Customer. 
     */
    SAY_GOODBYE_TO_CUSTOMER,
    /**
     * Message used when the Entrepreneur is leaving the shop.
     */
    CLOSE_THE_DOOR,
    /**
     * Tells whether there are Customers still in the shop or not.
     */
    CUSTOMERS_IN_THE_SHOP,
    /**
     * Message used when the Entrepreneur is closing the shop door and preparing to leave.
     */
    PREPARE_TO_LEAVE,
    /**
     * Message used when the Entrepreneur is going to the workshop to attend a request.
     */
    GO_TO_WORKSHOP,
    /**
     * Message used when the Entrepreneur is coming back to the shop after finishing her chores.
     */
    RETURN_TO_SHOP,
    /**
     * Message used when the Entrepreneur is visiting the suppliers to get some prime materials.
     */
    VISIT_SUPPLIERS,
    /**
     * Message used when the Entrepreneur went to get more prime materials to the workshop.
     */
    REPLENISH_STOCK,
    /**
     * Message used to reset the request for prime materials by the Craftsman to the shop.
     */
    RESET_REQ_PMATERIALS,
    /**
     * Message used to reset the request for products by the Craftsman to the shop.
     */
    RESET_REQ_PRODUCTS,
    /**
     * Message used to start the log writing.
     */
    INIT_WRITE,
    /**
     * Message used to end the log writing.
     */
    END_WRITE,
    /**
     * Message used to end the Customer's operation.
     */
    END_OPER_CUSTOMER,
    /**
     * Message used to end the Craftsman's operation.
     */
    END_OPER_CRAFTSMAN,
    /**
     * Message used to end the Entrepreneur's operation.
     */
    END_OPER_ENTREPRENEUR,
    /**
     * Message used to update the logger's information on the request.
     */
    UPDATE_REQ_PMATERIALS,
    /**
     * Message used to update the logger's information on the request.
     */
    UPDATE_REQ_PRODUCTS,
    /**
     * Message used to write the Entrepreneur state on the logger file.
     */
    WRITE_ENTR_STATE,
    /**
     * Message used to write the Craftsman state on the logger file.
     */
    WRITE_CRAFT_STATE,
    /**
     * Message used to write the Customer state on the logger file.
     */
    WRITE_CUST_STATE,
    /**
     * Message used to write the Shop state on the logger file.
     */
    WRITE_SHOP,
    /**
     * Message used to write both Entrepreneur and Shop state on the logger file.
     */
    WRITE_SHOP_ENTR_STATE,
    /**
     * Message used to write both Craftsman and Shop state on the logger file.
     */
    WRITE_SHOP_CRAFT_STATE,
    /**
     * Message used to write both Customer and Shop state on the logger file.
     */
    WRITE_SHOP_CUST_STATE,
    /**
     * Message used to write the Workshop state on the logger file.
     */
    WRITE_WSHOP,
    /**
     * Message used to write both Craftsman and Workshop state on the logger file.
     */
    WRITE_WSHOP_CRAFT_STATE,
    /**
     * Message used to write both Entrepreneur and Workshop state on the logger file.
     */
    WRITE_WSHOP_ENTR_STATE;
}
