package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;

import org.bson.Document;

public class Project {
	private static String projectname;
	private static String projectinfo;
	private static String currentplan;
	private static boolean flag=false;
	private ArrayList<String> plans = new ArrayList<String>();
	
	public Project(String projectname){
		this.projectname=projectname;
		DBhelper db = new DBhelper("AllProject",projectname);
		projectinfo=db.FindAll().get(0).getString("project_description");
		//System.out.println(db.GetCollectionName());
		db=new DBhelper(projectname+"_result");
		this.plans = db.GetCollectionName();
	}
	public Project(){
		
	}
	public boolean getFlag(){
		return flag;
	}
	public void isAddproject(){
		flag=true;
	}
	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getProjectinfo() {
		return projectinfo;
	}

	public void setProjectinfo(String projectinfo) {
		this.projectinfo = projectinfo;
	}

	public ArrayList<String> getPlans() {
		return plans;
	}

	public void setPlans(ArrayList<String> plans) {
		this.plans = plans;
	}
	//add plan
	public boolean addPlan(String planname){
		if(this.plans.add(planname)){
			return true;
		}
		return false;
	}
	//delete plan
	public boolean deletePlan(String planname){
		if(this.plans.remove(planname)){
			return true;
		}
		return false;
	}
	//change plans into a string separated by "#"
	public String changePlanstoString(){
		String str="info";
		for(int i=0;i<this.plans.size();i++){
			//modified by rhys
			if(this.plans.get(i).equals("info") || this.plans.get(i).equals("total"))	continue;
			str = str + "#" + this.plans.get(i);
		}
		System.out.println(str);
		return str;
	}
	//get all document from the given collection
	public String getdata(String collection){
		DBhelper db = new DBhelper(projectname,collection);
		Vector<Document> vd = db.FindAll();
		String str="[";
		for(int i=0;i<vd.size();i++){
			if (i!=vd.size()-1){
				str = str + vd.get(i).toJson()+",";
			}
			else{
				str = str + vd.get(i).toJson();
			}
		}
		str+="]";
		System.out.println(str);
		return str;
	}
	public String getdes(){
		DBhelper db = new DBhelper("AllProject",projectname);
		Vector<Document> vd = db.FindAll();
		String str="[";
		for(int i=0;i<vd.size();i++){
			if (i!=vd.size()-1){
				str = str + vd.get(i).toJson()+",";
			}
			else{
				str = str + vd.get(i).toJson();
			}
		}
		str+="]";
		System.out.println(str);
		return str;
	}
}
