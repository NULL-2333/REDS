package model;

import java.util.ArrayList;

//���ڴ�Ŵ����ݿ���ȡ���Ĺ�ϵ��ȡ�����ʵ��
public class Relation {
	private String sentence;//���
	private String entity1;//ʵ���ϵ1
	private String entity2;//ʵ���ϵ2
	private ArrayList<String> labels;//��ǩ
	private String note;//��ע
	//��ӱ�ǩ
	public boolean AddLabel(String label){
		labels.add(label);
		return true;
	}
	//ɾ����ǩ
	public boolean RemoveLabel(String label){
		labels.remove(label);
		return true;
	}
	//���±�ע
	public boolean UpdateNote(String note){
		return true;
	}
	public Relation(String relation){
		//�����ݿ��ж����ĵ������ת����string֮��
		//��ΪRelation�Ĳ��������г�ʼ��
		//��String���л��֣�Ϊ��صı�����ֵ
		
	}

}
