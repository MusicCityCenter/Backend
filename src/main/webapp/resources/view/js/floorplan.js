      var Floorplan = function(id) {
    	  this.id = id;
    	  this.nodes = {};
    	  this.edges = [];
    	  
    	  this.removeEdge = function(edge){
    		  for(var i = 0; i < this.edges.length; i++){
    			  if(this.edges[i] == edge){
    				  this.edges.splice(i,1);
    				  break;
    			  }
    		  }
    		  edge.remove();
    	  }
    	  
    	  this.removeLocation = function(location){
    		  var rm = [];
    		  for(var i = 0; i < this.edges.length; i++){
    			  var edge = this.edges[i];
    			  if(edge.start == location || edge.end == location){
    				  edge.remove();
    				  console.log("Will remove edge at "+i);
    				  rm.push(i);
    			  }
    			 
    		  }
    		  
    		  var delta = 0;
    		  for(var i = 0; i < rm.length; i++){
    			  this.edges.splice(rm[i] - delta, 1);
    			  delta++;
    		  }
    		  
    		  
    		  delete this.nodes[location.id];
    	  }
    	  
    	  this.addLocation = function(id, type, x, y){
    		  var loc = new FloorplanLocation(id,type,x,y);
    		  this.nodes[id] = loc;
    		  return loc;
    	  }
    	  
    	  this.findLocation = function(x,y,fudge){
    		 for(var id in this.nodes){
    			 var node = this.nodes[id];
    			 var dist = Math.sqrt(Math.pow(node.x - x, 2) + Math.pow(node.y - y, 2));
    			 if(dist < fudge){
    				 return node;
    			 }
    		 }  
    		 
    		 return null;
    	  }
    	  
    	  this.connectLocations = function(start,end,weight){
    		  var edge = new FloorplanEdge(start,end,weight,null);
    		  this.edges.push(edge);
    		  //debug
    		  console.log("Adding edge from "+edge.start.id+" to "+edge.end.id + " of weight "+edge.weight+" where weight was "+weight);
    		  return edge;
    	  }
      }
      
      var FloorplanEdge = function(start,end,weight,image){
    	  this.start = start;
    	  this.end = end;
    	  this.weight = (weight == 0)? start.distanceTo(end) : weight;
    	  this.image=image;
    	  this.isEdge = true;
    	  
    	  //this.angle = Math.atan((end.y - start.y) / (end.x - start.x)) * 180 / Math.PI;
    	  
    	  this.angle = Math.atan2(start.y - end.y, end.x - start.x);
    	  if(this.angle < 0){this.angle += (2 * Math.PI);}
    	  this.angle *= 180 / Math.PI
    	  
    	  start.edges.push(this);
    	  
    	  this.getId = function(){
    		  return this.start.id + "-->" + this.end.id;
    	  }
    	  
    	  this.remove = function(){
    		  this.removeEdgeFromNode(this.start);
    		  this.removeEdgeFromNode(this.end);
    	  }
    	  
    	  this.removeEdgeFromNode = function(node){
    		  var index = -1;
    		  for(var i = 0; i < node.edges.length; i++){
    			  if(node.edges[i] == this){
    				  index = i;
    				  break;
    			  }
    		  }
    		  if(index > -1){
    			  node.edges.splice(index,1);
    		  }
    	  }
    	  
    	  this.getOppositeEdge = function(){
    		  var op = null;
    		  for(var i = 0; i < end.edges.length; i++){
    			  if(end.edges[i].end == this.start){
    				  op = end.edges[i];
    				  break;
    			  }
    		  }
    		  return op;
    	  }
      }
      
      var FloorplanLocation = function(id,type,x,y){
    	  this.id = id;
    	  this.type = type;
    	  this.x = x;
    	  this.y = y;
    	  this.edges = [];
    	  
    	  this.getId = function(){
    		  return this.id;
    	  }
    	  
    	  this.distanceTo = function(node){
    		  return Math.sqrt(Math.pow(node.x - this.x, 2) + Math.pow(node.y - this.y, 2));
    	  }
      }
    