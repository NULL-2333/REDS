package model;
import org.bson.Document;

public class User {
	private String account;
	private String password;
	private MyFile[] MyFiles;
	
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
		System.out.println("call User.isExit");
		User user=new User(account,password);
		DBhelper db=new DBhelper("UserInfo","User");
		//db.FindManyEqualDocument("User", attribute, value);
		return false;
	}
	//查询登陆信息
	public boolean Query(String account,String password){
		DBhelper db=new DBhelper("UserInfo","User");	
		Document document = new Document("account",account).append("password",password);
		System.out.println("printing document...");
		System.out.println("hhhhhhh");
		System.out.println(document);
		System.out.println("finished");
		try {
			//db.InsertOneDocument(db.collection, document);
			System.out.println("query:");
			return db.FindCertainDocument(document);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}
	//在数据库中创建对应的用户
	public boolean Create(){		
		DBhelper db=new DBhelper("UserInfo","User");
		Document document = new Document("account",account).append("password",password);
		try {
			db.InsertOneDocument(db.collection, document);
			return true;
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			return false;
		}
	}
}
