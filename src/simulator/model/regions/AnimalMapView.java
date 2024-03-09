package simulator.model.regions;

import java.util.List;
import java.util.function.Predicate;

import simulator.model.animals.Animal;


public interface AnimalMapView extends MapInfo, FoodSupplier { 
	/**
	 * Retruns a list of the animals that satisfy the filter given [considering the filter predicate must be negated to work properly] and
	 * are at the range of sight of the animal given as an argument.
	 * @param e - Animal instance that will be used to search its surroundings.
	 * @param filter - Filter predicate used to remove the animals near that are of no interest (Predicate must be negated)
	 * @return List of animals that satisfy the filter and are inside the range of sight of the animal e. 
	 */
	public List<Animal> get_animals_in_range(Animal e, Predicate<Animal> filter);
} 
