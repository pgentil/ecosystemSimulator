package simulator.factories;



import org.json.JSONArray;
import org.json.JSONObject;

import simulator.misc.Utils;
import simulator.misc.Vector2D;
import simulator.model.animals.Animal;

public abstract class AnimalBuilder extends Builder<Animal> {

	public AnimalBuilder(String type_tag, String desc) {
		super(type_tag, desc);
	}

	protected Vector2D setPos(JSONObject data) {
		Vector2D pos = null;
		if (data.has("pos")) {
			JSONObject posJ = data.getJSONObject("pos");
			JSONArray x_range =  posJ.getJSONArray("x_range");
			JSONArray y_range = posJ.getJSONArray("y_range");
			if (x_range.getDouble(0) == y_range.getDouble(0) && x_range.getDouble(1) == y_range.getDouble(1)) {
				pos = Vector2D.get_random_vector(x_range.getDouble(0), x_range.getDouble(1));
			}
			else {
				double x = Utils._rand.nextDouble(x_range.getDouble(0), x_range.getDouble(1));
				double y = Utils._rand.nextDouble(y_range.getDouble(0), y_range.getDouble(1));
				pos = new Vector2D(x, y);
			}
		}
		return pos;
	}


}
