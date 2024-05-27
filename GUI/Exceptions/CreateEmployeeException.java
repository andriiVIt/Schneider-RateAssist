package GUI.Exceptions;

public class CreateEmployeeException extends Exception {
    public CreateEmployeeException(String message) {
        super(message);
    }

    public CreateEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}