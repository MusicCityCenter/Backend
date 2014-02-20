  
      var FloorplanEditorToolset = function(listener){

    	  this.listener = listener;
    	  
    	  this.deleteElement = (function(editor){
    		  if(editor.selection){
    			  
    			  var rm = editor.selection;
    			  editor.setSelection(null);
    			  
    			  if(this.listener){
    				  this.listener.setSelection(null);
    			      this.listener.setDirty(true);
    			  }
    			  
    			  if(rm.isEdge){
    				  var op = rm.getOppositeEdge();
    				  editor.floorplan.removeEdge(rm);
    				  if(op != null){editor.floorplan.removeEdge(op);}
    			  }
    			  else {
    				  editor.floorplan.removeLocation(rm);
    			  }
    			  
    			  editor.refresh();
    		  }
    	  }).bind(this);
    	  
    	  this.keyBindings = {
    	      8:this.deleteElement,
    	      46:this.deleteElement
    	  };
    	  
		  this.promptForRoomInfo = function(editor){
			  var id = prompt("Room ID");
			  while(id in editor.floorplan.nodes){
				  alert("A room with that ID already exists. Please choose another ID.");
				  id = prompt("Room ID");
			  }
			  return id;
		  }
    	  
		  this.floorplanClicked = function(editor, x,y){
			  var id = this.promptForRoomInfo(editor);
		      if(id && id.trim().length > 0){
		    	  var loc = editor.floorplan.addLocation(id.trim(),'room',x,y);
		    	  editor.setSelection(loc);
    			  if(this.listener){
    				  this.listener.setSelection(loc);
    			      this.listener.setDirty(true);
    			  }
		    	  editor.refresh();
		      }
		  }
		  
		  this.connectLocations = function(editor,start,end){
			  console.log("Connect ['"+start.id+"' --> '"+end.id+"']");
			  
			  if(start != end){
				  var w = start.distanceTo(end).toFixed(2);
				  editor.floorplan.connectLocations(start,end,w);
				  editor.floorplan.connectLocations(end,start,w);
				  
    			  if(this.listener){
    			      this.listener.setDirty(true);
    			  }
				  
				  editor.refresh();
			  }
		  }
		  
		  this.edgeClicked = function(editor,edge){
			  //alert("edge clicked "+edge.start.id+" --> "+edge.end.id);
			  editor.setSelection(edge);
			  if(this.listener){this.listener.setSelection(edge);}
		  }
		  
		  this.locationClicked = function(editor, location){
			  
			  if(editor.keyboard.shiftDown){
				 if(editor.selection){
					 var start = editor.selection;
					 var end = location;
					 this.connectLocations(editor,start,end);
				 } 
			  }
			  else {
			 	 editor.setSelection(location);
			 	if(this.listener){this.listener.setSelection(location);}
			  }
		  }
      }