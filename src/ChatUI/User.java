package ChatUI;


//用户信息类

public class User{
	private String name;
	private String id;

	public String getIp() {
		return ip;
	}

	private String ip;



	public User(String name, String ip) {
		this.name = name;
		this.ip = ip;
	}



	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}



	public String getId() {
		return id;
	}

	public void setIp(String ip) {
		this.id = ip;
	}
}

