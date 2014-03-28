/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import java.io.InputStream;

public interface PersistentImageLoader {

	public PersistentImage load(String id);
	
	public void save(String id, Double scale, InputStream data);
	
}
