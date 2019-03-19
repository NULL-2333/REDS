package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;

import model.JsonBean.*;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


public class DBhelper {
	
	/*public static void main(String args[]) throws IOException{
		
//		DBhelper db = new DBhelper("projectA_data", "projectA");
//		String id = "8001";
//		Vector<Document> test = db.FindManyEqualDocument(db.collection, "id", id);
//		for(Document doc: test){
//			ArrayList<String> tmp = new ArrayList<String>();
//			tmp = doc.get("label", tmp);
//			System.out.println(tmp.get(0));
//		}
//		System.out.println(test);
		int tp = 1554, fp = 0, tn = 1127, fn = 2;
		double a = (double)(tp + tn)/(double)(tp + fn + fp + tn);
		System.out.println(a);
		System.out.println(String.format("%.2f",a));
	}*/
	
	public static MongoDatabase database = null;
	public static MongoCollection<Document> collection = null;
	
	//构造函数
	public DBhelper(String databaseName,String collectionName){
		database = DatabaseConnect(databaseName);
  		collection = CollectionConnect(collectionName, database);
	}
	
	//重载构造函数
	public DBhelper(String databaseName){
		database = DatabaseConnect(databaseName);
	}
		
	//getDatabase----by rhys
	public MongoDatabase getDatabase(){
		return database;
	}
	
	//getCellection
	public MongoCollection<Document> getCollection(){
		return collection;
	}
	
	//setDatabase
	public void setDatabase(MongoDatabase database_){
		database = database_;
	}
		
	//setCellection
	public void setCollection(MongoCollection<Document> collection_){
		collection = collection_;
	}
	
