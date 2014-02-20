/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import org.magnum.mcc.building.FloorplanImageMapping;

public class FloorplanImageMappingJsonMarshaller implements FloorplanImageMappingMarshaller {

	private final MCCObjectMapper mapper_ = new MCCObjectMapper();
	
	public FloorplanImageMappingJsonMarshaller(){
	}
	
	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.loader.FloorplanImageMappingMarshaller#toString(org.magnum.mcc.building.FloorplanImageMapping)
	 */
	@Override
	public String toString(FloorplanImageMapping fp) throws Exception {
		return mapper_.writeValueAsString(fp);
	}

	/* (non-Javadoc)
	 * @see org.magnum.mcc.building.loader.FloorplanImageMappingMarshaller#fromString(java.lang.String)
	 */
	@Override
	public FloorplanImageMapping fromString(String fp) throws Exception {
		return mapper_.readValue(fp, FloorplanImageMapping.class);
	}
}
