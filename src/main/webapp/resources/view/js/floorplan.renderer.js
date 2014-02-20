var Keyboard = function(listener) {
	this.shiftDown = false;
	this.lastKeyCode = -1;
	this.listener = listener;
	this.ingoreEventsOnInputFocus = true;

	var checkKeyDown = (function(event) {
		this.lastKeyCode = event.keyCode;

		var act = document.activeElement;
		var ignore =  this.ingoreEventsOnInputFocus && (act != null
				&& (act.tagName && act.tagName.toLowerCase() == "input" || act.tagName
						&& act.tagName.toLowerCase() == "textarea"));
			
		

		if (event.keyCode === 16 || event.charCode === 16) {
			this.shiftDown = true;
			console.log("shift down");
		}
		if(!ignore){this.listener.keyDown(event, event.keyCode);}
	}).bind(this);

	var checkKeyUp = (function(event) {
		this.lastKeyCode = -1;
		if (event.keyCode === 16 || event.charCode === 16) {
			this.shiftDown = false;
			console.log("shift up");
		}
		
		var act = document.activeElement;
		var ignore =  this.ingoreEventsOnInputFocus && (act != null
				&& (act.tagName && act.tagName.toLowerCase() == "input" || act.tagName
						&& act.tagName.toLowerCase() == "textarea"));
		
		if(!ignore){this.listener.keyUp(event, event.keyCode);}
	}).bind(this);

	window.addEventListener ? document
			.addEventListener('keydown', checkKeyDown) : document.attachEvent(
			'keydown', checkKeyDown);
	window.addEventListener ? document.addEventListener('keyup', checkKeyUp)
			: document.attachEvent('keyup', checkKeyUp);
}

var FloorplanRenderer = function(listener, floorplan, stage, scale) {
	this.listener = listener;
	this.floorplan = floorplan;
	this.stage = stage;
	this.edgeLayer = new Kinetic.Layer();
	this.graphLayer = new Kinetic.Layer();
	this.stage.add(this.graphLayer);
	this.scale = scale;

	this.selection = null;
	this.renderNodes = {};
	this.renderEdges = [];

	this.nodeRadius = 10;
	this.nodeStrokeWidth = 4;
	this.nodeFillColor = "red";
	this.nodeStrokeColor = "black";
	this.nodeSelectedFillColor = "red";
	this.nodeSelectedStrokeColor = "yellow";

	this.edgeStrokeWidth = 4;
	this.edgeStrokeColor = "red";
	this.edgeStrokeSelectedColor = "yellow";
	this.edgeDash = [ 1, 2 ];

	this.refresh = function() {
		this.graphLayer.destroyChildren();
		this.renderNodes = {};

		//We render the edges first so that the
		//nodes (locations) appear on top
		for ( var nid in floorplan.nodes) {
			var location = floorplan.nodes[nid];
			for ( var i = 0; i < location.edges.length; i++) {
				var edge = location.edges[i];
				this.renderEdge(edge);
			}
		}
		
		for ( var nid in floorplan.nodes) {
			var node = floorplan.nodes[nid];
			this.renderLocation(node);
		}

		this.graphLayer.draw();
	}

	this.renderLocation = function(location) {

		var x = location.x * this.scale;
		var y = location.y * this.scale;

		//console.log("Rendering ['" + location.id + "' at " + x + "," + y + "]");

		var selected = (this.selection != null && location.getId() in this.selection);

		var stroke = (selected) ? this.nodeSelectedStrokeColor
				: this.nodeStrokeColor;
		var fill = (selected) ? this.nodeSelectedFillColor : this.nodeFillColor;

		var node = new Kinetic.Circle({
			x : x,
			y : y,
			radius : this.nodeRadius,
			fill : fill,
			stroke : stroke,
			strokeWidth : this.nodeStrokeWidth
		});
		this.renderNodes[location.id] = node;

		node.on('mousedown', (function(evt) {
			evt.cancelBubble = true;
			this.listener.locationClicked(location);
		}).bind(this));

		this.addLocationTooltip(node,location);

		this.graphLayer.add(node);
	}
	
	this.addLocationTooltip = function(node, location){
		
		// label with left pointer
	      var labelLeft = new Kinetic.Label({
	        x: location.x * this.scale,
	        y: location.y * this.scale,
	        opacity: 0.75
	      });
	      
	      labelLeft.add(new Kinetic.Tag({
	        fill: 'green',
	        pointerDirection: 'left',
	        pointerWidth: 20,
	        pointerHeight: 28,
	        lineJoin: 'round'
	      }));
	      
	      labelLeft.add(new Kinetic.Text({
	        text: location.id,
	        fontFamily: 'Calibri',
	        fontSize: 18,
	        padding: 5,
	        fill: 'white'
	      }));
	      
		
		node.on('mouseover', (function(){
		   this.graphLayer.add(labelLeft); 
		   this.graphLayer.draw();
		}).bind(this));
		
		node.on('mouseout', (function(){
			labelLeft.remove();
			this.graphLayer.draw();
		}).bind(this));
	}

	this.renderEdge = function(edge) {
		var x1 = edge.start.x * this.scale;
		var y1 = edge.start.y * this.scale;
		var x2 = edge.end.x * this.scale;
		var y2 = edge.end.y * this.scale;

		var selected = (this.selection != null && edge.getId() in this.selection);

		var strokeColor = (selected) ? this.edgeStrokeSelectedColor
				: this.edgeStrokeColor;

		var line = new Kinetic.Line({
			points : [ x1, y1, x2, y2 ],
			stroke : strokeColor,
			strokeWidth : this.edgeStrokeWidth,
			lineJoin : 'round',
			dash : this.edgeDash
		});

		line.on('mousedown', (function(evt) {
			evt.cancelBubble = true;
			this.listener.edgeClicked(edge);
		}).bind(this));

		this.graphLayer.add(line);

		this.renderEdges.push(line);
	}
}

