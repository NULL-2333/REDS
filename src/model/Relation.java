package model;

import java.util.ArrayList;

//用于存放从数据库中取出的关系抽取结果的实例
public class Relation {
	private String id;//语句编号
	private String sentence;//语句
	private String relation;//所属关系
	private String entity1;//实体1
	private String entity2;//实体2
	private String predict_relation;//预测关系
	private String predict_entity1;//预测实体1
	private String predict_entity2;//预测实体2
	private String labels;//标签
	private String note;//备注
	//添加标签
	public boolean AddLabel(String label){
		int index=this.labels.indexOf(label);
		//已存在
		if(index!=-1){
			return false;
		}
		if(this.labels.isEmpty()){
			this.labels=label;
		}
		this.labels=this.labels+","+label;
		return true;
	}
	//删除标签
	public boolean RemoveLabel(String label){
		//不存在该标签
		int index=this.labels.indexOf(label);
		if(index==-1){
			return false;
		}
		String sub=","+label+"|"+label+",";
		this.labels=this.labels.replaceAll(sub, "");
		return true;
	}
	//更新备注
	public boolean UpdateNote(String note){
		this.note=note;
		return true;
	}
	public Relation(String relation){
		//将数据库中读出的单条语句转化成string之后，relation为json格式
		//作为Relation的参数，进行初始化
		//对String进行划分，为相关的变量赋值
		
	}
	public Relation() {
		// TODO 自动生成的构造函数存根
	}
	public String toString(){
		return id+":"+sentence+":"+relation+":"+entity1+":"+entity2+":"+
				predict_relation+":"+predict_entity1+":"+predict_entity2
				+":"+labels+":"+note;
	}
	public String getId() {
		return id;
	}
	private void setId(String id) {
		this.id = id;
	}
	public String getSentence() {
		return sentence;
	}
	private void setSentence(String sentence) {
		this.sentence = sentence;
	}
	public String getRelation() {
		return relation;
	}
	private void setRelation(String relation) {
		this.relation = relation;
	}
	public String getEntity1() {
		return entity1;
	}
	private void setEntity1(String entity1) {
		this.entity1 = entity1;
	}
	public String getEntity2() {
		return entity2;
	}
	private void setEntity2(String entity2) {
		this.entity2 = entity2;
	}
	public ArrayList<String> getLabels() {
		return labels;
	}
	private void setLabels(ArrayList<String> labels) {
		this.labels = labels;
	}
	public String getNote() {
		return note;
	}
	private void setNote(String note) {
		this.note = note;
	}
	public String getPredict_relation() {
		return predict_relation;
	}
	private void setPredict_relation(String predict_relation) {
		this.predict_relation = predict_relation;
	}
	public String getPredict_entity1() {
		return predict_entity1;
	}
	private void setPredict_entity1(String predict_entity1) {
		this.predict_entity1 = predict_entity1;
	}
	public String getPredict_entity2() {
		return predict_entity2;
	}
	private void setPredict_entity2(String predict_entity2) {
		this.predict_entity2 = predict_entity2;
	}

}
