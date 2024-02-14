package simulator.model.regions;

import java.util.List;
import java.util.function.Predicate;

import simulator.model.animals.Animal;

public interface AnimalMapView extends FoodSupplier { //include mapinfo pepe te quiero
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter);
}
//TODO