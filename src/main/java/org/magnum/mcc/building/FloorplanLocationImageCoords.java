/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * This class contains the coordinates where a FloorplanLocation
 * appears on an image. This class is used to map between locations
 * and a specific jpg/png of a floorplan.
 * 
 * @author jules
 *
 */
public class FloorplanLocationImageCoords {

	private final int x;
	private final int y;

	@JsonCreator
	public FloorplanLocationImageCoords(@JsonProperty("x") int x, @JsonProperty("y") int y) {
		super();
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(x,y);
	}

	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof FloorplanLocationImageCoords) &&
				((FloorplanLocationImageCoords)obj).x == x &&
				((FloorplanLocationImageCoords)obj).y == y;
	}

}
