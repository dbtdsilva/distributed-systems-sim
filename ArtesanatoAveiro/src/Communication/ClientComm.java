package Communication;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.NoRouteToHostException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * This call implements the communication channel, on the client side, 
 * for a message based communication over sockets using the TCP protocol.
 * Data transfer is based in objects, one object at a time.
 * 
 * @author Prof.Rui Borges
 */
public class ClientComm {

    /**
     * Communication socket.
     *
     * @serialField commSocket
     */

    private Socket commSocket = null;

    /**
     * Name of the system where the server is running.
     *
     * @serialField serverHostName
     */
    private String serverHostName = null;

    /**
     * Server's listening port number .
     *
     * @serialField serverPortNumb
     */
    private int serverPortNumb;

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
     * Communication channel instantiation.
     *
     * @param hostName name of the system where the server is located
     * @param portNumb server's listening port number
     */
    public ClientComm(String hostName, int portNumb) {
        serverHostName = hostName;
        serverPortNumb = portNumb;
    }

    /**
     * Communication channel opening. Instantiation of a communication socket
     * and association with the server's address.
     * Opening of both socket input and output streams.
     * @return <li>true, if the communication channel was open
     * <li>false, otherwise
     */
    public boolean open() {
        boolean success = true;
        SocketAddress serverAddress = new InetSocketAddress(serverHostName, serverPortNumb);

        try {
            commSocket = new Socket();
            commSocket.connect(serverAddress);
        } catch (UnknownHostException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o nome do sistema computacional onde reside o servidor é desconhecido: "
                    + serverHostName + "!");
            System.exit(1);
        } catch (NoRouteToHostException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o nome do sistema computacional onde reside o servidor é inatingível: "
                    + serverHostName + "!");
            System.exit(1);
        } catch (ConnectException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - o servidor não responde em: " + serverHostName + "." + serverPortNumb + "!");
            if (e.getMessage().equals("Connection refused")) {
                success = false;
            } else {
                System.out.println(e.getMessage() + "!");
                System.exit(1);
            }
        } catch (SocketTimeoutException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - ocorreu um time out no estabelecimento da ligação a: "
                    + serverHostName + "." + serverPortNumb + "!");
            success = false;
        } catch (IOException e) // erro fatal --- outras causas
        {
            System.out.println(Thread.currentThread().getName()
                    + " - ocorreu um erro indeterminado no estabelecimento da ligação a: "
                    + serverHostName + "." + serverPortNumb + "!");
            System.exit(1);
        }

        if (!success) {
            return (success);
        }

        try {
            out = new ObjectOutputStream(commSocket.getOutputStream());
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de saída do socket!");
            System.exit(1);
        }

        try {
            in = new ObjectInputStream(commSocket.getInputStream());
        } catch (IOException e) {
            System.out.println(Thread.currentThread().getName()
                    + " - não foi possível abrir o canal de entrada do socket!");
            System.exit(1);
        }

        return (success);
    }

    /**
     * Communication channel closing. Socket input and output streams closing.
     * Communication socket closing.
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
        Object fromServer = null;                            // objecto

        try {
            fromServer = in.readObject();
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

        return fromServer;
    }

    /**
     * Communication channel object writing.
     *
     * @param toServer object to be written.
     */
    public void writeObject(Object toServer) {
        try {
            out.writeObject(toServer);
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
