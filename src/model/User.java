package model;
public class User {
	private String account;
	private String password;
	public User(String account,String password){
		this.account=account;
		this.password=password;
	}
	public String getAccount(){
		return account;
	}
	public String getPassword(){
		return password;
	}
	public boolean isExist(){
		User user=new User(account,password);
		
		return false;
	}
	public boolean Query(String keyword){
		DBhelper db=new DBhelper();
		return db.Query(keyword);
	}
	
}
