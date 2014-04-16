package org.magnum.mcc.building.locating;


import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.persistence.BeaconObservation;
import org.magnum.mcc.building.persistence.BeaconsLoader;
import org.magnum.mcc.building.persistence.MCCObjectMapper;
import org.magnum.mcc.building.persistence.BeaconsAtFloorplanLocation;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



public class BeaconProbabalisticLocator  implements ProbabalisticLocator  {

	private final MCCObjectMapper mapper_ = new MCCObjectMapper();
	
	@Override
	public FloorplanLocation locateBy(String jsonData, Floorplan floorplan, BeaconsLoader loader, String floorplanId) throws JsonParseException, JsonMappingException, IOException {
		BeaconDataObservations observedBeacons = mapper_.readValue(jsonData, BeaconDataObservations.class);
		List<BeaconData> beacons = observedBeacons.observations;
		
		
		return mostLikelyLocation(beacons, floorplan, loader, floorplanId);
	}
	
	private FloorplanLocation mostLikelyLocation(List<BeaconData> observedBeacons, Floorplan floorplan, BeaconsLoader loader, String floorplanId){
		String locationId =null;
		int mostMatches = 0;
		Set<String> strObservedIds = new TreeSet<String>();
		for (BeaconData observedBeacon: observedBeacons){
			strObservedIds.add(observedBeacon.getAggregateId());
			//debug
			System.out.println("Observed beacon id "+observedBeacon.getAggregateId());
		}
		
		Set<BeaconsAtFloorplanLocation> beaconsAtLocations = loader.getBeacons(floorplanId);
		for (BeaconsAtFloorplanLocation beaconsAtLocation: beaconsAtLocations){
			Set<BeaconObservation> idsAtLocation = beaconsAtLocation.getBeaconIds();
			Set<String> strIdsAtLocation = new TreeSet<String>();
			for (BeaconObservation possibleBeacon: idsAtLocation){
				strIdsAtLocation.add(possibleBeacon.getBeaconId());
			}
			
			//debug 
			for (String s: strIdsAtLocation)
				System.out.println("Ids at location "+beaconsAtLocation.getLocationId()+" include "+s);
			strIdsAtLocation.retainAll(strObservedIds);
			int count = strIdsAtLocation.size();
			if (count >= mostMatches){
				mostMatches = count;
				//debug
				System.out.println("count is "+count);
				locationId = beaconsAtLocation.getLocationId();
			}
			
		}
		return floorplan.locationById(locationId);
	}
	
	
}
