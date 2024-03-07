package simulator.control;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Simulator;
import simulator.model.animals.AnimalInfo;
import simulator.model.regions.MapInfo;
import simulator.view.SimpleObjectViewer;
import simulator.view.SimpleObjectViewer.ObjInfo;

public class Controller {
	private Simulator _sim;
	
	public Controller(Simulator simulator) {
		this._sim = simulator;
	}
	
	 public void load_data(JSONObject data) {
		 if (data.has("regions")) {
			 //Retrieving regions (optional)
			 JSONArray jRegions = data.getJSONArray("regions");
			 for (int i = 0; i < jRegions.length(); ++i) {
				 JSONObject r_json = jRegions.getJSONObject(i);
				 JSONArray minMaxCols = r_json.getJSONArray("col"); //range of columns where you want to initialize this type of region
				 JSONArray minMaxRows = r_json.getJSONArray("row"); //range of rows where you want to initialize this type of region
				 for (int j = minMaxCols.getInt(0); j <= minMaxCols.getInt(1); ++j) { //iterate over range of columns (int)
					 for (int k = minMaxRows.getInt(0); k <= minMaxRows.getInt(1); ++k) { //iterate over range of rows (int)
						 _sim.set_region(k, j, r_json.getJSONObject("spec")); //row, col, specification of region
					 }
				 }
			 }
		 }
		//Retrieving animals
		 JSONArray jAnimals = data.getJSONArray("animals");
		 for (int i = 0; i < jAnimals.length(); ++i) {
			 JSONObject a_json = jAnimals.getJSONObject(i);
			 int amount = a_json.getInt("amount"); //amount of specified animal
			 for (int j = 0; j < amount; ++j){
				 _sim.add_animal(a_json.getJSONObject("spec"));
			 }
		 }
	 }
	 
	 public void run(double t, double dt, boolean sv, OutputStream out) {

		 JSONObject result = new JSONObject();

		 //Given code
		 SimpleObjectViewer view = null;
		 if (sv) {
			 MapInfo m = _sim.get_map_info();
			 view = new SimpleObjectViewer("[ECOSYSTEM]", 
					 m.get_width(), m.get_height(), 
					 m.get_cols(), m.get_rows());
			 view.update(to_animals_info(_sim.get_animals()), _sim.get_time(), dt);
		 }

		 result.put("in", _sim.as_JSON());
		 while (_sim.get_time() <= t) {
			 if (_sim.get_time() >= 40) {
				 System.out.println("");
			 }
			 _sim.advance(dt);
			 if (sv) {
				 view.update(to_animals_info(_sim.get_animals()), _sim.get_time(), dt);
			 }
		 }
		 result.put("out", _sim.as_JSON());
		 if (sv) { 
			 view.close();
		 }
		 PrintStream p = new PrintStream(out);
		 p.println(result);
	 }
	 
	 private List<ObjInfo> to_animals_info(List<? extends AnimalInfo> animals) {
		 List<ObjInfo> ol = new ArrayList<>(animals.size());
		 
		 for (AnimalInfo a : animals)
		 ol.add(new ObjInfo(a.get_genetic_code(), 
		                    (int) a.get_position().getX(), 
		                    (int) a.get_position().getY(),(int)Math.round(a.get_age())+2));
		 
		 return ol;
	}
}
