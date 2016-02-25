package ServerSide.Shop;

import ClientSide.Craftsman.CraftsmanState;
import ClientSide.Customer.CustomerState;
import ClientSide.Entrepreneur.EntrepreneurState;
import Communication.Message.Message;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;
import Communication.ServerComm;
import java.net.SocketException;

/**
 * This file defines the shop implementation of the server interface.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class ShopInterface implements ServerInterface {
    /**
     * Shop entity.
     */
    private final Shop shop;
    
    /**
     * Tells the service if it can end or not.
     */
    private boolean serviceEnded;
    
    /**
     * Constructor for the shop server.
     * @param shop Shop entity.
     */
    public ShopInterface(Shop shop) {
        this.shop = shop;
        this.serviceEnded = false;
    }

    /**
     * Processes the received messages and replies to the entity that sent it.
     * 
     * @param inMessage The received message.
     * @param scon Server communication.
     * @return Returns the reply to the received message.
     * @throws MessageException
     * @throws SocketException 
     */
    @Override
    public Message processAndReply(Message inMessage, ServerComm scon) throws MessageException, SocketException {
        Message outMessage = null;
        
        switch (inMessage.getType()) {
            case TERMINATE:
                outMessage = new Message(MessageType.ACK);
                this.serviceEnded = true;
                break;
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
                shop.exitShop(inMessage.getId());
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
                shop.resetRequestProducts();
                outMessage = new Message(MessageType.ACK);
                break;
            default:
                System.out.println("Mensagem inválida recebida: " + inMessage);
                break;
        }
        return outMessage;
    }

    /**
     * Tell the service if it is allowed to end or not.
     * @return True if the system can terminate, false otherwise.
     */
    @Override
    public boolean serviceEnded() {
        return serviceEnded;
    }
}
