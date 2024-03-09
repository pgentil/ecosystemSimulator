package simulator.model.animals;

import java.util.function.Predicate;

import simulator.misc.Vector2D;
import simulator.misc.Utils;


public class Sheep extends Animal{
	//useful variables specific to Sheep
	static final double _field_of_view = 40.0;
	static final double _initial_velocity = 35.0;
	static final double _max_age = 8.0;
	static final double _times20 = 20.0;
	static final double _times40 = 40.0;
	static final double _times2 = 2.0;
	

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
			updateNormal(dt); //point 1 of the normal state update
			updateStateNormal(); // point 2 of the normal state update
			//we seperated it like this because in the mate and danger states, if it doesnt find the corresponding 
			//target, it updates point as in point 1 of the normal state
			break;
		case MATE:
			updateMate(dt);
			break;
		case HUNGER:
			break;
		case DANGER:
			updateDanger(dt);
			break;
		}
		
		double x = _pos.getX(); double y =  _pos.getY(); 
		if(outOfMap(x, y)) {
			_pos = adjustPos(_pos.getX(), _pos.getY());
			changeToNormalSheep();//changes the sheep to the normal state
		}
		if(_energy == _min_energy || _age > _max_age)
			_state = State.DEAD;
		if(!_state.equals(State.DEAD))
		{
			double food = _region_mngr.get_food(this, dt);
			_energy = ensureNotOver100(_energy, food);
		}
	}
	
	private void selectDangerSource() {  ///we later call removeif with the predicate, so we need to remove all animals not carnivorous from the list
		Predicate<Animal> notCarnivorousPredicate = animal -> animal.get_diet() != Diet.CARNIVORE; 
		_danger_source = _danger_strategy.select(this,_region_mngr.get_animals_in_range(this, notCarnivorousPredicate));      
	}
	
	private void updateNormal(double dt)
	{
		
		if(_pos.distanceTo(_dest) < _close_to_dest)
			_dest = randomDestination(); //returns a random position in the map and sets the destination to that postiion
			
		
		move(_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
		_age = _age + dt;
		_energy = ensureNotBelow0(_energy, _times20*dt); //useful function explained in animal class
		_desire = ensureNotOver100(_desire, _times40*dt); //useful function explained in animal class
	}
	private void updateStateNormal()
	{
		if(_danger_source == null)
		{
			selectDangerSource();  //selects a danger source 
		}	
		if(_danger_source != null) 
			changeToDangerSheep(); //_state = State.DANGER;
		else if(_danger_source == null && _desire > _desireToMate)
			changeToMateSheep();//_state = State.MATE;
	}
		
	
	
	private void updateDanger(double dt)
	{
		if(_danger_source != null && _danger_source.get_state().equals(State.DEAD))
			_danger_source = null;
		if(_danger_source == null)
			updateNormal(dt); //updates as in point 1 of the normal case
		else
		{
			_dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
			move(_times2*_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
			_age = _age + dt;
			_energy = ensureNotBelow0(_energy, _times20*_times1point2*dt); //useful function explained in animal class
			_desire = ensureNotOver100(_desire, _times40*dt); //useful function explained in animal class
		}
		if(_danger_source == null || _pos.distanceTo(_danger_source.get_position()) > _sight_range)
			selectDangerSource();     
		
		if (_danger_source == null)
		{
			if(_desire < _desireToMate)
				changeToNormalSheep(); //_state = State.NORMAL;
			else
				changeToMateSheep();
		}
	}

	
	private void updateMate(double dt)
	{
		if(_mate_target != null && ( _mate_target.get_state().equals(State.DEAD) || _pos.distanceTo(_mate_target.get_position()) > _sight_range ))
			_mate_target = null;
		if(_mate_target == null)
		{
			selectMate();
			if(_mate_target == null) 
				updateNormal(dt);
			
		}
		if (_mate_target != null)
		{
			_dest = _mate_target.get_position();
			move(_times2*_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
			_age = _age + dt;
			_energy = ensureNotBelow0(_energy, _times20*_times1point2*dt); //useful function explained in animal class
			_desire = ensureNotOver100(_desire, _times40*dt); //useful function explained in animal class
			
			//Lets say we have 2 sheep, sheep 1 and sheep 2. they are at a distance of 8.8, so for sheep 1 it doesnt enter this loop.
			// However, when sheep 2 gets updated, it moves closer to sheep 1, and therefore the distance is now 6, so it does enter this loop
			//for sheep 2. So sheep 2 has a baby and the desire of both sheeps are set to 0 and sheep 2 changes state to either normal or danger.
			//however, sheep 1 still has the state set to mate since it never changes. So when it updates sheep 1, it also has a baby. I think an extra
			//condition is needed (something like to have a baby both sheeps must be each others targets, or to have a baby both sheeps desire must be greater
			//than 65. 
			if(_pos.distanceTo(_mate_target.get_position()) < _close_to_dest)
			{
				_desire = 0.0;
				_mate_target._desire = 0.0;
				if(!is_pregnant() && Utils._rand.nextDouble() <= 0.9) 
				{
					_baby = new Sheep(this, _mate_target);
					_mate_target = null;
				}
			}
		}
		
		
		if(_danger_source == null)
		{
			selectDangerSource(); 
		}
		if(_danger_source != null)
			changeToDangerSheep(); //_state = State.DANGER;
		else if (_danger_source == null && _desire < _desireToMate)
			changeToNormalSheep(); //_state = State.NORMAL;
	}
	
	
	//these 3 methods take the necessary steps to change the sheep's state
	private void changeToNormalSheep()
	{
		_state = State.NORMAL;
		_danger_source = null;
		_mate_target = null;
	}
	private void changeToMateSheep()
	{
		_state = State.MATE;
		_danger_source = null;
	}
	private void changeToDangerSheep()
	{
		_state = State.DANGER;
		_mate_target = null;
	}
	

	
	
	
	
	
}
