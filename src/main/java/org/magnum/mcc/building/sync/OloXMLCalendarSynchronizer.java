/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.sync;

import java.io.IOException;
import java.util.List;

import org.magnum.mcc.building.persistence.Event;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class OloXMLCalendarSynchronizer implements EventCalendarSynchronizer {

	public static class XMLEventSpec {
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

	}

	@Override
	public void syncCalendar() {
		// TODO Auto-generated method stub

	}

	public List<XMLEventSpec> parseRemoteCalendar(String cal) throws JsonParseException, JsonMappingException, IOException {
		XmlMapper mapper = new XmlMapper();
		List<XMLEventSpec> eventSpecs = mapper.readValue(cal, new TypeReference<List<XMLEventSpec>>() { });
		
		return eventSpecs;
	}

}
