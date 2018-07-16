package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;


import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class DBhelper {
	
	public static void main(String args[]){
		DBhelper db = new DBhelper("test","myFirst");
		//Document document = new Document("category", "it").  
		//         append("languages", "c#").  
		//         append("id", 1).  
		//         append("pop", true);
		String[] attrList={"category","languages","id","pop"};
		Document document = new Document(db.Json2Document("test.json", attrList, 4));
		db.InsertOneDocument(collection, document);
		//db.DeleteCollection(collection);
		//db.DeleteDatabase(database);
		
	}
	protected static MongoDatabase database = null;
	protected static MongoCollection<Document> collection = null;
	//���캯��
	public DBhelper(String databaseName,String collectionName){
		database = DatabaseConnect(databaseName);
  		collection = CollectionConnect(collectionName, database);
	}
	
	//JSONתDocument
	public Document Json2Document(String jsonName, String[] attrList, int attrNum){
		try {
			
			JsonParser parser = new JsonParser();
			JsonObject object;
			object = (JsonObject) parser.parse(new FileReader(jsonName));
			Document document = new Document();
			for(int i = 0;i < attrNum;i++){
				document.append(attrList[i], object.get(attrList[i]).getAsString());
			}
			return document;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
		
		
	}
	//���ݿ�����
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
	
	//���ݿ���ѡ��
	public MongoCollection<Document> CollectionConnect(String name, MongoDatabase mongoDatabase){
		MongoCollection<Document> collection = mongoDatabase.getCollection(name);
		System.out.println("Connect to collection successfully!");
		return collection;
	}

	//�����ݿ���в��뵥���ĵ�
	public void InsertOneDocument(MongoCollection<Document> mongoCollection, Document document){
		mongoCollection.insertOne(document);  
		System.out.println("Single document insertion succeed");
	}
	
	//�����ݿ���в������ĵ�
	public void InsertManyDocument(MongoCollection<Document> mongoCollection, List<Document> documents){ 
		mongoCollection.insertMany(documents);  
		System.out.println("Multiple documents insertion succeed");
	}
	
	//��ӡ�����ݿ⼯���е������ĵ�
	public void FindAll(MongoCollection<Document> collection){
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator(); 
        System.out.println("------------------------------------------------");
        System.out.println("All documents:");
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        }
        System.out.println("------------------------------------------------");
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ�����ȣ�
	public void FindManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
        System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " = " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
        System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ�����ȣ�
	public void DeleteManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Delete all documents with "+ attribute + " = " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ȣ�
	public void FindManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " != " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ȣ�
	public void DeleteManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Delete all documents with "+ attribute + " != " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ڣ�
	public void FindManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " > " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ڣ�
	public void DeleteManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Delete all documents with "+ attribute + " > " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ���С�ڣ�
	public void FindManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " < " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ���С�ڣ�
	public void DeleteManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Delete all documents with "+ attribute + " < " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ڵ��ڣ�
	public void FindManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " >= " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ڵ��ڣ�
	public void DeleteManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Delete all documents with "+ attribute + " >= " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ���С�ڵ��ڣ�
	public void FindManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Find all documents with "+ attribute + " <= " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ���С�ڵ��ڣ�
	public void DeleteManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
		System.out.println("------------------------------------------------");
        System.out.println("Delete all documents with "+ attribute + " <= " + value + ":");
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
		System.out.println("------------------------------------------------");
	}
	
	//ɾ�����ݿ⼯��
	public void DeleteCollection(MongoCollection<Document> collection){
		collection.drop();
		System.out.println("Collection: " + collection.getNamespace()+" has been deleted!");
	}
	
	//ɾ�����ݿ�
	public void DeleteDatabase(MongoDatabase database){
		database.drop();
		System.out.println("Database: " + database.getName()+" has been deleted!");
	}
	
	
	
}
