/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.sync;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class CalendarSyncSettings {

	@PrimaryKey
	@Persistent
	private String id;

	@Persistent
	private String remoteCalendarUrl;

	@Persistent
	private boolean syncEnabled;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRemoteCalendarUrl() {
		return remoteCalendarUrl;
	}

	public void setRemoteCalendarUrl(String remoteCalendarUrl) {
		this.remoteCalendarUrl = remoteCalendarUrl;
	}

	public boolean isSyncEnabled() {
		return syncEnabled;
	}

	public void setSyncEnabled(boolean syncEnabled) {
		this.syncEnabled = syncEnabled;
	}

}
