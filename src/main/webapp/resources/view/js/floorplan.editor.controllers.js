function eventsEditor($scope, $http, $routeParams) {
	$scope.floorplanId = $routeParams.floorplanId;

	$scope.eventsEditor = true;
	
	$scope.loading = true;
	
	var now = moment();
	$scope.date = now.format("MM-DD-YYYY");
	$scope.start = "07:00";
	$scope.name = "";
	$scope.description = "";
	$scope.locationId = "";

	$scope.end = "23:00";
	
	$scope.eventTime = function(event) {
		var t = $scope.eventTimeValue(event.startTime) +" - " + $scope.eventTimeValue(event.endTime);
		return t;
	}
	
	$scope.eventTimeValue = function(val) {
		var sm = val % 60;
		var sh = (val - sm) / 60;
		var s = moment({hour: sh, minute: sm});
		var t = s.format("HH:mm"); 
		return t;
	}
	
	$scope.eventDate = function(event){
		var d = (moment({month:(event.month-1),day:event.day,year:event.year})).format("MM-DD-YYYY");
		return d;
	}
	
	$scope.deleteEvent = function(event){
		
		var url = "/mcc/events/delete/"+encodeURI(event.floorplanId)+"/"+encodeURI(event.id);
		$http.post(url, {})
		.success(function(rslt) {
			$scope.updateEvents();
			alert("Event deleted.");
		}).error(function() {
			alert("Something went wrong. Please try again.");
		});
	}
	
	$scope.selectEvent = function(event){
		$scope.id = event.id;
		$scope.name = event.name;
		$scope.date = $scope.eventDate(event);
		$scope.start = $scope.eventTimeValue(event.startTime);
		$scope.end = $scope.eventTimeValue(event.endTime);
		$scope.description = event.description;
		$scope.locationId = event.floorplanLocationId;
	}

	$scope.saveEvent = function() {
		$scope.saving = true;
		var url = $scope.getUrlWithTime();
		var data = {
			name : $scope.name,
			description : $scope.description
		};
		$http.post(url, data).success(function(rslt) {
			$scope.updateEvents();
			alert("Event saved.");
			$scope.saving = false;
		}).error(function() {
			$scope.saving = false;
			alert("Something went wrong. Please try again.");
		});
	}

	$scope.getUrlWithTime = function() {
		var start = moment($scope.start, "HH:mm");
		var end = moment($scope.end, "HH:mm");

		return $scope.getUrl(true) + "/"
				+ encodeURI(start.minute() + start.hour() * 60) + "/"
				+ encodeURI(end.minute() + end.hour() * 60);
	}
	
	$scope.getUrlWithPrefix = function(prefix, withLoc) {
		var when = moment($scope.date, "MM-DD-YYYY");

		return prefix + encodeURI($scope.floorplanId) + "/"
				+ ((withLoc)? encodeURI($scope.locationId.id) + "/" : "") + "on/"
				+ encodeURI(when.month() + 1) + "/" + encodeURI(when.date())
				+ "/" + encodeURI(when.year());
	}

	$scope.getUrl = function(withLoc) {
		return $scope.getUrlWithPrefix("/mcc/events/",withLoc);
	}

	$http.get("/mcc/floorplan/" + $scope.floorplanId).success(function(rslt) {
		$scope.floorplan = rslt;
	}).error(function() {
		alert("Something went wrong...please refresh the page.");
	});

	$scope.updateEvents = function() {
	    $scope.loading = true;
		var url = $scope.getUrl(false);
		$http.get(url)
				.success(function(rslt) {
					$scope.loading = false;
					$scope.events = rslt;
				}).error(function() {
					$scope.loading = false;
					alert("Unable to fetch events for the specified day. Please refresh the page and try again.");
				});
	}
	
	$scope.updateEvents();
}

