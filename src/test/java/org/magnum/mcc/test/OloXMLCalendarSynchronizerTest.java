/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.magnum.mcc.building.sync.OloXMLCalendarSynchronizer;
import org.magnum.mcc.building.sync.OloXMLCalendarSynchronizer.XMLEventSpec;

public class OloXMLCalendarSynchronizerTest {

	private static final String TEST_DATA_MCCCALENDAR_XML = "test-data/mcccalendar.xml";

	@Test
	public void testXMLUnmarshalling() throws Exception {
		OloXMLCalendarSynchronizer sync = new OloXMLCalendarSynchronizer();
		List<XMLEventSpec> specs = sync.parseRemoteCalendar(FileUtils.readFileToString(new File(TEST_DATA_MCCCALENDAR_XML)));
	}

}
