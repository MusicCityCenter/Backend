/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class FloorplanLocationsNearBeacon {

	@Persistent
	@PrimaryKey
	private String beaconId;

	@Persistent
	private String floorplanId;

	@Persistent
	private Set<String> floorplanLocationIds = new HashSet<String>();

	public String getBeaconId() {
		return beaconId;
	}

	public void setBeaconId(String beaconId) {
		this.beaconId = beaconId;
	}

	public String getFloorplanId() {
		return floorplanId;
	}

	public void setFloorplanId(String floorplanId) {
		this.floorplanId = floorplanId;
	}

	public Set<String> getFloorplanLocationIds() {
		return floorplanLocationIds;
	}

	public void setFloorplanLocationIds(Set<String> floorplanLocationIds) {
		this.floorplanLocationIds = floorplanLocationIds;
	}

}
