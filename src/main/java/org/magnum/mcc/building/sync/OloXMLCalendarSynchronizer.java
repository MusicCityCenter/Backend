/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.sync;

import static com.google.common.base.Preconditions.checkArgument;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;

import org.joda.time.DateTime;
import org.magnum.mcc.building.persistence.Event;
import org.magnum.mcc.building.persistence.PMF;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class OloXMLCalendarSynchronizer implements EventCalendarSynchronizer {

	private PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

	public void syncAllCalendars() {
		@SuppressWarnings("unchecked")
		Collection<CalendarSyncSettings> all = (Collection<CalendarSyncSettings>) getPersistenceManager()
				.newQuery(CalendarSyncSettings.class).execute();
		for(CalendarSyncSettings settings : all){
			syncCalendar(settings);
		}
	}

	public void syncCalendar(CalendarSyncSettings settings) {
		try {
			List<Event> evts = fetchRawEvents(settings.getId(), settings.getRemoteCalendarUrl());
			getPersistenceManager().makePersistentAll(evts);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<Event> fetchRawEvents(String floorplanId, String url)
			throws MalformedURLException, IOException, JsonParseException,
			JsonMappingException {
		String rawcal = fetchCalendar(url);
		List<Event> evts = parseNativeEvents(floorplanId, rawcal);
		return evts;
	}

	@Override
	public void syncCalendar(String floorplanId) {
		PersistenceManager pm = getPersistenceManager();
		CalendarSyncSettings settings = pm.getObjectById(
				CalendarSyncSettings.class, floorplanId);

		syncCalendar(settings);
	}

	
	private List<Event> parseNativeEvents(String floorplanId, String caldata)
			throws JsonParseException, JsonMappingException, IOException {
		List<XMLEventSpec> evtspecs = parseRemoteCalendar(caldata);
		List<Event> evts = convertToNativeEvents(floorplanId, evtspecs);
		return evts;
	}

	private String fetchCalendar(String calUrl) throws MalformedURLException,
			IOException {
		URL url = new URL(calUrl);
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				url.openStream()));
		String line = null;

		StringBuffer buff = new StringBuffer();
		while ((line = reader.readLine()) != null) {
			buff.append(line);
			buff.append("\n");
		}
		reader.close();
		return buff.toString();
	}

	public List<Event> convertToNativeEvents(String floorplanId,
			List<XMLEventSpec> eventspecs) {
		List<Event> events = new ArrayList<>(eventspecs.size());
		for (XMLEventSpec spec : eventspecs) {

			Set<DateTime> when = spec.getEventDays();
			for (DateTime day : when) {
				try {
					Event evt = createEvent(floorplanId, day, null, spec);
					events.add(evt);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return events;
	}

	private Event createEvent(String floorplanId, DateTime when, DateTime endswhen,
			XMLEventSpec spec) {
		Event evt = new Event();
		evt.setName(spec.getName());
		evt.setConference(spec.getName());
		evt.setDescription(spec.getDescription());
		evt.setId(spec.getEventid());
		evt.setFloorplanId(floorplanId);
		evt.setFloorplanLocationId(spec.getLocation());
		evt.setDay(when.getDayOfMonth());
		evt.setMonth(when.getMonthOfYear());
		evt.setYear(when.getYear());

		endswhen = (endswhen == null)? new DateTime(when).withHourOfDay(23) : endswhen;
		
		int start = getEventTime(when);
		int end = getEventTime(endswhen);
		evt.setStartTime(start);
		evt.setEndTime(end);
		return evt;
	}

	private int getEventTime(DateTime when) {
		return when.getMinuteOfHour() + when.getHourOfDay()*60;
	}

	public List<XMLEventSpec> parseRemoteCalendar(String cal)
			throws JsonParseException, JsonMappingException, IOException {
		XmlMapper mapper = new XmlMapper();
		List<XMLEventSpec> eventSpecs = mapper.readValue(cal,
				new TypeReference<List<XMLEventSpec>>() {
				});

		return eventSpecs;
	}

	@Override
	public void enableCalendarSync(CalendarSyncSettings settings) {
		checkArgument(settings.getId() != null);
		checkArgument(settings.getRemoteCalendarUrl() != null);
		
		// Ensure the remote calendar can be reached before enabling sync
		try{
			URL url = new URL(settings.getRemoteCalendarUrl());
			url.openStream().close();
		}catch(Exception e){
			throw new RuntimeException("Unable to connect to the specified remote calendar", e);
		}
		
		settings.setSyncEnabled(true);
		getPersistenceManager().makePersistent(settings);
		
		syncCalendar(settings);
	}

	@Override
	public void disableCalendarSync(String floorplanId) {
		try{
			CalendarSyncSettings settings = getPersistenceManager().getObjectById(CalendarSyncSettings.class, floorplanId);
			getPersistenceManager().deletePersistent(settings);
		}catch(Exception e){
			// We don't care if it doesn't exist.
		}
	}

}
