package simulator.model;

import java.util.List;
import java.util.function.Predicate;

import org.json.JSONObject;

import simulator.model.animals.Animal;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.State;
import simulator.model.regions.RegionManager;
import simulator.model.regions.MapInfo;
import simulator.model.regions.Region;
import java.util.ArrayList;
import java.util.Collections;

public class Simulator {
	//private RegionFactory animalFactory; TODO
	//private AnimalFactory regionFactory; TODO
	
	private RegionManager _manager;
	private List<Animal> _animalList;
	private double _time;
	
	 public Simulator(int cols, int rows, int width, int height) //,
			// Factory<Animal> animals_factory, Factory<Region> regions_factory) TODO
	 {
		 this._time = 0;
		 this._manager = new RegionManager(cols, rows, width, height);
		 this._animalList = new ArrayList<Animal>();
	 }
	 
	 private void set_region(int row, int col, Region r) {
		 _manager.set_region(row, col, r);
	 }
	 
	 public void set_region(int row, int col, JSONObject r_json) { //Ask if you should as well retrieve the animals from the region FIXME
		  Region region = null;
		 //TODO
		  assert(region != null);
		  set_region(row, col, region);
	 }
	 
	 private void add_animal(Animal a) { //change them to public when debugging
		 _animalList.add(a);
		 _manager.register_animal(a);
	 }
	 
	 public void add_animal(JSONObject a_json) {
		 Animal animal = null;
		 //TODO
		 assert(animal != null);
		 add_animal(animal);
	 }
	 
	 public MapInfo get_map_info() {
		 return _manager;
	 }
	 
	 public List<? extends AnimalInfo> get_animals(){
		 List<Animal> list = Collections.unmodifiableList(_animalList);
		 
		 return list;
	 }
	 
	 public double get_time() {
		 return _time;
	 }
	 
	 private void removeDead() {
		 Predicate<Animal> filterNotDead = animal -> animal.get_state() != State.DEAD;
		 List<Animal> deadAnimals = new ArrayList<Animal>();
		 
		 deadAnimals.addAll(_animalList);
		 deadAnimals.removeIf(filterNotDead);
		 
		 for (Animal a: deadAnimals) {
			 _manager.unregister_animal(a);
			 _animalList.remove(a);
		 }
	 }
	 
	 private void updateAnimals(double dt)  {
		 for (Animal a : _animalList) {
			 a.update(dt);
		 }
	 }
	 
	 private void offspringCreation() {
		 for (Animal a : _animalList) {
			 if (a.is_pregnant()) {
				 add_animal(a.deliver_baby());
			 }
		 }
	 }
	 
	 public void advance(double dt) {
		 _time += dt;
		 removeDead();
		 updateAnimals(dt);
		 _manager.update_all_regions(dt);
		 offspringCreation();
		 
		 
		 
	 }
	 
	 public JSONObject as_JSON() {
		 JSONObject jo = new JSONObject();
		 jo.put("time", _time);
		 jo.put("state", _manager.as_JSON());
		 

		 return jo;

	 }
	 
	 
	 
	 
	 
	 
	 
	 
}
