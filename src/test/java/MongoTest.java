package test.java;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class MongoTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTestDB() {
		MongoClient mongoClient=null;
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			fail(e.getMessage());
		}
		DB db = mongoClient.getDB( "MCC" );
		DBCollection coll = db.getCollection("testNodes");
		assertEquals(7,coll.count());
		
		coll = db.getCollection("testEdges");
		assertEquals(12, coll.count());
	}

}
