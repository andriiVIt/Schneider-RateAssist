package GUI.Exceptions;

public class TeamCreationException extends Exception {
    public TeamCreationException(String message) {
        super(message);
    }

    public TeamCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}