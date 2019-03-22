package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.mongodb.util.JSON;

import model.DBhelper.*;


@SuppressWarnings("deprecation")
public class FileDao {
	private DBhelper db=new DBhelper("test","MyFirst");
	MyFile my_file=null;
	List<MyFile> files=null;
	public void UpFile(String filename, MyFile my_file, boolean flag){
		System.out.println("UpFile!!!!!!!!");
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
        
        Gson gson = new Gson();
		java.lang.reflect.Type type = new TypeToken<JsonBean>(){}.getType();
		System.out.println(text);
		JsonBean jsonBean = gson.fromJson(text, type);
//		System.out.println(text);
		jsonBean.file = filename;
		Vector<Document> vd = DBhelper.Json2Document(jsonBean,flag);

		//创建新的project 上传数据集
		ProjectManager pm=new ProjectManager();
		System.out.println("uploadservlet:"+pm.getProject());
		if(flag){
			DBhelper dbh = new DBhelper(pm.getProject()+"_data", filename);
			dbh.InsertManyDocument(dbh.collection, vd);
		}
		else{
			DBhelper dbh = new DBhelper(pm.getProject()+"_result", filename);
			dbh.InsertManyDocument(dbh.collection, vd);
		}
		System.out.println("loading...");
	}
	
}