function beaconsEditor($scope, $http, $routeParams) {

	$scope.floorplanId = $routeParams.floorplanId;

	$scope.beaconsEditor = true;
	
	$http
			.get("/mcc/beacons/" + $scope.floorplanId)
			.success(
					function(rslt) {
						$scope.beaconsMapping = rslt;

						$scope.beacons = {};
						$scope.beaconsLookup = {};

						for ( var i = 0; i < rslt.length; i++) {
							var ar = "";
							for ( var j = 0; j < rslt[i].beaconIds.length; j++) {
								ar += ((j == 0) ? "" : ",")
										+ rslt[i].beaconIds[j].beaconId + "["
										+ rslt[i].beaconIds[j].distance + "]";
							}

							$scope.beacons[rslt[i].locationId] = ar;
							$scope.beaconsLookup[rslt[i].locationId] = rslt[i].beaconIds;
						}
					})
			.error(
					function() {
						alert("Unable to load the beacons for this floor plan. Please refresh the page and try again.");
					});

	$scope.update = function(locationId) {
		$scope.dirty = true;
		var ar = $scope.beacons[locationId];
		var bids = ar.split(",");

		$scope.beaconsLookup[locationId] = [];
		for ( var i = 0; i < bids.length; i++) {
			if (bids[i].trim().length > 0) {
				var bid = bids[i];
				var d = -1;
				var start = bids[i].indexOf("[");
				var end = bids[i].indexOf("]");
				if (start > 0) {
					bid = bids[i].substring(0, start);
					if (end > 0) {
						d = bids[i].substring(start + 1, end);
						d = parseFloat(d);
					}
				}
				$scope.beaconsLookup[locationId].push({
					distance : d,
					beaconId : bid
				});
			}
		}
		console.log(JSON.stringify($scope.beaconsLookup[locationId]));
	}

	
	$scope.saveBeacons = function() {

		var data = [];
		for ( var loc in $scope.beaconsLookup) {
			data.push({
				locationId : loc,
				floorplanId : $scope.floorplanId,
				beaconIds : $scope.beaconsLookup[loc]
			});
		}

		var data = {
			beaconsMapping : JSON.stringify(data)
		};

		$http
				.post("/mcc/beacons/" + $scope.floorplanId, data)
				.success(function(rslt) {
					alert("Beacons saved.");
					$scope.dirty = false;
				})
				.error(
						function() {
							alert("Failed to save beacons mapping. Please try again and ensure that you have an Internet connection.");
						});
	}
	
	$scope.save = $scope.saveBeacons;
}

function floorplanCreator($scope, $http) {
	$scope.imageFile = null;
	$scope.imageScale = null;
	$scope.floorplanId = null;

	$scope.save = function() {

	}
}

function floorplanOpener($scope, $http) {
	$scope.floorplanId = null;

	$scope.updateUrl = function() {
		$scope.url = "/floorplan.editor.html#/floorplan/"
				+ encodeURI($scope.floorplanId);
	}
}

function floorplanEditorController($scope, $http, $routeParams) {

	$scope.floorplanEditor = true;
	
	$scope.setSelection = function(sel) {
		$scope.locationSelection = null;
		$scope.edgeSelection = null;
		$scope.edge2Selection = null;

		if (sel != null && sel.start) {
			$scope.edgeSelection = sel;
			$scope.edge2Selection = sel.getOppositeEdge();
		} else if (sel != null) {
			$scope.locationSelection = sel;
		}
		$scope.$digest();
	}

	$scope.setDirty = function(dirty) {
		$scope.dirty = dirty;
		$scope.$digest();
	}

	$scope.edited = function(node) {
		$scope.dirty = true;
		var locs = $scope.editor.floorplan.nodes;
		for ( var id in locs) {
			var loc = locs[id];
			if (loc == node) {
				delete locs[id];
				locs[node.id] = node;
				console.log("Updated node name in mapping.");
				break;
			}
		}
	}
	
	$scope.editedEdge = function(edge) {
		$scope.dirty = true;
		var edges = $scope.editor.floorplan.edges;
		for ( var i = 0; i< edges.length; i++) {
			var old_edge = edges[i];
			if (old_edge.start.id == edge.start.id && old_edge.end.id == edge.end.id){
				old_edge.weight = edge.weight;
				console.log("Updated edge weight");
				break;
			}
		}
	}

	$scope.loadFloorplan = function(floorplanId) {
		$http
				.get("/mcc/floorplan/mapping/" + floorplanId)
				.success(
						function(rslt) {
							var marshaller = new FloorplanMarshaller();
							var mapping = marshaller.unmarshallMapping(
									floorplanId, rslt);
							var floorplan = mapping.floorplan;
							var imgurl = mapping.imageUrl;
							$scope.loadFloorplanEditor(floorplanId, floorplan,
									imgurl);
						})
				.error(
						function() {
							var imgurl = prompt("Please provide the URL of the floorplan image.");
							$scope.loadFloorplanEditor(floorplanId,
									new Floorplan(floorplanId), imgurl);
						});
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
	
	$scope.save = $scope.saveFloorplan;

	$scope.loadFloorplan($routeParams.floorplanId);

}
