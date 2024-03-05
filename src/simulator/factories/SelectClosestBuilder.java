package simulator.factories;

import org.json.JSONObject;

import simulator.model.animals.SelectClosest;

public class SelectClosestBuilder extends Builder<SelectClosest>{

	public SelectClosestBuilder() {
		
		super("closest", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SelectClosest create_instance(JSONObject data) {
		return new SelectClosest();
	}

}
