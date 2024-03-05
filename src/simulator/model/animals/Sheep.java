package simulator.model.animals;

import java.util.function.Predicate;

import simulator.misc.Vector2D;
import simulator.misc.Utils;


public class Sheep extends Animal{
	
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
			updateNormal(dt);
			updateStateNormal();
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
		if(outOfMap(x, y))
			_pos = adjustPos(_pos.getX(), _pos.getY());
			changeToNormalSheep();//_state = State.NORMAL;
		if(_energy == _min_energy || _age > _max_age)
			_state = State.DEAD;
		if(!_state.equals(State.DEAD))
		{
			double food = _region_mngr.get_food(this, dt);
			_energy = ensureNotOver100(_energy, food);
		}
	}
	
	private void selectDangerSource() { //FIXME
		Predicate<Animal> notCarnivorousPredicate = animal -> animal.get_diet() != Diet.CARNIVORE; ///si no son carnivoros los remueve OJO AL PIOJO
		_danger_source = _danger_strategy.select(this,_region_mngr.get_animals_in_range(this, notCarnivorousPredicate));      
	}
	
	private void updateNormal(double dt)
	{
		
		if(_pos.distanceTo(_dest) < _close_to_dest)
			_dest = randomDestination(); 
			
		
		move(_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
		_age = _age + dt;
		_energy = ensureNotBelow0(_energy, _times20*dt);
		_desire = ensureNotOver100(_desire, _times40*dt);
	}
	private void updateStateNormal()
	{
		if(_danger_source == null)
		{
			selectDangerSource();  
		}	
		if(_danger_source != null) //not inside the loop so that if it doesnt enter the above if, it can still change to danger will never happen but just in case
			changeToDangerSheep(); //_state = State.DANGER;
		else if(_danger_source == null && _desire > _desireToMate)
			changeToMateSheep();//_state = State.MATE;
	}
		
	
	
	private void updateDanger(double dt)
	{
		if(_danger_source != null && _danger_source.get_state().equals(State.DEAD))
			_danger_source = null;
		if(_danger_source == null)
			updateNormal(dt);
		else
		{
			_dest = _pos.plus(_pos.minus(_danger_source.get_position()).direction());
			move(_times2*_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
			_age = _age + dt;
			_energy = ensureNotBelow0(_energy, _times20*_times1point2*dt);
			_desire = ensureNotOver100(_desire, _times40*dt);
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
			if(_mate_target == null) //should i out it outside of the if
				updateNormal(dt);
			else
			{
				_dest = _mate_target.get_position();
				move(_times2*_speed * dt * Math.exp( (_energy - _max_energy) * _times0point007 )); 
				_age = _age + dt;
				_energy = ensureNotBelow0(_energy, _times20*_times1point2*dt);
				_desire = ensureNotOver100(_desire, _times40*dt);
			}
		}
		if(_pos.distanceTo(_mate_target.get_position()) < _close_to_dest)
		{
			_desire = 0.0;
			_mate_target._desire = 0.0;
			
			if(!is_pregnant() && Utils._rand.nextDouble() <= 0.9) //and probability of 0.9 
			{
				_baby = new Sheep(this, _mate_target);
				_mate_target = null;
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
