package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectManager {
	private static String currentproject;
	private static String currentplan;
	private static boolean flag;
	public ProjectManager(){
		DBhelper db=new DBhelper("Allproject");
	}
	public String getProject(){
		return this.currentproject;
	}
	public String getPlan(){
		return this.currentplan;
	}
	public void setProject(String projectname){
		currentproject=projectname;
	}
	public void setPlan(String plan){
		currentplan=plan;
	}
	public boolean addProject(String projectname,String description){
		DBhelper db=new DBhelper("AllProject",projectname);
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String time=format.format(date);
		System.out.println(time);
		if(db.AddProject(time, description)){
			db=new DBhelper(projectname+"_data");
			db=new DBhelper(projectname+"_result","default");
			System.out.println("add project true");
			this.currentproject=projectname;
			return true;
		}
		System.out.println("add project false");
		return false;
	}
	public void deleteProject(String projectname){
		System.out.println("project_delete:"+projectname);
		DBhelper db=new DBhelper("AllProject",projectname);
		db.DeleteCollection(db.collection);
		db=new DBhelper(projectname+"_data");
		db.database.drop();
		System.out.println("deleted!!!");
		db=new DBhelper(projectname+"_result");
		db.DeleteDatabase(db.database);
	}
	
}
