package simulator.model;

import java.util.List;
import java.util.function.Predicate;

import org.json.JSONObject;

import simulator.factories.Factory;
import simulator.model.animals.Animal;
import simulator.model.animals.AnimalInfo;
import simulator.model.animals.State;
import simulator.model.regions.RegionManager;
import simulator.model.regions.MapInfo;
import simulator.model.regions.Region;
import java.util.ArrayList;
import java.util.Collections;

public class Simulator {
	
	private RegionManager _manager;
	private List<Animal> _animalList;
	private double _time;
	private Factory<Animal> _animal_factory;
	private Factory<Region> _region_factory;
	
	 public Simulator(int cols, int rows, int width, int height,
			 Factory<Animal> animals_factory, Factory<Region> regions_factory) 
	 {
		 this._time = 0;
		 this._manager = new RegionManager(cols, rows, width, height);
		 this._animalList = new ArrayList<Animal>();
		 this._animal_factory = animals_factory;
		 this._region_factory = regions_factory;
	 }
	 
	 private void set_region(int row, int col, Region r) {
		 _manager.set_region(row, col, r);
	 }
	 
	 public void set_region(int row, int col, JSONObject r_json) { 
		  set_region(row, col, _region_factory.create_instance(r_json));
	 }
	 
	 private void add_animal(Animal a) { //change them to public when debugging
		 _animalList.add(a);
		 _manager.register_animal(a);
	 }
	 
	 public void add_animal(JSONObject a_json) {
		 add_animal(_animal_factory.create_instance(a_json));
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
	 
	 /**
	  * Removes dead animals from simulation.
	  */
	 private void removeDead() {
		 Predicate<Animal> filterNotDead = animal -> animal.get_state() != State.DEAD; //Predicate: if not dead
		 List<Animal> deadAnimals = new ArrayList<Animal>();
		 
		 deadAnimals.addAll(_animalList); //adss all animals from list
		 deadAnimals.removeIf(filterNotDead); //removes if not dead
		 
		 for (Animal a: deadAnimals) { //for all dead animals
			 _manager.unregister_animal(a);
			 _animalList.remove(a);
		 }
	 }
	 
	 /**
	  * Updates the state of the animals.
	  * @param dt - delta time of simulation.
	  */
	 private void updateAnimals(double dt)  {
		 for (Animal a : _animalList) {
			 a.update(dt);
			 _manager.update_animal_region(a);
		 }
	 }
	 
	 /**
	  * Method in charge of deploying offsprings in the simulation
	  */
	 private void offspringCreation() {
		 List<Animal> babyList = new ArrayList<Animal>(); 
		 for (Animal a : _animalList) {
			 if (a.is_pregnant()) {
				 Animal baby = a.deliver_baby();
				 babyList.add(baby);
			 }
		 }
		 for (Animal baby: babyList) {
			 add_animal(baby);
		 }
	 }
	 
	 /**
	  * Advances the simulation by delta time. Removes dead animals, updates the living ones, updates regions and 
	  * delivers offsprings into the simulation.
	  * @param dt - delta time of simulation.
	  */
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
	 
//	 public static void main(String[] args) throws IncorrectParametersException {
//		 Simulator sim = new Simulator(10,10, 1000, 1000);
//		 
//		 for (int i = 0; i < 10; ++i) {
//			 Animal a = new Sheep(new SelectFirst(), new SelectFirst(), null);
//			 Animal b = new Wolf(new SelectFirst(), new SelectClosest(), Vector2D.get_random_vector(0, 1000));
//			 sim.add_animal(a);
//			 sim.add_animal(b);
//		 }
//		 System.out.println("Nashe");
//		 while (true) {
//			 int i = 0;
//			 if (i % 3 == 0) {
//				 sim.advance(0.2);
//			 }
//		 }
//	 }
	 
	 
	 
	 
	 
	 
	 
	 
}
