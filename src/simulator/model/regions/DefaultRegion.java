package simulator.model.regions;

import simulator.model.animals.Animal;
import simulator.model.animals.Diet;

public class DefaultRegion extends Region{

	@Override
	public double get_food(Animal a, double dt) {
		double food = 0;
		
		if (a.get_diet() == Diet.HERBIVORE) 
			food = parameterFood1 * Math.exp(-Math.max(0, herbivorous_animals - parameterFood2) * parameterFood3) * dt;
		
		return food;
	}

	@Override
	public void update(double dt) {
		//DOES NOTHING
		
	}

}
