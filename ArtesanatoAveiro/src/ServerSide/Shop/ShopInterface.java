/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServerSide.Shop;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;

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
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do cliente inválido,", inMessage);
                
                shop.goShopping(inMessage.getId());
                
                outMessage = new Message(MessageType.ACK, CustomerState.CHECKING_SHOP_DOOR_OPEN);
                break;
            case IS_DOOR_OPEN:
                outMessage = shop.isDoorOpen() ? new Message(MessageType.POSITIVE) : new Message(MessageType.NEGATIVE);
                break;
            case ENTER_SHOP:
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do cliente inválido,", inMessage);
                shop.enterShop(inMessage.getId());
                outMessage = new Message(MessageType.ACK, CustomerState.APPRAISING_OFFER_IN_DISPLAY);
                break;
            case EXIT_SHOP:
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do cliente inválido,", inMessage);
                shop.enterShop(inMessage.getId());
                outMessage = new Message(MessageType.ACK, CustomerState.CARRYING_OUT_DAILY_CHORES);
                break;
            case PERUSING_AROUND:                
                int nProducts = shop.perusingAround();
                outMessage = new Message(MessageType.ACK, nProducts);
                break;
            case I_WANT_THIS:
                if (inMessage.getId() == Message.ERROR_INT || 
                    inMessage.getnProducts() == Message.ERROR_INT)
                    throw new MessageException("Id do cliente inválido ou número de produtos inválido.", inMessage);
                shop.iWantThis(inMessage.getId(), inMessage.getnProducts());
                outMessage = new Message(MessageType.ACK, CustomerState.BUYING_SOME_GOODS);
                break;
            case TRY_AGAIN_LATER:
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do cliente inválido,", inMessage);
                shop.tryAgainLater(inMessage.getId());
                outMessage = new Message(MessageType.ACK, CustomerState.CARRYING_OUT_DAILY_CHORES);
                break;
            case PREPARE_TO_WORK:
                shop.prepareToWork();
                outMessage = new Message(MessageType.ACK, EntrepreneurState.WAITING_FOR_NEXT_TASK);
                break;
            case APPRAISE_SIT:
                char nextTask = shop.appraiseSit();
                outMessage = new Message(MessageType.ACK, nextTask);
                break;
            case ADDRESS_A_CUSTOMER:
                int customerId = shop.addressACustomer();
                outMessage = new Message(MessageType.ACK, EntrepreneurState.ATTENDING_A_CUSTOMER, customerId);
                break;
            case SAY_GOODBYE_TO_CUSTOMER:
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do cliente inválido,", inMessage);
                shop.sayGoodByeToCustomer(inMessage.getId());
                outMessage = new Message(MessageType.ACK, EntrepreneurState.WAITING_FOR_NEXT_TASK);
                break;
            case CLOSE_THE_DOOR:
                shop.closeTheDoor();
                outMessage = new Message(MessageType.ACK);
                break;
            case CUSTOMERS_IN_THE_SHOP:
                boolean ret = shop.customersInTheShop();
                if (ret) {
                    outMessage = new Message(MessageType.POSITIVE);
                } else {
                    outMessage = new Message(MessageType.NEGATIVE);
                }
                break;
            case PREPARE_TO_LEAVE:
                shop.prepareToLeave();
                outMessage = new Message(MessageType.ACK, EntrepreneurState.CLOSING_THE_SHOP);
                break;
            case RETURN_TO_SHOP:
                if (inMessage.getnProducts() == Message.ERROR_INT)
                    throw new MessageException("Número de produtos inválido,", inMessage);
                shop.returnToShop(inMessage.getnProducts());
                outMessage = new Message(MessageType.ACK, EntrepreneurState.OPENING_THE_SHOP);
                break;
            case PRIME_MATERIALS_NEEDED:
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do artesão inválido,", inMessage);
                shop.primeMaterialsNeeded(inMessage.getId());
                outMessage = new Message(MessageType.ACK, CraftsmanState.CONTACTING_ENTREPRENEUR);
                break;
            case BATCH_READY_FOR_TRANSFER:
                if (inMessage.getId() == Message.ERROR_INT)
                    throw new MessageException("Id do artesão inválido,", inMessage);
                shop.batchReadyForTransfer(inMessage.getId());
                outMessage = new Message(MessageType.ACK, CraftsmanState.CONTACTING_ENTREPRENEUR);
                break;
            case RESET_REQ_PMATERIALS:
                shop.resetRequestPrimeMaterials();
                outMessage = new Message(MessageType.ACK);
                break;
            case RESET_REQ_PRODUCTS:
                shop.resetRequestPrimeMaterials();
                outMessage = new Message(MessageType.ACK);
                break;
            default:
                System.out.println("Mensagem inválida recebida: " + inMessage);
                break;
        }
        return outMessage;
    }
}
