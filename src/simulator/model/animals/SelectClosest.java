package simulator.model.animals;

import java.util.List;

import simulator.misc.Vector2D;

public class SelectClosest implements SelectionStrategy{

	@Override
	public Animal select(Animal a, List<Animal> as) {
		Vector2D position = a.get_position();
		Animal closestAnimal = as.get(0);
		Vector2D firstInListPos = closestAnimal.get_position();
		double leastDist = position.distanceTo(firstInListPos);
		for (Animal x: as)
		{
			if(x != a)
			{
				Vector2D listPos = x.get_position();
				double dist = position.distanceTo(listPos);
				if(dist < leastDist) {	
					leastDist = dist;
					closestAnimal = x;
				}
			}
		}
		return closestAnimal;
	}
}
