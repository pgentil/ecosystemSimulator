package simulator.model.animals;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.Entity;
import simulator.model.regions.AnimalMapView;

public abstract class Animal implements Entity, AnimalInfo{
	
	public static final double width = _region_mngr.get_width()-1;
	public static final double height = _region_mngr.get_height()-1;	
	
	private String _genetic_code;
	private Diet _diet;
	protected State _state;
	protected Vector2D _pos;
	protected Vector2D _dest;
	protected double _energy;
	protected double _speed;
	protected double _age;
	protected double _desire;
	private double _sight_range;
	private Animal _mate_target;
	private Animal _baby;
	protected AnimalMapView _region_mngr ;
	private SelectionStrategy _mate_strategy;

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
		}
	}
	
	protected Animal(Animal p1, Animal p2){
		_dest = null;
		_mate_target = null;
		_baby = null;
		_region_mngr = null;
		_desire = 0.0;
		_genetic_code = p1.get_genetic_code(); //can i do p1._genetic_code
		_diet = p1.get_diet();
		_mate_strategy = p2._mate_strategy;
		_energy = ( p1.get_energy() + p2.get_energy() ) / 2; 
		_pos = p1.get_position().plus( Vector2D.get_random_vector(-1,1).scale(60.0*(Utils._rand.nextGaussian()+1)));
		_sight_range = Utils.get_randomized_parameter( (p1.get_sight_range() + p2.get_sight_range())/2, 0.2 );
		_speed = Utils.get_randomized_parameter((p1.get_speed()+p2.get_speed())/2, 0.2);
	}
//	@Override
//	public void update(double dt) {
//	
//	}
	
	public void init(AnimalMapView reg_mngr)
	{
		_region_mngr = reg_mngr;
		if (_pos == null)	
			_pos = Vector2D.get_random_vector(width, height);
		else
		{
			double x =  _pos.getX();
			double y =	_pos.getY();
			_pos = adjustPos(x,y);
		}
		_dest = Vector2D.get_random_vector(width, height);
				
				
				
	}
	//prolly add it to pos
	public Vector2D adjustPos(double x, double y)
	{
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
	public boolean outOfMap(double x, double y)
	{
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
	
	public JSONObject as_JSON() //ni idea
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
		
	
	

}
