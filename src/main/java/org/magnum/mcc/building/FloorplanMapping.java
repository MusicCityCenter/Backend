/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building;

public class FloorplanMapping {

	private final Floorplan floorplan_;

	private final FloorplanImageMapping imageMapping_;

	public FloorplanMapping(Floorplan floorplan,
			FloorplanImageMapping imageMapping) {
		super();
		floorplan_ = floorplan;
		imageMapping_ = imageMapping;
	}

	public Floorplan getFloorplan() {
		return floorplan_;
	}

	public FloorplanImageMapping getMapping() {
		return imageMapping_;
	}

}
