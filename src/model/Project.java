package model;

import java.util.ArrayList;
import java.util.Vector;

import org.bson.Document;

public class Project {
	private String projectname;
	private String projectinfo;
	private ArrayList<String> plans = new ArrayList<String>();
	
	public Project(String projectname, Boolean isDefault){
		this.projectname=projectname;
		if (isDefault) return;
		DBhelper db = new DBhelper(projectname,"info");
		//System.out.println(db.GetCollectionName());
		this.plans = db.GetCollectionName();
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
}
