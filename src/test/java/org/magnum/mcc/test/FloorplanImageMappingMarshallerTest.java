/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanImageMapping;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.FloorplanLocationImageCoords;
import org.magnum.mcc.building.LocationType;
import org.magnum.mcc.building.persistence.FloorplanImageMappingJsonMarshaller;
import org.magnum.mcc.building.persistence.FloorplanImageMappingMarshaller;

public class FloorplanImageMappingMarshallerTest {
	private final LocationType rootType_ = new LocationType("root", null);
	private final LocationType hallwayType = new LocationType("hallway",
			rootType_);
	private final LocationType roomType = new LocationType("room", rootType_);
	private final LocationType bedroomType = new LocationType("bedroom",
			roomType);
	private final LocationType kitchenType = new LocationType("kitchen",
			roomType);
	private final LocationType bathroomType = new LocationType("bathroom",
			roomType);

	private final Floorplan floorPlan_ = new Floorplan(rootType_);

	private final FloorplanImageMappingMarshaller marshaller_ = new FloorplanImageMappingJsonMarshaller();
	
	private final FloorplanImageMapping mapping_ = new FloorplanImageMapping("http://test/foo.jpg");

	@Before
	public void setUp() {
		FloorplanLocation bedroom1 = floorPlan_.addLocation("bedroom1",
				bedroomType);
		FloorplanLocation bedroom2 = floorPlan_.addLocation("bedroom2",
				bedroomType);
		floorPlan_.addLocation("bedroom3",
				bedroomType);
		floorPlan_.addLocation("bedroom4", bedroomType);
		floorPlan_.addLocation("kitchen", kitchenType);
		FloorplanLocation bathroom1 = floorPlan_.addLocation("bathroom1",
				bathroomType);
		floorPlan_.addLocation("bathroom2", bathroomType);
		floorPlan_.addLocation("bathroom3", bathroomType);
		floorPlan_.addLocation("upstairsHallway",
				hallwayType);
		floorPlan_.addLocation("downstairsHallway", hallwayType);

		mapping_.mapLocation(bathroom1, 10, 10);
		mapping_.mapLocation(bedroom1, 23, 780);
		mapping_.mapLocation(bedroom2, 213, 800);
	}

	@Test
	public void test() throws Exception {
		String json = marshaller_.toString(mapping_);
		FloorplanImageMapping out = marshaller_.fromString(json);
		
		assertEquals(out.getImageUrl(), mapping_.getImageUrl());
		
		for(FloorplanLocation loc : floorPlan_.getLocations()){
			FloorplanLocationImageCoords coords = out.getLocation(loc);
			FloorplanLocationImageCoords correct = mapping_.getLocation(loc);
			assertEquals(coords, correct);
		}
		
		// Sanity check
		FloorplanLocation bedroom1 = floorPlan_.locationById("bedroom1");
		FloorplanLocationImageCoords coords = out.getLocation(bedroom1);
		FloorplanLocationImageCoords correct = mapping_.getLocation(bedroom1);
		assertEquals(coords, correct);
		assertEquals(coords.getX(), 23);
		assertEquals(coords.getY(), 780);
	}

}
