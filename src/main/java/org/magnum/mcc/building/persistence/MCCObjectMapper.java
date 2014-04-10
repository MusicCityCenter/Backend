/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.locating.BeaconDataDeserializer;
import org.magnum.mcc.building.locating.BeaconDataObservations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;


public class MCCObjectMapper extends ObjectMapper {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MCCObjectMapper(){
		SimpleModule module = new SimpleModule();
		module.addSerializer(new FloorplanSerializer());
		module.addDeserializer(Floorplan.class, new FloorplanDeserializer());
		module.addDeserializer(BeaconDataObservations.class, new BeaconDataDeserializer());
		registerModule(module);
	}
}
