package simulator.model.regions;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Entity;
import simulator.model.animals.Animal;

abstract class Region implements Entity, FoodSupplier, RegionInfo{
	static final double parameterFood1 = 60.0;
	static final double parameterFood2 = 5.0;
	static final double parameterFood3 = 2.0;
	
	
	protected List<Animal> animalList;
	protected int herviborous_animals;
	
	
	//REMOVE
	/*
	public static void main(String[] args) {
		Region testR = new Region();
		Animal a1 = new Animal("Juan");
		Animal a2 = new Animal("Pablo");
		
		testR.add_animal(a1);
		System.out.println(testR);
		testR.add_animal(a2);
		System.out.println(testR);
	}
	*/
	
	public Region() {
		animalList = new ArrayList<Animal>();
		herviborous_animals = 0;
	}
	
	final void add_animal(Animal a) {
		// TODO pone el if que necesitas para incrementar el numero de herbivurous animals
		animalList.add(a);
	}
	
	final void remove_animal(Animal a) {
		int index = animalList.indexOf(a);
		if (index != -1)
			animalList.remove(index);
	}
	
	final List<Animal> getAnimals(){
		//	TODO
		return null;
	}

	public JSONObject as_JSON() {
		JSONObject obj = new JSONObject();
		JSONArray ja = new JSONArray();
		 
		// TODO
		 
		 
		return obj;
	}
	
	
	
	
	//METHODS TO TEST IMPLEMENTATION
	public String toString() {
		StringBuffer str = new StringBuffer();
		
		str.append("Animal List");
		str.append(System.lineSeparator());
		str.append(System.lineSeparator());
		for( int i = 0; i < animalList.size(); ++i) {
			str.append(animalList.get(i)); //ANIMALS NEED A toString() method
			str.append(System.lineSeparator());
		}
		
		return str.toString();
	}
	
	/*
	public static void main(String[] args) {
		Region testR = new Region();
		Animal a1 = new Animal("Juan");
		Animal a2 = new Animal("Pablo");
		
		testR.add_animal(a1);
		System.out.println(testR);
		testR.add_animal(a2);
		System.out.println(testR);
	}
	*/
}