package simulator.factories;


import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.animals.Animal;
import simulator.model.animals.IncorrectParametersException;
import simulator.model.animals.SelectionStrategy;
import simulator.model.animals.Sheep;

import simulator.launcher.Main;

public class SheepBuilder extends Builder<Animal> {
	

	
	public SheepBuilder()  {
		
		super("sheep", "Sheep");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Sheep create_instance(JSONObject data) {
fill_in_data(data);
		
		Vector2D pos = null;
		JSONObject mateStrat = data.getJSONObject("mate_strategy");
		JSONObject dangerStrat = data.getJSONObject("danger_strategy");
		if (data.has("pos")) {
			JSONObject posJ = data.getJSONObject("pos");
			JSONArray x_range =  posJ.getJSONArray("x_range");
			JSONArray y_range = posJ.getJSONArray("y_range");
			if (x_range.getDouble(0) == y_range.getDouble(0) && x_range.getDouble(1) == y_range.getDouble(1)) {
				pos = Vector2D.get_random_vector(x_range.getDouble(0), x_range.getDouble(1));
			}
			else {
				double x = Utils._rand.nextDouble(x_range.getDouble(0), x_range.getDouble(1));
				double y = Utils._rand.nextDouble(y_range.getDouble(0), y_range.getDouble(1));
				pos = new Vector2D(x, y);
			}
		}
		
		
		Factory<SelectionStrategy> selection_strategy_factory = Main.getStrategyFactory();
		
		Sheep sheep = null;
		try {
			sheep = new Sheep(selection_strategy_factory.create_instance(mateStrat), selection_strategy_factory.create_instance(dangerStrat), pos);
		} catch (IncorrectParametersException e) {
			assert(1 == 0);
		}
		
		return sheep;
	}
	@Override
	protected void fill_in_data(JSONObject o) {
		
		if (!o.has("mate_strategy")) {
			SelectFirstBuilder f = new SelectFirstBuilder();
			o.put("mate_strategy", f.get_info());
		}
		if (!o.has("danger_strategy")) {
			SelectFirstBuilder f = new SelectFirstBuilder();
			o.put("danger_strategy", f.get_info());
		}
	}
	
}


