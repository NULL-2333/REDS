package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.mongodb.util.JSON;

@SuppressWarnings("deprecation")
public class FileDao {
	private DBhelper db=new DBhelper("test","MyFirst");
	MyFile my_file=null;
	List<MyFile> files=null;
	public void UpFile(MyFile my_file){
		//调用dbhelper中的上传
		Relation re=new Relation();
		String fileName=my_file.getFilePath()+"/"+my_file.getFileName();
		File file=new File(fileName);
		StringBuilder buffer=new StringBuilder();
		Scanner scanner=null;
		try {
			scanner=new Scanner(file);
			while(scanner.hasNextLine()){
				buffer.append(scanner.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}finally{
			if(scanner!=null){
				scanner.close();
			}
		}
		JsonObject object=
	}
	
}
