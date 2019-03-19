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
	
	public void setCurrentplan(String plan){
		this.currentplan=plan;
	}
	
	public String getCurrentplan(){
		return currentplan;
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
	public String getScores(){
		//计算4个指标，返回string，数据之间用"#####"隔开
		String result=null;
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		DBhelper db_result = new DBhelper(projectname + "_result", currentplan);
		Vector<Document> project_result = db_result.FindAll();
		int tp = 0, tn = 0, fp = 0, fn = 0;
		db_data = new DBhelper(projectname + "_data", projectname);
		for(Document doc: project_result) {
			String id = doc.getString("id");
			/*System.out.println(id);*/
			String tmpString = doc.getString("relation_type");
			//System.out.println(tmpString);
			String relation_type_result = tmpString;
			String relation_result = doc.getString("relation");
			//System.out.println(db_data.collection.getNamespace());
//			System.out.println(db_result.collection.getNamespace());
			Vector<Document> project_data = db_data.FindManyEqualDocument(db_data.collection, "id", id);
			//System.out.println(project_data);
			if(project_data.size() == 0){
				System.out.println(projectname + "的Data数据库于Result数据库存在不匹配");
				return result;
			}
			else if(project_data.size() > 1){
				System.out.println(projectname + "的Result数据库存在相同id的情况");
				return result;
			}
			Document doc_data = project_data.get(0);
			ArrayList<String> tmp = new ArrayList<String>();
			tmp = doc_data.get("label", tmp);
/*			System.out.println(doc_data);
			System.out.println(tmp);*/
			String relation_type_data = tmp.get(0).split(":")[1];
			
			String relation_data = doc_data.getString("relation");
/*			System.out.println(relation_type_data);
			System.out.println(relation_type_result);
			System.out.println(relation_data);
			System.out.println(relation_result);*/
			if(relation_type_data.equalsIgnoreCase("other") && relation_type_result.equalsIgnoreCase("other")){
				tp += 1;
				continue;
			}
			if(relation_type_data.equalsIgnoreCase("other") && !relation_type_result.equalsIgnoreCase("other")){
				fn += 1;
				continue;
			}
			if(!relation_type_data.equalsIgnoreCase("other") && relation_type_result.equalsIgnoreCase("other")){
				fn += 1;
				continue;
			}
			if(relation_data.equalsIgnoreCase(relation_result)){
				if(relation_type_data.equalsIgnoreCase(relation_type_result)){
					tp += 1;
				}
				else{
					fp += 1;
					
				}
			}
			else{
				if(relation_type_data.equalsIgnoreCase(relation_type_result)){
					tn += 1;
					
				}
				else{
					fn += 1;
					
				}
			}
		}
		//accuracy
		double accuracy = (double)(tp + tn)/(double)(tp + fn + fp + tn);
		//precision
		double precision = (double)tp /(double)(tp + fp);
		//recall
		double recall = (double)tp/(double)(tp + fn);
		//F1
		double f1 = 2 * (double)tp/(double)(2 * tp + fp + fn);
		//micro average
		double micro_average = (double)(tp + fp)/(double)(tp + fn + fp + tn);
		result = String.format("%.2f",accuracy) + "&&&&&" 
				+ String.format("%.2f",precision) + "&&&&&" 
				+ String.format("%.2f",recall) + "&&&&&"
				+ String.format("%.2f",f1) + "&&&&&"
				+ String.format("%.2f",micro_average);
		return result;
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
	//delete current plan
	public boolean deleteCurrentplan(){
		DBhelper db=new DBhelper(projectname+"_result",currentplan);
		if(db.DeleteCollection(db.collection))	return true;
		else 
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
