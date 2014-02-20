/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import org.magnum.mcc.building.Floorplan;

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
		registerModule(module);
	}
}
