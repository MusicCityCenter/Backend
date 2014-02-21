/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import java.util.UUID;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Lob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Event {

	@PrimaryKey
	@Persistent
	private String id = UUID.randomUUID().toString();

	@Persistent
	private String name;

	@Persistent
	@Lob
	private Text description;

	@Persistent
	private int day;

	@Persistent
	private int month;

	@Persistent
	private int year;

	@Persistent
	private long startTime;

	@Persistent
	private long endTime;

	@Persistent
	private String floorplanId;

	@Persistent
	private String floorplanLocationId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description.getValue();
	}

	public void setDescription(String description) {
		this.description = new Text(description);
	}

	public String getFloorplanLocationId() {
		return floorplanLocationId;
	}

	public void setFloorplanLocationId(String floorplanLocationId) {
		this.floorplanLocationId = floorplanLocationId;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getFloorplanId() {
		return floorplanId;
	}

	public void setFloorplanId(String floorplanId) {
		this.floorplanId = floorplanId;
	}

}
