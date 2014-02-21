/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import java.util.Set;

public interface EventLoader {

	public Set<Event> getEventsOn(String floorplanId, int month, int day, int year);
	
	public Set<Event> getEventsInLocationOn(String floorplanId, String locationId, int month, int day, int year);
	
	public void saveEvent(Event evt);

	public void deleteEvent(String id);
}
