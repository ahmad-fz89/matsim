/* *********************************************************************** *
 * project: org.matsim.*
 * CreateSelectedPlansTables.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2009 by the members listed in the COPYING,        *
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

package playground.anhorni.locationchoice.preprocess.analyzePlansAndFacs;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.matsim.api.basic.v01.population.BasicPlanElement;
import org.matsim.core.api.facilities.Facilities;
import org.matsim.core.api.population.Activity;
import org.matsim.core.api.population.Person;
import org.matsim.core.api.population.Plan;
import org.matsim.core.api.population.Population;
import org.matsim.core.facilities.FacilitiesReaderMatsimV1;
import org.matsim.core.gbl.Gbl;
import org.matsim.core.network.MatsimNetworkReader;
import org.matsim.core.network.NetworkLayer;
import org.matsim.core.population.MatsimPopulationReader;
import org.matsim.core.population.PopulationImpl;
import org.matsim.core.population.PopulationReader;
import org.matsim.core.utils.io.IOUtils;
import org.matsim.world.World;

public class AnalyzePlans {

	private Population plans = new PopulationImpl();
	private Facilities facilities;
	private NetworkLayer network;
	
	private final static Logger log = Logger.getLogger(AnalyzePlans.class);
	
	
	public static void main(final String[] args) {

		Gbl.startMeasurement();
		final AnalyzePlans analyzer = new AnalyzePlans();
		if (args.length < 1) {
			log.info("Too few arguments!");
			System.exit(0);
		}
		analyzer.init("input/networks/ivtch.xml", args[0], "input/facilities/facilities.xml.gz");
		analyzer.write("./output/valid/plans/plan_activities_summary.txt", args[0]);
		Gbl.printElapsedTime();
	}
	
	public void init(String networkfilePath, String plansfilePath, String facilitiesfilePath) {
		
		//World world = Gbl.createWorld();
				
		log.info("reading the facilities ...");
		this.facilities =(Facilities)Gbl.getWorld().createLayer(Facilities.LAYER_TYPE, null);
		new FacilitiesReaderMatsimV1(this.facilities).readFile(facilitiesfilePath);
			
		log.info("reading the network ...");
		this.network = new NetworkLayer();
		new MatsimNetworkReader(this.network).readFile(networkfilePath);
		
		log.info("  reading file " + plansfilePath);
		final PopulationReader plansReader = new MatsimPopulationReader(this.plans, network);
		plansReader.readFile(plansfilePath);				
	}
	

	private void write(String outfile, String infile) {

		try {
			final BufferedWriter out = IOUtils.getBufferedWriter(outfile);
			
			int numberOfShoppingActs = 0;
			double totalDesiredShoppingDuration = 0.0;
			int numberOfLeisureActs = 0;
			double totalDesiredLeisureDuration = 0.0;
			
			Iterator<Person> person_it = this.plans.getPersons().values().iterator();
			while (person_it.hasNext()) {
				Person person = person_it.next();
				Plan selectedPlan = person.getSelectedPlan();
				
				int numberOfShoppingActsPerPerson = 0;
				int numberOfLeisureActsPerPerson = 0;
				double desiredShopPerPerson = 0.0;
				double desiredLeisurePerPerson = 0.0;
				
				final List<? extends BasicPlanElement> actslegs = selectedPlan.getPlanElements();
				for (int j = 0; j < actslegs.size(); j=j+2) {
					final Activity act = (Activity)actslegs.get(j);
					if (act.getType().startsWith("shop")) {
						numberOfShoppingActs++;
						desiredShopPerPerson += person.getDesires().getActivityDuration("shop");
						numberOfShoppingActsPerPerson++;
					}
					else if (act.getType().startsWith("leisure")) {
						numberOfLeisureActs++;
						desiredLeisurePerPerson += person.getDesires().getActivityDuration("leisure");
						numberOfLeisureActsPerPerson++;
					}
				}
				if (numberOfShoppingActsPerPerson > 0) {
					totalDesiredShoppingDuration += (desiredShopPerPerson / numberOfShoppingActsPerPerson);
				}
				if (numberOfLeisureActsPerPerson > 0) {
					totalDesiredLeisureDuration += (desiredLeisurePerPerson / numberOfLeisureActsPerPerson);
				}				
			}
			out.write("Plans file: " + infile);
			out.write("Number of shopping activities: \t" + numberOfShoppingActs + "\n");
			out.write("Total desired duration of shopping activities: \t" + 1/3600.0 * totalDesiredShoppingDuration + " [h] \n");
			out.write("Avg. desired shopping duration: \t" + 1/3600.0 * (totalDesiredShoppingDuration / numberOfShoppingActs) + " [h] \n");
			out.newLine();
			out.write("Number of leisure activities: \t" + numberOfLeisureActs + "\n");
			out.write("Total desired duration of leisure activities: \t" + 1/3600.0 * totalDesiredLeisureDuration + " [h] \n");
			out.write("Avg. desired leisure duration: \t" + 1/3600.0 * (totalDesiredLeisureDuration / numberOfLeisureActs) + " [h] \n");
			out.flush();
			out.close();
		}
		catch (final IOException e) {
			Gbl.errorMsg(e);
		}
	}
}
