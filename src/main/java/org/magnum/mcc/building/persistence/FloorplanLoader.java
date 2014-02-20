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
 * This class loads and saves floorplans. Implementers of this interface
 * provide either database or file based storage of floorplans.
 * 
 * @author jules
 *
 */
public interface FloorplanLoader {

	public Floorplan load(String id);
	
	public void save(String id, Floorplan fp);
	
}
