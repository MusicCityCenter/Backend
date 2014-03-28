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

public class JDOPersistentImageLoader implements PersistentImageLoader {

	@Inject
	public JDOPersistentImageLoader() {
		super();
	}

	@Override
	public PersistentImage load(String id) {
		final PersistenceManager pm = getPersistenceManager();
		final PersistentImage sf = pm.getObjectById(PersistentImage.class, id);
		return sf;
	}

	@Override
	public void save(String id, Double scale, InputStream img) {
		final PersistenceManager pm = getPersistenceManager();
		try {
			final PersistentImage sf = new PersistentImage();
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
