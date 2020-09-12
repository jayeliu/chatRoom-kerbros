package as;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class AS {
	private int port=2222;
	private ServerSocket server;
	private Socket socket;
	public AS() throws Exception
	{
		init();
	}
    private void init() throws Exception {  
        server = new ServerSocket(port);  
        System.out.println("AS socket is start, port is: " + port);  
         while(true) {  
            socket = server.accept();  
            System.out.println(socket.getInetAddress().getHostAddress()+" "+socket.getInetAddress().getHostName());
            handle(socket); 
       }  
    }  
    private void handle(Socket socket) throws Exception {  
        Thread thread = new Thread(new Asthread(socket));//非数据共享线程
        thread.start();//多线程处理来自客户端的请求
       
        
        
    }  
    public static void main(String []args) throws Exception
    {
    	AS as= new AS();
    }
}
