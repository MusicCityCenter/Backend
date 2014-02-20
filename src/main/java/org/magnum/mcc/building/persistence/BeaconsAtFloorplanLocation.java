/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Element;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.CharMatcher;
import com.google.common.base.Objects;

@PersistenceCapable
@FetchGroup(name = "beaconGroup", members = { @Persistent(name = "beaconIds") }) 
public class BeaconsAtFloorplanLocation {

	public static String getId(String fid, String lid) {
		lid = CharMatcher.JAVA_LETTER_OR_DIGIT.retainFrom(lid);
		fid = CharMatcher.JAVA_LETTER_OR_DIGIT.retainFrom(fid);
		return fid + "." + lid;
	}
	

	@PrimaryKey
	@Persistent
	private String id;

	@Persistent
	@Element(dependent = "true")
	private Set<BeaconObservation> beaconIds = new HashSet<BeaconObservation>();

	@Persistent
	private String floorplanId;

	@Persistent
	private String locationId;

	public BeaconsAtFloorplanLocation() {
	}

	@JsonCreator
	public BeaconsAtFloorplanLocation(@JsonProperty("floorplanId") String fid,
			@JsonProperty("locationId") String lid) {
		this.id = getId(fid, lid);
		this.floorplanId = fid;
		this.locationId = lid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<BeaconObservation> getBeaconIds() {
		return beaconIds;
	}

	public void setBeaconIds(Set<BeaconObservation> beaconIds) {
		this.beaconIds = beaconIds;
	}

	public String getFloorplanId() {
		return floorplanId;
	}

	public void setFloorplanId(String floorplanId) {
		this.floorplanId = floorplanId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String toString(){
		return Objects.toStringHelper(getClass())
			.add("floorplanId", this.floorplanId)
			.add("locationId", this.locationId)
			.add("beacons", this.beaconIds)
			.toString();
	}
}
