package org.magnum.mcc.building.locating;

import java.io.IOException;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.persistence.BeaconsLoader;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ProbabalisticLocator {
	public FloorplanLocation locateBy(String jsonData, Floorplan floorplan, BeaconsLoader loader, String floorplanId) throws JsonParseException, JsonMappingException, IOException;
}
