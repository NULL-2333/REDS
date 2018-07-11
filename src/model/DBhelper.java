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
	//数据库连接
	private static MongoDatabase DBconnect(String databaseName) {
		// TODO Auto-generated method stub
		try{
			//连接到 mongodb 服务
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
	        //连接到数据库
	        MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
	        System.out.println("Connect to database successfully");
	        return mongoDatabase;
		}catch(Exception e){
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	    }
		return null;
		
	}
	
	//数据库表的选择
	public static MongoCollection<Document> CollectionBuild(String name, MongoDatabase mongoDatabase){
		MongoCollection<Document> collection = mongoDatabase.getCollection(name);
		System.out.println("Connect to collection successfully");
		return collection;
	}

	//在数据库表中插入单个文档
	public static void InsertOneDocument(MongoCollection<Document> mongoCollection, Document document){
		mongoCollection.insertOne(document);  
		System.out.println("Document Insertion Succeed");
	}
	
	//在数据库表中插入多个文档
	public static void InsertManyDocument(MongoCollection<Document> mongoCollection, List<Document> documents){ 
		mongoCollection.insertMany(documents);  
		System.out.println("Documents Insertion Succeed");
	}
	
	//打印该数据库集合中的所有文档
	public static void FindAll(MongoCollection<Document> collection){
		FindIterable<Document> findIterable = collection.find();  
        MongoCursor<Document> mongoCursor = findIterable.iterator();  
        while(mongoCursor.hasNext()){  
           System.out.println(mongoCursor.next());  
        }
	}
	
	//查找数据库集合中满足某一条件的所有文档（相等）
	public static void FindManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//删除数据库集合中满足某一条件的所有文档（相等）
	public static void DeleteManyEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.eq(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//查找数据库集合中满足某一条件的所有文档（不等）
	public static void FindManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//删除数据库集合中满足某一条件的所有文档（不等）
	public static void DeleteManyNotEqualDocument(MongoCollection<Document> collection, String attribute, String value){
		FindIterable<Document> iter = collection.find(Filters.ne(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//查找数据库集合中满足某一条件的所有文档（大于）
	public static void FindManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//删除数据库集合中满足某一条件的所有文档（大于）
	public static void DeleteManyGreaterDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//查找数据库集合中满足某一条件的所有文档（小于）
	public static void FindManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//删除数据库集合中满足某一条件的所有文档（小于）
	public static void DeleteManyLessDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lt(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//查找数据库集合中满足某一条件的所有文档（大于等于）
	public static void FindManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//删除数据库集合中满足某一条件的所有文档（大于等于）
	public static void DeleteManyGreaterEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.gte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//查找数据库集合中满足某一条件的所有文档（小于等于）
	public static void FindManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				System.out.println(doc.toJson());
				}
			});
	}
	
	//删除数据库集合中满足某一条件的所有文档（小于等于）
	public static void DeleteManyLessEqualDocument(MongoCollection<Document> collection, String attribute, Float value){
		FindIterable<Document> iter = collection.find(Filters.lte(attribute,value));
		iter.forEach(new Block<Document>(){
			public void apply(Document doc){
				collection.deleteMany(doc);
				}
			});
	}
	
	//删除数据库集合
	public static void DeleteCollection(MongoCollection<Document> collection){
		collection.drop();
	}
	
	//删除数据库
	public static void DeleteDatabase(MongoDatabase database){
		database.drop();
	}
	
  	public DBhelper(){
		
	}
	
	
}
