/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class FloorplanImageMapping {

	private Map<String, FloorplanLocationImageCoords> mapping = new HashMap<String, FloorplanLocationImageCoords>();
	
	private final String imageUrl;

	@JsonCreator
	public FloorplanImageMapping(@JsonProperty("imageUrl") String imageUrl) {
		super();
		this.imageUrl = imageUrl;
	}
	
	public void mapLocation(FloorplanLocation loc, int x, int y){
		this.mapping.put(loc.getId(), new FloorplanLocationImageCoords(x, y));
	}
	
	public FloorplanLocationImageCoords getLocation(FloorplanLocation loc){
		return this.mapping.get(loc.getId());
	}
	
	/**
	 * Returns the angle of an edge from the X axis in degrees
	 * from 0 to 360. A line from 0,0 to 10,0 would have an angle
	 * of 0. A line from 0,0 to 0,10 would have an angle of 90.
	 * 
	 * @param edge
	 * @return
	 */
	public double getEdgeAngle(FloorplanLocationEdge edge){
		FloorplanLocationImageCoords n1 = getLocation(edge.getStart());
		FloorplanLocationImageCoords n2 = getLocation(edge.getEnd());
		
		double angle = Math.atan2(n1.getY() - n2.getY(), n2.getX() - n1.getX());
    	if(angle < 0){angle += (2 * Math.PI);}
    	angle *= 180 / Math.PI;
    	
    	return angle;
	}
	
	public double getEdgeLength(FloorplanLocationEdge edge){
		//FloorplanLocationImageCoords n1 = getLocation(edge.getStart());
		//FloorplanLocationImageCoords n2 = getLocation(edge.getEnd());
		
		//return Math.sqrt(((double)(Math.pow(n1.getX() - n2.getX(),2) + (Math.pow(n1.getY() - n2.getY(),2))))); 
		return edge.getLength();
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Map<String, FloorplanLocationImageCoords> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, FloorplanLocationImageCoords> mapping) {
		this.mapping = mapping;
	}
}
