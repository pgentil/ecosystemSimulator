package simulator.model.animals;

import simulator.misc.Vector2D;

public class Sheep extends Animal{
	
	static final double _field_of_view = 40.0;
	static final double _initial_velocity = 35.0;
	static final double _max_age = 8.0;
	static final double _min_energy = 0.0;
	static final double _max_energy = 100.0;
	static final double _min_desire = 0.0;
	static final double _max_desire = 100.0;
	static final double _close_to_dest = 8.0;
	static final double _times0007 = 0.007;
	static final double _times20 = 20.0;
	static final double _times40 = 40.0;
	static final double _desireToMate = 65.0;


	
	
	
	
	private Animal _danger_source;
	private SelectionStrategy _danger_strategy;
	
	
	public Sheep(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) throws IncorrectParametersException {
		super("Sheep", Diet.HERBIVORE, _field_of_view, _initial_velocity, mate_strategy, pos);
		_danger_strategy = danger_strategy;
		_danger_source = null;
	}
	protected Sheep(Sheep p1, Animal p2)
	{
		super(p1, p2);
		_danger_strategy = p1.get_danger_strategy();
		_danger_source = null;
	}

	
	
	public SelectionStrategy get_danger_strategy(){
		return _danger_strategy;
	}
	
	@Override
	public void update(double dt) {
		switch (_state) {
		case DEAD:
			return;
		case NORMAL:
			
			break;
		case MATE:
			
			break;
		case HUNGER:
			
			break;
		case DANGER:
			
			break;
		}
		double x = _pos.getX(); double y =  _pos.getY(); 
		if(outOfMap(x, y))
			_state = State.NORMAL;
		if(_energy == _min_energy || _max_age > 8.0)
			_state = State.DEAD;
		if(!_state.equals(State.DEAD))
		{
			double food = _region_mngr.get_food(this, dt);
			_energy = ensureNotOver100(_energy, food);
		}
		
	}
	
	
	private double ensureNotOver100(double num1, double num2)
	{
		double sum = num1 + num2;
		if (sum > 100)
			sum = 100;
		return sum;
	}
	private double ensureNotBelow0(double num1, double num2)
	{
		double sub = num1 - num2;
		if (sub < 0)
			sub = 0;
		return sub;
	}
	
	private void updateNormal(double dt)
	{
		if(_pos.distanceTo(_dest) <= _close_to_dest)
			_dest = Vector2D.get_random_vector(width, height);
		move(_speed * dt * Math.exp( (_energy - 100.0) * 0.007 )); 
		_age = _age + dt;
		_energy = ensureNotBelow0(_energy, _times20*dt);
		_desire = ensureNotOver100(_desire, _times40*dt);
		if(_danger_source == null)
			//_danger_source = _region_mngr.get_animals_in_range(_danger_source, null)
		if(_danger_source != null)
			_state = State.DANGER;
		else if(_danger_source == null && _desire > _desireToMate)
			_state = State.MATE;
		
	}
	
}
