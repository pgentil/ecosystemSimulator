package simulator.factories;

import org.json.JSONObject;

import simulator.model.regions.DefaultRegion;
import simulator.model.regions.Region;

public class DefaultRegionBuilder extends Builder<Region>{

	public DefaultRegionBuilder() {
		super("default", "DefaultRegion");
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
