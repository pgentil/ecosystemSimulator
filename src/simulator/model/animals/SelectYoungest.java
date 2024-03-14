package simulator.model.animals;

import java.util.List;

public class SelectYoungest implements SelectionStrategy{

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Animal youngest = (as.isEmpty() ? null : as.get(0));
		if (!as.isEmpty()) {
			double youngestAge = youngest.get_age();
			for (Animal x: as)
			{
				double xAge = x.get_age();
				if(x != a && xAge < youngestAge)
				{
					youngest = x;
					youngestAge = xAge;
				}	
			}
		}
		return youngest;
	}
}