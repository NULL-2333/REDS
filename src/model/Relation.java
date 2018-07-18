package model;

import java.util.ArrayList;

//用于存放从数据库中取出的关系抽取结果的实例
public class Relation {
	private String sentence;//语句
	private String entity1;//实体关系1
	private String entity2;//实体关系2
	private ArrayList<String> labels;//标签
	private String note;//备注
	//添加标签
	public boolean AddLabel(String label){
		labels.add(label);
		return true;
	}
	//删除标签
	public boolean RemoveLabel(String label){
		labels.remove(label);
		return true;
	}
	//更新备注
	public boolean UpdateNote(String note){
		return true;
	}
	public Relation(String relation){
		//将数据库中读出的单条语句转化成string之后
		//作为Relation的参数，进行初始化
		//对String进行划分，为相关的变量赋值
		
	}

}
