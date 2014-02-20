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
package org.magnum.mcc.controllers;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.gmr.web.multipart.GMultipartFile;
import org.magnum.mcc.building.Floorplan;
import org.magnum.mcc.building.FloorplanImageMapping;
import org.magnum.mcc.building.FloorplanLocation;
import org.magnum.mcc.building.FloorplanLocationEdge;
import org.magnum.mcc.building.FloorplanMapping;
import org.magnum.mcc.building.PathData;
import org.magnum.mcc.building.persistence.BeaconsAtFloorplanLocation;
import org.magnum.mcc.building.persistence.BeaconsLoader;
import org.magnum.mcc.building.persistence.FloorplanImage;
import org.magnum.mcc.building.persistence.FloorplanImageLoader;
import org.magnum.mcc.building.persistence.FloorplanImageMappingLoader;
import org.magnum.mcc.building.persistence.FloorplanImageMappingMarshaller;
import org.magnum.mcc.building.persistence.FloorplanLoader;
import org.magnum.mcc.building.persistence.FloorplanMarshaller;
import org.magnum.mcc.building.persistence.MCCObjectMapper;
import org.magnum.mcc.modules.StandaloneServerModule;
import org.magnum.mcc.paths.Path;
import org.magnum.mcc.paths.ShortestPathSolver;
import org.magnum.mcc.paths.ShortestPaths;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class is the entry point for all HTTP requests to the MCC server.
 * 
 * 
 * @author jules
 * 
 */
@Controller
public class NavController {

	private final FloorplanMarshaller floorplanMarshaller_;
	private final FloorplanLoader floorplanLoader_;

	private final FloorplanImageMappingMarshaller imageMappingMarshaller_;
	private final FloorplanImageMappingLoader imageMappingLoader_;

	private final BeaconsLoader beaconsLoader_;

	private final FloorplanImageLoader imageLoader_;

	private ShortestPathSolver solver_;

	public NavController() {
		Injector injector = Guice.createInjector(new StandaloneServerModule());
		floorplanLoader_ = injector.getInstance(FloorplanLoader.class);
		solver_ = injector.getInstance(ShortestPathSolver.class);
		floorplanMarshaller_ = injector.getInstance(FloorplanMarshaller.class);
		imageMappingLoader_ = injector
				.getInstance(FloorplanImageMappingLoader.class);
		imageMappingMarshaller_ = injector
				.getInstance(FloorplanImageMappingMarshaller.class);
		imageLoader_ = injector.getInstance(FloorplanImageLoader.class);
		beaconsLoader_ = injector.getInstance(BeaconsLoader.class);
	}

	// A duplicate of the next method to make posting floorplans
	// via form without javascript possible. The key difference is
	// that this method accepts the floorplanId as a post parameter
	// rather than a path parameter.
	@RequestMapping(value = "/floorplan", method = RequestMethod.POST)
	public @ResponseBody
	Floorplan setFloorplanUsingForm(
			@RequestParam("floorplanId") String floorplanId,
			@RequestParam("floorplan") String floorplanjson) throws Exception {
		return setFloorplan(floorplanId, floorplanjson);
	}

	@RequestMapping(value = "/floorplan/{floorplanId}", method = RequestMethod.POST)
	public @ResponseBody
	Floorplan setFloorplan(@PathVariable("floorplanId") String floorplanId,
			@RequestParam("floorplan") String floorplanjson) throws Exception {

		// This is highly insecure. We are blindly trusting the client
		// and storing data it provides into the datastore. This data
		// should be highly sanitized here...
		Floorplan fp = floorplanMarshaller_.fromString(floorplanjson);

		syncFloorplanAndBeacons(floorplanId, fp);
		floorplanLoader_.save(floorplanId, fp);

		return fp;
	}

