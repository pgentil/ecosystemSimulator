package simulator.factories;

import org.json.JSONObject;
import simulator.model.animals.SelectFirst;

public class SelectFirstBuilder extends Builder<SelectFirst>{

	public SelectFirstBuilder() {
		
		super("first", null);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected SelectFirst create_instance(JSONObject data) {
		
		return new SelectFirst();
	}

}
