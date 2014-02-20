/*************************************************************************
* Copyright 2014 Jules White
* 
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at http://www.apache.org/licenses/
* LICENSE-2.0 Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an "AS IS"
* BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
* implied. See the License for the specific language governing permissions
* and limitations under the License.
**************************************************************************/
package org.magnum.mcc.building.persistence;

import org.magnum.mcc.building.Floorplan;

/**
 * This class converts Floorplans to Json and Json to Floorplans. Although it
 * would have been theoretically possible to use an object mapping library, such
 * as Jackson, the internal structure of FloorplanLocation and DirectedGraph
 * make using Jackson non-trivial. The simplest solution (for now) is to use the
 * hand-written marshalling code below.
 * 
 * 
 * @author jules
 * 
 */
public class FloorplanJsonMarshaller implements FloorplanMarshaller {

	private final MCCObjectMapper mapper_ = new MCCObjectMapper();
	
	public FloorplanJsonMarshaller(){
	}
	
	@Override
	public String toString(Floorplan fp) throws Exception {
		return mapper_.writeValueAsString(fp);
	}

	@Override
	public Floorplan fromString(String fp) throws Exception {
		return mapper_.readValue(fp, Floorplan.class);
	}

}
