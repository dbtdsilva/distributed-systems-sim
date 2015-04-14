/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Shop;

import Message.Message;
import Message.MessageException;
import ServerSide.ServerInterface;

/**
 *
 * @author diogosilva
 */
public class ShopInterface implements ServerInterface {
    private final Shop shop;
    
    public ShopInterface(Shop shop) {
        this.shop = shop;
    }

    @Override
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;
        
        switch (inMessage.getType()) {
            case GO_SHOPPING:
                break;
            case IS_DOOR_OPEN:
                break;
            case ENTER_SHOP:
                break;
            case EXIT_SHOP:
                break;
            case PERUSING_AROUND:
                break;
            case I_WANT_THIS:
                break;
            case TRY_AGAIN_LATER:
                break;
            case PREPARE_TO_WORK:
                break;
            case APPRAISE_SIT:
                break;
            
            case ADDRESS_A_CUSTOMER:
                break;
            case SAY_GOODBYE_TO_CUSTOMER:
                break;
            case CLOSE_THE_DOOR:
                break;
            case CUSTOMERS_IN_THE_SHOP:
                break;
            case PREPARE_TO_LEAVE:
                break;
            case RETURN_TO_SHOP:
                break;
            case PRIME_MATERIALS_NEEDED:
                break;
            case BATCH_READY_FOR_TRANSFER:
                break;
            case RESET_REQ_PMATERIALS:
                break;
            case RESET_REQ_PRODUCTS:
                break;
            default:
                break;
        }
        return outMessage;
    }
}
