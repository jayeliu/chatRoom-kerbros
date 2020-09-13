package v;

import DES.DES;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(4444); // 监听指定端口
        System.out.println("server is running...");
        for (; ; ) {
            Socket sock = ss.accept();
            System.out.println("connected from " + sock.getRemoteSocketAddress());
            Thread t = new Handler(sock);
            t.start();
        }
    }
}

class Handler extends Thread {
    Socket sock;

    public Handler(Socket sock) {
        this.sock = sock;
    }

    @Override
    public void run() {
        try (ObjectInputStream input = new ObjectInputStream(sock.getInputStream())) {
            try (ObjectOutputStream output = new ObjectOutputStream(sock.getOutputStream())) {
                handle(input, output);
            }
        } catch (Exception e) {
            try {
                this.sock.close();
            } catch (IOException ioe) {
            }
            System.out.println("client disconnected.");
        }
    }

    private void handle(ObjectInputStream input, ObjectOutputStream output) throws Exception {
        String data = (String) input.readUTF();
        String[] group = data.split(",");
        if (group[0].equals("10")) {
            String ticket_v = group[1];
            String auth_c = group[2];
            String[] tvs =null;
            try {
                //!暂时固定
                String k_v = "hellov";
                tvs = DES.DES_Decryp(ticket_v,k_v).split(",");
            } catch (Exception e) {
                e.printStackTrace();
            }
            String[] auths =null;
            String k_c_v = tvs[0];
            try {
                auths = DES.DES_Decryp(auth_c,k_c_v).split(",");
            } catch (Exception e) {
                e.printStackTrace();
            }
            //打印
            for (String s : tvs) {
                System.out.println(s);
            }
            System.out.println("ready!");
            for (String s : auths) {
                System.out.println(s);
            }
            System.out.println(auths[2]);
            //
            long tsp=Long.parseLong(auths[2])+1;
            System.out.println("tsp:"+tsp);
            String data1=String.join(",","11",DES.DES(String.valueOf(tsp),k_c_v));
            System.out.println(data1);
            output.writeUTF(data1);
            //开启聊天
        }else {
            output.writeObject("12");
            output.flush();
        }
    }
}
