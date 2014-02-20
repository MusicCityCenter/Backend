/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;

import java.io.InputStream;

import javax.jdo.PersistenceManager;

import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;
import com.google.inject.Inject;

public class JDOFloorplanImageLoader implements FloorplanImageLoader {

	@Inject
	public JDOFloorplanImageLoader() {
		super();
	}

	@Override
	public FloorplanImage load(String id) {
		final PersistenceManager pm = getPersistenceManager();
		final FloorplanImage sf = pm.getObjectById(FloorplanImage.class, id);
		return sf;
	}

	@Override
	public void save(String id, Double scale, InputStream img) {
		final PersistenceManager pm = getPersistenceManager();
		try {
			final FloorplanImage sf = new FloorplanImage();
			sf.setId(id);
			sf.setData(new Blob(IOUtils.toByteArray(img)));
			sf.setScale(scale);
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
