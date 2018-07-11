package model;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;



public class DBhelper {
	public static void main(String args[]){
		MongoDatabase mongoDatabase = DBconnect("mycol");
		MongoCollection<Document> mongoCollection = CollectionBuild("test",mongoDatabase);
		MongoCollection<Document> mongoCollection2 = CollectionBuild("test2",mongoDatabase);
		Document document = new Document("title", "MongoDB").  
		         append("description", "database").  
		         append("likes", 100).  
		         append("by", "Fly"); 
		InsertOneDocument(mongoCollection,document);
		InsertOneDocument(mongoCollection2,document);
		//FindAll(mongoCollection);
		//FindManyNotEqualDocument(mongoCollection, "by","Fly");
		//DeleteManyEqualDocument(mongoCollection,"by","Fly");
		//FindAll(mongoCollection);
		//FindManyNotEqualDocument(mongoCollection, "by","Fly");
		//DeleteCollection(mongoCollection);
		DeleteCollection(mongoDatabase);
	}
	//���ݿ�����
	private static MongoDatabase DBconnect(String databaseName) {
		// TODO Auto-generated method stub
		try{
			//���ӵ� mongodb ����
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	        //���ӵ����ݿ�
	        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
	        System.out.println("Connect to database successfully");
	        return mongoDatabase;
		}catch(Exception e){
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
		return null;
		
	}
	
	//���ݿ���ѡ��
	public static MongoCollection<Document> CollectionBuild(String name, MongoDatabase mongoDatabase){
		MongoCollection<Document> collection = mongoDatabase.getCollection(name);
		System.out.println("Connect to collection successfully");
		return collection;
	}

	//�����ݿ���в��뵥���ĵ�
	public static void InsertOneDocument(MongoCollection<Document> mongoCollection, Document document){
		mongoCollection.insertOne(document);  
		System.out.println("Document Insertion Succeed");
	}
	
	//�����ݿ���в������ĵ�
	public static void InsertManyDocument(MongoCollection<Document> mongoCollection, List<Document> documents){ 
		mongoCollection.insertMany(documents);  
		System.out.println("Documents Insertion Succeed");
	}
	
	//��ӡ�����ݿ⼯���е������ĵ�
	public static void FindAll(MongoCollection<Document> collection){
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        }
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ�����ȣ�
	public static void FindManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ�����ȣ�
	public static void DeleteManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ȣ�
	public static void FindManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ȣ�
	public static void DeleteManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ڣ�
	public static void FindManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ڣ�
	public static void DeleteManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ���С�ڣ�
	public static void FindManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ���С�ڣ�
	public static void DeleteManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ������ڵ��ڣ�
	public static void FindManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ������ڵ��ڣ�
	public static void DeleteManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//�������ݿ⼯��������ĳһ�����������ĵ���С�ڵ��ڣ�
	public static void FindManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//ɾ�����ݿ⼯��������ĳһ�����������ĵ���С�ڵ��ڣ�
	public static void DeleteManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//ɾ�����ݿ⼯��
	public static void DeleteCollection(MongoCollection<Document> collection){
		collection.drop();
	}
	
	//ɾ�����ݿ�
	public static void DeleteDatabase(MongoDatabase database){
		database.drop();
	}
	
  	public DBhelper(){
		
	}
	
	
}
