######################################################################
##																	##
## Existe 3 regras possíveis										##
## make all (ou make) 												##
##		-> Compila todo o código fonte.								##
## make run 														##
##		-> Executa uma vez a simulação.								##
##		NOTA: Convém compilar o código primeiro, se não dá erro.	##
## make clean 														##
##		-> Limpa todos os ficheiros .class 	 						##
##																	##
######################################################################

JFLAGS = -g
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java -d __bin

all:	__bin classes

run:	
	cd __bin; java Exec.ArtesanatoAveiro

clean:
	rm -r __bin/

CLASSES = \
		ServerSide/Workshop/Workshop.java \
		ServerSide/Workshop/WorkshopExec.java \
		ServerSide/Workshop/WorkshopInterface.java \
		ServerSide/Warehouse/Warehouse.java \
		ServerSide/Warehouse/WarehouseExec.java \
		ServerSide/Warehouse/WarehouseInterface.java \
		ServerSide/Shop/Shop.java \
		ServerSide/Shop/ShopExec.java \
		ServerSide/Shop/ShopState.java \
		ServerSide/Shop/ShopInterface.java \
		ServerSide/Logger/Logging.java \
		ServerSide/Logger/LoggingExec.java \
		ServerSide/Logger/LoggingInterface.java \
		Communication/ProbConst.java \
		Communication/ClientComm.java \
		Communication/CommConst.java \
		Communication/ServerComm.java \
		Communication/Proxy/ClientProxy.java \
		Communication/Proxy/ServerInterface.java \
		Communication/Message/Message.java \
		Communication/Message/MessageException.java \
		Communication/Message/MessageType.java \
		ClientSide/Craftsman/Craftsman.java \
		ClientSide/Craftsman/CraftsmanExec.java \
		ClientSide/Craftsman/CraftsmanState.java \
		ClientSide/Customer/Customer.java \
		ClientSide/Customer/CustomerExec.java \
		ClientSide/Customer/CustomerState.java \
		ClientSide/Entrepreneur/Entrepreneur.java \
		ClientSide/Entrepreneur/EntrepreneurExec.java \
		ClientSide/Entrepreneur/EntrepreneurState.java 

classes: $(CLASSES:.java=.class)

__bin:
	mkdir __bin

