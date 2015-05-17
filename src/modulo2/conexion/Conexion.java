package modulo2.conexion;

import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

import twitter4j.JSONArray;
import twitter4j.JSONObject;
import twitter4j.Paging;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UserMentionEntity;
import twitter4j.conf.ConfigurationBuilder;

public class Conexion{  
	   private static ConfigurationBuilder cb;  
	   private DB db;  
	   private DBCollection items;
	   private static Conexion instancia;
	   private TwitterFactory tf;
	   
	   public static Conexion getInstancia(){
		   if(instancia==null)
			   instancia = new Conexion();
		   return instancia;
	   }
	   
	   public Conexion() { 
		   cb = new ConfigurationBuilder();  
		   cb.setDebugEnabled(true);  
		   cb.setOAuthConsumerKey("iKRGfs4BVTt1D7vICixKRmJUJ");  
		   cb.setOAuthConsumerSecret("73ZibURdOwnYtbNOewsTVGjCKX3s2Lv7cL1iqM5nF1LfEF3iQW");  
		   cb.setOAuthAccessToken("2484444423-8TPTOvFzZHhGUcyOkqECzcwAoBmhn7eXDNRu0lv");  
		   cb.setOAuthAccessTokenSecret("SjkCRLxsiqHIIrM2UoD3i9eqAW6A70Lr7x1gYOFQAIopb");  
		   try {  
			   // on constructor load initialize MongoDB and load collection  
			   initMongoDB();  
			   items = db.getCollection("tweetColl");  
		   } catch (MongoException ex) {  
			   System.out.println("MongoException :" + ex.getMessage());  
		   }
		   //Se crea el objeto TwitterFactory
		   tf = new TwitterFactory(cb.build());
	   }  

	public void initMongoDB() throws MongoException {  
	     try {  
	       System.out.println("Connecting to Mongo DB..");  
	       MongoClient mongo;  
	       mongo = new MongoClient("127.0.0.1");
	       db = mongo.getDB("tweetDB");  
	     } catch (UnknownHostException ex) {  
	       System.out.println("MongoDB Connection Errro :" + ex.getMessage());  
	     }  
	}
	
	public void loadInformationOfTwitter(List<String> users){
		if(cb!=null){
			
			DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			Twitter twitter = tf.getInstance();
			Paging paging = new Paging(1, 100);
			for(String user: users){
				try {
					List<Status> statuses = twitter.getUserTimeline(user ,paging);
					for(Status tweet: statuses){
						//System.out.println(" ID: " + tweet.getId() + " Texto: " + tweet.getText() + "#Retweet: " + tweet.getRetweetCount() + "# Fecha: " + formato.format(tweet.getCreatedAt()));
						BasicDBObject basicObj = new BasicDBObject();  
						basicObj.put("user_name", tweet.getUser().getScreenName());  
						basicObj.put("retweet_count", tweet.getRetweetCount());  
						basicObj.put("tweet_followers_count",  
						      tweet.getUser().getFollowersCount());  
						UserMentionEntity[] mentioned = tweet.getUserMentionEntities();  
						basicObj.put("tweet_mentioned_count", mentioned.length);  
						basicObj.put("tweet_date", formato.format(tweet.getCreatedAt()));
						basicObj.put("tweet_ID", tweet.getId());  
						basicObj.put("tweet_text", tweet.getText());  
						try {
							items.insert(basicObj);  
						} catch (Exception e) {  
							System.out.println("MongoDB Connection Error : "  + e.getMessage());  
						}
					}
				}catch (TwitterException e) {
					e.printStackTrace();
				}
			}
		}else{
			System.out.println("CB is null");
		}
	}


	public JSONArray search(BasicDBObject query, BasicDBObject campos){
		//items.drop();
		//loadInformationOfTwitter(Arrays.asList("Henzer García"));
		
		JSONArray result = new JSONArray();
		DBCursor cursor = items.find(query, campos);
		try{
			while(cursor.hasNext()){
				DBObject element = cursor.next();
				result.put(new JSONObject(element.toString()));
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			cursor.close();
		}
		return result;
	}
	
	
	
	
	
public void getTweetByQuery(boolean loadRecords) {  
    if (cb != null) {  
      TwitterFactory tf = new TwitterFactory(cb.build());  
      Twitter twitter = tf.getInstance();  
      
      
      try {  
        Query query = new Query("source:twitter4j yusukey");  
        query.setCount(50);  
        QueryResult result;  
        result = twitter.search(query);  
        System.out.println("Getting Tweets...");  
        List<Status> tweets = result.getTweets();  
        for (Status tweet : tweets) {  
          BasicDBObject basicObj = new BasicDBObject();  
          basicObj.put("user_name", tweet.getUser().getScreenName());  
          basicObj.put("retweet_count", tweet.getRetweetCount());  
          basicObj.put("tweet_followers_count",  
              tweet.getUser().getFollowersCount());  
          UserMentionEntity[] mentioned = tweet.getUserMentionEntities();  
          basicObj.put("tweet_mentioned_count", mentioned.length);  
          basicObj.put("tweet_ID", tweet.getId());  
          basicObj.put("tweet_text", tweet.getText());  
          try {  
            items.insert(basicObj);  
          } catch (Exception e) {  
            System.out.println("MongoDB Connection Error : "  
                + e.getMessage());  
            //loadMenu();  
          }  
        }  
        // Printing fetched records from DB.  
        if (loadRecords) {  
          getTweetsRecords();  
        }  
      } catch (TwitterException te) {  
        System.out.println("te.getErrorCode() " + te.getErrorCode());  
        System.out.println("te.getExceptionCode() " + te.getExceptionCode());  
        System.out.println("te.getStatusCode() " + te.getStatusCode());  
        if (te.getStatusCode() == 401) {  
          System.out.println("Twitter Error : \nAuthentication "  
              + "credentials (https://dev.twitter.com/pages/auth) "  
              + "were missing or incorrect.\nEnsure that you have "  
              + "set valid consumer key/secret, access "  
              + "token/secret, and the system clock is in sync.");  
        } else {  
          System.out.println("Twitter Error : " + te.getMessage());  
        }  
        //loadMenu();  
      }  
    } else {  
      System.out.println("MongoDB is not Connected!"  
          + " Please check mongoDB intance running..");  
    }  
  }
	
public void getTweetsRecords() {  
    BasicDBObject fields = new BasicDBObject("_id", true).append("user_name", true).append("tweet_text", true);  
    DBCursor cursor = items.find(new BasicDBObject(), fields);  
    while (cursor.hasNext()) {  
      System.out.println(cursor.next());  
    }  
    //loadMenu();  
  }

public void getTopRetweet() {  
    if (items.count() <= 0) {  
      getTweetByQuery(false);  
    }  
    BasicDBObject query = new BasicDBObject();  
    query.put("retweet_count", -1);  
    DBCursor cursor = items.find().sort(query).limit(10);  
    System.out.println("items length " + items.count());  
    while (cursor.hasNext()) {  
      System.out.println(cursor.next());  
    }  
    //loadMenu();  
  } 

}
	
