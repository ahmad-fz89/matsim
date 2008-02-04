/* *********************************************************************** *
 * project: org.matsim.*
 * ShapeMatsimConverter.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2007 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package playground.gregor.shapeFileToMATSim;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;

import org.geotools.data.FeatureSource;
import org.geotools.feature.Feature;
import org.matsim.network.NetworkLayer;

/**
 * Converter class for street network shape files as provided by DLR in Last Mile project 
 * @author laemmel
 *
 */
public class GISToMatsimConverter {

	private static String polygonFile = null;
	private static String linestringFile = null;
	HashMap<String,FeatureSource> features = new  HashMap<String,FeatureSource>();
	
	FeatureSource polygons = null;
	FeatureSource linestrings = null;
	
	public GISToMatsimConverter(){
//		this("./padang/padang_streets.shp","./padang/vd10_streetnetwork_padang_v0.5_utm47s.shp");
		this("./padang/padang_streets.shp","./padang/debug.shp");
	}
	
	public GISToMatsimConverter(final String polyFile, final String lineFile){
		polygonFile = polyFile;
		linestringFile = lineFile;
	}

	public void run(){
		try {
			readData();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		try {
			processData();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void processData() throws Exception {
		NetworkBuilder netBuild = new NetworkBuilder(features.get(linestringFile));
		Collection<Feature> network =  netBuild.createNetwork();
		int i = 0;
		i++;
	}

	private void readData() throws Exception{
//		if (polygonFile != null){
			features.put(polygonFile, ShapeFileReader.readDataFile(polygonFile));			
//		}
//		if (linestringFile != null){
			features.put(linestringFile, ShapeFileReader.readDataFile(linestringFile));			
//		}		
		
	}
	
	public static void main(String [] args){
		GISToMatsimConverter converter = new GISToMatsimConverter();
		converter.run();
	}
}
