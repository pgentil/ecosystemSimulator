package simulator.factories;


import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.animals.IncorrectParametersException;
import simulator.model.animals.SelectionStrategy;
import simulator.model.animals.Wolf;

import simulator.launcher.Main;

public class WolfBuilder extends AnimalBuilder{

	public WolfBuilder() {
		super("wolf", "Wolf");
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
		
		
		JSONObject mateStrat = data.getJSONObject("mate_strategy");
		JSONObject huntStrat = data.getJSONObject("hunt_strategy");
		Vector2D pos = setPos(data);
		
		Factory<SelectionStrategy> selection_strategy_factory = Main.getStrategyFactory();
		
		Wolf wolf = null;
		try {
			wolf = new Wolf(selection_strategy_factory.create_instance(mateStrat), selection_strategy_factory.create_instance(huntStrat), pos);
		} catch (IncorrectParametersException e) {
			assert(1 == 0); //unreachable statement as we know that the constructor in this case will never throw an exception
		}
		
		return wolf;
		
	}

}
