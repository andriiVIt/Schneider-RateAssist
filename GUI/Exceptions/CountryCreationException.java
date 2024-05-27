package GUI.Exceptions;

public class CountryCreationException extends Exception {
    public CountryCreationException(String message) {
        super(message);
    }

    public CountryCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

