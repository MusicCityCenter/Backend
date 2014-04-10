/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import java.io.IOException;
import java.util.Map;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.LocationType;
import org.magnum.mcc.paths.EdgeData;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class FloorplanSerializer extends JsonSerializer<Floorplan> {

	@Override
	public Class<Floorplan> handledType() {
		return Floorplan.class;
	}

	@Override
	public void serialize(Floorplan floorplan, JsonGenerator gen,
			SerializerProvider arg2) throws IOException,
			JsonProcessingException {
		gen.writeStartObject();

		serializeFloorplanLocations(floorplan, gen);
		serializeFloorplanEdges(floorplan, gen);
		serializeLocationTypes(floorplan.getRootType(), gen);

		gen.writeEndObject();

	}

	private void serializeLocationTypes(LocationType root, JsonGenerator gen)
			throws IOException, JsonProcessingException {
		gen.writeFieldName("types");
		serializeLocationType(root, gen);
	}

	private void serializeLocationType(LocationType type, JsonGenerator gen)
			throws IOException, JsonProcessingException {
		gen.writeStartObject();
		gen.writeStringField("name", type.getName());
		if (type.getChildren() != null && type.getChildren().size() > 0) {
			gen.writeArrayFieldStart("children");
			for (LocationType child : type.getChildren()) {
				serializeLocationType(child, gen);
			}
			gen.writeEndArray();
		}
		gen.writeEndObject();
	}

	private void serializeFloorplanLocations(Floorplan floorplan,
			JsonGenerator gen) throws IOException, JsonGenerationException {

		gen.writeArrayFieldStart("locations");

		for (FloorplanLocation loc : floorplan.getLocations()) {
			serializeFloorplanLocation(gen, loc);
		}

		gen.writeEndArray();
	}

	private void serializeFloorplanEdges(Floorplan floorplan, JsonGenerator gen)
			throws IOException, JsonGenerationException {
		gen.writeArrayFieldStart("edges");

		for (FloorplanLocation loc : floorplan.getLocations()) {

			Map<FloorplanLocation, EdgeData> edges = floorplan.getEdgesFrom(loc);
			for (FloorplanLocation end : edges.keySet()) {
				EdgeData eData = edges.get(end);
				Double w = eData.getLength();
				String imageId = eData.getImageId();
				serializeFloorplanEdge(gen, loc, end, w, imageId);
				
				//corresponding edge
				//EdgeData other = floorplan.getEdgesFrom(end).get(loc);
				//String otherImage = other.getImageId();
				//serializeFloorplanEdge(gen, end, loc, w, otherImage);
			}
		}
		gen.writeEndArray();
	}

	private void serializeFloorplanEdge(JsonGenerator gen,
			FloorplanLocation loc, FloorplanLocation end, Double w, String edgeImageId)
			throws IOException, JsonGenerationException {
		gen.writeStartObject();
		gen.writeStringField("start", loc.getId());
		gen.writeStringField("end", end.getId());
		gen.writeNumberField("weight", w);
		gen.writeStringField("image", edgeImageId);
		gen.writeEndObject();
	}

	private void serializeFloorplanLocation(JsonGenerator gen,
			FloorplanLocation loc) throws IOException, JsonGenerationException {
		gen.writeStartObject();
		gen.writeStringField("id", loc.getId());
		gen.writeStringField("type", loc.getLocationType().getName());
		gen.writeEndObject();
	}

}
