package Communication;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * This class implements the communication channel, on the server side, 
 * for a message based communication over sockets using the TCP protocol.
 * Data transfer is based in objects, one object at a time.
 * 
 * @author Prof.Rui Borges
 */
public class ServerComm {

    /**
     * Listening socket.
     *
     * @serialField listeningSocket
     */
    private ServerSocket listeningSocket = null;

    /**
     * Communication socket
     *
     * @serialField commSocket
     */
    private Socket commSocket = null;

    /**
     * Server's listening port number
     *
     * @serialField serverPortNumb
     */
    private final int serverPortNumb;

    /**
     * Communication channel input stream.
     *
     * @serialField in
     */
    private ObjectInputStream in = null;

    /**
     * Communication channel output stream.
     *
     * @serialField out
     */
    private ObjectOutputStream out = null;

    /**
     * Communication channel instantiation (first form).     
     *
     * @param portNumb server listening port number
     */
    public ServerComm(int portNumb) {
        serverPortNumb = portNumb;
    }

    /**
     * Communication channel instantiation (second form).     
     *
     * @param portNumb server listening port number
     * @param lSocket server's listening socket
     */
    public ServerComm(int portNumb, ServerSocket lSocket) {
        serverPortNumb = portNumb;
        listeningSocket = lSocket;
    }

    /**
     * Service start. Instantiation of a listening socket and its association with the 
     * system's address.
     * Instantiation of a listening socket and its association to the local
     * machine's public address and corresponding listening port number.
     */
    public void start() {
        try {
            listeningSocket = new ServerSocket(serverPortNumb);
        } catch (BindException e) // erro fatal --- port já em uso
        {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível a associação do socket de escuta ao port: "
                    + serverPortNumb + "!");

            System.exit(1);
        } catch (IOException e) // erro fatal --- outras causas
        {
            System.out.println(Thread.currentThread().getName()
                    + " - ocorreu um erro indeterminado na associação do socket de escuta ao port: "
                    + serverPortNumb + "!");

            System.exit(1);
        }
    }

    /**
     * Service closing. Listening socket closing.
     */
    public void end() {
        try {
            listeningSocket.close();
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o socket de escuta!");

            System.exit(1);
        }
    }

    /**
     * Listening process. Creation of a communication channel for a pending 
     * request. Instantiation of a communication socket and its association 
     * with the client's address. Opening of the socket's input and output streams.
     *
     * @return communication channel
     * @throws SocketTimeoutException
     */
    public ServerComm accept() throws SocketTimeoutException {
        ServerComm scon;                                      // canal de comunicação

        scon = new ServerComm(serverPortNumb, listeningSocket);
        try {
            scon.commSocket = listeningSocket.accept();   
            
        } catch (SocketException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - foi fechado o socket de escuta durante o processo de escuta!");

            System.exit(1);
        } catch(SocketTimeoutException e) {
            throw e;
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir um canal de comunicação para um pedido pendente!");

            System.exit(1);
        }
        
        try {
            scon.in = new ObjectInputStream(scon.commSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de entrada do socket!");

            System.exit(1);
        }

        try {
            scon.out = new ObjectOutputStream(scon.commSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de saída do socket!");

            System.exit(1);
        }

        return scon;
    }

    /**
     * Closing of the communication channel. Closing to the socket's input
     * and output streams. Closing to the communication socket.
     */
    public void close() {
        try {
            in.close();
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o canal de entrada do socket!");

            System.exit(1);
        }

        try {
            out.close();
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o canal de saída do socket!");

            System.exit(1);
        }

        try {
            commSocket.close();
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível fechar o socket de comunicação!");

            System.exit(1);
        }
    }

    /**
     * Communication channel object reading.
     *
     * @return read object
     */
    public Object readObject() {
        Object fromClient = null;                            // objecto

        try {
            fromClient = in.readObject();
        } catch (InvalidClassException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto lido não é passível de desserialização!");

            System.exit(1);
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - erro na leitura de um objecto do canal de entrada do socket de comunicação!");

            System.exit(1);
        } catch (ClassNotFoundException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto lido corresponde a um tipo de dados desconhecido!");

            System.exit(1);
        }

        return fromClient;
    }

    /**
     * Communication channel object writing.
     *
     * @param toClient object to be written.
     */
    public void writeObject(Object toClient) {
        try {
            out.writeObject(toClient);
        } catch (InvalidClassException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto a ser escrito não é passível de serialização!");

            System.exit(1);
        } catch (NotSerializableException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o objecto a ser escrito pertence a um tipo de dados não serializável!");

            System.exit(1);
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - erro na escrita de um objecto do canal de saída do socket de comunicação!");

            System.exit(1);
        }
    }
    
}
