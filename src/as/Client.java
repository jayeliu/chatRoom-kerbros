package as;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;

import DES.DES;
import MD5.MD5;
public class 	Client {    
	private static Socket socket = null;    
    private static int port=2222;    
	public static void main(String[] args) throws Exception {    
		
           socket = new Socket("localhost", port);    
			// ���͹ر�����    
			ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
			String type="4";
			String IDTGS="062609c8-9207-4416-8cca-07550a20f12b";
			String id="zhou";
            // String password=MD5.gethash("hello");
			String TS1=Handledata.gettime();
			String C_AS=String.join(",", type,id,IDTGS,TS1);
			
			System.out.println(C_AS);
			write.writeObject(C_AS);
			String AS_C=(String)read.readObject();
			
			System.out.println(DES.DES_Decryp(AS_C.substring(2, AS_C.length()),MD5.gethash("hello")));    
       // System.out.print("finish");
        System.exit(0);

   
   
    }    
}    
