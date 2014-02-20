/* 
**
** Copyright 2014, Jules White
**
** 
*/
package org.magnum.mcc.building.persistence;

import javax.jdo.PersistenceManager;

import org.magnum.mcc.building.FloorplanImageMapping;

import com.google.appengine.api.datastore.Text;
import com.google.inject.Inject;

public class JDOFloorplanImageMappingLoader implements
		FloorplanImageMappingLoader {

	private final FloorplanImageMappingMarshaller marshaller_;
	
	@Inject
	public JDOFloorplanImageMappingLoader(
			FloorplanImageMappingMarshaller marshaller) {
		super();
		marshaller_ = marshaller;
	}

	@Override
	public FloorplanImageMapping load(String id) {

		final PersistenceManager pm = getPersistenceManager();
		final StoredFloorplanImageMapping sf = pm.getObjectById(StoredFloorplanImageMapping.class, id);
		final String json = sf.getMappingJson().getValue();
		FloorplanImageMapping fp = null;

		try {
			fp = marshaller_.fromString(json);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pm.close();
		}

		return fp;
	}

	@Override
	public void save(String id, FloorplanImageMapping fp) {
		final PersistenceManager pm = getPersistenceManager();
		try {
			final StoredFloorplanImageMapping sf = new StoredFloorplanImageMapping();
			sf.setId(id);
			final String json = marshaller_.toString(fp);
			sf.setMappingJson(new Text(json));
			pm.makePersistent(sf);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			pm.close();
		}
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

}
