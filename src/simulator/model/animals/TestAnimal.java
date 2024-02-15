package simulator.model.animals;

import java.util.List;

public class TestAnimal extends Animal{

	public TestAnimal() throws IncorrectParametersException {
		super("TestAnimal", Diet.CARNIVORE, 2, 2,
			new SelectionStrategy() {

				@Override
				public Animal select(Animal a, List<Animal> as) {
					// TODO Auto-generated method stub
					return null;
				}
			},
			null);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean is_pregnant() {
		// TODO Auto-generated method stub
		return false;
	}

}
