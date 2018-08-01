package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
		//Relation re=new Relation();
		String fileName=my_file.getFilePath()+"\\"+my_file.getFileName();
		System.out.println(fileName);
		File file=new File(fileName);
		Long filelength=file.length();
		String text=null;
		String encoding = "UTF-8"; 
		byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            text= new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
        }  
		
		db.InsertManyDocument(text);
		System.out.println("loading...");
	}
	
}
