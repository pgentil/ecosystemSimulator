package simulator.factories;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.animals.IncorrectParametersException;
import simulator.model.animals.SelectionStrategy;
import simulator.model.animals.Wolf;

public class WolfBuilder extends Builder<Wolf>{

	public WolfBuilder() {
		super("wolf", "Wolf");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		if (!o.has("mate_strategy")) {
			SelectFirstBuilder f = new SelectFirstBuilder();
			o.put("mate_strategy", f.get_info());
		}
		if (!o.has("hunt_strategy")) {
			SelectFirstBuilder f = new SelectFirstBuilder();
			o.put("hunt_strategy", f.get_info());
		}
	}

	@Override
	protected Wolf create_instance(JSONObject data) {
		fill_in_data(data);
		
		Vector2D pos = null;
		JSONObject mateStrat = data.getJSONObject("mate_strategy");
		JSONObject huntStrat = data.getJSONObject("hunt_strategy");
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
		
		List<Builder<SelectionStrategy>> selection_strategy_builders = new ArrayList<Builder<SelectionStrategy>>();
		selection_strategy_builders.add(new SelectFirstBuilder());
//		selection_strategy_builders.add(new SelectClosestBuilder());
//		selection_strategy_builders.add(new SelectYoungestBuilder());
		Factory<SelectionStrategy> selection_strategy_factory = new BuilderBasedFactory<SelectionStrategy>(selection_strategy_builders);
		
		Wolf wolf = null;
		try {
			wolf = new Wolf(selection_strategy_factory.create_instance(mateStrat), selection_strategy_factory.create_instance(huntStrat), pos);
		} catch (IncorrectParametersException e) {
			assert(1 == 0);
		}
		
		return wolf;
		
	}

}
