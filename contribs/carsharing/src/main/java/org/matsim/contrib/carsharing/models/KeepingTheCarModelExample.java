/**
 * 
 */
package org.matsim.contrib.carsharing.models;

import org.matsim.api.core.v01.population.Person;
import org.matsim.core.gbl.MatsimRandom;

/**
 * @author balacm
 *
 */
public class KeepingTheCarModelExample implements KeepingTheCarModel {

	
	public KeepingTheCarModelExample() {		
		
	}
	
	@Override
	public boolean keepTheCarDuringNextACtivity(double durationOfActivity, Person person) {

		return false;
		//if (durationOfActivity < 2 *3600) {
			
		//	return 	MatsimRandom.getRandom().nextDouble() > durationOfActivity / (2.0 * 3600.0);

			
		//}		
		//return false;		
	}
}