	/**
	 * Returns the raw floorplan definition for the specified ID.
	 * 
	 * 
	 * @param floorplanId
	 * @return
	 */
	@RequestMapping(value = "/floorplan/{floorplanId}", method = RequestMethod.GET)
	public @ResponseBody
	Floorplan getFloorplan(@PathVariable("floorplanId") String floorplanId) {
		return floorplanLoader_.load(floorplanId);
	}

	/**
	 * This endpoint is used for getting a floorplan with its associated image
	 * and mapping of locations to coordinates on the image.
	 * 
	 * @param floorplanId
	 * @return
	 */
	@RequestMapping(value = "/floorplan/mapping/{floorplanId}", method = RequestMethod.GET)
	public @ResponseBody
	FloorplanMapping getFloorplanMapping(
			@PathVariable("floorplanId") String floorplanId) {
		Floorplan fp = floorplanLoader_.load(floorplanId);
		FloorplanImageMapping mapping = imageMappingLoader_.load(floorplanId);
		return new FloorplanMapping(fp, mapping);
	}

	/**
	 * This endpoint is used to save a floorplan and its associated image and
	 * mapping of locations to coordinates on the image.
	 * 
	 * @param floorplanId
	 * @return
	 */
	@RequestMapping(value = "/floorplan/mapping/{floorplanId}", method = RequestMethod.POST)
	public @ResponseBody
	FloorplanMapping setFloorplanMapping(
			@PathVariable("floorplanId") String floorplanId,
			@RequestParam("floorplan") String floorplanjson,
			@RequestParam("mapping") String mappingjson) throws Exception {

		Floorplan fp = floorplanMarshaller_.fromString(floorplanjson);
		FloorplanImageMapping mapping = imageMappingMarshaller_
				.fromString(mappingjson);

		syncFloorplanAndBeacons(floorplanId, fp);
		floorplanLoader_.save(floorplanId, fp);
		imageMappingLoader_.save(floorplanId, mapping);

		return new FloorplanMapping(fp, mapping);
	}

