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

import com.google.appengine.api.datastore.Text;

@PersistenceCapable
public class Conference {

	@PrimaryKey
	@Persistent
	private String id = UUID.randomUUID().toString();

	@Persistent
	private String name;

	@Persistent
	@Lob
	private Text description;

	@Persistent
	private String floorplanId;

	@Persistent
	private int startDay;

	@Persistent
	private int startMonth;

	@Persistent
	private int startYear;

	@Persistent
	private int endDay;

	@Persistent
	private int endMonth;

	@Persistent
	private int endYear;

	@Persistent
	private long startTime;

	@Persistent
	private long endTime;

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

	public int getStartDay() {
		return startDay;
	}

	public void setStartDay(int startDay) {
		this.startDay = startDay;
	}

	public int getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(int startMonth) {
		this.startMonth = startMonth;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndDay() {
		return endDay;
	}

	public void setEndDay(int endDay) {
		this.endDay = endDay;
	}

	public int getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(int endMonth) {
		this.endMonth = endMonth;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public void setDescription(Text description) {
		this.description = description;
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
