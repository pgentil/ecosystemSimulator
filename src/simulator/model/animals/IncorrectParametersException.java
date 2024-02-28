package simulator.model.animals;

@SuppressWarnings("serial")
public class IncorrectParametersException extends Exception {
	public IncorrectParametersException(String errorMessage) {
        super(errorMessage);
    }
}
