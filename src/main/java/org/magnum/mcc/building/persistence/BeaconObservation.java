/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.api.datastore.Key;
import com.google.common.base.Objects;

@PersistenceCapable
public class BeaconObservation {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String beaconId;

	@Persistent
	private String distance;

	@JsonIgnore
	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getBeaconId() {
		return beaconId;
	}

	public void setBeaconId(String beaconId) {
		this.beaconId = beaconId;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String observedValue) {
		this.distance = observedValue;
	}

	@Override
	public int hashCode() {
		return beaconId.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof BeaconObservation
				&& ((BeaconObservation) obj).beaconId.equals(beaconId);
	}

	public String toString() {
		return Objects.toStringHelper(getClass())
				.add("beaconId", this.beaconId).add("distance", this.distance)
				.toString();
	}
}
