package simulator.factories;

import org.json.JSONObject;
import simulator.model.animals.SelectYoungest;
import simulator.model.animals.SelectionStrategy;

public class SelectYoungestBuilder extends Builder<SelectionStrategy>{

	public SelectYoungestBuilder() {
		
		super("youngest", "SelectYoungest");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SelectYoungest create_instance(JSONObject data) {
		
		return new SelectYoungest();
	}

}
