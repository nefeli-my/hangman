package medialab.hangman.exceptions;

public class UndersizeException extends Exception {
    public UndersizeException() {
        super("Less than 20 words contained");
    }
}

