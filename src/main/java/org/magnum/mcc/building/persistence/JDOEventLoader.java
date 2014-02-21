/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class JDOEventLoader implements EventLoader {

	private PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Set<Event> getEventsOn(String floorplanId, int month, int day, int year) {
		
		Set<Event> events = null;
		final PersistenceManager pm = getPersistenceManager();
		try{
			Query query = pm.newQuery(Event.class);
			query.setFilter("floorplanId == f && year == y && month == m && day == d");
			query.declareParameters("String f,int y,int m,int d");
			events = new HashSet<Event>((List<Event>)query.executeWithArray(floorplanId,year,month,day));
			
		}finally{
			pm.close();
		}
		return events;
	}

	@Override
	public void saveEvent(Event evt) {
		final PersistenceManager pm = getPersistenceManager();
		try{
			pm.makePersistent(evt);
		}finally{
			pm.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Event> getEventsInLocationOn(String floorplanId,
			String locationId, int month, int day, int year) {

		Set<Event> events = null;
		final PersistenceManager pm = getPersistenceManager();
		try{
			Query query = pm.newQuery(Event.class);
			query.setFilter("floorplanId == f && locationId == l && day == d && month == m && year == y");
			query.declareParameters("String f, String l, int d, int m, int y");
			events = (Set<Event>)query.execute(new Object[]{floorplanId, locationId, month, day, year});
		}finally{
			pm.close();
		}
		return events;
	}

	@Override
	public void deleteEvent(String id) {
		final PersistenceManager pm = getPersistenceManager();
		try{
			Event evt = pm.getObjectById(Event.class, id);
			pm.deletePersistent(evt);
		}finally{
			pm.close();
		}
	}

}
