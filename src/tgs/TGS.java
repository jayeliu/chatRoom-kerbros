package tgs;

import DES.DES;
import bin.Authenticator;
import bin.TicketTGS;
import bin.TicketV;

import java.net.ServerSocket;
import java.net.Socket;

public class TGS {

    private int port=3333;
    private ServerSocket server;
    private Socket socket;
    public TGS() throws Exception
    {
        init();
    }
    private void init() throws Exception {
        server = new ServerSocket(port);
        System.out.println("TGS.TGS socket is start, port is: " + port);
        while(true) {
            socket = server.accept();
            System.out.println(socket.getInetAddress().getHostAddress());
            handle(socket);
        }
    }
    private void handle(Socket socket) throws Exception {
        String key = socket.getInetAddress().getHostAddress()+":"+socket.getPort();
        System.out.println("TGS.TGS accept a socket: " + key);
        Thread thread = new Thread(new TGSThread(socket));//非数据共享线程
        thread.start();//多线程处理来自客户端的请求
    }
    public static void main(String []args) throws Exception
    {
        TGS tgs=new TGS();
    }


    //解析客户端发给TGS的报文
    public static String parseMessage3(String message) throws Exception {
        String lifetime="120";
        String k_cv = "123";//tgs自动生成
        String k_v = "123";//数据库查询
        String k_tgs = "333";//tgs自带
        String tgs_id = "123";//tgs_id
        String temp[];
        //解析原始报文
        temp=message.split(",");
        String type = temp[0];
        String id_v = temp[1];
        String ticket = temp[3];
        String authenticatorC = temp[3];
        //解析票据
        ticket= DES.DES_Decryp(ticket,k_tgs);
        String T[] = ticket.split(",");
        if (type=="7") {
            //初始化票据类
            TicketTGS ticketTGS = new TicketTGS(k_tgs,T[0], T[1], T[2], T[3], T[4], T[5]);
            //解析Authenticator
            authenticatorC= DES.DES_Decryp(authenticatorC,ticketTGS.getKc_tgs());
            String[] A = authenticatorC.split(",");
            //初始化Authenticator类
            Authenticator authenticator = new Authenticator(ticketTGS.getKc_tgs(),A[0], A[1], A[2]);
            //验证client身份
            if (ticketTGS.getId_tgs().equals(tgs_id)&&authenticator.getAd_c().equals(ticketTGS.getAdc())&&authenticator.getId_c().equals(ticketTGS.getIdc())){
                //验证成功
                //生成时间戳
                String data = Handledata.gettime();
                TicketV ticketV = new TicketV(k_cv, k_v, ticketTGS.getIdc(), ticketTGS.getAdc(), id_v, data, lifetime);
                //生成TGS返回给client的报文
                return Message.Message4(id_v, ticketV, ticketTGS.getKc_tgs(), ticketV.getKc_v());
            }
            //失败
            else {
                System.out.println("error!");
                return "error";
            }
        }
        else {
            //返回报文“9”
            System.out.println("error!");
            return "error";
        }
    }

}
