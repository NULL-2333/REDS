package model;

import java.util.ArrayList;

//���ڴ�Ŵ����ݿ���ȡ���Ĺ�ϵ��ȡ�����ʵ��
public class Relation {
	private String id;//�����
	private String sentence;//���
	private String relation;//������ϵ
	private String entity1;//ʵ��1
	private String entity2;//ʵ��2
	private String predict_relation;//Ԥ���ϵ
	private String predict_entity1;//Ԥ��ʵ��1
	private String predict_entity2;//Ԥ��ʵ��2
	private String labels;//��ǩ
	private String note;//��ע
	//��ӱ�ǩ
	public boolean AddLabel(String label){
		int index=this.labels.indexOf(label);
		//�Ѵ���
		if(index!=-1){
			return false;
		}
		if(this.labels.isEmpty()){
			this.labels=label;
		}
		this.labels=this.labels+","+label;
		return true;
	}
	//ɾ����ǩ
	public boolean RemoveLabel(String label){
		//�����ڸñ�ǩ
		int index=this.labels.indexOf(label);
		if(index==-1){
			return false;
		}
		String sub=","+label+"|"+label+",";
		this.labels=this.labels.replaceAll(sub, "");
		return true;
	}
	//���±�ע
	public boolean UpdateNote(String note){
		this.note=note;
		return true;
	}
	public Relation(String relation){
		//�����ݿ��ж����ĵ������ת����string֮��relationΪjson��ʽ
		//��ΪRelation�Ĳ��������г�ʼ��
		//��String���л��֣�Ϊ��صı�����ֵ
		
	}
	public Relation() {
		// TODO �Զ����ɵĹ��캯�����
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
