package org.magnum.mcc.building.locating;


import java.io.IOException;
import java.util.List;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.persistence.MCCObjectMapper;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;



public class BeaconProbabalisticLocator  implements ProbabalisticLocator  {

	private final MCCObjectMapper mapper_ = new MCCObjectMapper();
	
	@Override
	public FloorplanLocation locateBy(String jsonData, Floorplan floorplan) throws JsonParseException, JsonMappingException, IOException {
		BeaconDataObservations observedBeacons = mapper_.readValue(jsonData, BeaconDataObservations.class);
		List<BeaconData> beacons = observedBeacons.observations;
		
		
		return mostLikelyLocation(beacons, floorplan);
	}
	
	private FloorplanLocation mostLikelyLocation(List<BeaconData> observedBeacons, Floorplan floorplan){
		
		//TODO fix this implementation
		return floorplan.getLocations().iterator().next();
	}
	
	
}
