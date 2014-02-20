/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import java.util.Set;

public interface BeaconsLoader {

	public void setBeaconMapping(String floorplanId, Set<BeaconsAtFloorplanLocation> beaconMapping);
	
	public void removeAllBeaconsAtLocation(String floorplanId, String locationId);
	
	public Set<BeaconObservation> getBeaconsAtLocation(String floorplanId, String locationId);
	
	public Set<String> getFloorplanLocationIdsAtBeacon(String beaconid);
	
	public Set<BeaconsAtFloorplanLocation> getBeacons(String floorplanid);
	
	public void save(BeaconsAtFloorplanLocation bal);
}
