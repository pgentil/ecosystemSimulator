package simulator.factories;


import org.json.JSONObject;

import simulator.misc.Vector2D;
import simulator.model.animals.IncorrectParametersException;
import simulator.model.animals.SelectionStrategy;
import simulator.model.animals.Sheep;

import simulator.launcher.Main;

public class SheepBuilder extends AnimalBuilder {
	
	public SheepBuilder()  {
		super("sheep", "Sheep");
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

	@Override
	protected Sheep create_instance(JSONObject data) {
		fill_in_data(data);
		
		
		JSONObject mateStrat = data.getJSONObject("mate_strategy");
		JSONObject dangerStrat = data.getJSONObject("danger_strategy");
		Vector2D pos = setPos(data);
		
		Factory<SelectionStrategy> selection_strategy_factory = Main.getStrategyFactory();
		
		Sheep sheep = null;
		try {
			sheep = new Sheep(selection_strategy_factory.create_instance(mateStrat), selection_strategy_factory.create_instance(dangerStrat), pos);
		} catch (IncorrectParametersException e) {
			assert(1 == 0); //unreachable statement as we know that the constructor in this case will never throw an exception
		}
		
		return sheep;
	}
	
	
}


