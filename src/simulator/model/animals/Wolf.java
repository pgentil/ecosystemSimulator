package simulator.model.animals;

import simulator.misc.Vector2D;

public class Wolf extends Animal {
	
	static final double _field_of_view = 50.0;
	static final double _initial_velocity = 60.0;
	
	Animal _hunt_target;
	SelectionStrategy _hunting_strategy;
	
	public Wolf(SelectionStrategy mate_strategy, SelectionStrategy hunting_strategy, Vector2D pos) throws IncorrectParametersException {
		super("Wolf", Diet.CARNIVORE, _field_of_view, _initial_velocity, mate_strategy, pos);
		_hunting_strategy = hunting_strategy;
		_hunt_target = null;
	}
	
	protected Wolf(Wolf p1, Animal p2) {
		super(p1, p2);
		_hunting_strategy = p1.get_hunting_strategy();
		_hunt_target = null;
	}
	
	public SelectionStrategy get_hunting_strategy(){
		return _hunting_strategy;
	}

	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
	}

}
