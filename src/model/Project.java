package model;

import java.util.ArrayList;

public class Project {
	private String projectname;
	private String projectinfo;
	private ArrayList<String> plans = new ArrayList<String>();
	
	public Project(String projectname){
		this.projectname=projectname;
		DBhelper db = new DBhelper(projectname,"aaa");
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
		String str="plans";
		for(int i=0;i<this.plans.size();i++){
			str = str + "#" + this.plans.get(i);
		}
		System.out.println(str);
		return str;
	}
}
