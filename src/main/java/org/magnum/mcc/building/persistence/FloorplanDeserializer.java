/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class FloorplanDeserializer extends JsonDeserializer<Floorplan> {

	@Override
	public Floorplan deserialize(JsonParser jsonParser,
			DeserializationContext arg1) throws IOException,
			JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);

		Map<String, LocationType> types = new HashMap<String, LocationType>();
		LocationType root = deserializeTypes(node, types);
		
		Floorplan floorplan = new Floorplan(root);

		deserializeLocations(node, floorplan, types);
		deserializeEdges(node, floorplan);

		return floorplan;
	}
	
	private LocationType deserializeTypes(JsonNode node, Map<String,LocationType> types){
		JsonNode root = node.get("types");
		return deserializeType(root, null, types);
	}
	
	private LocationType deserializeType(JsonNode node, LocationType parent, Map<String,LocationType> types){
		String name = node.get("name").textValue();
		
		LocationType rt = new LocationType(name, parent);
		types.put(name, rt);
		JsonNode children = node.get("children");
		if(children != null){
			for(JsonNode child : children){
				deserializeType(child, rt, types);
			}
		}
		return rt;
	}

	private void deserializeEdges(JsonNode node, Floorplan floorplan) {
		JsonNode edges = node.get("edges");
		for (JsonNode edge : edges) {
			deserializeEdge(floorplan, edge);
		}
	}

	private void deserializeEdge(Floorplan floorplan, JsonNode edge) {
		String start = edge.get("start").textValue();
		String end = edge.get("end").textValue();
		Double len = edge.get("weight").asDouble();
		JsonNode tmp = edge.get("imageId");
		String imageId;
		if (tmp ==null)
			imageId="";
		else
			imageId = tmp.textValue();

		FloorplanLocation s = floorplan.locationById(start);
		FloorplanLocation e = floorplan.locationById(end);

		floorplan.connectsTo(s, e, len, imageId);
	}

	private void deserializeLocations(JsonNode node, Floorplan floorplan, Map<String,LocationType> types) {
		

		JsonNode locations = node.get("locations");
		for (JsonNode loc : locations) {
			deserializeLocation(floorplan, types, loc);
		}
	}

	private void deserializeLocation(Floorplan floorplan,
			Map<String, LocationType> types, JsonNode loc) {
		String name = loc.get("id").textValue();
		String type = loc.get("type").textValue();

		LocationType lt = types.get(type);

		if (lt == null) {
			lt = new LocationType(type, floorplan.getRootType());
			types.put(type, lt);
		}

		floorplan.addLocation(name, lt);
	}

}
