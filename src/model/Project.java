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
	public ArrayList<String> getLabelStat(){
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> labelArray = new ArrayList<String>();
		ArrayList<String> labelName = new ArrayList<String>();
		ArrayList<String> labelValueArray = new ArrayList<String>();
		labelArray = this.getLabel();
		int size = labelArray.size();
		for(int i = 0; i < size; i++){
			String[] elements = labelArray.get(i).split("##");
			int labelSize = elements.length;			
			for(int j = 1; j < labelSize; j++){
				String name = elements[j].split(":")[0];
				String value = elements[j].split(":")[1];
				int name2Index = labelName.indexOf(name);
				if(name2Index == -1){
					labelName.add(name);
					labelValueArray.add(value + "@@1" + "&&");
				}
				else {
					String currentValueList = labelValueArray.get(name2Index);
					String[] ValueList = currentValueList.split("&&");
					int valueSize = ValueList.length;
					boolean flag = false;
					int place;
					for(place = 0; place < valueSize; place++){
						String valueName = ValueList[place].split("@@")[0];
						if(valueName.equals(value)){
							flag = true;
							break;
						}
					}
					if(flag == false) {
						labelValueArray.set(name2Index, currentValueList + value + "@@1" + "&&");
					}
					else {
						int valueNo = Integer.parseInt(ValueList[place].split("@@")[1]) + 1;
						String current = "";
						for(int k = 0; k < valueSize; k++){
							if(place == k){
								String tmp = ValueList[place].split("@@")[0];
								current += tmp + "@@" + String.valueOf(valueNo) + "&&";
							}else{
								current += ValueList[k] + "&&";
							}
							
						}
						labelValueArray.set(name2Index, current);
					}
				}
			}
		}
		int labelNum = labelName.size();
		for(int i = 0; i < labelNum; i++){
			result.add(labelName.get(i) + ":" + labelValueArray.get(i));
		}
		return result;
	}
	public ArrayList<String> getLabel(){
		ArrayList<String> result = new ArrayList<String>();
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		Vector<Document> project_data = db_data.FindAll();
		for(Document doc: project_data) {
			
			String id = doc.getString("id");
			String single_result = id + "##";
			ArrayList<String> labels = new ArrayList<String>();
			labels = doc.get("label", labels);
			int size = labels.size();
			String label_type = "", value = "";
			for(int i = 0; i < size; i++){
				label_type = labels.get(i).split(":")[0];
				value = labels.get(i).split(":")[1];
				single_result += label_type + ":" + value + "##";
			}
			result.add(single_result);
		}
		return result;
		
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
