/* *********************************************************************** *
 * project: org.matsim.*
 * PlanRouterWithVehicleRessourcesTest.java
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2013 by the members listed in the COPYING,        *
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
package org.matsim.contrib.socnetsim.sharedvehicles;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.matsim.api.core.v01.Id;
import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Link;
import org.matsim.api.core.v01.population.Activity;
import org.matsim.api.core.v01.population.Leg;
import org.matsim.api.core.v01.population.Person;
import org.matsim.api.core.v01.population.Plan;
import org.matsim.api.core.v01.population.PlanElement;
import org.matsim.api.core.v01.population.PopulationFactory;
import org.matsim.core.config.ConfigUtils;
import org.matsim.core.population.routes.LinkNetworkRouteImpl;
import org.matsim.core.population.routes.NetworkRoute;
import org.matsim.core.router.PlanRouter;
import org.matsim.core.router.RoutingModule;
import org.matsim.core.router.StageActivityTypes;
import org.matsim.core.router.StageActivityTypesImpl;
import org.matsim.core.router.TripRouter;
import org.matsim.core.router.TripStructureUtils;
import org.matsim.core.router.TripStructureUtils.Trip;
import org.matsim.core.scenario.ScenarioUtils;
import org.matsim.facilities.Facility;
import org.matsim.vehicles.Vehicle;

/**
 * @author thibautd
 */
public class PlanRouterWithVehicleRessourcesTest {

	@Test
	public void testVehicleIdsAreKeptIfSomething() throws Exception {
        final PopulationFactory factory = ScenarioUtils.createScenario(ConfigUtils.createConfig()).getPopulation().getFactory();

		final Id<Link> linkId = Id.create( "the_link" , Link.class );
		final Id<Person> personId = Id.create( "somebody" , Person.class );
		final Id<Vehicle> vehicleId = Id.create( "stolen_car" , Vehicle.class );
		final Person person = factory.createPerson( personId );
		final Plan plan = factory.createPlan();
		person.addPlan( plan );
		plan.setPerson( person );

		final Activity firstAct = factory.createActivityFromLinkId( "h" , linkId );
		plan.addActivity( firstAct );
		firstAct.setEndTime( 223 );

		final Leg leg = factory.createLeg( TransportMode.car );
		plan.addLeg( leg );
		final NetworkRoute route = new LinkNetworkRouteImpl( linkId , Collections.<Id<Link>>emptyList() , linkId );
		route.setVehicleId( vehicleId );
		leg.setRoute( route );

		plan.addActivity( factory.createActivityFromLinkId( "h" , linkId ) );

		final TripRouter tripRouter = createTripRouter( factory );

		final PlanRouterWithVehicleRessources router =
			new PlanRouterWithVehicleRessources(
				new PlanRouter( tripRouter ) );

		router.run( plan );

		for ( Trip trip : TripStructureUtils.getTrips( plan , tripRouter.getStageActivityTypes() ) ) {
			for (Leg l : trip.getLegsOnly()) {
				assertEquals(
					"unexpected vehicle id",
					vehicleId,
					((NetworkRoute) l.getRoute()).getVehicleId());
			}
		}
	}

	private static TripRouter createTripRouter(final PopulationFactory factory) {
		// create some stages to check the behavior with that
		final String stage = "realize that actually, you did't forget to close the window, and go again";
		final TripRouter tripRouter = new TripRouter();
		tripRouter.setRoutingModule(
				TransportMode.car,
				new RoutingModule() {
					@Override
					public List<? extends PlanElement> calcRoute(
							final Facility fromFacility,
							final Facility toFacility,
							final double departureTime,
							final Person p) {
						final List<PlanElement> legs = new ArrayList<PlanElement>();

						for (int i=0; i < 5; i++) {
							final Leg l = factory.createLeg( TransportMode.car );	
							l.setRoute( new LinkNetworkRouteImpl( fromFacility.getLinkId() , Collections.<Id<Link>>emptyList() , fromFacility.getLinkId() ) );
							legs.add( l );
							legs.add( factory.createActivityFromLinkId( stage , fromFacility.getLinkId() ) );
						}

						final Leg l = factory.createLeg( TransportMode.car );	
						l.setRoute( new LinkNetworkRouteImpl( fromFacility.getLinkId() , Collections.<Id<Link>>emptyList() , toFacility.getLinkId() ) );
						legs.add( l );

						return legs ;
					}

					@Override
					public StageActivityTypes getStageActivityTypes() {
						return new StageActivityTypesImpl( stage );
					}
				});
		return tripRouter;
	}
}
