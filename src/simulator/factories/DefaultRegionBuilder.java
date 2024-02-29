package simulator.factories;

import org.json.JSONObject;

import simulator.model.regions.DefaultRegion;

public class DefaultRegionBuilder extends Builder<DefaultRegion>{

	public DefaultRegionBuilder() {
		super("default", "");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected DefaultRegion create_instance(JSONObject data) {
//		if (!data.getString("type").equals("default")) {
//			throw new IllegalArgumentException("Information is incorrect or not available");
//		}
		return new DefaultRegion();
	}

}
