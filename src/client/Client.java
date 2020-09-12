package client;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import DES.DES;
//import MD5.MD5;
//import RSA.RSA;
import com.sun.org.apache.bcel.internal.generic.LRETURN;


public class Client{
    //发送自己的信息，请求与TGS通信
    static String MACid="19315214";//设定物理机ID
    static String asip="192.168.43.183";//AS服务器固定IP
    static String tgsip="192.168.43.81";//TGS服务器固定IP
    static String vip="192.168.43.44";//V服务器固定IP
    static int asport=2222;
    static int tgsport=3333;
    static  int tovport=4444;//连接服务器，服务器接收端口
    static  int fromvport=5555;//服务器连接 客户端接收端口
    static String asfail="00100";//用户不存在包首部
    static String tgsticket="00011";//TGS票据包首部
    static String vticket="00110";//V票据包首部
    static String vconfirm="01011";//服务器返回的同步认证包
    static String vfail="10001";//服务器返回的认证失败包
    static String tgsid="11111111";
    static String clientid="001";//注册序列号
    static String pukey="79&518940563";//公钥
    static String prkey="282436519&518940563";//私钥  这是已有的

    /*
    // 客户端发给应用服务器显示
    public static void sendpukey()//发送物理机公钥包
    {
        String sendinfo="01111"+"00000100"+MACid+pukey;//发送给V服务器的物理机序列号和公钥
        Socket socket=null;
        try {
            socket = new Socket(vip,tovport);
            ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            write.writeObject(sendinfo);//发送
            System.out.println(((String)read.readObject()).substring(13));//反馈结果
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            try {
                if (socket!=null) {
                    //Thread.sleep(10000);
                    socket.close();
                }//关闭SOCKET
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }
    */

    // 客户端从返回报文中提取出报文类型并核实消息类型
    public static String validateType(String msg){
        // 报文分割的数组
        String[] data = msg.split(",");
        // 报文的类型
        String code = data[0];
        String error; //错误信息用于客户端回显
        switch(code){
            case "3":
                error = "注册错误";
                break;
            case "6":
                error = "账号或密码有误";
                break;
            case "9":
                error = "TGS返回错误";
                break;
            case "12":
                error = "应用服务器错误";
                break;
            default:
                error = "";
                break;
        }
        return error;
    }

    // 客户端提取出报文中的时间戳进行验证
    public static boolean validateTimeStamp(String timestamp, String lifttime){
        long curTimeStamp = System.currentTimeMillis();
        long start = Long.valueOf(timestamp);
        long end = Long.valueOf(timestamp) + Long.valueOf(lifttime);
        if(curTimeStamp > start && curTimeStamp < end){
            System.out.println("当前报文时间戳满足要求"); // 日志
            return true;
        }else{
            System.out.println("当前报文时间戳不满足要求!"); // 日志
            return false;
        }
    }

    // 客户端向AS请求注册的密码哈希函数，登陆时不调用该函数，不传密码
    public static String gethash(String str) throws NoSuchAlgorithmException
    {
        String MD5=str;
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5 = md.digest(MD5.getBytes());
        StringBuffer sb = new StringBuffer();
        String part = null;
        for (int i = 0; i < md5.length; i++) {
            part = Integer.toHexString(md5[i] & 0xFF);
            if (part.length() == 1) {
                part = "0" + part;
            }
            sb.append(part);
        }
        String result=sb.append(part).toString();//MD5密文字符串
        return result;
    }


