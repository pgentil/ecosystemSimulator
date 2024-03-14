package simulator.factories;

import org.json.JSONObject;

import simulator.model.animals.SelectClosest;
import simulator.model.animals.SelectionStrategy;

public class SelectClosestBuilder extends Builder<SelectionStrategy>{

	public SelectClosestBuilder() {
		
		super("closest", "SelectClosest");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SelectClosest create_instance(JSONObject data) {
		return new SelectClosest();
	}

}
