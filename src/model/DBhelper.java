package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class DBhelper {
	
	//public static void main(String args[]){
		//DBhelper db = new DBhelper("test","myFirst");
		//Vector<Document> r = Json2Document("testdata.json");
		
		//for(int i = 0;i< r.size();i++){
		//	Document2Json(r.get(i));
		//}
		
		//Document2Json(r.get(0));
		//Json2Document(Document2Json(r.get(0)));
	//}
	
	protected static MongoDatabase database = null;
	protected static MongoCollection<Document> collection = null;
	
	//构造函数
	public DBhelper(String databaseName,String collectionName){
		database = DatabaseConnect(databaseName);
  		collection = CollectionConnect(collectionName, database);
	}
	
	//JSON转Vector<Document>
	public static Vector<Document> Json2Document(String jsonName){
		try {
			Vector<Document> res = new Vector<Document>();
			JsonParser parser = new JsonParser();
			JsonObject object = (JsonObject) parser.parse(new FileReader(jsonName));
			JsonArray ja = object.get("data").getAsJsonArray();
			int size = ja.size();
			
			for(int i = 0;i < size;i++){
				JsonObject ob = ja.get(i).getAsJsonObject();
				String[] keys = new String[ob.keySet().size()];
				ob.keySet().toArray(keys);
				Document document = new Document();
				for(int i1 = 0;i1 < keys.length;i1++){
					document.append(keys[i1], ob.get(keys[i1]).toString());
				}
				res.add(document);
			}
			return res;	
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;	
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
		for(int i = 0;i < vDocument.size();i++){
			collection.insertOne(vDocument.get(i));
		}
		System.out.println("Multiple documents insertion succeed");
		return true;
	}
	
	//打印该数据库集合中的所有文档
	public Vector<Document> FindAll(MongoCollection<Document> collection){
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
		System.out.println("Find all documents with "+ attribute + " = " + value);
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
        System.out.println("Find all documents with "+ attribute + " != " + value);
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
