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
	
	//���캯��
	public DBhelper(String databaseName,String collectionName){
		database = DatabaseConnect(databaseName);
  		collection = CollectionConnect(collectionName, database);
	}
	
	//���ع��캯��
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
	
	//JSONתVector<Document>
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
	//Vector<Document>תJsonBean
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
		
	//JsonObjectתDocument
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
	//DocumentתJsonObject
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
	
	
	//���ݿ�����
	@SuppressWarnings("resource")
	public MongoDatabase DatabaseConnect(String databaseName) {
		// TODO Auto-generated method stub
		try{
			//���ӵ� mongodb ����
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	        //���ӵ����ݿ�
	        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
	        System.out.println("Connect to database successfully!");
	        return mongoDatabase;
		}catch(Exception e){
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
		return null;
		
	}
	
	//��ȡ���ݿ�����б�����-----by jc
	public ArrayList<String> GetCollectionName(){
		ArrayList<String> plans=new ArrayList<String>();
		MongoIterable<String> iterator = this.database.listCollectionNames();
		MongoCursor<String> cursor = iterator.iterator();
		while(cursor.hasNext()){
			plans.add(cursor.next());
		}
		return plans;
	}
	
	//���ݿ���ѡ��
	public MongoCollection<Document> CollectionConnect(String name, MongoDatabase mongoDatabase){
		MongoCollection<Document> collection = mongoDatabase.getCollection(name);
		System.out.println("Connect to collection successfully!");
		return collection;
	}

	//�����ݿ���в��뵥���ĵ�
	public boolean InsertOneDocument(MongoCollection<Document> mongoCollection, Document document){
		mongoCollection.insertOne(document);  
		System.out.println("Single document insertion succeed");
		return true;
	}
	
	//�����ݿ���в������ĵ�
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
	
	//�����ݿ���и����ĵ���ǩ
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
		
	//��ӡ�����ݿ⼯���е������ĵ�
	public Vector<Document> FindAll(){
		Vector<Document> res = new Vector<Document>();
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator(); 
        while(mongoCursor.hasNext()){  
        	res.add(mongoCursor.next());
        }
        return res;
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ�����ȣ�
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
             System.out.println("����������:" + doc.toJson());
             cursor.close();
             return true;
		 }
		 cursor.close();
		return false;
	}
	//�����½�projectʱ����allproject���ݿ����document
	//��ʽΪ��document��Ϊproject�����ں�project_description��date
	//by jc
	public boolean AddProject(String date, String description){
		Document d=new Document();
		d.append("project_description", description);
		d.append("creation_date", date);
		if(this.InsertOneDocument(this.collection, d))	return true;
		else return false;
	}
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ�����ȣ�
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
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ȣ�
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
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ȣ�
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
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ڣ�
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
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ڣ�
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
	
	//�������ݿ⼯��������ĳһ�����������ĵ���С�ڣ�
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
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ���С�ڣ�
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
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ڵ��ڣ�
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
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ڵ��ڣ�
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
	
	//�������ݿ⼯��������ĳһ�����������ĵ���С�ڵ��ڣ�
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
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ���С�ڵ��ڣ�
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
	
	//ɾ�����ݿ⼯��
	public boolean DeleteCollection(MongoCollection<Document> collection){
		collection.drop();
		System.out.println("Collection: " + collection.getNamespace()+" has been deleted!");
		return true;
	}
	
	//ɾ�����ݿ�
	public boolean DeleteDatabase(MongoDatabase database){
		database.drop();
		System.out.println("Database: " + database.getName()+" has been deleted!");
		return true;
	}
	
}
