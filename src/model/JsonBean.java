package model;

import java.util.ArrayList;
import java.util.List;
//
public class JsonBean{
	public String file;
	public String description;
	public List<Data> data;
	
	public JsonBean(){
		file = "";
		description = "";
		data = new ArrayList<Data>();
	}
	public static class Data{
		public String relation_type;
		public String id;
		public String text;
		public String relation;
		public String comment;
		public Label label;
	}
	
	public static class Label{
		public String relation_type;
	}
	
}