/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.junit.Test;
import org.magnum.mcc.building.persistence.Event;
import org.magnum.mcc.building.sync.OloXMLCalendarSynchronizer;
import org.magnum.mcc.building.sync.XMLEventSpec;

public class OloXMLCalendarSynchronizerTest {

	private static final String TEST_BUILDING = "test-building";
	private static final String TEST_DATA_MCCCALENDAR_XML = "test-data/mcccalendar.xml";

	@Test
	public void testXMLUnmarshalling() throws Exception {
		OloXMLCalendarSynchronizer sync = new OloXMLCalendarSynchronizer();
		List<XMLEventSpec> specs = sync.parseRemoteCalendar(FileUtils.readFileToString(new File(TEST_DATA_MCCCALENDAR_XML)));
		
		assertEquals(120, specs.size());
		
		List<Event> evts = sync.convertToNativeEvents(TEST_BUILDING, specs);
		
		int j = 0;
		for(int i = 0; i < specs.size(); i++){
			
			XMLEventSpec spec = specs.get(i);
			Set<DateTime> days = spec.getEventDays();
			
			for(DateTime day : days){
				Event evt = evts.get(j);
				assertEquals(evt.getName(), spec.getName());
				assertEquals(evt.getId(), spec.getEventid());
				assertEquals(evt.getDescription(), spec.getDescription());
				assertEquals(evt.getFloorplanLocationId(), spec.getLocation());
				assertEquals(evt.getFloorplanId(), TEST_BUILDING);
				assertEquals(evt.getDay(), day.getDayOfMonth());
				assertEquals(evt.getMonth(), day.getMonthOfYear());
				assertEquals(evt.getYear(), day.getYear());
				assertEquals(evt.getStartTime(),  day.getHourOfDay() * 60 + day.getMinuteOfHour());
				j++;
			}
		}
	}
	
	@Test
	public void testSyncFromURL() throws Exception {
		OloXMLCalendarSynchronizer sync = new OloXMLCalendarSynchronizer();
		File f = new File(TEST_DATA_MCCCALENDAR_XML);
		@SuppressWarnings("deprecation")
		URL url = f.toURL();
		List<Event> evts = sync.fetchRawEvents(TEST_BUILDING, url.toExternalForm());
		assertEquals(197, evts.size());
	}

	@Test
	public void testLiveCalFeedFromMCC() throws Exception {
		OloXMLCalendarSynchronizer sync = new OloXMLCalendarSynchronizer();
		String url = "http://olo.nashvillemusiccitycenter.com/calendar/mcccalendar.xml";
		List<Event> evts = sync.fetchRawEvents(TEST_BUILDING, url);
		assertTrue(evts.size() > 50);
	}
}
