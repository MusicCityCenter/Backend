/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;
import org.magnum.mcc.building.persistence.FloorplanJsonMarshaller;
import org.magnum.mcc.paths.EdgeData;

public class FloorplanJsonMarshallerTest {

	private final LocationType rootType_ = new LocationType("root", null);
	private final LocationType hallwayType = new LocationType("hallway",rootType_);
	private final LocationType roomType = new LocationType("room", rootType_);
	private final LocationType bedroomType = new LocationType("bedroom", roomType);
	private final LocationType kitchenType = new LocationType("kitchen", roomType);
	private final LocationType bathroomType = new LocationType("bathroom", roomType);
	
	private final Floorplan floorPlan_ = new Floorplan(rootType_);
	
	private final FloorplanJsonMarshaller marshaller_ = new FloorplanJsonMarshaller();

	@Before
	public void setUp() {
		FloorplanLocation bedroom1 = floorPlan_.addLocation("bedroom1", bedroomType);
		FloorplanLocation bedroom2 = floorPlan_.addLocation("bedroom2", bedroomType);
		FloorplanLocation bedroom3 = floorPlan_.addLocation("bedroom3", bedroomType);
		floorPlan_.addLocation("bedroom4", bedroomType);
		floorPlan_.addLocation("kitchen", kitchenType);
		FloorplanLocation bathroom1 = floorPlan_.addLocation("bathroom1", bathroomType);
		floorPlan_.addLocation("bathroom2", bathroomType);
		floorPlan_.addLocation("bathroom3", bathroomType);
		FloorplanLocation uhall = floorPlan_.addLocation("upstairsHallway", hallwayType);
		floorPlan_.addLocation("downstairsHallway", hallwayType);
		
		floorPlan_.connectsTo(bathroom1, bedroom1, 1,"");
		floorPlan_.connectsTo(bedroom1, uhall, 1,"");
		floorPlan_.connectsTo(bedroom2, uhall, 1,"");
		floorPlan_.connectsTo(bedroom3, uhall, 1,"");
		floorPlan_.connectsTo(bedroom1,bathroom1, 1,"");
		floorPlan_.connectsTo(uhall,bedroom1,  1,"");
		floorPlan_.connectsTo(uhall,bedroom2, 1,"");
		floorPlan_.connectsTo(uhall,bedroom3, 1,"");
	}
	
	@Test
	public void test() throws Exception {
		
		String obj = marshaller_.toString(floorPlan_);
		Floorplan in = marshaller_.fromString(obj);
		
		assertThat(in.getLocations(), is(floorPlan_.getLocations()));
		assertThat(in.getRootType(), is(floorPlan_.getRootType()));
		
		for(FloorplanLocation loc : in.getLocations()){
			Map<FloorplanLocation, EdgeData> testEdges = floorPlan_.getEdgesFrom(loc);
			Map<FloorplanLocation, EdgeData> actualEdges = in.getEdgesFrom(loc);
			for(FloorplanLocation location : testEdges.keySet()){
				assertThat(testEdges.get(location).getLength(), is(actualEdges.get(location).getLength()));
				assertThat(testEdges.get(location).getImageId(), is(actualEdges.get(location).getImageId()));
			}
			//assertThat(in.getEdgesFrom(loc), is(floorPlan_.getEdgesFrom(loc)));
		}
		
		String obj2 = marshaller_.toString(in);
		
		assertThat(obj2.length(), is(obj.length()));
	}

}
