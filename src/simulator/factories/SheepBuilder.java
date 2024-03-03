package simulator.factories;

import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.animals.SelectionStrategy;
import simulator.model.animals.Sheep;

public class SheepBuilder extends Builder<Sheep> {
	
	SelectionStrategy mateStrategy;
	SelectionStrategy dangerStrategy;
	Vector2D _pos;
	
	public SheepBuilder()  {
		
		super("sheep", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Sheep create_instance(JSONObject data) {
		//throw exception
		
		mateStrategy = null;//how do i choose between the three builder strategy classes
				// i can do it with ifs but idk if it looks fgood
		dangerStrategy = null;//same here
		_pos = new Vector2D(data.getDouble("x"), data.getDouble("y")); //says to choose a coord in the range x_range and y_range
																	   //Not sure if this is it
	
		return null;
	}
	@Override
	protected void fill_in_data(JSONObject o) {
		
		if(!o.has("mate_strategy"))
			o.put("mate_strategy", new SelectFirstBuilder());
		if(!o.has("danger_strategy"))
			o.put("danger_strategy", new SelectFirstBuilder());
		if(!o.has("pos"))
			o.put("pos", null);
	}
	
}