    // 客户端向AS注册
    public boolean registsendToAS (String account,String password) throws NoSuchAlgorithmException{//注册方法
        // 客户端形成给AS的注册报文
        String type = "1";
        String hashPwd = gethash(password);
        String loginMsg = account + "&" + hashPwd;
        String id = "-1";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String[] array = {type, loginMsg, id, timestamp};
        String send = String.join(",",array);
        // 发送
        System.out.println("注册报文:" + send); // 日志
        Socket socket = null;
        try {
            socket = new Socket(asip,asport);
            ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            //System.out.println("send "+send);
            write.writeObject(send);
            String recv = ((String) read.readObject()); // 收到的报文string
            String error = validateType(recv);
            if ( ! error.isEmpty())
            {
                System.out.println(error);
                return false;
            }
            return true;
        }
        catch (Exception e) {
            System.out.println(e);
        }
        finally {
            try {
                if (socket!=null) {
                    //Thread.sleep(10000);
                    socket.close();
                }//注册成功后关闭SOCKET
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return false;
    }

    // 客户端向AS登陆
    public static String[] LogintoAS(String account,String password) throws NoSuchAlgorithmException//登录AS的方法 返回key(C,tgs)和票据或者用户失败码
    {
        String hashPwd = gethash(password);
        // 客户端形成给AS的登陆报文
        String type = "4";
        String cid = account;
        String ts1 = String.valueOf(System.currentTimeMillis());
        String[] array = {type, cid, tgsid, ts1};
        String send = String.join(",",array);
        /*
        String []retu=new String[4];//返回的字符串数组,若AS认证成功则将客户端和TGS之间的临时秘钥和票据以及票据明文长度放入数组作为返回值
        String curtime=Handledata.gettime();
        String mess=ClientID+tgsid+curtime;
        String lengh=Handledata.getbina(mess.length());
        String ctoas="00001"+lengh+mess;
        String ckey=MD5.gethash(password).substring(0, 8);//得到对称加密秘钥
         */
        Socket socket=null;
        String[] retu = new String[3]; // 返回数据
        try {
            socket = new Socket(asip,asport);
            ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            write.writeObject(send);
            System.out.println("客户端->AS(type,idc,idtgs,ts1):" + send);
            String recv = (String) read.readObject(); // 收到完整报文
            System.out.println("AS->客户端(type,ekcStr):" + recv);
            // 对报文类型进行验证
            String error = validateType(recv); // 该报文类型
            if ( ! error.isEmpty())
            {
                System.out.println(error);
                return null;
            }
            // 解密报文
            String cipher = recv.split(",")[1];
            String decrypt = DES.DES_Decryp(cipher,hashPwd);
            String[] arr = decrypt.split(",");
            System.out.println("AS->客户端 ekcStr(ctgskey,idtgs,ts2,lifetime2,tickettgs):" + arr[0] +"," + arr[1] + "," + arr[2] + "," + arr[3] + "," + arr[4]);


            String TGSticket = arr[4];//票据
            String idc = account;
            String ctgskey = arr[0];//临时秘钥

            retu[0] = ctgskey;
            retu[1] = TGSticket;
            retu[2] = idc;
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket!=null) {
                    //Thread.sleep(10000);
                    socket.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retu;
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

    // 客户与TGS通信
    public static String[] logintotgs(String clientid,String ctgskey,String TGSticket) throws Exception
    {
        String idv="b784cc31-d7b9-4d31-bb68-430da87a49c8";//服务器ID
        //String adc=InetAddress.getLocalHost().getHostAddress();//服务器在异地 得到本地网络地址
        String adc = InetAddress.getLocalHost().getHostAddress();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String[] authArr = {clientid, adc, timestamp};
        String auth = String.join(",",authArr);
        String authen = DES.DES(auth,ctgskey);
        String[] msgArr = {"7", idv, TGSticket, authen};
        String send = String.join(",",msgArr);
        System.out.println("客户端->TGS(type,idv,tickettgs,authenticator):" + send);
        System.out.println("客户端->TGS authenticator(idc,adc,ts3):" + auth);

        Socket socket=null;
        String[] retu = new String[3];
        try {
            socket = new Socket(tgsip,tgsport);
            ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            write.writeObject(send);//发送给TGS
            String backtgs=(String) read.readObject();//接收返回的数据包
            System.out.println("TGS->客户端(type,ekctgsStr):" + backtgs);

            String[] part = backtgs.split(",");
            String error = validateType(part[0]);
            if ( ! error.isEmpty()){
                System.out.println(error);
                retu[0] = error;
                return retu;
            }
            String decrypt = DES.DES_Decryp(part[1], ctgskey);
            String[] arr = decrypt.split(",");
            System.out.println("TGS->客户端 ekctgsStr(kcv,idv,ts4,ticketv):" + arr[0] + "," + arr[1] + "," + arr[2] + "," + arr[3]);
            //形成auth
            String adc2 = InetAddress.getLocalHost().getHostAddress();
            String timestamp2 = String.valueOf(System.currentTimeMillis());
            String[] authcArr = { clientid, adc2, timestamp2};
            String authc = String.join(",", authcArr);
            String authEn = DES.DES(authc,ctgskey);
            retu[0] = clientid; //用户
            retu[1] = arr[0]; // kcv
            retu[2] = arr[3]; // 访问服务器的票据
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                if (socket!=null) {
                    //Thread.sleep(10000);
                    socket.close();//关闭Socket
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retu;
    }

    // 二进制字符串转十六进制字符串
    public static String binaryString2hexString(String bString) {
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)
            return null;
        StringBuffer tmp=new StringBuffer();
        int iTmp = 0;
        for (int i = 0; i < bString.length(); i += 4) {
            iTmp = 0;
            for (int j = 0; j < 4; j++) {
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);
            }
            tmp.append(Integer.toHexString(iTmp));
        }
        return tmp.toString();
    }
    // 客户端与V通信
    public static String logintov(String clientid, String cvkey, String Vticket) throws Exception//返回成功认证识别码然后进入聊天线程
    {
        //形成auth
        String adc = InetAddress.getLocalHost().getHostAddress();
        String timestamp = String.valueOf(System.currentTimeMillis());
        String[] authcArr = { clientid, adc, timestamp};
        String authc = String.join(",", authcArr);
        String authEn = DES.DES(authc,cvkey);
        String[] arr = {"10", Vticket, authEn};
        String send = String.join(",", arr);
        System.out.println("客户端->V(type,ticketv,authenticator):" + send);
        System.out.println("客户端->V authenticator(idc,adc,ts5):" + clientid + "," + adc + "," + timestamp);

        Socket socket = null;
        String retu = "";
        try {
            socket = new Socket(vip,tovport);
            ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
            write.writeUTF(send);//发送信息
            write.flush();
            //String sendback=(String)read.readObject();//接收反馈消息
            String sendback=(String)read.readUTF();//接收反馈消息
            System.out.println("V->客户端(type,ekcStr):" + sendback);

            String[] part = sendback.split(",");
            String error = validateType(part[0]);
            if ( ! error.isEmpty()){
                System.out.println(error);
                retu = error;
                return retu;
            }
            Long tsAdd = Long.valueOf(DES.DES_Decryp(part[1], cvkey));
            System.out.println("V->客户端 ekcStr(ts5+1):" + tsAdd);
            if( ! tsAdd.equals(Long.valueOf(timestamp) + 1 ))//返回的认证成功同步认证包
            {
                retu = "时间戳认证失败";
                return retu;
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                if (socket!=null) {
                    //Thread.sleep(10000);
                    socket.close();//关闭Socket
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return retu;
    }

    /*
    public static String getdigsign(String idc)//获取数字签名
    {
        String sign="";
        byte[]en=idc.getBytes();
        sign=new String(Base64.getEncoder().encodeToString(RSA.encrypt(prkey, en)));
        return sign;
    }
     */
}