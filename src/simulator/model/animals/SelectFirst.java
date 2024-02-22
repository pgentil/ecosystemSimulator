package simulator.model.animals;

import java.util.List;

public class SelectFirst implements SelectionStrategy{

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal first;
		if (as.get(0)!= a)
			first = as.get(0);
		else
			first = as.get(1);
			
		return first;
	}
}
