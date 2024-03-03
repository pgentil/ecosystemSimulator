package simulator.factories;

import org.json.JSONObject;
import simulator.model.animals.SelectYoungest;

public class SelectYoungestBuilder extends Builder<SelectYoungest>{

	public SelectYoungestBuilder() {
		
		super("youngest", null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SelectYoungest create_instance(JSONObject data) {
		
		return new SelectYoungest();
	}

}
