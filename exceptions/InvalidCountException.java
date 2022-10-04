package medialab.hangman.exceptions;

public class InvalidCountException extends Exception {
    public InvalidCountException() {
        super("Duplicate words contained");
    }
}
