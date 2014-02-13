package main.java;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReflectionDBObject;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

public class PopulateMongo {

	private static final String DIRECTION = "direction";
	private static final String NODE2 = "node2";
	private static final String NODE1 = "node1";
	private static final String WEST = "west";
	private static final String EAST = "east";
	private static final String SOUTH = "south";
	private static final String NORTH = "north";
	private static final String HALL = "hall";
	private static final String NEIGHBORS = "neighbors";
	private static final String TYPE = "type";
	private static final String ROOM = "room";
	private static final String NODE_ID = "NodeID";

	public static void main(String[] args) throws UnknownHostException {
		
		MongoClient mongoClient = new MongoClient();
		DB db = mongoClient.getDB( "MCC" );
		DBCollection coll = db.getCollection("testNodes");
		
		class Neighbor extends ReflectionDBObject{
			private String node_id;
			private String to_the;
			public Neighbor(String id, String to_the){
				node_id = id;
				this.to_the = to_the;
			}
		}
		
		//insert the info for room R209
		BasicDBObject doc = new BasicDBObject(NODE_ID, "R209").
                append(TYPE, ROOM);
		coll.insert(doc);
		
		//insert the info for room R208
		doc = new BasicDBObject(NODE_ID, "R208").
                append(TYPE, ROOM);
		coll.insert(doc);
		
		//insert the info for room R207
		doc = new BasicDBObject(NODE_ID, "R207").
                append(TYPE, ROOM);
		coll.insert(doc);
		
		//insert the info for room R206
		doc = new BasicDBObject(NODE_ID, "R206").
                append(TYPE, ROOM);
		coll.insert(doc);
		
		//insert the info for room H201
		doc = new BasicDBObject(NODE_ID, "H201").
                append(TYPE, HALL);
		coll.insert(doc);
		

		//insert the info for room h202
		doc = new BasicDBObject(NODE_ID, "h202").
                append(TYPE, HALL);
		coll.insert(doc);
		
		//insert the info for room h203
		doc = new BasicDBObject(NODE_ID, "h203").
                append(TYPE, HALL);
		coll.insert(doc);
		
		coll = db.getCollection("testEdges");
		doc = new BasicDBObject(NODE1, "R209").append(NODE2,"H201").append(DIRECTION, NORTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "R208").append(NODE2,"H202").append(DIRECTION, NORTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "R207").append(NODE2,"H201").append(DIRECTION, SOUTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "R206").append(NODE2,"H202").append(DIRECTION, SOUTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H201").append(NODE2,"R209").append(DIRECTION, SOUTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H201").append(NODE2,"R207").append(DIRECTION, NORTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H201").append(NODE2,"H202").append(DIRECTION, EAST);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H202").append(NODE2,"R208").append(DIRECTION, SOUTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H202").append(NODE2,"R206").append(DIRECTION, NORTH);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H202").append(NODE2,"H201").append(DIRECTION, WEST);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H202").append(NODE2,"H203").append(DIRECTION, EAST);
		coll.insert(doc);
		doc = new BasicDBObject(NODE1, "H203").append(NODE2,"H202").append(DIRECTION, WEST);
		coll.insert(doc);

	}

}
