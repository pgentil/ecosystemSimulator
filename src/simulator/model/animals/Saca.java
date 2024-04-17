package simulator.model.animals;

import simulator.misc.Vector2D;

public class Saca extends Sheep {
	public Saca(SelectionStrategy mate_strategy, SelectionStrategy danger_strategy, Vector2D pos) throws IncorrectParametersException {
		super(mate_strategy, danger_strategy, pos);
		_genetic_code = "Saca";
	}
	protected Saca(Saca p1, Animal p2)
	{
		super(p1, p2);
		_genetic_code = "Saca";
	}
}
 