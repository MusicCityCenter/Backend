

  var FloorplanMarshaller = function(){
	  
	  this.marshallFloorplanToString = function(floorplan){
		  return JSON.stringify(this.marshallFloorplan(floorplan));
	  }
	  
	  this.marshallCoordsToString = function(floorplan, imgurl){
		  return JSON.stringify(this.marshallCoordinates(floorplan,imgurl));
	  }
	   
	  this.marshallFloorplan = function(floorplan){
		  var mf = {};
		  mf.locations = [];
		  mf.edges = [];
		  mf.types = {name:"root",children:[]};
		  var types = {};
		  types["root"] = mf.rootType;
		  
		  for(var id in floorplan.nodes){
			  var location = floorplan.nodes[id];
			  
			  var ml = {
				 id: location.id,
				 type: location.type
			  };
			  mf.locations.push(ml);
			  
			  if(!(location.type in types)){
				  var nt = {name:location.type,children:[],parent:"root"};
				  mf.types.children.push(nt);
				  types[location.type] = nt
			  }
			  
			  for(var i = 0; i < location.edges.length; i++){
				  var me = {
					 start: location.edges[i].start.id,
					 end: location.edges[i].end.id,
					 length: location.edges[i].weight,
					 angle: location.edges[i].angle,
					 image: location.edges[i].image
				  }
				  mf.edges.push(me);
			  }
		  }
		  
		  return mf;
	  }
	  
	  this.marshallCoordinates = function(floorplan,imgurl){
		  var coords = {imageUrl:imgurl,mapping:{}};
		  
		  for(var id in floorplan.nodes){
			  var location = floorplan.nodes[id];
			  coords.mapping[location.id] = {x:location.x, y:location.y};
		  }
		  
		  return coords;
	  }
	  
	  this.unmarshallMapping = function(id,json){
		  var floorplan = new Floorplan(id);
		  
		  var locations = json.floorplan.locations;
		  var edges = json.floorplan.edges;
		  var coords = json.mapping.mapping;
		  var imgurl = json.mapping.imageUrl;
		  
		  for(var i = 0; i < locations.length; i++){
			  var node = locations[i];
			  var id = node.id;
			  var type = node.type;
			  var coord = (id in coords) ? coords[id] : {x:0, y:0};
			  var x = coord.x;
			  var y = coord.y;
			  floorplan.addLocation(id,type,x,y);
		  }
		  
		  for(var i = 0; i < edges.length; i++){
			  var edge = edges[i];
			  var start = floorplan.nodes[edge.start];
			  var end = floorplan.nodes[edge.end];
			  var len = edge.length;
			  floorplan.connectLocations(start, end, len);
		  }
		  
		  return {floorplan:floorplan, imageUrl:imgurl};
	  }
  }