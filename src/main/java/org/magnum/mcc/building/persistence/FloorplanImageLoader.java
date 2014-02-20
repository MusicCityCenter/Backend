/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import java.io.InputStream;

public interface FloorplanImageLoader {

	public FloorplanImage load(String floorplanId);
	
	public void save(String floorplanId, Double scale, InputStream data);
	
}
