package v.fake;

import ChatUI.Client;
import ChatUI.User;
import DES.DES;

import java.io.*;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    static int max=30;
    int port=4444;
    String k_v = "hellov";
    private ServerSocket serverSocket;
    private ServerThread serverThread;

    private CopyOnWriteArrayList<ClientThread> clients;
    public static void main(String[]args) throws BindException {
        Server server=new Server();
        server.ServerStart();

    }
    public void ServerStart() throws BindException {
        System.out.println("聊天服务启动");
        try {
            clients = new CopyOnWriteArrayList<>();
            serverSocket = new ServerSocket(port);
            serverThread = new ServerThread(serverSocket);
            serverThread.start();
        } catch (BindException e) {
            throw new BindException("端口号已被占用，请换一个！");
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new BindException("启动服务器异常！");
        }
    }
    // 关闭服务器

    @SuppressWarnings("deprecation")
    public void CloseServer() {
        try {
            if (serverThread != null)
                serverThread.stop();// 停止服务器线程

            for (int i = clients.size() - 1; i >= 0; i--) {
                // 给所有在线用户发送关闭命令
                clients.get(i).getWriter().println("CLOSE");
                clients.get(i).getWriter().flush();
                // 释放资源
                clients.get(i).stop();// 停止此条为客户端服务的线程
                clients.get(i).reader.close();
                clients.get(i).writer.close();
                clients.get(i).socket.close();
                clients.remove(i);
            }

            if (serverSocket != null) {
                serverSocket.close();// 关闭服务器端连接
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
// 服务器线程

    class ServerThread extends Thread {
        private ServerSocket serverSocket;
        private int max=30;// 人数上限
        // 服务器线程的构造方法

        public ServerThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;

        }
        public void run() {
            while (true) {// 不停的等待客户端的链接
                try {

                    Socket socket = serverSocket.accept();
                    //handle(socket);
                    if (clients.size() == max) {// 如果已达人数上限
                        BufferedReader r = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        PrintWriter w = new PrintWriter(socket
                                .getOutputStream());
                        // 接收客户端的基本用户信息

                        String inf = r.readLine();
                        StringTokenizer st = new StringTokenizer(inf, "@");
                        User user = new User(st.nextToken(), st.nextToken());
                        // 反馈连接成功信息
                        w.println("MAX@服务器：对不起，" + user.getName() +" "+ user.getIp() + "，服务器在线人数已达上限，请稍后尝试连接！");
                        w.flush();
                        // 释放资源
                        r.close();
                        w.close();
                        socket.close();
                        continue;
                    }

                    ClientThread client = new ClientThread(socket);
                    client.start();// 开启对此客户端服务的线程
                    clients.add(client);
                    System.out.println("当前用户数量："+clients.size());

                } catch (IOException e) {
                    e.printStackTrace();
                    CloseServer();
                }
            }

        }
    }



// 为一个客户端服务的线程

    class ClientThread extends Thread {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private User user;
        private String k_c_v;
        public BufferedReader getReader() {
            return reader;
        }

        public PrintWriter getWriter() {
            return writer;
        }

        public User getUser() {
            return user;

        }

        // 客户端线程的构造方法

        ClientThread(Socket socket) {
            try {
                this.socket = socket;
                reader = new BufferedReader(new InputStreamReader(socket
                        .getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
                //kerbros
                String data = reader.readLine();
                String[] group = data.split(",");
                if (group[0].equals("10")) {
                    String ticket_v = group[1];
                    String auth_c = group[2];
                    String[] tvs =null;
                    try {
                        //!暂时固定

                        tvs = DES.DES_Decryp(ticket_v,k_v).split(",");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] auths =null;
                    k_c_v = tvs[0];
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
                    for (ClientThread client:clients){
                        if(client.getUser().getName().equals(auths[0])){
                            writer.write("12\n");
                            writer.flush();
                        }
                    }
                    long tsp=Long.parseLong(auths[2])+1;
                    String data1=String.join(",","11",DES.DES(String.valueOf(tsp),k_c_v))+"\n";
                    System.out.println(data1);
                    writer.write(data1);
                    writer.flush();
                }else {
                    writer.write("12\n");
                    writer.flush();
                }
                // 接收客户端的基本用户信息
                String inf = reader.readLine();
                inf = DES.DES_Decryp(inf, k_c_v);
                System.out.println("inf:"+inf);
                StringTokenizer st = new StringTokenizer(inf, "@");
                user = new User(st.nextToken(), st.nextToken());
                // 反馈连接成功信息
                writer.println(DES.DES(user.getName() + user.getIp() + "与服务器连接成功!",k_c_v));
                writer.flush();
                // 反馈当前在线用户信息

                if (clients.size() > 0) {
                    String temp = "";
                    for (int i = clients.size() - 1; i >= 0; i--) {
                        temp += (clients.get(i).getUser().getName() + "/" + clients
                                .get(i).getUser().getIp())
                                + "@";
                    }
                    writer.println(DES.DES("USERLIST@" + clients.size() + "@" + temp,k_c_v));
                    writer.flush();
                }

                // 向所有在线用户发送该用户上线命令
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println(DES.DES(
                            "ADD@" + user.getName() + user.getIp(),clients.get(i).k_c_v));
                    clients.get(i).getWriter().flush();
                }
            } catch (IOException | NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }



        @SuppressWarnings("deprecation")
        public void run() {// 不断接收客户端的消息，进行处理。
            String message = null;
            while (true) {
                try {
                    message = reader.readLine();// 接收客户端消息
                    message = DES.DES_Decryp(message, k_c_v);
                    System.out.println("message:"+message);
                    if (message.equals("CLOSE"))// 下线命令
                    {
                        // 断开连接释放资源

                        reader.close();
                        writer.close();
                        socket.close();

                        // 向所有在线用户发送该用户的下线命令
                        for (int i = clients.size() - 1; i >= 0; i--) {
                            clients.get(i).getWriter().println(DES.DES(
                                    "DELETE@" + user.getName(),clients.get(i).k_c_v));
                            clients.get(i).getWriter().flush();
                        }


                        // 删除此条客户端服务线程

                        for (int i = clients.size() - 1; i >= 0; i--) {
                            if (clients.get(i).getUser() == user) {
                                ClientThread temp = clients.get(i);
                                clients.remove(i);// 删除此用户的服务线程
                                System.out.println("当前用户数量："+clients.size());
                                temp.stop();// 停止这条服务线程
                                return;
                            }
                        }
                    } else {
                        StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
                        String source = stringTokenizer.nextToken();
                        String owner = stringTokenizer.nextToken();
                        if(owner.equals("Private")) {
                            try {
                                dispatcherMessaeg_private( message);
                            } catch (NoSuchAlgorithmException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }else {
                            try {
                                dispatcherMessage(message);
                            } catch (NoSuchAlgorithmException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        }
        //单聊
        public void dispatcherMessaeg_private(String message) throws NoSuchAlgorithmException {
            StringTokenizer stringTokenizer = new StringTokenizer(message, "@");

            String source = stringTokenizer.nextToken();

            String owner = stringTokenizer.nextToken();

            String friend = stringTokenizer.nextToken();

            String content=stringTokenizer.nextToken();

            message = source + "说：" + content+"("+source+"@你)";
            for (int i = clients.size() - 1; i >= 0; i--) {

                if (clients.get(i).getUser().getName().equals(friend)) {

                    clients.get(i).getWriter().println(DES.DES(message, clients.get(i).k_c_v));

                    clients.get(i).getWriter().flush();

                    return;

                }
            }

        }
        // 转发消息

        public void dispatcherMessage(String message) throws NoSuchAlgorithmException {
            StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
            String source = stringTokenizer.nextToken();
            String owner = stringTokenizer.nextToken();
            String content = stringTokenizer.nextToken();
            message = source + " 说：" + content;
            if (owner.equals("ALL")) {// 群发
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println(DES.DES(message,clients.get(i).k_c_v));
                    clients.get(i).getWriter().flush();
                }
            }
        }

    }
}
