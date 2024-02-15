package simulator.model.regions;

import simulator.model.animals.Animal;

public class DefaultRegion extends Region{

	@Override
	public double get_food(Animal a, double dt) {
		return parameterFood1 * Math.exp(-Math.max(0, herviborous_animals - parameterFood2) * parameterFood3) * dt;
	}

	@Override
	public void update(double dt) {
		//DOES NOTHING
		
	}

}
