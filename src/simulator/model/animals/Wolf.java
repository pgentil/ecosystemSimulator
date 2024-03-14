package simulator.model.animals;

import java.util.function.Predicate;

import simulator.misc.Utils;
import simulator.misc.Vector2D;

public class Wolf extends Animal {
	//useful variables specific to Wolf

	
	/*
	 * Functionality of Wolf is similar to that of Sheep. Methods are further explained in Sheep.java. 
	 * */
	
	
	static final double _field_of_view = 50.0;
	static final double _initial_velocity = 60.0;
	static final double _max_age = 14.0;
	static final double _hunger_energy = 50.0;
	static final double _times18 = 18.0;
	static final double _times30 = 30.0;
	static final double _times3 = 3.0;
	static final double _energyFromFood = 50.0;
	static final double _energyUsedInMating = 10.0;

	
	Animal _hunt_target;
	SelectionStrategy _hunting_strategy;
	State auxState;
	
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
	
	@Override
	public void update(double dt) {
		switch (_state) {
		case DEAD:
			return;
		case NORMAL:
			updateNormal(dt);//point 1 of the normal state update
			updateStateNormal();// point 2 of the normal state update
			//we seperated it like this because in the mate and danger states, if it doesnt find the corresponding 
			//target, it updates point as in point 1 of the normal state
			break;
		case MATE:
			updateMate(dt);
			break;
		case HUNGER:
			updateHunger(dt);
			break;
		case DANGER:
			break;
		}
		
		double x = _pos.getX(); double y =  _pos.getY(); 
		if(outOfMap(x, y))
		{
			_pos = adjustPos(_pos.getX(), _pos.getY());
			changeToNormalWolf();//_state = State.NORMAL;
		}
			
		if(_energy == _min_energy || _age > _max_age)
			_state = State.DEAD;
		if(!_state.equals(State.DEAD))
		{
			double food = _region_mngr.get_food(this, dt);
			_energy = ensureNotOver100(_energy, food);
		}
	}
	
	private void updateNormal(double dt)
	{
		if(_pos.distanceTo(_dest) < _close_to_dest)
			_dest = randomDestination(); 
		
		move(_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
		_age = _age + dt;
		_energy = ensureNotBelow0(_energy, _times18*dt);
		_desire = ensureNotOver100(_desire, _times30*dt);
	}
	
	private void updateStateNormal() {
		if(_energy < _hunger_energy)
			changeToHungerWolf();
		else if (_energy > _hunger_energy && _desire > _desireToMate)
			changeToMateWolf();
	}
	
	private void updateHunger(double dt)
	{
		if(_hunt_target != null)
			auxState = _hunt_target.get_state(); //encapsulation
		if(_hunt_target == null || auxState.equals(State.DEAD) || _pos.distanceTo(_hunt_target.get_position()) > _field_of_view)
		{
			Predicate<Animal> notHerbivorousPredicate = animal -> animal.get_diet() != Diet.HERBIVORE;
			_hunt_target = _hunting_strategy.select(this,_region_mngr.get_animals_in_range(this, notHerbivorousPredicate));   
		}
		if(_hunt_target == null)
			updateNormal(dt);//updates as in point 1 of the normal case
		else
		{
			_dest = _hunt_target.get_position();
			move(_times3 * _speed * dt * Math.exp( (_energy - _max_energy) *_times0point007 ));
			_age = _age + dt;
			_energy = ensureNotBelow0(_energy, _times1point2*_times18*dt);
			_desire = ensureNotOver100(_desire, _times30*dt);
			if(_pos.distanceTo(_hunt_target.get_position()) < _close_to_dest)
			{
				_hunt_target._state = State.DEAD;
				_hunt_target = null;
				_energy = ensureNotOver100(_energy, _energyFromFood);
			}
		}
		if(_energy > _energyFromFood)
		{
			if(_desire < _desireToMate)
				changeToNormalWolf();
			else
				changeToMateWolf();
		}
	}
	private void updateMate(double dt)
	{
		if(_mate_target != null) // && ( _mate_target.get_state().equals(State.DEAD) || _pos.distanceTo(_mate_target.get_position()) > _sight_range ))
		{
			State aux_mate_state = _mate_target.get_state(); //encapsulation
			if( aux_mate_state.equals(State.DEAD) || _pos.distanceTo(_mate_target.get_position()) > _sight_range )
				_mate_target = null;
		}
		if(_mate_target == null)
		{
			selectMate();
			if(_mate_target == null) 
				updateNormal(dt);
			
		}
		if(_mate_target != null)
		{
			_dest = _mate_target.get_position();
			move(_times3*_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
			_age = _age + dt;
			_energy = ensureNotBelow0(_energy, _times18*_times1point2*dt);
			_desire = ensureNotOver100(_desire, _times30*dt);
			
			//same problem as sheep
			if(_pos.distanceTo(_mate_target.get_position()) < _close_to_dest)
			{
				_desire = 0.0;
				_mate_target._desire = 0.0;
				
				if(!is_pregnant() && Utils._rand.nextDouble() <= 0.9) 
				{
					_baby = new Wolf(this, _mate_target);
					_energy = ensureNotBelow0(_energy, 10.0);
					_mate_target = null;
				}
			}
		}
		if(_energy < _energyFromFood)
			changeToHungerWolf();
		else if (_desire < _desireToMate)
			changeToNormalWolf();
		}
	
	public SelectionStrategy get_hunting_strategy(){
		return _hunting_strategy;
	}
	
	//these 3 methods take the necessary steps to change the wolf's state

	private void changeToNormalWolf()
	{
		_state = State.NORMAL;
		_hunt_target = null;
		_mate_target = null;
	}
	private void changeToMateWolf()
	{
		_state = State.MATE;
		_hunt_target = null;
	}
	private void changeToHungerWolf()
	{
		_state = State.HUNGER;
		_mate_target = null;
	}
	



}
