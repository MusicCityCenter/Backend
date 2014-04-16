/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.magnum.mcc.building.persistence;


import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanLocation;

import com.google.inject.Inject;

public class JDOBeaconsLoader implements BeaconsLoader {

	private final FloorplanLoader floorplanLoader_;

	@Inject
	public JDOBeaconsLoader(FloorplanLoader floorplanLoader) {
		super();
		floorplanLoader_ = floorplanLoader;
	}

	private PersistenceManager getPersistenceManager() {
		return PMF.get().getPersistenceManager();
	}

	@Override
	public void setBeaconMapping(String floorplanId,
			Set<BeaconsAtFloorplanLocation> beaconMapping) {

		final PersistenceManager pm = getPersistenceManager();

		try {

			Map<String, BeaconsAtFloorplanLocation> mapped = new TreeMap<String, BeaconsAtFloorplanLocation>();

			Floorplan fp = floorplanLoader_.load(floorplanId);
			assert (fp != null);
			
			//Get the current mapping, so that only new information will be overwritten
			Set<BeaconsAtFloorplanLocation> currentBeaconsAtLocations = getBeacons(floorplanId);
			for (BeaconsAtFloorplanLocation currentBeacons: currentBeaconsAtLocations){
				mapped.put(currentBeacons.getLocationId(), currentBeacons);
				
			}

			for (BeaconsAtFloorplanLocation bal : beaconMapping) {
				pm.makePersistent(bal);

				mapped.put(bal.getLocationId(), bal);

				for (BeaconObservation bid : bal.getBeaconIds()) {
					final String beacon = bid.getBeaconId();
					FloorplanLocationsNearBeacon near = null;

					try {
						near = pm.getObjectById(
								FloorplanLocationsNearBeacon.class, beacon);
					} catch (Exception e) {
					}

					if (near == null) {
						near = new FloorplanLocationsNearBeacon();
						near.setBeaconId(beacon);
						near.setFloorplanId(floorplanId);
					}
					near.getFloorplanLocationIds().add(bal.getLocationId());
					pm.makePersistent(near);
				}
			}

			for (FloorplanLocation loc : fp.getLocations()) {
				if (!mapped.containsKey(loc.getId())) {
					BeaconsAtFloorplanLocation bal = new BeaconsAtFloorplanLocation(
							floorplanId, loc.getId());
					pm.makePersistent(bal);
				}
			}

		} finally {
			pm.close();
		}

	}

	@Override
	public Set<BeaconObservation> getBeaconsAtLocation(String floorplanId,
			String locationId) {

		final PersistenceManager pm = getPersistenceManager();
		final String id = BeaconsAtFloorplanLocation.getId(floorplanId,
				locationId);
		BeaconsAtFloorplanLocation bal = null;
		try {
			bal = pm.getObjectById(BeaconsAtFloorplanLocation.class, id);
		} finally {
			pm.close();
		}

		return (bal != null) ? bal.getBeaconIds()
				: new TreeSet<BeaconObservation>();
	}

	@Override
	public Set<String> getFloorplanLocationIdsAtBeacon(String beaconid) {
		final PersistenceManager pm = getPersistenceManager();

		FloorplanLocationsNearBeacon near = null;
		try {
			near = pm.getObjectById(FloorplanLocationsNearBeacon.class,
					beaconid);
		} finally {
			pm.close();
		}
		return near.getFloorplanLocationIds();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<BeaconsAtFloorplanLocation> getBeacons(String floorplanid) {
		final PersistenceManager pm = getPersistenceManager();
		// pm.getFetchPlan().setGroup(FetchGroup.ALL);
		List<BeaconsAtFloorplanLocation> beacons = null;

		Floorplan fp = floorplanLoader_.load(floorplanid);
		assert (fp != null);

		Map<String, BeaconsAtFloorplanLocation> mapped = new TreeMap<String, BeaconsAtFloorplanLocation>();

		try {
			final Query q = pm.newQuery(BeaconsAtFloorplanLocation.class);
			q.setFilter("floorplanId == fid");
			q.declareParameters("String fid");

			beacons = (List<BeaconsAtFloorplanLocation>) q.execute(floorplanid);

			for (BeaconsAtFloorplanLocation b : beacons) {
				b.getBeaconIds();
				mapped.put(b.getLocationId(), b);
			}

		} finally {
			pm.close();
		}

		return (beacons != null) ? new TreeSet<BeaconsAtFloorplanLocation>(
				beacons) : new TreeSet<BeaconsAtFloorplanLocation>();
	}

	@Override
	public void removeAllBeaconsAtLocation(String floorplanId, String locationId) {
		final PersistenceManager pm = getPersistenceManager();
		try {
			BeaconsAtFloorplanLocation bal = pm.getObjectById(
					BeaconsAtFloorplanLocation.class,
					BeaconsAtFloorplanLocation.getId(floorplanId, locationId));
			
			for(BeaconObservation obs : bal.getBeaconIds()){
				FloorplanLocationsNearBeacon near = null;
				try {
					near = pm.getObjectById(
							FloorplanLocationsNearBeacon.class, obs.getBeaconId());
				} catch (Exception e) {
				}
				
				if(near != null){
					near.getFloorplanLocationIds().remove(locationId);
					pm.makePersistent(near);
				}
			}
			if(bal != null){
				pm.deletePersistent(bal);
			}
		} finally {
			pm.close();
		}
	}

	@Override
	public void save(BeaconsAtFloorplanLocation bal) {
		final PersistenceManager pm = getPersistenceManager();
		try {
			pm.makePersistent(bal);
		} finally {
			pm.close();
		}
	}

}
