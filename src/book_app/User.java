package book_app;

public class User {

	private String userid;
	private String userpw;
	private String username;
	
	public User(String userid, String userpw, String username) {
		this.userid=userid;
		this.userpw = userpw;
		this.username = username;
	}
	
	public User(String userid) {
		this.userid=userid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserpw() {
		return userpw;
	}

	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
