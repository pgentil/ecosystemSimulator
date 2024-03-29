package simulator.model.animals;

import java.util.function.Predicate;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Entity;
import simulator.model.regions.AnimalMapView;

public abstract class Animal implements Entity, AnimalInfo{
	
	static final double _min_energy = 0.0;
	static final double _max_energy = 100.0;
	static final double _min_desire = 0.0;
	static final double _max_desire = 100.0;
	static final double _close_to_dest = 8.0;
	static final double _times0point007 = 0.007;
	static final double _desireToMate = 65.0;
	static final double _times1point2 = 1.2;
	
	protected String _genetic_code;
	private Diet _diet;
	protected State _state;
	protected Vector2D _pos;
	protected Vector2D _dest;
	protected double _energy;
	protected double _speed;
	protected double _age;
	protected double _desire;
	protected double _sight_range;
	protected Animal _mate_target;
	protected Animal _baby;
	protected AnimalMapView _region_mngr ;
	protected SelectionStrategy _mate_strategy;

	protected Animal(String genetic_code, Diet diet, double sight_range,
			 double init_speed, SelectionStrategy mate_strategy, Vector2D pos) throws IncorrectParametersException {
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
			_pos = pos;
		}
	}
	
	protected Animal(Animal p1, Animal p2){
		_state = State.NORMAL;
		_dest = null;
		_mate_target = null;
		_baby = null;
		_region_mngr = null;
		_desire = 0.0;
		_genetic_code = p1.get_genetic_code(); 
		_diet = p1.get_diet();
		_mate_strategy = p2._mate_strategy;
		_energy = ( p1.get_energy() + p2.get_energy() ) / 2; 
		_pos = p1.get_position().plus( Vector2D.get_random_vector(-1,1).scale(60.0*(Utils._rand.nextGaussian()+1)));
		_sight_range = Utils.get_randomized_parameter( (p1.get_sight_range() + p2.get_sight_range())/2, 0.2 );
		_speed = Utils.get_randomized_parameter((p1.get_speed()+p2.get_speed())/2, 0.2);
	}

	
	public void init(AnimalMapView reg_mngr)
	{
		_region_mngr = reg_mngr;
		if (_pos == null)	
			_pos = randomDestination();
		else
		{
			double x =  _pos.getX();
			double y =	_pos.getY();
			_pos = adjustPos(x,y);
		}
		_dest = randomDestination();;
				
				
				
	}
	
	//adjusts the position to be inside the map
	public Vector2D adjustPos(double x, double y)
	{
		double width = _region_mngr.get_width()-1;
		double height = _region_mngr.get_height()-1;	
		 
		while (x >= width) 
			x = (x - width);
		while (x < 0) 
			x = (x + width);
		while (y >= height) 
			y = (y - height);
		while (y < 0) 
			y = (y + height);
		return new Vector2D(x, y);
	}
	
	//checks if a position is out of the map
	public boolean outOfMap(double x, double y)
	{
		if (y >= 300)
			System.out.println("");
		double width = _region_mngr.get_width()-1;
		double height = _region_mngr.get_height()-1;	
		 
		return ( x < 0 || x >= width || y < 0 || y >= height);
	}
	
	public Animal deliver_baby()
	{
		Animal auxBaby = _baby;
		_baby = null;
		return auxBaby;
	}
	
	protected void move(double speed)
	{
		_pos = _pos.plus(_dest.minus(_pos).direction().scale(speed));
	}
	
	public JSONObject as_JSON()
	{
		JSONObject json = new JSONObject();
        json.put("pos", _pos);
        json.put("gcode", _genetic_code);
        json.put("diet", _diet);
        json.put("state", _state);
        return json;
	}
	
	 public boolean is_pregnant() {
		 return _baby != null;
	 }

	public String get_genetic_code(){
		return _genetic_code;
	}
	public Diet get_diet(){
		return _diet;
	}
	public String toString(){
		return _genetic_code;
	}
	public State get_state(){
		return _state;
	}
	public Vector2D get_position(){
		return _pos;
	}
	public double get_speed(){
		return _speed;
	}
	public double get_sight_range(){
		return _sight_range;
	}
	public double get_energy(){
		return _energy;
	}
	public double get_age(){
		return _age;
	}
	public Vector2D get_destination(){
		return _dest;
	}
	
	//a method that adds num1 to num2 and returns the sum ensuring it doesnt pass 100
	protected double ensureNotOver100(double num1, double num2)
	{
		double sum = num1 + num2;
		if (sum > 100)
			sum = 100;
		return sum;
	}
	//a method that does num1 minus num2 and returns the subtraction ensuring it doesnt go below 0
	protected double ensureNotBelow0(double num1, double num2)
	{
		double sub = num1 - num2;
		if (sub < 0)
			sub = 0;
		return sub;
	}
	// returns a 2DVector destination inside the map
	protected Vector2D randomDestination() {
		double width = _region_mngr.get_width()-1;
		double height = _region_mngr.get_height()-1;	
		Vector2D randomDest = new Vector2D(Utils._rand.nextDouble(width), Utils._rand.nextDouble(height));
		
		return randomDest;
	}
	
	//The predicate works with inverted logic, since we then call removeIf with the predicate. In other words, if we want 
	// 2 animals to mate, they need to have the same genetic code. So we remove all the animals with a different genetic code from the list
	protected void selectMate() {
		Predicate<Animal> differentCodePredicate = animal -> animal.get_genetic_code() != _genetic_code;
		_mate_target = _mate_strategy.select(this,_region_mngr.get_animals_in_range(this, differentCodePredicate));
		
	}

}
