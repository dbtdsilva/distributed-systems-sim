
import ClientSide.Craftsman.CraftsmanState;
import Message.Message;
import Message.MessageType;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author diogosilva
 */
public class Test {
    public static void main(String [] args) throws FileNotFoundException, IOException {
        Message msg = new Message(MessageType.WRITE_CRAFT_STATE, CraftsmanState.CONTACTING_ENTREPRENEUR);
        
        FileOutputStream fileOut = new FileOutputStream("./x.tmp");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(msg);
        
        out.close();
        fileOut.close();
    }   
}
