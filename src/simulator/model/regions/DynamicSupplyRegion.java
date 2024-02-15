package simulator.model.regions;

import simulator.misc.Utils;
import simulator.model.animals.Animal;

public class DynamicSupplyRegion extends Region{
	private double _food;
	private double _factor;
	
	public DynamicSupplyRegion(double food, double factor) {
		this._food = food;
		this._factor = factor;
	}

	@Override
	public void update(double dt) {
		if (Utils._rand.nextDouble() < 0.5) {
			_food += dt * _factor;
		}
		
	}

	@Override
	public double get_food(Animal a, double dt) {
		return Math.min(_food, parameterFood1 * Math.exp(-Math.max(0, herviborous_animals - parameterFood2) * parameterFood3) * dt);
	}

}