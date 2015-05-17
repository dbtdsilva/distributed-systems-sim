package ServerSide.Logger;

import Communication.Message.Message;
import static Communication.Message.Message.ERROR_INT;
import Communication.Message.MessageException;
import Communication.Message.MessageType;
import Communication.Proxy.ServerInterface;
import Communication.ServerComm;
import java.net.SocketException;

/**
 * This file defines the logging implementation of the server interface.
 *
 * @author Diogo Silva, 60337
 * @author Tânia Alves, 60340
 */
public class LoggingInterface implements ServerInterface {
    /**
     * Logger file.
     */
    private final Logging log;
    
    /**
     * Tells the service if it can end or not.
     */
    public boolean serviceEnded;
    
    /**
     * Constructor for the logger server.
     * @param log Logging to store information.
     */
    public LoggingInterface(Logging log) {
        this.log = log;
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
                if (log.clientsTerminated()) {
                    log.terminateServers();
                    System.out.println("Terminating servers...");
                    serviceEnded = true;
                    log.EndWriting();
                }
                
                break;
            case END_OPER_CUSTOMER:
                if (log.endOpCustomer())
                    outMessage = new Message(MessageType.POSITIVE);
                else
                    outMessage = new Message(MessageType.NEGATIVE);
                break;
            case END_OPER_CRAFTSMAN:
                int val = log.endOperCraft();
                if (val == 2)
                    outMessage = new Message(MessageType.POSITIVE, true);
                else if (val == 1) {
                    outMessage = new Message(MessageType.POSITIVE, false);
                } else {
                    outMessage = new Message(MessageType.NEGATIVE, false);
                }
                break;
            case END_OPER_ENTREPRENEUR:
                if (log.endOpEntrep())
                    outMessage = new Message(MessageType.POSITIVE);
                else
                    outMessage = new Message(MessageType.NEGATIVE);
                break;
            case UPDATE_REQ_PMATERIALS:
                log.UpdatePrimeMaterialsRequest(inMessage.isRequestPrimeMaterials());
                outMessage = new Message(MessageType.ACK);
                break;
            case UPDATE_REQ_PRODUCTS:
                log.UpdateFetchProductsRequest(inMessage.isRequestFetchProducts());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_ENTR_STATE:
                if (inMessage.getEntrState() == null) 
                    throw new MessageException("Estado da dona inválido,", inMessage);
                log.UpdateEntreperneurState(inMessage.getEntrState());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_CRAFT_STATE:
                if (inMessage.getCraftState() == null || inMessage.getId() == ERROR_INT) 
                    throw new MessageException("Estado do artesão ou id inválido,", inMessage);
                log.UpdateCraftsmanState(inMessage.getId(), inMessage.getCraftState());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_CUST_STATE:
                if (inMessage.getCustState() == null || inMessage.getId() == ERROR_INT) 
                    throw new MessageException("Estado do cliente ou id inválido,", inMessage);
                log.UpdateCustomerState(inMessage.getId(), inMessage.getCustState());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_SHOP:
                if (inMessage.getShopState() == null ||
                        inMessage.getnCustomerIn() == ERROR_INT ||
                        inMessage.getnGoodsInDisplay() == ERROR_INT)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                
                log.WriteShop(inMessage.getShopState(), inMessage.getnCustomerIn(),
                        inMessage.getnGoodsInDisplay(), inMessage.isRequestFetchProducts(), 
                        inMessage.isRequestPrimeMaterials());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_SHOP_ENTR_STATE:
                if (inMessage.getEntrState() == null || 
                        inMessage.getShopState() == null ||
                        inMessage.getnCustomerIn() == ERROR_INT ||
                        inMessage.getnGoodsInDisplay() == ERROR_INT)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                
                log.WriteShopAndEntrepreneurStat(inMessage.getShopState(), inMessage.getnCustomerIn(),
                        inMessage.getnGoodsInDisplay(), inMessage.isRequestFetchProducts(), 
                        inMessage.isRequestPrimeMaterials(), inMessage.getEntrState());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_SHOP_CRAFT_STATE:
                if (inMessage.getShopState() == null ||
                        inMessage.getnCustomerIn() == ERROR_INT ||
                        inMessage.getnGoodsInDisplay() == ERROR_INT ||
                        inMessage.getCraftState() == null ||
                        inMessage.getId() == ERROR_INT)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                
                log.WriteShopAndCraftsmanStat(inMessage.getShopState(), inMessage.getnCustomerIn(),
                        inMessage.getnGoodsInDisplay(), inMessage.isRequestFetchProducts(), 
                        inMessage.isRequestPrimeMaterials(), inMessage.getCraftState(),
                        inMessage.getId());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_SHOP_CUST_STATE:
                if (inMessage.getShopState() == null ||
                        inMessage.getnCustomerIn() == ERROR_INT ||
                        inMessage.getnGoodsInDisplay() == ERROR_INT ||
                        inMessage.getCustState() == null ||
                        inMessage.getId() == ERROR_INT ||
                        inMessage.getnBoughtGoods() == ERROR_INT)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                
                log.WriteShopAndCustomerStat(inMessage.getShopState(), inMessage.getnCustomerIn(),
                        inMessage.getnGoodsInDisplay(), inMessage.isRequestFetchProducts(), 
                        inMessage.isRequestPrimeMaterials(), inMessage.getCustState(),
                        inMessage.getId(), inMessage.getnBoughtGoods());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_WSHOP:
                if (inMessage.getnCurrentPrimeMaterials() == ERROR_INT ||
                        inMessage.getnProductsStored() == ERROR_INT ||
                        inMessage.getnTimesPrimeMaterialsFetched() == ERROR_INT ||
                        inMessage.getnTotalPrimeMaterialsSupplied() == ERROR_INT ||
                        inMessage.getnFinishedProducts() == ERROR_INT)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                log.WriteWorkshop(inMessage.getnCurrentPrimeMaterials(), 
                        inMessage.getnProductsStored(), 
                        inMessage.getnTimesPrimeMaterialsFetched(), 
                        inMessage.getnTotalPrimeMaterialsSupplied(), 
                        inMessage.getnFinishedProducts());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_WSHOP_CRAFT_STATE:
                if (inMessage.getnCurrentPrimeMaterials() == ERROR_INT ||
                        inMessage.getnProductsStored() == ERROR_INT ||
                        inMessage.getnTimesPrimeMaterialsFetched() == ERROR_INT ||
                        inMessage.getnTotalPrimeMaterialsSupplied() == ERROR_INT ||
                        inMessage.getnFinishedProducts() == ERROR_INT ||
                        inMessage.getCraftState() == null ||
                        inMessage.getId() == ERROR_INT)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                log.WriteWorkshopAndCraftsmanStat(inMessage.getnCurrentPrimeMaterials(), 
                        inMessage.getnProductsStored(), 
                        inMessage.getnTimesPrimeMaterialsFetched(), 
                        inMessage.getnTotalPrimeMaterialsSupplied(), 
                        inMessage.getnFinishedProducts(),
                        inMessage.getCraftState(),
                        inMessage.getId(),
                            inMessage.isFinishedProduct());
                outMessage = new Message(MessageType.ACK);
                break;
            case WRITE_WSHOP_ENTR_STATE:
                if (inMessage.getnCurrentPrimeMaterials() == ERROR_INT ||
                        inMessage.getnProductsStored() == ERROR_INT ||
                        inMessage.getnTimesPrimeMaterialsFetched() == ERROR_INT ||
                        inMessage.getnTotalPrimeMaterialsSupplied() == ERROR_INT ||
                        inMessage.getnFinishedProducts() == ERROR_INT ||
                        inMessage.getEntrState() == null)
                    throw new MessageException("Formato de mensagem inválido,", inMessage);
                log.WriteWorkshopAndEntrepreneurStat(inMessage.getnCurrentPrimeMaterials(), 
                        inMessage.getnProductsStored(), 
                        inMessage.getnTimesPrimeMaterialsFetched(), 
                        inMessage.getnTotalPrimeMaterialsSupplied(), 
                        inMessage.getnFinishedProducts(),
                        inMessage.getEntrState());
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
