package model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;

import org.bson.Document;

public class Project {
	private static String projectname;
	private static String projectinfo;
	private static String currentplan;
	private static boolean flag=false;
	private static String horiresult;
	private static String project1;
	private static String project2;
	private static String plan1;
	private static String plan2;
	private static String currentlabel;
	private ArrayList<String> plans = new ArrayList<String>();
	private static String displaylabel;
	public Project(String projectname){
		this.projectname=projectname;
		DBhelper db = new DBhelper("AllProject",projectname);
		projectinfo=db.FindAll().get(0).getString("project_description");
		//System.out.println(db.GetCollectionName());
		db=new DBhelper(projectname+"_result");
		this.plans = db.GetCollectionName();
		System.out.println("project:  "+this.plans);
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
	public String gethoriresult(){
		return horiresult;
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
	public void setHoricomp(String project1,String project2,String plan1,String plan2){
		this.project1=project1;
		this.project2=project2;
		this.plan1=plan1;
		this.plan2=plan2;
		
	}
	public void setcurrentlabel(String labelname){
		this.currentlabel=labelname;
	}
	public String getproject1(){
		return project1;
	}
	public String getproject2(){
		return project2;
	}
	public String getplan1(){
		return plan1;
	}
	public String getplan2(){
		return plan2;
	}
	public void setPlans(ArrayList<String> plans) {
		this.plans = plans;
	}
	public void setdisplaylabel(String label){
		this.displaylabel=label;
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
		//[relation_type:Message-Topic@@261&&Product-Producer@@231&&Instrument-Agency@@156&&Entity-Destination@@292&&Cause-Effect@@328&&Component-Whole@@310&&Member-Collection@@233&&other@@456&&Entity-Origin@@258&&Content-Container@@192&&, length:Short@@912&&Long@@902&&Medium@@903&&]
		int labelNum = labelName.size();
		for(int i = 0; i < labelNum; i++){
			result.add(labelName.get(i) + ":" + labelValueArray.get(i));
		}
		return result;
	}
	public String label2json(ArrayList<String> labelstat){
		System.out.println("project_displaylabel:"+this.displaylabel);
		if(this.displaylabel==null){
			this.setdisplaylabel("relation_type");
		}
		String result="[";
		String temp="";
		String[] str=null;
		for(int i=0;i<labelstat.size();i++){
			if(labelstat.get(i).split(":")[0].equals(this.displaylabel)){
				temp=labelstat.get(i).split(":")[1];
				str=temp.split("&&");
				System.out.println("����"+str.length);
				for(int j=0;j<str.length;j++){
					if(!result.equals("[")){
						result+=",";
					}
					result=result+"{label:\""+str[j].split("@@")[0]+"\",value:"+str[j].split("@@")[1]+"}";
				}
				result+="]";
				return result;
			}
		}
		return "error";
	}
	public String getalllabels(ArrayList<String> labelstat){
		String str = null;
		for(int i=0;i<labelstat.size();i++){
			if(str==null){
				str=labelstat.get(i).split(":")[0];
			}
			else{
				str=str+"&&"+labelstat.get(i).split(":")[0];
			}			
		}
		return str;
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
	public boolean deleteText(String id){
		//ɾ��currentplan�£���Ӧid��document
		DBhelper db_result = new DBhelper(projectname + "_result", currentplan);
		boolean result = db_result.DeleteManyEqualDocument(db_result.collection, "id", id);
		return result;
	}
	public boolean updateText(String id, String relation,String prediction,String labels){
		//����currentplan��,��Ӧ��document
		DBhelper db_result = new DBhelper(projectname + "_result", currentplan);
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		Document doc_result = db_result.FindManyEqualDocument(db_result.collection, "id", id).get(0);
		Document doc_data = db_data.FindManyEqualDocument(db_data.collection, "id", id).get(0);
		Document newDocument = new Document();
		newDocument.append("$set", new Document().append("relation", relation));
		db_data.collection.updateOne(doc_data, newDocument);
		
		newDocument = new Document();
		newDocument.append("$set", new Document().append("relation_type", prediction));
		db_result.collection.updateOne(doc_result, newDocument);
		
		ArrayList<String> label_list = new ArrayList<String>(Arrays.asList(labels.split(";")));
		newDocument = new Document();
		newDocument.append("$set", new Document().append("label", label_list));
		db_data.collection.updateOne(doc_data, newDocument);
		
		return true;
	} 
	public String getScores(){
		//����4��ָ�꣬����string������֮����"#####"����
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
				System.out.println(projectname + "��Data���ݿ���Result���ݿ���ڲ�ƥ��");
				return result;
			}
			else if(project_data.size() > 1){
				System.out.println(projectname + "��Result���ݿ������ͬid�����");
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
	
	public String getScores(String projectName, String planName){
		//����4��ָ�꣬����string������֮����"&&&&&"����
		String result=null;
		DBhelper db_data = new DBhelper(projectName + "_data", projectName);
		DBhelper db_result = new DBhelper(projectName + "_result", planName);
		Vector<Document> project_result = db_result.FindAll();
		int tp = 0, tn = 0, fp = 0, fn = 0;
		db_data = new DBhelper(projectName + "_data", projectName);
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
				System.out.println(projectName + "��Data���ݿ���Result���ݿ���ڲ�ƥ��");
				return result;
			}
			else if(project_data.size() > 1){
				System.out.println(projectName + "��Result���ݿ������ͬid�����");
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
	
	public String getText(){
		String info = "[";
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		DBhelper db_result = new DBhelper(projectname + "_result", currentplan);
		Vector<Document> project_result = db_result.FindAll();
		System.out.println(project_result.size());
		db_data = new DBhelper(projectname + "_data", projectname);
		for(Document doc: project_result){
			info += "{";
			String id = doc.getString("id");
			info += "id:\"" + id + "\",";
			Vector<Document> project_data = db_data.FindManyEqualDocument(db_data.collection, "id", id);
			//System.out.println(project_data);
			if(project_data.size() == 0){
				System.out.println(projectname + "��Data���ݿ���Result���ݿ���ڲ�ƥ��");
				return "[]";
			}
			else if(project_data.size() > 1){
				System.out.println(projectname + "��Result���ݿ������ͬid�����");
				return "[]";
			}
			Document doc_data = project_data.get(0);
			String text = doc_data.getString("text");
			info += "text:\"" + text + "\",";
			ArrayList<String> tmp = new ArrayList<String>();
			tmp = doc_data.get("label", tmp);
			String relation_type = tmp.get(0).split(":")[1];
			info += "relation_type:\"" + relation_type + "\",";
			String prediction = doc.getString("relation_type");
			info += "prediction:\"" + prediction + "\",";
			String comment = doc_data.getString("comment");
			if(comment==null){
				comment = "";
			}
			info += "comment:\"" + comment + "\",";
			info += "label:\"";
			for(String label: tmp){
				info += label + ";";
			}
			info += "\"},";
		}
		int length_of_info = info.length();
		if(info.charAt(length_of_info - 1) == ','){
			info = info.substring(0, length_of_info - 1);
		}
		info += "]";
		return info;
		
	}
	
//******************************
//����project1��plan1��project2��plan2�Ķ�Ӧ��5��ֵ�����ҽ�this.horiresult���ó�
//	��ʽ(���Ի��С��ո�,���޸�����)��
/*	[{
        y: 'Accuracy',
        a: 1,
        b: 0.9
    }, {
        y: 'Recall',
        a: 0.6,
        b: 0.65
    }, {
        y: 'Precision',
        a: 0.5,
        b: 0.4
    }, {
        y: 'F-Score',
        a: 0.75,
        b: 0.65
    }, {
        y: 'Micro-Score',
        a: 0.50,
        b: 0.40
    }]*/

//project1, project2,plan1,plan2��Ϊ��̬������ֱ���þ�����
	public boolean horicomp(){
		String result = "";
		String[] plan1Result = this.getScores(project1, plan1).split("&&&&&");
		String[] plan2Result = this.getScores(project2, plan2).split("&&&&&");
		System.out.println("Get");
		if(plan1Result.length != 5) return false;
		if(plan2Result.length != 5) return false;
		result = "[{y: 'Accuracy',a: " + plan1Result[0] + ",b: " + plan2Result[0] + "}, "
				+ "{y: 'Recall',a: " + plan1Result[1] + ",b: " + plan2Result[1] + "}, " 
				+ "{y: 'Precision',a: " + plan1Result[2] + ",b: " + plan2Result[2] + "}, "
				+ "{y: 'F-Score',a: " + plan1Result[3] + ",b: " + plan2Result[3] + "}, "
				+ "{y: 'Micro-Score',a: " + plan1Result[4] + ",b: " + plan2Result[4] + "}]";
		this.horiresult = result;
		return true;
	}
//****************************************
//����֮ǰ��getText����ȡthis.project1��plan1��this.project2��plan2�е��ı���id��relation��prediction1��prediction2
//��ʽ����֮ǰ��[{text:"aa",id:"001",relation_type:"sddf",prediction1:"11",predicton2:"22"},{},{}...]	
	public String getHoritext(){
		String str = null;
		
		return str;
	}
	
	
	public String dataManage(){
		String info = "[";
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		Vector<Document> project_data = db_data.FindAll();
		System.out.println(project_data.size());
		for(Document doc: project_data){
			info += "{";
			String id = doc.getString("id");
			info += "id:\"" + id + "\",";
			String text = doc.getString("text");
			info += "text:\"" + text + "\",";
			String relation = doc.getString("relation");
			info += "relation:\"" + relation + "\",";			
			ArrayList<String> tmp = new ArrayList<String>();
			tmp = doc.get("label", tmp);
			String relation_type = tmp.get(0).split(":")[1];
			info += "relation_type:\"" + relation_type + "\",";
			String comment = doc.getString("comment");
			if(comment==null){
				comment = "";
			}
			info += "comment:\"" + comment + "\",";
			info += "label:\"";
			for(String label: tmp){
				info += label + ";";
			}
			info += "\"},";
		}
		int length_of_info = info.length();
		if(info.charAt(length_of_info - 1) == ','){
			info = info.substring(0, length_of_info - 1);
		}
		info += "]";
		return info;
		
	}
	
	
	public String verticalData(){
		String info = "[";
		DBhelper db_plan1 = new DBhelper(projectname + "_result", plan1);
		Vector<Document> plan1_data = db_plan1.FindAll();
		
		DBhelper db_plan2 = new DBhelper(projectname + "_result", plan2);
		Vector<Document> plan2_data = db_plan2.FindAll();
		
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		Vector<Document> project_data = db_data.FindAll();
		
		db_plan2 = new DBhelper(projectname + "_result", plan2);
		for(Document doc: plan1_data){
			String id = doc.getString("id");
			Vector<Document> plan2_data_tmp = db_plan2.FindManyEqualDocument(db_plan2.collection, "id", id);
			//System.out.println(project_data);
			if(plan2_data_tmp.size() == 0){
				System.out.println(projectname + "��" + plan1 + "���ݿ���" + plan2 + "���ݿ���ڲ�ƥ��");
				return "[]";
			}
			else if(plan2_data_tmp.size() > 1){
				System.out.println(projectname + "��" + plan1 + "��" + plan2 + "���ݿ������ͬid�����");
				return "[]";
			}
		}
		
		for(Document doc: plan1_data){
			info += "{";
			String id = doc.getString("id");
			info += "id:\"" + id + "\",";
			
			Document doc_data = new Document();
			Document doc_plan2 = new Document();
			for(Document doc_data_element: project_data){
				if(doc_data_element.getString("id").equals(id)){
					doc_data = doc_data_element;
					break;
				}
			}
			for(Document doc_plan2_element: plan2_data){
				if(doc_plan2_element.getString("id").equals(id)){
					doc_plan2 = doc_plan2_element;
					break;
				}
			}
			String relation = doc_data.getString("relation");
			info += "relation:\"" + relation + "\",";
			relation = doc.getString("relation");
			info += "relation1:\"" + relation + "\",";
			relation = doc_plan2.getString("relation");
			info += "relation2:\"" + relation + "\",";
			
			ArrayList<String> tmp = new ArrayList<String>();
			tmp = doc_data.get("label", tmp);
			String relation_type = tmp.get(0).split(":")[1];
			info += "relation_type:\"" + relation_type + "\",";
			relation_type = doc.getString("relation_type");
			info += "relation_type1:\"" + relation_type + "\",";
			relation_type = doc_plan2.getString("relation_type");
			info += "relation_type2:\"" + relation_type + "\"";
			
			info += "},";
		}
		int length_of_info = info.length();
		if(info.charAt(length_of_info - 1) == ','){
			info = info.substring(0, length_of_info - 1);
		}
		info += "]";
		return info;
	}
//************************************
//�㼶��ϵ��Labels(database);projectname(��ǰproject��collection)��{"label"��"Length","value0":"Long","value1":"Short"}(document)	
//�ж�this.projectname�Ƿ��Ѿ���Labels���ݿ��д�������¼
//������ֱ�Ӷ�ȡLabels���ݿ�����Ϊprojectname��collection�µ�document����������label������(&����)
//��û�У������project_data���ݿ⣬��Labels�д���projectname��collection���Ұ�����label���Ӧȡֵ���document	
	public String labelmanage(){
		String labelsStr = "";
		String databaseName = "Labels";
		DBhelper db_label = new DBhelper(databaseName);
		if(db_label.GetCollectionName().indexOf(projectname) == -1){
			db_label = new DBhelper(databaseName, projectname);
			ArrayList<String> labelStat = getLabelStat();
			db_label = new DBhelper(databaseName, projectname);
			Vector<Document> vd = new Vector<Document>();
			for(String element: labelStat){
				Document d = new Document();
				String name = element.split(":")[0];
				labelsStr += name + "&";
				d.append("label", name);
				String[] valueWithSizeList = element.split(":")[1].split("&&");
				int index = 0;
				for(String valueWithSize: valueWithSizeList){
					String value = valueWithSize.split("@@")[0];
					d.append("value" + String.valueOf(index), value);
					index += 1;
				}
				db_label.InsertOneDocument(db_label.collection, d);
			}
			
		}
		else{
			db_label = new DBhelper("Labels", projectname);
			Vector<Document> labelVector = db_label.FindAll();
			for(Document labelInfo: labelVector){
				labelsStr += labelInfo.getString("label") + "&";
			}
		}
		return labelsStr.substring(0,labelsStr.length()-1);
	}
//***********************************
//���Labels���ݿ��У�projectname��collection�У�labelname��Ӧ��document�����ҷ�������ȡֵ(����labelname&����)	
	public String getlabelvalue(String labelname){
		String labelWithValueStr = "";
		DBhelper db_label = new DBhelper("Labels", projectname);
		Vector<Document> labelVector = db_label.FindAll();
		for(Document labelInfo: labelVector){
			if(labelInfo.getString("label").equals(labelname)){
				for(String key: labelInfo.keySet()){
					if(key.equals("_id") == false){
						labelWithValueStr += labelInfo.getString(key) + "&";
					}
				}
			}
		}
		return labelWithValueStr.substring(0, labelWithValueStr.length()-1);
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
			//if(this.plans.get(i).equals("info") || this.plans.get(i).equals("total"))	continue;
			str = str + "#" + this.plans.get(i);
		}
		System.out.println("project.changeplantostring:"+str);
		return str;
	}
	//get all plans from a selected project
	public String getAllplans(String selectedproject){	
		DBhelper db=new DBhelper(selectedproject+"_result");
		ArrayList<String> p=db.GetCollectionName();
		String str=p.get(0);
		for(int i=1;i<p.size();i++){
			str=str+"&"+p.get(i);
		}		
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
		System.out.println("getdes:"+str);
		return str;
	}
	///!!!!!!!!!!!!!!!!!!ע�ⲻ��Ҫ�޸�_data���ݿ⣬��Ҫ�޸�Labels���ݿ�
	//��ǰproject�е�����µ�label
	public void addlabel(String labelname){
		DBhelper db_labels = new DBhelper("Labels", projectname);
		Document d = new Document();
		d.append("label", labelname);
		db_labels.InsertOneDocument(db_labels.collection, d);
		
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		
		Vector<Document> docs = db_data.FindAll();
		Document newDocument = new Document();
		for(Document doc_data: docs){
			newDocument.append("$push", new Document().append("label", labelname + ":null"));
			db_data.collection.updateOne(doc_data, newDocument);
		}
	}
	//ɾ��label
	public void deletelabel(String labelname){
		DBhelper db_labels = new DBhelper("Labels", projectname);
		db_labels.DeleteManyEqualDocument(db_labels.collection, "label", labelname);
		
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		
		Vector<Document> docs = db_data.FindAll();
		Document newDocument = new Document();
		for(Document doc_data: docs){
			String id = doc_data.getString("id");
			ArrayList<String> labels = (ArrayList<String>) doc_data.get("label");
			for(String label: labels){
				if(label.split(":")[0].equals(labelname)){
					labels.remove(label);
					break;
				}
			} 
			newDocument.append("$set", new Document().append("label", labels));
			Document searchQuery = new Document().append("id", id);  
			db_data.collection.updateOne(searchQuery, newDocument);
		}
	}
	//����labelֵ,��ǰ��labelΪthis.currentlabel
	public void addlabelvalue(String value){
		DBhelper db_labels = new DBhelper("Labels", projectname);
		Document currentLabel = db_labels.FindManyEqualDocument(db_labels.collection, "label", currentlabel).get(0);
		Document newDocument = new Document();
		newDocument.append("$set", new Document().append("value" + String.valueOf(currentLabel.keySet().size() - 2), value));
		db_labels.collection.updateOne(currentLabel, newDocument);
		
	}
	//ɾ��labelֵ
	public void deletelabelvalue(String value){
		DBhelper db_labels = new DBhelper("Labels", projectname);
		Document currentLabel = db_labels.FindManyEqualDocument(db_labels.collection, "label", currentlabel).get(0);
		db_labels.DeleteManyEqualDocument(db_labels.collection, "label", currentlabel);
		Document newLabelValueList = new Document();
		int index = 0;
		for(String key: currentLabel.keySet()){
			if(key.equals("_id")) continue;
			if(key.equals("label")){
				newLabelValueList.append(key, currentLabel.getString(key));
				continue;
			}
			String labelValue = currentLabel.getString(key);
			if(labelValue.equals(value)){
				continue;
			}
			else{
				newLabelValueList.append(value + String.valueOf(index), labelValue);
				index += 1;
			}
		}
		db_labels.InsertOneDocument(db_labels.collection, newLabelValueList);
		
		DBhelper db_data = new DBhelper(projectname + "_data", projectname);
		Vector<Document> docs = db_data.FindAll();
		Document newDocument = new Document();
		for(Document doc_data: docs){
			String id = doc_data.getString("id");
			ArrayList<String> labels = (ArrayList<String>) doc_data.get("label");
			for(String label: labels){
				if(label.split(":")[0].equals(currentlabel) && label.split(":")[1].equals(value)){
					labels.remove(label);
					labels.add(currentlabel + ":null");
					break;
				}
			}
			newDocument.append("$set", new Document().append("label", labels));
			Document searchQuery = new Document().append("id", id);  
			db_data.collection.updateOne(searchQuery, newDocument);
		}
	}
}
