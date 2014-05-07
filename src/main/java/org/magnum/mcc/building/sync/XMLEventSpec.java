/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class XMLEventSpec {
	private String date;
	private String datelong;
	private String name;
	private String url;
	private String open;
	private String begdate;
	private String enddate;
	private String begdatelong;
	private String enddatelong;
	private String description;
	private String location;
	private String nature;
	private String attendance;
	private String eventid;
	private List<String> days;
	private List<String> times;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDatelong() {
		return datelong;
	}

	public void setDatelong(String datelong) {
		this.datelong = datelong;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOpen() {
		return open;
	}

	public void setOpen(String open) {
		this.open = open;
	}

	public String getBegdate() {
		return begdate;
	}

	public void setBegdate(String begdate) {
		this.begdate = begdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getBegdatelong() {
		return begdatelong;
	}

	public void setBegdatelong(String begdatelong) {
		this.begdatelong = begdatelong;
	}

	public String getEnddatelong() {
		return enddatelong;
	}

	public void setEnddatelong(String enddatelong) {
		this.enddatelong = enddatelong;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public String getEventid() {
		return eventid;
	}

	public void setEventid(String eventid) {
		this.eventid = eventid;
	}

	public List<String> getDays() {
		return days;
	}

	public void setDays(List<String> days) {
		this.days = days;
	}

	public List<String> getTimes() {
		return times;
	}

	public void setTimes(List<String> times) {
		this.times = times;
	}

	public List<DateTime> getDateAndTimes() {
		DateTimeFormatter fmt = DateTimeFormat
				.forPattern("M/d/yyyy hh:mm a");
		List<DateTime> datetimes = new ArrayList<>(getTimes().size());
		for (String dt : getTimes()) {
			dt = dt.trim();
			dt = dt.replaceAll("\\/[\\s]*", "/");
			dt = dt.replaceAll("[\\s]*\\/", "/");
			dt = dt.replaceAll("[\\s]+", " ");
			datetimes.add(fmt.parseDateTime(dt));
		}

		Collections.sort(datetimes);

		return datetimes;
	}

	public Set<DateTime> getEventDays() {
		Set<String> unique = new HashSet<>();
		Set<DateTime> days = new HashSet<>();
		for (DateTime dt : getDateAndTimes()) {
			String key = dt.getMonthOfYear() + "-" + dt.getDayOfMonth()
					+ "-" + dt.getYear();
			if (!unique.contains(key)) {
				days.add(dt);
			}
		}
		return days;
	}

}