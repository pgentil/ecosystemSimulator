package simulator.factories;

import org.json.JSONObject;
import simulator.model.animals.SelectFirst;

public class SelectFirstBuilder extends Builder<SelectFirst>{

	public SelectFirstBuilder() {
		
		super("first", "");
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
