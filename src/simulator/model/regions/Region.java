package simulator.model.regions;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import simulator.model.Entity;
import simulator.model.animals.Animal;

public class Region implements Entity, FoodSupplier, RegionInfo{
	protected List<Animal> animalList;
	
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
	}
	
	final void add_animal(Animal a) {
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
	
	@Override
	public void update(double dt) {
		// TODO Auto-generated method stub
		
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
