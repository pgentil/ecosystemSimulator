package simulator.model.animals;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Entity;
import simulator.model.regions.AnimalMapView;

public abstract class Animal implements Entity, AnimalInfo{
	
	private String _genetic_code;
	private Diet _diet;
	private State _state;
	private Vector2D _pos;
	private Vector2D _dest;
	private double _energy;
	private double _speed;
	private double _age;
	private double _desire;
	private double _sight_range;
	private Animal _mate_target;
	private Animal _baby;
	private AnimalMapView _region_mngr ;
	private SelectionStrategy _mate_strategy;

	protected Animal(String genetic_code, Diet diet, double sight_range,
			 double init_speed, SelectionStrategy mate_strategy, Vector2D pos) throws IncorrectParametersException
	{
		if(genetic_code == null || sight_range < 0 || init_speed < 0 || mate_strategy == null)
		{
			throw new IncorrectParametersException("Please enter the correct parameters"); 
		}
		else
		{
			_genetic_code = genetic_code;
			_diet = diet;
			_sight_range = sight_range;
			_mate_strategy = mate_strategy;
			_speed = Utils.get_randomized_parameter(init_speed, 0.1);
			_state = State.NORMAL;
			_energy = 100.0;
			_desire = 0.0;
			_dest = null;
			_mate_target = null;
			_baby = null;
			_region_mngr = null;
		}
	}
	
	protected Animal(Animal p1, Animal p2)
	{
		_dest = null;
		_mate_target = null;
		_baby = null;
		_region_mngr = null;
		_desire = 0.0;

		
		
		
	}

	public String get_genetic_code()
	{
		return _genetic_code;
	}
	
	
	@Override
	public void update(double dt) {

	}
	
	
	 public Diet get_diet()
	 {
		 return _diet;
	 }
	 
	 public String toString()
	 {
		 return _genetic_code;
	 }
	
	
	
	

}
