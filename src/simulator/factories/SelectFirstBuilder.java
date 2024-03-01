package simulator.factories;

import org.json.JSONObject;
import simulator.model.animals.SelectFirst;
import simulator.model.animals.SelectionStrategy;

public class SelectFirstBuilder extends Builder<SelectionStrategy>{

	public SelectFirstBuilder() {
		
		super("first", "SelectFirst");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SelectFirst create_instance(JSONObject data) {
//		if (!data.getString("type").equals("first")) {
//			throw new IllegalArgumentException("Information is incorrect or not available");
//		}
		return new SelectFirst();
	}

}
