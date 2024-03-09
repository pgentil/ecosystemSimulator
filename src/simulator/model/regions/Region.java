package simulator.model.regions;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;


import simulator.model.Entity;
import simulator.model.animals.Animal;
import simulator.model.animals.Diet;

public abstract class Region implements Entity, FoodSupplier, RegionInfo{
	static final double parameterFood1 = 60.0;
	static final double parameterFood2 = 5.0;
	static final double parameterFood3 = 2.0;
	
	protected List<Animal> animalList;
	protected int herbivorous_animals; //used to maintain counting of herbivorous animals (for method get_food(Animal, double))
	
	
	public Region() {
		animalList = new ArrayList<Animal>();
		herbivorous_animals = 0;
	}
	
	final void add_animal(Animal a) {
		if (a.get_diet() == Diet.HERBIVORE)
			herbivorous_animals++;
		animalList.add(a);
	}
	
	final void remove_animal(Animal a) {
		int index = animalList.indexOf(a);
		if (index != -1) {
			animalList.remove(index);
			if (a.get_diet() == Diet.HERBIVORE) {
				herbivorous_animals--;
			}
		}
		
	}
	
	final List<Animal> getAnimals(){
		List<Animal> aux = Collections.unmodifiableList(animalList); //used unmodifiable list (only the list is unmodifiable the objects inside it are not)
		return aux;
	}

	public JSONObject as_JSON() {
		JSONObject obj = new JSONObject();
		JSONArray ja = new JSONArray();
		 
		for (Animal animal: animalList) {
			ja.put(animal.as_JSON());
		}
		obj.put("animals", ja);
		 
		return obj;
	}
		
}
