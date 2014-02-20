/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import org.magnum.mcc.building.FloorplanImageMapping;

public interface FloorplanImageMappingLoader {

	public FloorplanImageMapping load(String id);
	
	public void save(String id, FloorplanImageMapping fp);
	
}
