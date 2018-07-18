package model;

import java.util.List;

public class FileDao {
	private DBhelper db=new DBhelper("test","MyFirst");
	MyFile my_file=null;
	List<MyFile> files=null;
	public void UpFile(MyFile my_file){
		//调用dbhelper中的上传
	}
	
}
