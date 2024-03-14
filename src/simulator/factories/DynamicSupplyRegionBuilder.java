package simulator.factories;

import org.json.JSONObject;

import simulator.model.regions.DynamicSupplyRegion;
import simulator.model.regions.Region;

public class DynamicSupplyRegionBuilder extends Builder<Region> {
	static final double defaultFood = 100.0;
	static final double defaultFactor = 2.0;
	

	public DynamicSupplyRegionBuilder() {
		super("dynamic", "Dynamic Food Supply");
	}
	
	@Override
	protected void fill_in_data(JSONObject o) {
		if (!o.has("factor")) {
			o.put("factor", "food increase factor (optional, default 2.0)");
		}
		if (!o.has("food")) {
			o.put("food", "initial amount of food (optional, default 100.0)");
		}
	}

	@Override
	protected DynamicSupplyRegion create_instance(JSONObject data) {		
		fill_in_data(data); //REDUNDANT FIXME	
		return new DynamicSupplyRegion(defaultFood, defaultFactor);
	}

}
