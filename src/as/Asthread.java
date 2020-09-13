package as;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import DES.DES;
import RSA.RSA;

public class Asthread implements Runnable{
	String regist="1";//注册包首部
	String login="4";//登录认证包首部
	String tgs_key="hellotgs";//AS和TGS之间的对称秘钥
	String tgsid="df19c0e0-be96-40bf-ac3a-9d007c120452";
	private Socket socket;
	public  Asthread(Socket socket)
	{
		this.socket=socket;
	}
	@Override
	public void run() {
		final Lock lock = new ReentrantLock();//同一时间只能有一个线程占用数据库修改权限
		try {
			ObjectOutputStream write = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream read = new ObjectInputStream(socket.getInputStream());
			String data=(String)read.readObject();//AS从客户端接收到的数据包

			System.out.println("c->AS:"+data);
			String[] result=data.split(",");
			String type=result[0];//截取数据包类型
		    /*String lenght=data.substring(5, 13);//截取有效数据包长度字符串
		    int len=Handledata.getlenght(lenght);*/
			ResultSet rs=null;
			Connection conn=null;
			//String info=data.substring(13);//截取有效数据段
			if(type.equals(regist))//AS注册处理
			{
				String pack= RSA.Decryp(result[1]);
				result=pack.split(",");
				String user=result[0];//user id
				String IDTGS=result[1];//截取TGS id
				String TS1=result[2];//时间戳
				String user_id=user.split("&")[0];
				String user_password=user.split("&")[1];
				lock.lock();
				conn=Handledata.conmyASsql();
				String sql="select account  from user where account=?;";//用户的ID是根据注册顺序得来的第一个注册就位001
				PreparedStatement pStmt = (PreparedStatement) conn.prepareStatement(sql);
				pStmt.setString(1, user_id);
				rs=pStmt.executeQuery();
				if(rs.next()) {
					String fail_code="3";
					write.writeObject(fail_code);
					return;
				}
				sql="select count(account)  from user;";//用户的ID是根据注册顺序得来的第一个注册就位001
				pStmt = (PreparedStatement) conn.prepareStatement(sql);
				rs=pStmt.executeQuery();
				rs.next();
				int id=Integer.parseInt(rs.getString(1))+1;//计算出待注册用户在数据库的ID
				sql="insert into User values(?,?,?,0);";
				pStmt = (PreparedStatement) conn.prepareStatement(sql);
				pStmt.setString(1, String.valueOf(id));
				pStmt.setString(2, user_id);
				pStmt.setString(3, user_password);
				pStmt.executeUpdate();//插入用户信息，注册成功
				conn.close();
				lock.unlock();//数据库操作完成释放权限
				String sucback="2";
				System.out.println("user:"+user+id+" regist successfully");
				write.writeObject(sucback);//注册成功返回序列号
				socket.close();//操作完成关闭socket
			}
			//返回带有TGS票据的包   其中访问TGS的票据用的是AS和TGS之间的对称钥加密的，由于本次系统只设置了一个TGS所以提取
			if(type.equals(login))//AS登录处理
			{

				String user=result[1];//user id
				String IDTGS=result[2];//截取TGS id
				String TS1=result[3];//时间戳
				System.out.println("login user:"+user);
				lock.lock();
				conn=Handledata.conmyASsql();//连接数据库
				String sql="select password from User where account=?;";//用户的ID是根据注册顺序得来的第一个注册就位001
				PreparedStatement pStmt = (PreparedStatement) conn.prepareStatement(sql);
				pStmt.setString(1, user);
				rs=pStmt.executeQuery();
				lock.unlock();
				if(!rs.next())
				{
					//发送用户名不存在失败包
					String fail="6";//用户不存在数据包
					write.writeObject(fail);//发送失败码
				}
				else
				{
					String ckey=rs.getString(1);//获取对应的对称秘钥
					lock.lock();
					conn.close();
					String ctgskey=Handledata.random8();//随机生成八位临时tgs和C之间的对称秘钥
					System.out.println("C,TGS之间临时秘钥:"+ctgskey);
					String curtime=Handledata.gettime();//当前时间
					String lifetime="600";
					String ADC=socket.getInetAddress().getHostAddress();//客户端IP  修改
					System.out.println("客户端IP"+ADC);
					String bftgs=ctgskey+user+ADC+tgsid+curtime+lifetime;
					TicketTGS ticket=new TicketTGS(tgs_key,ctgskey,user,ADC,tgsid,curtime,lifetime);
					System.out.println("TGS票据明文："+bftgs);
					String tgsticket=ticket.toString();//TGS票据
					System.out.println("TGS票据："+tgsticket);
					String mess=String.join(",",ctgskey,tgsid,curtime,lifetime,tgsticket);//返回信息
					System.out.println("AS发送给C的数据:"+mess);
					String message;
					try {
						message = DES.DES(mess,ckey);
						System.out.println("AS发送给C的加密数据:"+message);
						write.writeObject("5,"+message);//向客户端发送TGS票据包
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}//加密后要发送的有效信息

					socket.close();
				}
			}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