var FloorplanManipulator = function(floorplan, floorplanImgUrl, toolSet) {
	this.keyboard = new Keyboard(this);
	this.stage = null;
	this.imageLayer = null;
	this.floorplan = floorplan;
	this.toolSet = toolSet;
	this.keyBindings = {};

	this.locationClicked = function(location) {
		this.toolSet.locationClicked(this, location);
	}

	this.edgeClicked = function(edge) {
		this.toolSet.edgeClicked(this, edge);
	}

	this.keyDown = function(evt, keyCode) {
		if (keyCode in this.keyBindings) {
			evt.preventDefault();
			this.keyBindings[keyCode](this);
		}
	}

	this.keyUp = function(evt, keyCode) {
		console.log("Key up: code:'" + keyCode + "'");
	}

	if (this.toolSet.keyBindings) {
		for ( var key in this.toolSet.keyBindings) {
			this.keyBindings[key] = this.toolSet.keyBindings[key];
		}
	}

	this.floorplanRenderer = null;

	this.refresh = function() {
		this.floorplanRenderer.refresh();
	}

	this.setSelection = function(obj) {
		this.selection = obj;
		
		this.floorplanRenderer.selection = {};
		
		if(obj != null && obj.getId){
			var id = obj.getId();
			this.floorplanRenderer.selection[id] = true;
		}
		this.floorplanRenderer.refresh();
	}

	this.mouseDown = (function(evt) {
		var x = this.stage.getPointerPosition().x / this.scale;
		var y = this.stage.getPointerPosition().y / this.scale;
		this.toolSet.floorplanClicked(this, x, y);
	}).bind(this);

	this.setupStage = function(stage) {
		stage.on('mousedown', this.mouseDown);

		this.floorplanRenderer = new FloorplanRenderer(this, this.floorplan,
				this.stage, this.scale);
		this.floorplanRenderer.refresh();
	}

	var imageObj = new Image();
	imageObj.onload = function() {
		var ratio = imageObj.width / imageObj.height;

		imgw = 700;
		imgh = imgw / ratio;

		this.scale = imgw / imageObj.width;

		this.stage = new Kinetic.Stage({
			container : 'container',
			width : imgw,
			height : imgh
		});

		this.imageLayer = new Kinetic.Layer();

		var floorplanImg = new Kinetic.Image({
			x : 0,
			y : 0,
			image : imageObj,
			width : imgw,
			height : imgh
		});

		// add the shape to the layer
		this.imageLayer.add(floorplanImg);

		// add the layer to the stage
		this.stage.add(this.imageLayer);

		this.setupStage(this.stage);
	};
	imageObj.onload = imageObj.onload.bind(this);
	imageObj.src = floorplanImgUrl;
}
