

Overview
---------
This is a basic implementation of a possible server for the MCC project. The server
provides the following capabilities:

1. Storage of floor plans in JSON format in AppEngine's database
2. Pathfinding on floor plans using Dijkstra + Fibonacci Heap
3. An HTTP interface via Spring to obtain floor plan json and retrieve shortest paths
4. An initial implementation of Austin's guesser framework with simple implementations
   of guesser strategies that rely on user input
5. A web-based floor plan and beacon editor
   
Environment Setup
------------------
1. Install Maven 3.1+ (http://maven.apache.org/download.cgi)
2. Install the Maven Eclipse Plug-in using the update site (http://download.eclipse.org/technology/m2e/releases)
3. In Eclipse Preferences, check that Java->Installed JREs points to a JDK and not a JRE (this is not the default setup).
   If it does not, select the JRE->Edit->Directory and select a JDK directory for JDK 1.7+
4. In Eclipse Preferences, check that Maven->Installations points to a Maven 3.1+ install. If not, add a 3.1+ install.

Importing the Project into Eclipse
----------------------------------
1. File->Import->Maven->Existing Maven Projects
2. Select the directory containing the pom.xml file
3. Finish

Building the Project for the First Time
---------------------------------------
1. Right-click on pom.xml->Run As->Maven Clean
2. Right-click on pom.xml->Run As->Maven Install
3. Right-click on the project root folder->Maven->Update Project

Building and Running the Project Locally 
----------------------------------------
1. Right-click on pom.xml->Run As->Build...
2. For "Goals," list "appengine:devserver"
3. When "INFO: Dev App Server is now running" shows up, open your browser to http://localhost:8080/floorplan.editor.html


Building Blocks
----------------
A variety of simple services are already available:

1. Add a new floorplan:

   (manual interface)
   http://localhost:8888/floorplan.editor.html
   
   (programmatic interface), POST to:
   http://localhost:8888/mcc/{floorplanId}
   
   Path: replace {floorplanId} with the desired identifier for the floorplan. 
   Params: floorplan - a json string representing the floorplan. See the example below for the format.
   
2. Obtain an existing floorplan via a GET request:
   http://localhost:8888/mcc/floorplan/{floorplanId}

3. Get the shortest path between two locations (e.g., between the upstairsHallway and bathroom1):
   http://localhost:8888/mcc/path/{floorplanId}/{startLocationId}/{endLocationId}

4. Obtain a full mapping of a floor plan to an image
    http://localhost:8888/mcc/floorplan/mapping/{floorplanId}

The programmatic building blocks are as follows:

1. This AppEngine project has been setup to use Spring and return JSON. You can add
   request paths that the app will respond to and methods to handle them by adding them
   to the Controller object in org.magnum.mcc.controllers.NavController:

```java 
   @RequestMapping(
   			// The url path that the method handles, http://localhost:8888/data/friends/find
   			value = "/data/friends/find", 
   			// The HTTP methods that the method supports
   			method = RequestMethod.GET)
   public @ResponseBody YourObject doSomething(
            // Parameters are bound to request parameters with the @RequestParam annotation,
            // example: http://localhost:8888/data/friends/find?someVal=1.23
            @RequestParam("someVal") double lat) {

		...
		//do stuff

		YourObject obj = new YourObject();
		...
		
		// Spring is setup to automatically convert your object to JSON
		return obj;
	}
```
	
2. AppEngine uses the JDO Java standard for automatically storing entire objects in a database
   using Object Relational Mapping (e.g., automatically map your object instances that you store
   to rows in the database and turn queried rows into objects). A basic persistent object
   to store floorplans is provided for in the org.magnum.mcc.building.loader.StoredFloorplan
   class. You can use it as a template for creating other persistent objects or see the
   Google tutorial: https://developers.google.com/appengine/docs/java/datastore/jdo/dataclasses

```java 
   // Mark the class with @PersistenceCapable to indicate that it is going to be stored in the
   // datastore
   @PersistenceCapable
   public class Foo {
   
   		// Add a primary key
   		@PrimaryKey
		private String key;

		// Create some properties that you want to persist
		@Persistent
		private String name;
		
		//Add getters/setters
	}
```	

AppEngine provides a management interface to view all persisted data in:
http://localhost:8888/_ah/admin (local debugging)
	
or
	
http://appspot.com (apps that have been deployed to AppEngine)
	
3. Most of the client-facing changes that affect the HTTP interface will need to go into the
   org.magnum.mcc.controllers.NavController class.

4. AppEngine provides tools to deploy your application to their servers. To deploy an app
   to AppEngine, first create a new app at:
   
   https://appengine.google.com/start/createapp?
   
   Next, right-click on the root of your project -> Properties -> Google -> Web Application. Click the check-box for "This project has a WAR directory" and choose "src/main/webapp" Click ok.

   The project may have issues deploying if the maven dependencies are not last in the build path.

   Then, right-click on the root of your project -> Google -> Deploy to AppEngine. Click the
   link to AppEngine settings to set the application ID that you set in the previous step
   (if needed). Your app will be packaged, deployed, and launched in Google's cloud in a
   few seconds. You can view all of your running versions at:
   
   https://appengine.google.com/instances?&app_id=&lt;your_app_id&gt;
   
   Notice that the AppEngine console has management tools for a variety of aspects of our
   app, ranging from logs to previously deployed versions. If you want to deploy multiple
   versions of your app, you need to change the version id in appengine-web.xml.
   
 5. Dependency Injection - this app uses Spring, you can modify the dependency injection
    by editing src/main/webapp/WEB-INF/conf/dispatcher-servlet.xml. The app is also using Google
    Guice for dependency injection in the NavController. This is not really ideal and
    should probably be changed at some point. The dependencies for Guice are bound in the
    org.magnum.mcc.modules.StandaloneServerModule class.
   
   
 Sample JSON Floorplan 
 ----------------
 ```	
{
   "locations":[
      {
         "id":"d",
         "type":"room"
      },
      {
         "id":"f",
         "type":"room"
      }
   ],
   "edges":[
      {
         "start":"d",
         "end":"f",
         "length":0.0
      },
      {
         "start":"f",
         "end":"d",
         "length":0.0
      }
   ],
   "types":{
      "name":"root",
      "children":[
         {
            "name":"room"
         }
      ]
   }
}
```	


[![Build Status](https://travis-ci.org/MusicCityCenter/Backend.png?branch=master)](https://travis-ci.org/MusicCityCenter/Backend)
