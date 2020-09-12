package tgs;

import DES.DES;
import bin.Authenticator;
import bin.TicketTGS;
import bin.TicketV;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class TGSThread implements Runnable{

    String lifetime="4";
    String k_cv = Handledata.random8();//tgs自动生成
    String k_tgs = "hellotgs";//tgs自带
    String tgs_id = "df19c0e0-be96-40bf-ac3a-9d007c120452";//tgs_id
    private Socket socket;
    public  TGSThread(Socket socket)
    {
        this.socket=socket;
    }
    @Override
    public void run() {
        try {
            System.out.println(socket.getInetAddress().getHostAddress());
            //int adclen = socket.getInetAddress().getHostAddress().length();//得到发送方的IP地址的长度
            ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            String message = (String) read.readObject();//TGS从客户端接收到的数据包
            System.out.println(message);
            String temp[];
            //解析原始报文
            temp=message.split(",");
            String type = temp[0];
            String id_v = temp[1];
            String ticket = temp[2];
            String authenticatorC = temp[3];
            //解析票据
            ticket= DES.DES_Decryp(ticket,k_tgs);
            String T[] = ticket.split(",");
            if (type.equals("7")) {
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
                    System.out.println("\n\n\n"+k_cv        );
                    TicketV ticketV = new TicketV(k_cv, "hellov", ticketTGS.getIdc(), ticketTGS.getAdc(), id_v, data, lifetime);
                    //生成TGS返回给client的报文
                    //System.out.println(TGS.Message.Message4(id_v, ticketV, ticketTGS.getKc_tgs(), ticketV.getKc_v()));
                    write.writeObject(Message.Message4(id_v, ticketV, ticketTGS.getKc_tgs(), ticketV.getKc_v()));//发送
                     }
                     else {
                    String wrong = "9" ;
                    write.writeObject(wrong);//发送失败报文
                    System.out.println("身份验证失败!");
                }
            } else {
                String wrong = "9" ;
                write.writeObject(wrong);//发送失败报文
                System.out.println("报文错误!");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
