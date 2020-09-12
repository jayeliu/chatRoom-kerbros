package tgs;

import DES.DES;
import bin.Authenticator;
import bin.TicketTGS;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket sock = new Socket("localhost", 3333); // 连接指定服务器和端口
        handle(sock);
        sock.close();
        System.out.println("disconnected.");
    }

    private static void handle(Socket socket) throws Exception {
        ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream read = new ObjectInputStream(socket.getInputStream());

        TicketTGS ticketTGS = new TicketTGS("tgs123", "123", "123", "222", "df19c0e0-be96-40bf-ac3a-9d007c120452", "1", "4");
        Authenticator authenticatorTGS = new Authenticator("123", "123", "222", "2");
        String a=Message.Message3("123", ticketTGS, authenticatorTGS);
        write.writeObject(a);//发送
        String result = ((String) read.readObject());
        System.out.println("\n"+result);//反馈结果
        parseMessage4(result, "123");


    }
    //解析tgs发送给client的报文4
    public static String parseMessage4(String message,String kc_tgs) throws Exception {
        String T[];
        T=message.split(",");
        String type = T[0];
        message = T[1];
        String M[];
        M= DES.DES_Decryp(message,kc_tgs).split(",");
        String Kc_v = M[0];
        String id_v = M[1];
        String ts = M[2];
        String ticketV = M[3];
        return ticketV;
    }
}