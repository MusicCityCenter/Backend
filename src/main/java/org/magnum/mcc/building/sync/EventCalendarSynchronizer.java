/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.sync;

public interface EventCalendarSynchronizer {

	public void syncCalendar(String floorplanId);
	
	public void syncAllCalendars();
	
	public void enableCalendarSync(CalendarSyncSettings settings);
	
	public void disableCalendarSync(String floorplanId);
}
