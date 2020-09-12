package tgs;

import ChatUI.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Server {
    static int max=30;
    int port=6666;
    private ServerSocket serverSocket;
    private ServerThread serverThread;

    private ArrayList<ClientThread> clients;
    public static void main(String[]args) throws BindException {
        Server server=new Server();
        server.ServerStart();

    }
    public void ServerStart() throws BindException {
        System.out.println("聊天服务启动");
        try {
            clients = new ArrayList<ClientThread>();
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

                // 接收客户端的基本用户信息
                String inf = reader.readLine();
                StringTokenizer st = new StringTokenizer(inf, "@");
                user = new User(st.nextToken(), st.nextToken());
                // 反馈连接成功信息
                writer.println(user.getName() + user.getIp() + "与服务器连接成功!");
                writer.flush();
                // 反馈当前在线用户信息

                if (clients.size() > 0) {
                    String temp = "";
                    for (int i = clients.size() - 1; i >= 0; i--) {
                        temp += (clients.get(i).getUser().getName() + "/" + clients
                                .get(i).getUser().getIp())
                                + "@";
                    }
                    writer.println("USERLIST@" + clients.size() + "@" + temp);
                    writer.flush();
                }

                // 向所有在线用户发送该用户上线命令
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println(
                            "ADD@" + user.getName() + user.getIp());
                    clients.get(i).getWriter().flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        @SuppressWarnings("deprecation")
        public void run() {// 不断接收客户端的消息，进行处理。
            String message = null;
            while (true) {
                try {
                    message = reader.readLine();// 接收客户端消息
                    if (message.equals("CLOSE"))// 下线命令
                    {
                        // 断开连接释放资源

                        reader.close();
                        writer.close();
                        socket.close();

                        // 向所有在线用户发送该用户的下线命令
                        for (int i = clients.size() - 1; i >= 0; i--) {
                            clients.get(i).getWriter().println(
                                    "DELETE@" + user.getName());
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
                        dispatcherMessage(message);// 转发消息
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        // 转发消息

        public void dispatcherMessage(String message) {
            StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
            String source = stringTokenizer.nextToken();
            String owner = stringTokenizer.nextToken();
            String content = stringTokenizer.nextToken();
            message = source + " ：" + content;
            if (owner.equals("ALL")) {// 群发
                for (int i = clients.size() - 1; i >= 0; i--) {
                    clients.get(i).getWriter().println(message);
                    clients.get(i).getWriter().flush();
                }
            }
        }

    }
}
