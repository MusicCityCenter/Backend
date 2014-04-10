package org.magnum.mcc.building.locating;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

public class BeaconDataDeserializer extends JsonDeserializer<BeaconDataObservations>{
	
	@Override
	public BeaconDataObservations deserialize(JsonParser jsonParser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
		JsonNode allBeaconData = node.get("beaconData");
		BeaconDataObservations observedBeacons = new BeaconDataObservations();
		
		for (JsonNode beaconData: allBeaconData){
			String uuid = beaconData.get("uuid").asText();
			int major = beaconData.get("major").asInt();
			int minor = beaconData.get("minor").asInt();
			int rssi = beaconData.get("rssi").asInt();
			String distance = beaconData.get("distance").asText();
			double accuracy = beaconData.get("accuracy").asDouble();
			
			BeaconData observed = new BeaconData(uuid, major, minor, rssi, distance, accuracy);
			observedBeacons.observations.add(observed);
		}

		return observedBeacons;
	}

}
