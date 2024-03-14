package simulator.factories;

import org.json.JSONObject;

import simulator.model.regions.DynamicSupplyRegion;
import simulator.model.regions.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region>{

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "DynamicSupplyRegion");
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		if (!o.has("factor")) {
			o.put("factor", 2.0);
		}
		if (!o.has("food")) {
			o.put("food", 1000.0);
		}
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {		
		fill_in_data(data);
	
		return new DynamicSupplyRegion(data.getDouble("food"), data.getDouble("factor"));
	}

}