	/**
	 * This endpoint is used to create a new floor plan and associate a floor
	 * plan image with it.
	 * 
	 * @param floorplanId
	 * @return
	 */
	@RequestMapping(value = "/floorplan/mapping", method = RequestMethod.POST)
	public void setFloorplanMappingAndImage(
			@RequestParam("floorplanId") String floorplanId,
			@RequestParam("scale") Double scale,
			@RequestParam("image") GMultipartFile image,
			HttpServletResponse response) throws Exception {

		String encodedFloorplanId = URLEncoder.encode(floorplanId, "UTF-8");

		imageLoader_.save(floorplanId, scale, image.getInputStream());

		Floorplan fp = new Floorplan();
		FloorplanImageMapping mapping = new FloorplanImageMapping(
				"/mcc/image/floorplan/" + encodedFloorplanId);

		syncFloorplanAndBeacons(floorplanId, fp);
		floorplanLoader_.save(floorplanId, fp);
		imageMappingLoader_.save(floorplanId, mapping);

		response.sendRedirect("/floorplan.editor.html#/floorplan/"
				+ encodedFloorplanId);
	}

	
	/**
	 * Every time that the floor plan is updated, we need to check
	 * if:
	 * 
	 * 1. Any existing mappings of beacons to locations are no longer
	 *    valid because one or more locations was removed
	 *    
	 * 2. Any new locations don't have BeaconsAtFloorplanLocation objects
	 *    persisted yet
	 *    
	 * Both of these cases are corrected in this sync. The sync is expensive
	 * on writes but avoids edge cases and additional client logic on reads.
	 * 
	 * @param floorplanId
	 * @param nw
	 */
	private void syncFloorplanAndBeacons(String floorplanId, Floorplan nw) {
		try {
			Floorplan old = null;
			
			try{
				old = floorplanLoader_.load(floorplanId);
			}catch(Exception e){}

			if (old != null) {
				for (FloorplanLocation ol : old.getLocations()) {
					if (nw.locationById(ol.getId()) == null) {
						beaconsLoader_.removeAllBeaconsAtLocation(floorplanId,
								ol.getId());
					}
				}
				for (FloorplanLocation nl : nw.getLocations()) {
					if (old == null || old.locationById(nl.getId()) == null) {
						BeaconsAtFloorplanLocation bal = new BeaconsAtFloorplanLocation(
								floorplanId, nl.getId());
						beaconsLoader_.save(bal);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve a floor plan image.
	 * 
	 * @param floorplanId
	 * @return
	 */
	@RequestMapping(value = "/image/floorplan/{floorplanId}", method = RequestMethod.GET)
	public void getFloorplanImage(
			@PathVariable("floorplanId") String floorplanId,
			HttpServletResponse response) throws Exception {

		FloorplanImage img = imageLoader_.load(floorplanId);

		if (img == null) {
			response.sendError(404, "Image not found");
		} else {
			response.getOutputStream().write(img.getData().getBytes());
		}
	}

	/**
	 * Sets the mapping of beacons to floor plan locations.
	 * 
	 * @param floorplanId
	 * @param beaconMappingJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/beacons/{floorplanId}", method = RequestMethod.POST)
	public @ResponseBody
	Set<BeaconsAtFloorplanLocation> setBeacons(
			@PathVariable("floorplanId") String floorplanId,
			@RequestParam("beaconsMapping") String beaconMappingJson)
			throws Exception {

		Set<BeaconsAtFloorplanLocation> beacons = (new MCCObjectMapper())
				.readValue(beaconMappingJson,
						new TypeReference<Set<BeaconsAtFloorplanLocation>>() {
						});

		beaconsLoader_.setBeaconMapping(floorplanId, beacons);

		return beacons;
	}

	/**
	 * Returns the mapping of beacons to floor plan locations for an entire
	 * floor plan.
	 * 
	 * @param floorplanId
	 * @param beaconMappingJson
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/beacons/{floorplanId}", method = RequestMethod.GET)
	public @ResponseBody
	Set<BeaconsAtFloorplanLocation> getBeacons(
			@PathVariable("floorplanId") String floorplanId) throws Exception {

		Floorplan fp = floorplanLoader_.load(floorplanId);
		Set<BeaconsAtFloorplanLocation> beacons = beaconsLoader_
				.getBeacons(floorplanId);

		if (fp != null && beacons.size() == 0) {
			beaconsLoader_.setBeaconMapping(floorplanId,
					new HashSet<BeaconsAtFloorplanLocation>());
			beacons = beaconsLoader_.getBeacons(floorplanId);
		}

		return beacons;
	}

	/**
	 * Returns the shortest path from the given starting location to the given
	 * ending location for the specified floorplan.
	 * 
	 * @param floorplanId
	 * @param from
	 * @param to
	 * @return
	 */
	@RequestMapping("/path/{floorplanId}/{from}/{to}")
	public @ResponseBody
	List<PathData> getDirections(
			@PathVariable("floorplanId") String floorplanId,
			@PathVariable("from") String from, @PathVariable("to") String to) {

		Floorplan fp = getFloorplan(floorplanId);

		FloorplanLocation source = fp.locationById(from);
		FloorplanLocation end = fp.locationById(to);

		ShortestPaths<FloorplanLocation> paths = solver_.shortestPaths(
				fp.asGraph(), source);
		
		FloorplanImageMapping mapping = imageMappingLoader_.load(floorplanId);

		// A quick hack to allow for edges to later be returned rather than nodes
		Path<FloorplanLocation> path = paths.getShortestPath(end);
		List<PathData> pathData = new ArrayList<PathData>();
		
		FloorplanLocation prev = null;
		for(FloorplanLocation fl : path){
			if(prev != null){
				FloorplanLocationEdge edge = new FloorplanLocationEdge(prev, fl, false, 0);
				double angle = mapping.getEdgeAngle(edge);
				double dist = mapping.getEdgeLength(edge);
				pathData.add(new PathData(prev.getId(), fl.getId(), dist, angle));
			}
			prev = fl;
		}
		
		return pathData;
	}

}