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
package org.magnum.mcc.building;

import org.magnum.mcc.paths.Node;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * A location in a floorplan, such as a room, hallway, etc.
 * 
 * 
 * @author jules
 *
 */
public class FloorplanLocation extends Node {

	private final LocationType locationType_;
	
	@JsonCreator
	public FloorplanLocation(@JsonProperty("id") String id, @JsonProperty("type") LocationType type) {
		super(id);
		locationType_ = type;
	}

	public LocationType getLocationType() {
		return locationType_;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId(), getLocationType());
	}

	@Override
	public boolean equals(Object obj) {
		return (obj != null) && (obj instanceof FloorplanLocation) && ((FloorplanLocation)obj).getId().equals(getId())
				 && Objects.equal(((FloorplanLocation)obj).locationType_,locationType_);
	}

	
}
