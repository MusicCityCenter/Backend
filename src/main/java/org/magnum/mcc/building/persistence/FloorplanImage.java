/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;

@PersistenceCapable
public class FloorplanImage {

	@PrimaryKey
	@Persistent
	private String id;

	@Persistent
	private Blob data;

	@Persistent
	private Double scale;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Blob getData() {
		return data;
	}

	public void setData(Blob data) {
		this.data = data;
	}

	public Double getScale() {
		return scale;
	}

	public void setScale(Double scale) {
		this.scale = scale;
	}

}
