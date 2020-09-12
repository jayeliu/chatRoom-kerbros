package tgs;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Handledata {
	public static int getlenght(String lenght)//将有效数据包长度从二进制字符转化为整形
	{
	   int len=0;
	   String str=lenght;
	   int temp;
	   for(int i=0;i<8;i++)
	   {
		   temp=str.charAt(7-i)-48;
		   len=(int) (len+temp*Math.pow(2,i));
	   }
	   return len;
	}
	public static String getbina(int lenght)//将十进制转化为八位二进制
	{
		int temp=lenght;
		String com="0";
		String fina="";
		String trans= Integer.toBinaryString(temp);
		for(int i=0;i<8-trans.length();i++)
		{
			fina+=com;
		}
		fina+=trans;
		return fina;
	}
	public  static String random8()//随机生成八位临时秘钥
	{
		String str = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
		return str;
	}
	public static String gettime()
	{
	    Date day=new Date();    
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String data=df.format(day);   
		return data;
	}
	public static Connection conmyASsql()//连接AS数据库
	{
	    String driver = "com.mysql.jdbc.Driver";//驱动程序名  
	    String url = "jdbc:mysql://127.0.0.1:3306/as_key";    // URL指向要访问的数据库名Test  
	    String user = "root";// MySQL配置时的用户名  
	    String password = "wyy19970410";// Java连接MySQL配置时的密码  
		Connection conn=null;  
	    try {         // 加载驱动程序  
	    Class.forName(driver);  
	    conn = DriverManager.getConnection(url, user, password);//连接数据库
	    } 
	    catch(Exception e){  
	        System.out.println("Succeeded connecting Not to the Database!");  
	        e.printStackTrace();
	    } 
	    return conn;
	}
	public static Connection conmyVsql()//连接服务器数据库
	{
	    String driver = "com.mysql.jdbc.Driver";//驱动程序名  
	    String url = "jdbc:mysql://127.0.0.1:3306/v_database";    // URL指向要访问的数据库名Test  
	    String user = "root";// MySQL配置时的用户名  
	    String password = "wyy19970410";// Java连接MySQL配置时的密码  
		Connection conn=null;  
	    try {         // 加载驱动程序  
	    Class.forName(driver);  
	    conn = DriverManager.getConnection(url, user, password);//连接数据库
	    } 
	    catch(Exception e){  
	        System.out.println("Succeeded connecting Not to the Database!");  
	        e.printStackTrace();
	    } 
	    return conn;
	}
	public static void main(String []arges)
	{
		String str="10000011";
		int test=7;
		System.out.println(getlenght(str));
		System.out.println(getbina(test));
	}
	public static String Get_Idc(String idv){
		String driver = "com.mysql.cj.jdbc.Driver";
		String url = "jdbc:MySQL://192.168.43.183:3306/central_database?useSSL=false&serverTimezone=UTC";
		String username = "myuser";
		String password = "123456";
		String id="";
		try{
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url,username,password);
			Statement statement =  conn.createStatement();
			String sql = "select * from application WHERE id = '"+idv+"';";

			System.out.println("######################\n"+sql);
			//获取数据
			ResultSet rs = statement.executeQuery(sql);
			while(rs.next()){
				id=rs.getString("key");
			}
			//关闭连接
			rs.close();
			conn.close();
		}catch(ClassNotFoundException e){
			System.out.println("数据库驱动未找到");
			e.printStackTrace();
		}catch(SQLException e){
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		return id;
	}
}
