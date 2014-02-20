/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import org.magnum.mcc.building.FloorplanImageMapping;

public interface FloorplanImageMappingMarshaller {

	public String toString(FloorplanImageMapping fp) throws Exception;

	public FloorplanImageMapping fromString(String fp) throws Exception;

}