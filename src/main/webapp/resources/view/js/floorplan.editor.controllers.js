
function beaconsEditor($scope, $http, $routeParams) {
	
	$scope.floorplanId = $routeParams.floorplanId;
	
	$http
	.get("/mcc/beacons/" + $scope.floorplanId)
	.success(function(rslt) {
		$scope.beaconsMapping = rslt;
		
		$scope.beacons = {};
		$scope.beaconsLookup = {};
		
		for(var i = 0; i < rslt.length; i++){
			var ar = "";
			for(var j = 0; j < rslt[i].beaconIds.length; j++){
				ar += ((j == 0)? "" : ",") + rslt[i].beaconIds[j].beaconId + "["+rslt[i].beaconIds[j].distance+"]"; 
			}
			
			
			$scope.beacons[rslt[i].locationId] = ar;
			$scope.beaconsLookup[rslt[i].locationId] = rslt[i].beaconIds;
		}
	})
	.error(
			function() {
				alert("Unable to load the beacons for this floor plan. Please refresh the page and try again.");
			}
	);
	
	$scope.update = function(locationId){
		$scope.dirty = true;
		var ar = $scope.beacons[locationId];
		var bids = ar.split(",");
		
		$scope.beaconsLookup[locationId] = [];
		for(var i = 0; i < bids.length; i++){
			if(bids[i].trim().length > 0){
				var bid = bids[i];
				var d = -1;
				var start = bids[i].indexOf("[");
				var end = bids[i].indexOf("]");
				if(start > 0 ){
					bid = bids[i].substring(0,start);
					if(end > 0){
						d = bids[i].substring(start+1,end);
						d = parseFloat(d);
					}
				}
				$scope.beaconsLookup[locationId].push({distance:d, beaconId:bid});
			}
		}
		console.log(JSON.stringify($scope.beaconsLookup[locationId]));
	}
	
	$scope.saveBeacons = function(){
		
		var data = [];
		for(var loc in $scope.beaconsLookup){
			data.push({locationId:loc, floorplanId:$scope.floorplanId, beaconIds:$scope.beaconsLookup[loc]});
		}
		
		var data = {beaconsMapping:JSON.stringify(data)};
		
		$http
		.post("/mcc/beacons/" + $scope.floorplanId,data)
		.success(function(rslt) {
			alert("Beacons saved.");
			$scope.dirty = false;
		})
		.error(
				function() {
					alert("Failed to save beacons mapping. Please try again and ensure that you have an Internet connection.");
				}
		);
	}
}

function floorplanCreator($scope, $http) {
	$scope.imageFile = null;
	$scope.imageScale = null;
	$scope.floorplanId = null;
	
	$scope.save = function(){
		
	}
}

function floorplanOpener($scope, $http) {
	$scope.floorplanId = null;
	
	$scope.updateUrl = function(){
		$scope.url = "/floorplan.editor.html#/floorplan/"+encodeURI($scope.floorplanId);
	}
}



function floorplanEditorController($scope, $http, $routeParams) {

	$scope.setSelection = function(sel){
		$scope.locationSelection = null;
		$scope.edgeSelection = null;
		$scope.edge2Selection = null;
		
		if(sel != null && sel.start){
			$scope.edgeSelection = sel;
			$scope.edge2Selection = sel.getOppositeEdge();
		}
		else if(sel != null){
			$scope.locationSelection = sel;
		}
		$scope.$digest();
	}
	
	$scope.setDirty = function(dirty){
		$scope.dirty = dirty;
		$scope.$digest();
	}
	
	$scope.edited = function(node){
		$scope.dirty = true;
		var locs = $scope.editor.floorplan.nodes;
		for(var id in locs){
			var loc = locs[id];
			if(loc == node){
				delete locs[id];
				locs[node.id] = node;
				console.log("Updated node name in mapping.");
				break;
			}
		}
	}
	
	$scope.loadFloorplan = function(floorplanId){
		$http
		.get("/mcc/floorplan/mapping/" + floorplanId)
		.success(function(rslt) {
			var marshaller = new FloorplanMarshaller();
			var mapping = marshaller.unmarshallMapping(floorplanId,rslt);
			var floorplan = mapping.floorplan;
			var imgurl = mapping.imageUrl;
			$scope.loadFloorplanEditor(floorplanId, 
					floorplan, 
					imgurl);
		})
		.error(
				function() {
					var imgurl = prompt("Please provide the URL of the floorplan image.");
					$scope.loadFloorplanEditor(floorplanId, 
						new Floorplan(floorplanId), 
						imgurl);
				}
		);
	}
	
	$scope.loadFloorplanEditor = function(floorplanId, floorplan, imgurl) {
		$scope.floorplanId = floorplanId;
		$scope.floorplan = floorplan;
		$scope.img = imgurl;
		$scope.toolset = new FloorplanEditorToolset($scope);
		$scope.editor = new FloorplanManipulator($scope.floorplan, $scope.img,
				$scope.toolset);

		$scope.json = "";
	}
	
	$scope.saveFloorplan = function() {
		var marshaller = new FloorplanMarshaller();
		var fp = marshaller.marshallFloorplanToString($scope.floorplan);
		var coords = marshaller.marshallCoordsToString($scope.floorplan,
				$scope.img);

		$http.defaults.headers.put["Content-Type"] = "application/x-www-form-urlencoded";

		$http
				.post("/mcc/floorplan/mapping/" + $scope.floorplanId, {
					floorplan : fp,
					mapping : coords
				})
				.success(function(rslt) {
					$scope.dirty = false;
					alert("Floorplan saved.");
				})
				.error(
						function() {
							alert("Unable to save floorplan. Please ensure you have an Internet connection and try again.")
						});
	}
	
	
	$scope.loadFloorplan($routeParams.floorplanId);
	
}