	//JSON转Vector<Document>
	public static Vector<Document> Json2Document(JsonBean jsonBean,boolean flag){
		try {
//			File file = new File(jsonName);
//			FileReader fileReader = new FileReader(file);
//			BufferedReader bReader = new BufferedReader(fileReader);
//			StringBuilder sb = new StringBuilder();
//			String s = "";
//			while((s = bReader.readLine()) != null){
//				sb.append(s + "\n");
//			}
//			bReader.close();
//			String jsonContent = sb.toString();
//			
//			Gson gson = new Gson();
//			java.lang.reflect.Type type = new TypeToken<JsonBean>(){}.getType();
//			JsonBean jsonBean = gson.fromJson(jsonContent, type);
			Vector<Document> res = new Vector<Document>();
			int len = jsonBean.data.size();
			if(flag){
				for(int i = 0; i < len; i++){
					Document d = new Document();
					d.append("file", jsonBean.file);
					d.append("description", jsonBean.description);
					d.append("id", jsonBean.data.get(i).id);
					d.append("text", jsonBean.data.get(i).text);
					d.append("relation", jsonBean.data.get(i).relation);
					d.append("comment", jsonBean.data.get(i).comment);
					//String label = "";//jsonBean.data.get(i).label
					List<String> label = new ArrayList<String>();
					label.add("relation_type:" + jsonBean.data.get(i).label.relation_type);
					d.append("label", label);
					res.add(d);
				}
			}
			else{
				for(int i = 0; i < len; i++){
					Document d = new Document();
					d.append("file", jsonBean.file);
					d.append("id", jsonBean.data.get(i).id);
					d.append("relation", jsonBean.data.get(i).relation);
					d.append("relation_type", jsonBean.data.get(i).relation_type);
					res.add(d);
				}
			}
			return res;
			} catch (JsonIOException | JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
	}
	//.....
	//Vector<Document>转JsonBean
	public static JsonBean Document2Json(Vector<Document> vd){
		JsonBean jb = new JsonBean();
		jb.file = vd.get(0).getString("file");
		jb.description = vd.get(0).getString("description");
		jb.data  = new ArrayList<Data>();
		int len = vd.size();
		for(int i = 0; i < len; i++){
			Document d = vd.get(i);
			Data data = new Data();
			data.id = d.getString("id");
			data.text = d.getString("text");
			data.relation = d.getString("relation");
			data.comment = d.getString("comment");
			data.label = (Label) d.get("label");
			jb.data.add(data);
		}
		return jb;
	}
		
	//JsonObject转Document
	public static Document Json2Document(JsonObject jo){
		Document d = new Document();
		d.append("id", jo.get("id").getAsInt());
		d.append("text", jo.get("text").toString());
		d.append("relation_type", jo.get("relation_type").toString());
		d.append("relation", jo.get("relation").toString());
		d.append("comment", jo.get("comment").toString());
		d.append("label", jo.get("label").toString());
		return d;
	}
	//Document转JsonObject
	public static JsonObject Document2Json(Document d){
		JsonObject jo = new JsonObject();		
		jo.addProperty("id", d.getString("id"));
		jo.addProperty("text", d.getString("text").split("\"")[1]);
		jo.addProperty("relation_type", d.getString("relation_type").split("\"")[1]);
		jo.addProperty("relation", d.getString("relation").split("\"")[1]);
		if(d.getString("comment").equals("null"))
			jo.addProperty("comment", d.getString("comment"));
		else{
			jo.addProperty("comment", d.getString("comment").split("\"")[1]);
		}
		if(d.getString("label").equals("null"))
			jo.addProperty("label", d.getString("label"));
		else{
			jo.addProperty("label", d.getString("label").split("\"")[1]);
		}
		//System.out.println(jo);
		return jo;
	}
	
	
	//数据库连接
	@SuppressWarnings("resource")
	public MongoDatabase DatabaseConnect(String databaseName) {
		// TODO Auto-generated method stub
		try{
			//连接到 mongodb 服务
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	        //连接到数据库
	        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
	        System.out.println("Connect to database successfully!");
	        return mongoDatabase;
		}catch(Exception e){
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
		return null;
		
	}
	
	//获取数据库的所有表名字-----by jc
	public ArrayList<String> GetCollectionName(){
		ArrayList<String> plans=new ArrayList<String>();
		MongoIterable<String> iterator = this.database.listCollectionNames();
		MongoCursor<String> cursor = iterator.iterator();
		while(cursor.hasNext()){
			plans.add(cursor.next());
		}
		return plans;
	}
	
	//数据库表的选择
	public MongoCollection<Document> CollectionConnect(String name, MongoDatabase mongoDatabase){
		MongoCollection<Document> collection = mongoDatabase.getCollection(name);
		System.out.println("Connect to collection successfully!");
		return collection;
	}

	//在数据库表中插入单个文档
	public boolean InsertOneDocument(MongoCollection<Document> mongoCollection, Document document){
		mongoCollection.insertOne(document);  
		System.out.println("Single document insertion succeed");
		return true;
	}
	
	//在数据库表中插入多个文档
	public boolean InsertManyDocument(MongoCollection<Document> mongoCollection, Vector<Document> vDocument){ 
		List<Document> ld = new ArrayList<Document>();
		for(int i = 0;i < vDocument.size();i++){
			System.out.println(vDocument.get(i).toString());
//			collection.insertOne(vDocument.get(i));
			ld.add(vDocument.get(i));
		}
		collection.insertMany(ld);
		System.out.println("Multiple documents insertion succeed");
		return true;
	}
	
	//在数据库表中更新文档标签
	public boolean UpdateDocument(MongoCollection<Document> mongoCollection, String labelWithValue, String id){
		String label = labelWithValue.split(":")[0];
		String value = labelWithValue.split(":")[1];
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator(); 
        while(mongoCursor.hasNext()){  
        	Document d = mongoCursor.next();
        	String currentId = d.get("id").toString();
        	if(currentId.equals(id)){
        		collection.updateOne(Filters.eq("id", currentId), new Document("$push", new Document("label",labelWithValue)));
        		//((List<String>)(d.get("label"))).add(labelWithValue);
        	}
        	else{
        		collection.updateMany(Filters.eq("id", currentId), new Document("$push", new Document("label",label + ":null")));
        		//((List<String>)(d.get("label"))).add(label + ":null");
        	}
        	
        	
        	
        }
		System.out.println("Update succeed");
		return true;
	}
		
	//打印该数据库集合中的所有文档
	public Vector<Document> FindAll(){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator(); 
        while(mongoCursor.hasNext()){  
        	res.add(mongoCursor.next());
        }
        return res;
	}
	
	//查找数据库集合中满足某一条件的所有文档（相等）
	public Vector<Document> FindManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
		//System.out.println("Find all documents with "+ attribute + " = " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				res.add(doc);
				}
			});
        return res;
	}
	
	//test for user login
	//find whether there's certain document in database
	//by JC
	public boolean FindCertainDocument(Document document){
		FindIterable<Document> iter = collection.find(document);
		 MongoCursor<Document> cursor = iter.iterator();
		 while(cursor.hasNext()){
			 Document doc = cursor.next();  
             System.out.println("符合条件的:" + doc.toJson());
             cursor.close();
             return true;
		 }
		 cursor.close();
		return false;
	}
	//对于新建project时，向allproject数据库添加document
	//格式为，document名为project名，内含project_description和date
	//by jc
	public boolean AddProject(String date, String description){
		Document d=new Document();
		d.append("project_description", description);
		d.append("creation_date", date);
		if(this.InsertOneDocument(this.collection, d))	return true;
		else return false;
	}
	//删除数据库集合中满足某一条件的所有文档（相等）
	public boolean DeleteManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
        System.out.println("Delete all documents with "+ attribute + " = " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		return true;
	}
	
	//查找数据库集合中满足某一条件的所有文档（不等）
	public Vector<Document> FindManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
        //System.out.println("Find all documents with "+ attribute + " != " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				res.add(doc);
				}
			});
		return res;
	}
	
	//删除数据库集合中满足某一条件的所有文档（不等）
	public boolean DeleteManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
        System.out.println("Delete all documents with "+ attribute + " != " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		return true;
	}
	
	//查找数据库集合中满足某一条件的所有文档（大于）
	public Vector<Document> FindManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
        System.out.println("Find all documents with "+ attribute + " > " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				res.add(doc);
				}
			});
		return res;
	}
	
	//删除数据库集合中满足某一条件的所有文档（大于）
	public boolean DeleteManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
        System.out.println("Delete all documents with "+ attribute + " > " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		return true;
	}
	
	//查找数据库集合中满足某一条件的所有文档（小于）
	public Vector<Document> FindManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
        System.out.println("Find all documents with "+ attribute + " < " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				res.add(doc);
				}
			});
		return res;
	}
	
	//删除数据库集合中满足某一条件的所有文档（小于）
	public boolean DeleteManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
        System.out.println("Delete all documents with "+ attribute + " < " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		return true;
	}
	
	//查找数据库集合中满足某一条件的所有文档（大于等于）
	public Vector<Document> FindManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " >= " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				res.add(doc);
				}
			});
		return res;
	}
	
	//删除数据库集合中满足某一条件的所有文档（大于等于）
	public boolean DeleteManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
        System.out.println("Delete all documents with "+ attribute + " >= " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		return true;
	}
	
	//查找数据库集合中满足某一条件的所有文档（小于等于）
	public Vector<Document> FindManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
        System.out.println("Find all documents with "+ attribute + " <= " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				res.add(doc);
				}
			});
		return res;
	}
	
	//删除数据库集合中满足某一条件的所有文档（小于等于）
	public boolean DeleteManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
        System.out.println("Delete all documents with "+ attribute + " <= " + value);
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		return true;
	}
	
	//删除数据库集合
	public boolean DeleteCollection(MongoCollection<Document> collection){
		collection.drop();
		System.out.println("Collection: " + collection.getNamespace()+" has been deleted!");
		return true;
	}
	
	//删除数据库
	public boolean DeleteDatabase(MongoDatabase database){
		database.drop();
		System.out.println("Database: " + database.getName()+" has been deleted!");
		return true;
	}
	
}
